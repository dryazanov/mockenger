package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.service.OAuth2TokenManager;
import com.socialstartup.mockenger.core.service.account.AccountService;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.core.web.exception.AccountDeleteException;
import com.socialstartup.mockenger.core.web.exception.ObjectNotFoundException;
import com.socialstartup.mockenger.data.model.persistent.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author Dmitry Ryazanov
 */
@Profile("security")
@RestController
public class AccountController extends AbstractController {

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
    @RequestMapping(value = ACCOUNT_ID_ENDPOINT, method = PUT)
    public ResponseEntity saveAccount(@PathVariable final String accountId,
                                      @Valid @RequestBody final Account account,
                                      final BindingResult result) {

        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }

        // Check if account exists
        final Account existingAccount = findAccountById(accountId);
        final Account.AccountBuilder accountBuilder = Account.builder()
                .id(existingAccount.getId())
                .password(existingAccount.getPassword())
                .username(account.getUsername())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .role(account.getRole());

        if (!StringUtils.isEmpty(account.getPassword())) {
            accountBuilder.password(encodePassword(account.getPassword()));
        }

        final Account updatedAccount = accountService.save(accountBuilder.build());

        // Revoke token if the username of the account was updated
        if (!account.getUsername().equals(existingAccount.getUsername())) {
            oAuth2TokenManager.revokeTokenByUsername(existingAccount.getUsername());
        }

        return new ResponseEntity(updatedAccount, getResponseHeaders(), HttpStatus.OK);
    }


    /**
     *
     * @param accountId
     * @return
     */
    @RequestMapping(value = ACCOUNT_ID_ENDPOINT, method = DELETE)
    public ResponseEntity deleteAccount(@PathVariable final String accountId, final Principal principal) {
        final Account account = findAccountById(accountId);

        if (principal.getName().equals(account.getUsername())) {
            throw new AccountDeleteException("Account cannot delete itself, operation aborted.");
        }

        accountService.remove(account);

        return new ResponseEntity(getResponseHeaders(), HttpStatus.NO_CONTENT);
    }


    /**
     *
     * @return
     */
    @RequestMapping(value = ACCOUNTS_ENDPOINT, method = GET)
    public ResponseEntity getAccountList() {
        return new ResponseEntity(accountService.findAll(), getResponseHeaders(), HttpStatus.OK);
    }


    /**
     *
     * @param account
     * @param result
     * @return
     */
    @RequestMapping(value = ACCOUNTS_ENDPOINT, method = POST)
    public ResponseEntity addAccount(@Valid @RequestBody final Account account, final BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }

        final Account accountBuilder = Account.builder()
                .password(encodePassword(account.getPassword()))
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .username(account.getUsername())
                .role(account.getRole())
                .build();

        return new ResponseEntity(accountService.save(accountBuilder), getResponseHeaders(), HttpStatus.OK);
    }


    private Account findAccountById(final String accountId) {
        final Account account = accountService.findById(accountId);

        if (account == null) {
            throw new ObjectNotFoundException("Account", accountId);
        }

        return account;
    }


    private String encodePassword(final String password) {
        if (!StringUtils.isEmpty(password)) {
            return passwordEncoder.encode(password);
        }
        return null;
    }
}
