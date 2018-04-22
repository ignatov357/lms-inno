package documents;

import java.net.Socket;
import java.util.Scanner;

public class GetDocumentAttack {

    public static void main(String[] args) throws Throwable{
        Socket socket = new Socket("api.awes-projects.com",80);
        String str = "GET /documents/getDocument?id=1 HTTP/1.1\r\n\r\n";
        socket.getOutputStream().write(str.getBytes());
        Scanner s = new Scanner(socket.getInputStream());
        while (s.hasNextLine()){
            System.out.println(s.nextLine());
        }
    }

}
