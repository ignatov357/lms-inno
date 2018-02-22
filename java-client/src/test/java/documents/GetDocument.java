package documents;

import com.awesprojects.lmsclient.api.DocumentsAPI;
import com.awesprojects.lmsclient.api.data.documents.Document;

public class GetDocument {

    public static void main(String[] args){
        int id = Integer.parseInt(args[0]);
        Document document = DocumentsAPI.getDocument(id);
    }

}
