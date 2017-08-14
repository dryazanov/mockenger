package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.service.OAuth2TokenManager;
import com.socialstartup.mockenger.core.service.account.AccountService;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.core.web.exception.AccountDeleteException;
import com.socialstartup.mockenger.core.web.exception.ObjectNotFoundException;
import com.socialstartup.mockenger.data.model.persistent.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

import static com.socialstartup.mockenger.core.web.controller.base.AbstractController.API_PATH;
import static java.util.Optional.ofNullable;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author Dmitry Ryazanov
 */
@Profile("security")
@RestController
@RequestMapping(path = API_PATH + "/accounts")
public class AccountController extends AbstractController {

	private static final String ACCOUNT_ID_ENDPOINT = "/{accountId}";

    @Autowired
    private AccountService accountService;

    @Autowired
    private OAuth2TokenManager oAuth2TokenManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     *
     * @param accountId
     * @param account
     * @return
     */
    @PutMapping(ACCOUNT_ID_ENDPOINT)
    public ResponseEntity saveAccount(@PathVariable final String accountId,
                                      @Valid @RequestBody final Account account,
                                      final BindingResult result) {

        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }

        // Check if account exists
        final Account existingAccount = findAccountById(accountId);
        final Account.AccountBuilder accountBuilder = getAccountBuilder(account)
                .id(existingAccount.getId())
                .password(existingAccount.getPassword());

        if (!isEmpty(account.getPassword())) {
            accountBuilder.password(encodePassword(account.getPassword()));
        }

        final Account updatedAccount = accountService.save(accountBuilder.build());

        // Revoke token if the username of the account was updated
        if (!account.getUsername().equals(existingAccount.getUsername())) {
            oAuth2TokenManager.revokeTokenByUsername(existingAccount.getUsername());
        }

        return okResponseWithDefaultHeaders(updatedAccount);
    }


    /**
     *
     * @param accountId
     * @return
     */
    @DeleteMapping(ACCOUNT_ID_ENDPOINT)
    public ResponseEntity deleteAccount(@PathVariable final String accountId, final Principal principal) {
        final Account account = findAccountById(accountId);

        if (principal.getName().equals(account.getUsername())) {
            throw new AccountDeleteException("Account cannot delete itself, operation aborted.");
        }

        accountService.remove(account);

        return noContentWithDefaultHeaders();
    }


    /**
     *
     * @return
     */
    @GetMapping
    public ResponseEntity getAccountList() {
        return okResponseWithDefaultHeaders(accountService.findAll());
    }


    /**
     *
     * @param account
     * @param result
     * @return
     */
    @PostMapping
    public ResponseEntity addAccount(@Valid @RequestBody final Account account, final BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }

		final Account accountBuilder = getAccountBuilder(account)
				.password(encodePassword(account.getPassword()))
				.build();

		return okResponseWithDefaultHeaders(accountService.save(accountBuilder));
    }


	private Account.AccountBuilder getAccountBuilder(final Account account) {
		return Account.builder()
				.firstName(account.getFirstName())
				.lastName(account.getLastName())
				.username(account.getUsername())
				.role(account.getRole());
	}


	private Account findAccountById(final String accountId) {
    	return ofNullable(accountService.findById(accountId))
				.orElseThrow(() -> new ObjectNotFoundException("Account", accountId));
    }


    private String encodePassword(final String password) {
        return !isEmpty(password) ? passwordEncoder.encode(password) : null;
    }
}
