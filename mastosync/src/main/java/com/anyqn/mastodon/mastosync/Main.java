package com.anyqn.mastodon.mastosync;

import com.anyqn.mastodon.mastosync.controllers.ExitWaiterController;
import com.anyqn.mastodon.mastosync.controllers.FetchDataController;
import com.anyqn.mastodon.mastosync.controllers.UserRegistrationController;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Slf4j
public class Main {
    private static final UserRegistrationController userRegistrationController =
            SimpleContextFabric.IT.getInstance(UserRegistrationController.class);
    private static final ExitWaiterController exitWaiterController =
            SimpleContextFabric.IT.getInstance(ExitWaiterController.class);
    private static final FetchDataController fetchDataController =
            SimpleContextFabric.IT.getInstance(FetchDataController.class);

    public static void main(String[] args) {
        try {
            log.info("Client started");
            userRegistrationController.fetchTokens();
            exitWaiterController.waitFinish(fetchDataController.sync());
        } catch (Throwable e) {
            log.error(ExceptionUtils.getRootCauseMessage(e));
        } finally {
            log.info("Exit");
        }
    }
}
