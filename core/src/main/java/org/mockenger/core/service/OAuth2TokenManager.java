package org.mockenger.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * This class class was created for only one purpose -
 * invalidate access/refresh tokens for the specific user
 *
 * @author Dmitry Ryazanov
 */
@Component
public class OAuth2TokenManager {

    @Value("${oauth2.client.app.name}")
    private String clientAppName;

    @Autowired
    private TokenStore tokenStore;


    /**
     * Revokes access and refresh tokens by username
     *
     * @param username the username
     */
    public void revokeTokenByUsername(final String username) {
        if (!StringUtils.isEmpty(username)) {
            getTokens(username)
                    .stream()
                    .filter(oAuth2AccessToken -> oAuth2AccessToken != null)
                    .forEach(oAuth2AccessToken -> {
                        tokenStore.removeAccessToken(oAuth2AccessToken);
                        tokenStore.removeRefreshToken(oAuth2AccessToken.getRefreshToken());
                    });
        }
    }

    private Collection<OAuth2AccessToken> getTokens(final String username) {
        return tokenStore.findTokensByClientIdAndUserName(clientAppName, username);
    }
}
