package documents;

import com.awesprojects.lmsclient.api.DocumentsAPI;
import com.awesprojects.lmsclient.api.ResponsableContainer;
import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.internal.Responsable;

public class GetDocuments {

    public static void main(String[] args){
        Responsable documents = DocumentsAPI.getDocuments();
        if (documents instanceof ResponsableContainer) {
            Document[] docs = ((ResponsableContainer<Document[]>) documents).get();
            for (Document d : docs )
                System.out.println(d);
        }
    }

}
