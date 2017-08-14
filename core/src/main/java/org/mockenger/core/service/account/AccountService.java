package org.mockenger.core.service.account;

import org.mockenger.core.web.exception.NotUniqueValueException;
import org.mockenger.data.model.persistent.account.Account;
import org.mockenger.data.model.persistent.log.Eventable;
import org.mockenger.data.repository.AccountEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Dmitry Ryazanov
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

    @Eventable
    public Account save(final Account entity) {
        try {
            return accountEntityRepository.save(entity);
        } catch (DuplicateKeyException ex) {
            throw new NotUniqueValueException("Account with username '" + entity.getUsername() + "' already exist");
        }
    }

    @Eventable
    public void remove(Account account) {
        accountEntityRepository.delete(account);
    }

    public void removeAll() {
        accountEntityRepository.deleteAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Account account = accountEntityRepository.findByUsername(username);

        if (account == null) {
            throw new UsernameNotFoundException("Username '" + username + "' not found");
        }

        return new User(username, account.getPassword(), Arrays.asList(account.getRole()));
    }
}