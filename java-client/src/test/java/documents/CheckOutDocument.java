package documents;

import com.awesprojects.lmsclient.api.DocumentsAPI;
import com.awesprojects.lmsclient.api.Response;
import com.awesprojects.lmsclient.api.internal.Responsable;
import testutils.Config;

public class CheckOutDocument {

    public static void main(String[] args){
        Responsable response = DocumentsAPI.checkOutDocument(Config.patronAccessToken.getToken(),31);
        System.out.println(response);
    }

}
