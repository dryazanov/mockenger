package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.service.account.AccountService;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.core.web.exception.AccountDeleteException;
import com.socialstartup.mockenger.core.web.exception.ObjectNotFoundException;
import com.socialstartup.mockenger.data.model.dto.AccountDTO;
import com.socialstartup.mockenger.data.model.persistent.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by Dmitry Ryazanov
 */
@Controller
public class AccountController extends AbstractController {

    @Autowired
    private AccountService accountService;


    /**
     *
     * @param account
     * @param result
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ACCOUNTS_ENDPOINT, method = POST)
    public ResponseEntity addAccount(@Valid @RequestBody Account account, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }
        account.setId(null);
        account.setPassword((new BCryptPasswordEncoder()).encode(account.getPassword()));
        accountService.save(account);
        return new ResponseEntity(account, getResponseHeaders(), HttpStatus.OK);
    }


    /**
     *
     * @param accountId
     * @param account
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ACCOUNT_ID_ENDPOINT, method = PUT)
    public ResponseEntity saveAccount(@PathVariable String accountId, @Valid @RequestBody Account account, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getFieldError().getDefaultMessage());
        }
        // Check if group exists
        Account accountToUpdate = accountService.findById(accountId);
        if (accountToUpdate == null) {
            throw new ObjectNotFoundException("Account", accountId);
        }

        if (!StringUtils.isEmpty(account.getPassword())) {
            accountToUpdate.setPassword((new BCryptPasswordEncoder()).encode(account.getPassword()));
        }

        accountToUpdate.setFirstName(account.getFirstName());
        accountToUpdate.setLastName(account.getLastName());
        accountToUpdate.setRoles(account.getRole());

        accountService.save(account);
        return new ResponseEntity(account, getResponseHeaders(), HttpStatus.OK);
    }


    /**
     *
     * @param accountId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ACCOUNT_ID_ENDPOINT, method = DELETE)
    public ResponseEntity deleteAccount(@PathVariable String accountId, java.security.Principal principal) {
        Account account = accountService.findById(accountId);
        if (principal.getName().equals(account.getUsername())) {
            throw new AccountDeleteException("Account cannot delete himself, operation aborted.");
        }
        accountService.remove(account);
        return new ResponseEntity(getResponseHeaders(), HttpStatus.NO_CONTENT);
    }


    /**
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ACCOUNTS_ENDPOINT, method = GET)
    public ResponseEntity getAccountList() {
        List<AccountDTO> accountsDTO = new ArrayList<>();

        accountService.findAll().forEach(account -> {
            AccountDTO dto = new AccountDTO(account.getFirstName(), account.getLastName(), account.getUsername(), account.getRole());
            dto.setId(account.getId());
            accountsDTO.add(dto);
        });

        return new ResponseEntity(accountsDTO, getResponseHeaders(), HttpStatus.OK);
    }
}
