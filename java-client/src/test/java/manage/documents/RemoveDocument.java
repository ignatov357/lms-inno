package manage.documents;

import com.awesprojects.lmsclient.api.ManageAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.documents.Book;
import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.internal.Responsable;
import testutils.Config;

public class RemoveDocument {

    public static void main(String[] args){
        AccessToken accessToken = Config.librarianAccessToken;
        Document document = Document.getImpl();
        document.setId(25);
        Responsable responsable = ManageAPI.Documents.removeDocument(accessToken,document);
        System.out.println(responsable);
    }
}
