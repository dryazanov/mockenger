package com.socialstartup.mockenger.core.web.controller.base;

import com.socialstartup.mockenger.core.service.account.AccountService;
import com.socialstartup.mockenger.data.model.dto.AccountDTO;
import com.socialstartup.mockenger.data.model.persistent.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 */
@Controller
@EnableResourceServer
@Profile("security")
public class OAuthExtendedController extends AbstractController {

    @Autowired
    private DefaultTokenServices defaultTokenServices;

    @Autowired
    private AccountService accountService;


    @ResponseBody
    @RequestMapping(value = "/oauth/revoke", method = RequestMethod.POST)
    public ResponseEntity revokeTokens(@RequestParam("token") String token) {
        defaultTokenServices.revokeToken(token);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ResponseBody
    @RequestMapping(value = "/oauth/user", method = RequestMethod.GET)
    public ResponseEntity<AccountDTO> getUser(java.security.Principal principal) {
        if (((OAuth2Authentication) principal).isAuthenticated() && !StringUtils.isEmpty(principal.getName())) {
            Account account = accountService.findByUsername(principal.getName());
            if (account != null) {
                AccountDTO dto = new AccountDTO(account.getFirstName(), account.getLastName(), account.getUsername(), account.getRole());
                return new ResponseEntity(dto, getResponseHeaders(), HttpStatus.OK);
            }
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
