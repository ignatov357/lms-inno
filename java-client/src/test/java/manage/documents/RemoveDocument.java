package manage.documents;

import com.awesprojects.lmsclient.api.ManageAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.internal.Responsable;

public class RemoveDocument {

    public static void main(String[] args){
        AccessToken accessToken = new AccessToken("aa259f750c917f7d28dc62dbde23d58d0d29939720b92c57bab40b1f738a29c0",0);
        Responsable responsable = ManageAPI.Documents.removeDocument(accessToken,10);
        System.out.println(responsable);
    }
}
