package com.anyqn.mastodon.pingen.controllers;

import com.anyqn.mastodon.common.enums.Scope;
import com.anyqn.mastodon.common.models.mastodon.MastodonAuthUrlResponse;
import com.anyqn.mastodon.common.models.mastodon.MastodonTokenResponse;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonAuthorizationCode;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonHost;
import com.anyqn.mastodon.pingen.services.mastodon.MastodonUserAuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class MastodonAuthRequestsController {

    private final MastodonUserAuthService mastodonUserAuthService;

    public MastodonAuthRequestsController(MastodonUserAuthService mastodonUserAuthService) {
        this.mastodonUserAuthService = mastodonUserAuthService;
    }

    @GetMapping("/api/v1/auth/url/mastodon")
    @ResponseBody
    MastodonAuthUrlResponse getMastodonAuthUrl(@RequestParam MastodonHost host,
                                               @RequestParam Set<Scope> scopes) {
        return mastodonUserAuthService.getAuthUrl(host, scopes);
    }

    @GetMapping("/api/v1/auth/token/mastodon")
    @ResponseBody
    MastodonTokenResponse getMastodonAccessToken(@RequestParam MastodonHost host,
                                                 @RequestParam MastodonAuthorizationCode code,
                                                 @RequestParam Set<Scope> scopes) {
        return mastodonUserAuthService.getUserAccessToken(host, code, scopes);
    }
}
