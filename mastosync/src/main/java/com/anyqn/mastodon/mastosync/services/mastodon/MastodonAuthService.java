package com.anyqn.mastodon.mastosync.services.mastodon;

import com.anyqn.mastodon.common.enums.Scope;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonAuthorizationCode;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.models.mastodon.MastodonPinUrlResponse;
import com.anyqn.mastodon.common.models.mastodon.MastodonTokenResponse;
import com.anyqn.mastodon.mastosync.repostitories.ConfigurationFileRepository;
import com.anyqn.mastodon.mastosync.repostitories.pingen.PingenAuthRepository;

import java.util.Set;

public class MastodonAuthService {

    private final PingenAuthRepository pingenAuthRepository =
            SimpleContextFabric.IT.getInstance(PingenAuthRepository.class);
    private final ConfigurationFileRepository configurationFileRepository = SimpleContextFabric.IT.getInstance(ConfigurationFileRepository.class);


    public MastodonTokenResponse getMastodonToken(MastodonAuthorizationCode mastodonCode, Set<Scope> scopes) {
        return pingenAuthRepository.getMastodonAccessToken(configurationFileRepository.getPingenMastodonHost(),
                configurationFileRepository.getMastodonHost(), mastodonCode, scopes);
    }

    public MastodonPinUrlResponse getMastodonPinUrl(Set<Scope> scopes) {
        return pingenAuthRepository.getMastodonCodeUrl(configurationFileRepository.getPingenMastodonHost(),
                configurationFileRepository.getMastodonHost(), scopes);
    }

    public boolean isMastodonMainUserTokenSaved() {
        return configurationFileRepository.getMastodonMainUserToken().isPresent();
    }

    public boolean isMastodonReplyUserTokenSaved() {
        return configurationFileRepository.getMastodonReplyUserToken().isPresent();
    }

    public void saveMastodonMainUserToken(MastodonTokenResponse mastodonTokenResponse) {
        configurationFileRepository.saveMastodonMainUserToken(mastodonTokenResponse);
    }

    public void saveMastodonReplyUserToken(MastodonTokenResponse mastodonTokenResponse) {
        configurationFileRepository.saveMastodonReplyUserToken(mastodonTokenResponse);
    }
}
