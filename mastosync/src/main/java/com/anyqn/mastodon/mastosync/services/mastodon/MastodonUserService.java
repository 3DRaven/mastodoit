package com.anyqn.mastodon.mastosync.services.mastodon;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonAccountId;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.models.mastodon.account.MastodonAccount;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.StatusPayload;
import com.anyqn.mastodon.mastosync.repostitories.ConfigurationFileRepository;
import com.anyqn.mastodon.mastosync.repostitories.mastodon.AuthorizedMastodonUsersRepository;

public class MastodonUserService {
    private final AuthorizedMastodonUsersRepository mastodonUserRepository =
            SimpleContextFabric.IT.getInstance(AuthorizedMastodonUsersRepository.class);
    private final ConfigurationFileRepository configurationFileRepository =
            SimpleContextFabric.IT.getInstance(ConfigurationFileRepository.class);

    public boolean isEventFromMainUser(StatusPayload statusPayload) {
        return statusPayload.getAccount().getId().equals(getMastodonMainUserId());
    }

    public MastodonAccountId getMastodonMainUserId() {
        return configurationFileRepository.getMastodonMainUserAccountId().orElseGet(() -> {
            MastodonAccount mastodonUserAccount = mastodonUserRepository.getMastodonMainUserAccount(
                    configurationFileRepository.getMastodonHost());
            configurationFileRepository.saveMastodonMainUserAccountId(mastodonUserAccount);
            return mastodonUserAccount.getId();
        });
    }

    public MastodonAccountId getMastodonReplyUserId() {
        return configurationFileRepository.getMastodonReplyUserAccountId().orElseGet(() -> {
            MastodonAccount mastodonUserAccount = mastodonUserRepository.getMastodonReplyUserAccount(
                    configurationFileRepository.getMastodonHost());
            configurationFileRepository.saveMastodonReplyUserAccountId(mastodonUserAccount);
            return mastodonUserAccount.getId();
        });
    }

}
