package documents;

import com.awesprojects.lmsclient.api.DocumentsAPI;
import com.awesprojects.lmsclient.api.Response;
import com.awesprojects.lmsclient.api.internal.Responsable;

public class CheckOutDocument {

    public static void main(String[] args){
        Responsable response = DocumentsAPI.checkOutDocument("acccceeeessss",222);
        System.out.println(response);
    }

}
