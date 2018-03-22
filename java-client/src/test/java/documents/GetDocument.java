package documents;

import com.awesprojects.lmsclient.api.DocumentsAPI;
import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.internal.Responsable;

public class GetDocument {

    public static void main(String[] args){
        int id = Integer.parseInt(args[0]);
        Responsable document = DocumentsAPI.getDocument(id);
        if (document instanceof Document){

        }else{

        }
    }

}
