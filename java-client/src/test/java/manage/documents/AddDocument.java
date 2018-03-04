package manage.documents;

import com.awesprojects.lmsclient.api.ManageAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.documents.Book;
import com.awesprojects.lmsclient.api.internal.Responsable;
import testutils.Config;

public class AddDocument {

    public static void main(String[] args){
        Book book = new Book();
        book.setEdition(1);
        book.setPublicationYear(2028);
        book.setPublisher("some publisher 2");
        book.setInstockCount(10);
        book.setKeywords("keyword1, keyword2");
        book.setPrice("1.00");
        book.setTitle("Testing in java: does it work 3");
        book.setAuthors("Ilya Potemin");
        AccessToken accessToken = Config.librarianAccessToken;
        Responsable responsable = ManageAPI.Documents.addDocument(accessToken,book);
        System.out.println(responsable);
    }

}
