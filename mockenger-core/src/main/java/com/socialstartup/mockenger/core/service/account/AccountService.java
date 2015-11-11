package com.socialstartup.mockenger.core.service.account;

import com.socialstartup.mockenger.data.model.persistent.account.Account;
import com.socialstartup.mockenger.data.repository.AccountEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by Dmitry Ryazanov on 14-Sep-15.
 */
@Component
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountEntityRepository accountEntityRepository;


    public Account findByUsername(String username) {
        return accountEntityRepository.findByUsername(username);
    }

    public Account findById(String id) {
        return accountEntityRepository.findOne(id);
    }

    public Iterable<Account> findAll() {
        return accountEntityRepository.findAll();
    }

    public void save(Account entity) {
        accountEntityRepository.save(entity);
    }

    public void remove(Account account) {
        accountEntityRepository.delete(account);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountEntityRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException(String.format("Username %s not found", username));
        }
        return new User(username, account.getPassword(), Arrays.asList(account.getRole()));
    }
}