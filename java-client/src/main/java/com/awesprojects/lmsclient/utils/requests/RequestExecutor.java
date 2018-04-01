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
import java.util.logging.Logger;

public class RequestExecutor {

    public static final String TAG = "RequestExecutor";
    public static Logger log = Logger.getLogger(TAG);

    public static String executeRequest(String request){
        return executeRequest(Config.getCurrentConfig().getApiDomain(),request);
    }

    public static String executeRequest(String address,String request){
        return executeRequest(address, 80,request);
    }

    public static String executeRequest(String address,int port,String request){
        AbstractApiRequest apiRequest = createApiRequest(address, port, request);
        return apiRequest.execute(request);
    }

    public static AbstractApiRequest createApiRequest(String request){
        return  createApiRequest(Config.getCurrentConfig().getApiDomain(),80,request);
    }

    public static AbstractApiRequest createApiRequest(String address,int port,String request){
        boolean secureConnection = Config.getCurrentConfig().isSecure();
        AbstractApiRequest apiRequest = null;
        try {
            apiRequest = secureConnection ?
                    new ApiSecureRequest(address, port) : new ApiRequest(address, port);
        }catch(Throwable t){
            log.warning("execute request failed : "+t.toString());
        }
        return apiRequest;
    }




    public static class ApiRequest extends AbstractApiRequest{

        private Socket socket;

        public ApiRequest(String address,int port) throws IOException {
            socket = SocketFactory.getDefault().createSocket(address, port);
        }

        @Override
        public Socket getSocket() {
            return socket;
        }

        @Override
        public String execute(String request) {
            String response;
            try {
                if (Config.getCurrentConfig().isVerbose())
                log.fine("request: "+request);
                socket.getOutputStream().write(request.getBytes());
                socket.getOutputStream().flush();
            } catch (IOException e) {
                log.warning(e.toString());
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
                log.fine("response: "+ret);
                return ret;
            } catch (IOException e) {
                log.warning(e.toString());
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
            //TODO: implement
            return null;
        }

        @Override
        public SSLSocket getSocket() {
            return socket;
        }
    }

    public static abstract class AbstractApiRequest{
        public abstract String execute(String request);
        public abstract Socket getSocket();
    }


}
