import com.awesprojects.lmsclient.api.DocumentsAPI;
import com.awesprojects.lmsclient.api.Response;

public class CheckOutDocument {

    public static void main(String[] args){
        Response response = DocumentsAPI.checkOutDocument("acccceeeessss",222);
        System.out.println(response);
    }

}
