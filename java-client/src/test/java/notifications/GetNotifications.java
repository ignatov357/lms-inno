package notifications;

import com.awesprojects.lmsclient.api.NotificationAPI;
import testutils.Config;

public class GetNotifications {

    public static void main(String[] args){
        NotificationAPI.NotificationReader reader = NotificationAPI.create(Config.patronAccessToken);
        reader.setNotificationReceiver((notification) -> {
            System.out.print(notification);
            return 0;
        });
        reader.run();
    }
}
