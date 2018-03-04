package manage.documents;

import com.awesprojects.lmsclient.api.ManageAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.internal.Responsable;
import testutils.Config;

public class RemoveDocument {

    public static void main(String[] args){
        AccessToken accessToken = Config.librarianAccessToken;
        Responsable responsable = ManageAPI.Documents.removeDocument(accessToken,25);
        System.out.println(responsable);
    }
}
