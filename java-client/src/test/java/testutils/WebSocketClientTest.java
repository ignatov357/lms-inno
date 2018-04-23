package testutils;

import com.awesprojects.lmsclient.utils.Config;
import com.awesprojects.lmsclient.utils.sockets.WebSocketClient;

public class WebSocketClientTest {

    public static void main(String[] args) throws Throwable{
        WebSocketClient webSocketClient = new WebSocketClient( Config.getCurrentConfig().getApiDomain(),
                                                3000,"/"+ testutils.Config.patronAccessToken.getToken());
        webSocketClient.setTextMessageListener(System.out::println);
        webSocketClient.connect();
        new Thread(()->{
            try {
                Thread.sleep(5000);
                webSocketClient.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}

//server=35.224.42.150&username=root&db=library_db&select=notifications
