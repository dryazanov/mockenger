package com.socialstartup.mockenger.data.repository;

import com.socialstartup.mockenger.data.model.persistent.account.Account;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Dmitry Ryazanov
 */
public interface AccountEntityRepository extends CrudRepository<Account, String> {

    Account findByUsername(String username);
}
