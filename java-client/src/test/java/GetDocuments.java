import com.awesprojects.lmsclient.api.DocumentsAPI;
import com.awesprojects.lmsclient.api.data.documents.Document;
import org.json.JSONArray;

public class GetDocuments {

    public static void main(String[] args){
        Document[] documents = DocumentsAPI.getDocuments();
        for (Document d : documents)
            System.out.println(d);
    }

}
