package documents;

import com.awesprojects.lmsclient.api.DocumentsAPI;
import com.awesprojects.lmsclient.api.ResponsableContainer;
import com.awesprojects.lmsclient.api.Search;
import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.internal.Responsable;
import testutils.Config;

public class SearchDocument {

    public static void main(String[] args){
        Responsable responsable = DocumentsAPI.searchDocuments(Config.patronAccessToken,"I",
                Search.Type.ANY,Search.Where.TITLE,Search.Availability.ANY);
        if (responsable instanceof ResponsableContainer) {
            Document[] docs = ((ResponsableContainer<Document[]>) responsable).get();
            for (Document d : docs )
                System.out.println(d);
        }else{
            System.out.println(responsable);
        }
    }

}
