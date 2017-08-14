package org.mockenger.core.web.controller.base;

import org.mockenger.core.service.account.AccountService;
import org.mockenger.data.model.persistent.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.mockenger.core.web.controller.base.AbstractController.API_PATH;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Dmitry Ryazanov
 */
@Profile("security")
@RestController
@EnableResourceServer
@RequestMapping(path = API_PATH + "/oauth")
public class OAuth2ExtendedController extends AbstractController {

	@Autowired
    private DefaultTokenServices defaultTokenServices;

    @Autowired
    private AccountService accountService;



    @PostMapping("/revoke")
    public ResponseEntity revokeTokens(@RequestParam("token") final String token) {
        defaultTokenServices.revokeToken(token);

        return noContent().build();
    }


    @GetMapping("/user")
    public ResponseEntity getUser(final Principal principal) {
        if (((OAuth2Authentication) principal).isAuthenticated() && !StringUtils.isEmpty(principal.getName())) {
            final Account account = accountService.findByUsername(principal.getName());

            if (account != null) {
                return ok().headers(getResponseHeaders()).body(account);
            }
        }

        return notFound().build();
    }
}
