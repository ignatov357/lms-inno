package com.awesprojects.lmsclient.api.data.documents;

import com.awesprojects.lmsclient.api.data.CheckOutInfo;
import com.awesprojects.lmsclient.api.internal.Responsable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


@Data
public abstract class Document implements Responsable,Serializable{

    public Document(){

    }

    public Document(JSONObject doc){
        type = doc.getInt("type");
        id = doc.getInt("id");
        authors = doc.getString("authors");
        title = doc.getString("title");
        price = doc.getString("price");
        instockCount = doc.getInt("instockCount");
    }

    protected int id;

    protected String authors;

    protected String title;

    protected int type;

    protected String keywords;

    protected String price;

    protected int instockCount;

    protected CheckOutInfo checkOutInfo;


    public static Document parseDocument(JSONObject object){
        if (!object.has("type"))
            throw new JSONException("unable to parse document = "+object);
        switch (object.getInt("type")){
            case 0: return new Book(object);
            case 1: return new Article(object);
            case 2: return new EMaterial(object);
        }
        throw new NullPointerException("document class type="+object.getInt("type")+" was not found");
    }

}
