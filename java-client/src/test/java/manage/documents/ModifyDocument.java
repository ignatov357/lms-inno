package manage.documents;

import com.awesprojects.lmsclient.api.ManageAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.documents.Book;
import com.awesprojects.lmsclient.api.internal.Responsable;
import testutils.Config;

public class ModifyDocument {

    public static void main(String[] args){
        Book book = new Book();
        book.setId(25);
        book.setEdition(2);
        book.setPublicationYear(2018);
        book.setPublisher("some publisher");
        book.setInstockCount(10);
        book.setKeywords("keyword1, keyword2, keyword3");
        book.setPrice("0.00");
        book.setTitle("Testing in java: how does it work modified");
        book.setAuthors("Ilya Potemin");
        AccessToken accessToken = Config.librarianAccessToken;
        Responsable responsable = ManageAPI.Documents.modifyDocument(accessToken,book);
        System.out.println(responsable);
    }
}
