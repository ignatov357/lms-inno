package documents;

import com.awesprojects.lmsclient.api.DocumentsAPI;
import com.awesprojects.lmsclient.api.Response;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.internal.Responsable;
import testutils.Config;

public class RenewDocument {

    public static void main(String[] args){
        AccessToken accessToken = Config.patronAccessToken;
        Response response = DocumentsAPI.renewDocument(accessToken.getToken(),-1);
        System.out.println(response.toString());
    }

}
