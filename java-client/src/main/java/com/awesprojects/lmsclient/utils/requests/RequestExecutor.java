package com.awesprojects.lmsclient.utils.requests;

import com.awesprojects.lmsclient.utils.Config;

import javax.net.SocketFactory;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.security.cert.Certificate;
import java.util.Scanner;

public class RequestExecutor {

    public static String executeRequest(String request){
        return executeRequest(Config.getCurrentConfig().getApiDomain(),request);
    }

    public static String executeRequest(String address,String request){
        return executeRequest(address, 80,request);
    }

    public static String executeRequest(String address,int port,String request){
        boolean secureConnection = Config.getCurrentConfig().isSecure();
        AbstractApiRequest apiRequest = null;
        try {
            apiRequest = secureConnection ?
                    new ApiSecureRequest(address, port) : new ApiRequest(address, port);
        }catch(Throwable t){
            t.printStackTrace();
            return null;
        }
        return apiRequest.execute(request);
    }

    public static class ApiRequest extends AbstractApiRequest{

        private Socket socket;

        public ApiRequest(String address,int port) throws IOException {
            socket = SocketFactory.getDefault().createSocket(address, port);
        }

        @Override
        public String execute(String request) {
            String response;
            try {
                if (Config.getCurrentConfig().isVerbose())
                Config.getCurrentConfig().getOut().println("request: "+request);
                socket.getOutputStream().write(request.getBytes());
                socket.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            try {
                StringBuffer sb = new StringBuffer();
                BufferedInputStream is = new BufferedInputStream(socket.getInputStream());
                Scanner scanner = new Scanner(is);

                while (scanner.hasNextLine())
                    sb.append(scanner.nextLine()).append("\n");

                String ret = sb.toString();
                if (Config.getCurrentConfig().isVerbose())
                Config.getCurrentConfig().getOut().println("response: "+ret);
                return ret;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static class ApiSecureRequest extends AbstractApiRequest{

        SSLSocket socket;

        public ApiSecureRequest(String address,int port) throws IOException {
            socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(address, port);
        }

        @Override
        public String execute(String request) {
            return null;
        }
    }

    private static abstract class AbstractApiRequest{
        public abstract String execute(String request);
    }


}
