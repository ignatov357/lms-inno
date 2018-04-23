package testutils;

import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;

public class DocPingTest {

    public static void main(String[] args) throws Throwable{
        Socket socket = new Socket("api.awes-projects.com",80);
        String str = "GET /documents/getDocuments/ HTTP/1.1\r\n" +
                "Host: api.awes-projects.com\r\n" +
                "Connection: keep-alive\r\n" +
                "Accept: */*\r\n" +
                "Origin: http://awes-projects.com\r\n" +
                "User-Agent: Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Mobile Safari/537.36\r\n" +
                "DNT: 1\r\n" +
                "Referer: http://awes-projects.com/catalogue.php\r\n" +
                "Accept-Encoding: deflate\r\n" +
                "Accept-Language: en-US,en;q=0.9,ru;q=0.8\r\n\r\n";
        //byte[] array = Charset.forName("UTF-16").encode(str).array();
        socket.getOutputStream().write(str.getBytes());
        //socket.getOutputStream().flush();
        Scanner s = new Scanner(socket.getInputStream());
        while (s.hasNextLine()){
            System.out.println(s.nextLine());
        }
    }

}
