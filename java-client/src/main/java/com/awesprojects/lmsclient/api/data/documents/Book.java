package com.awesprojects.lmsclient.api.data.documents;

import lombok.Data;
import lombok.ToString;
import org.json.JSONObject;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
public class Book extends Document implements Serializable{

    public Book(){
        super();
        setType(0);
        setBestseller(false);
        setReference(false);
        setPrice("0.00");
    }

    public Book(JSONObject doc) {
        super(doc);
        setType(0);
        reference = doc.optInt("reference",0)==1 ? true : false;
        bestseller = doc.optInt("bestseller",0)==1 ? true : false;
        publisher = doc.optString("publisher","unknown");
        edition = doc.optInt("edition",-1);
        publicationYear = doc.optInt("publication_year", -1);
    }

    protected boolean reference;

    protected boolean bestseller;

    protected String publisher;

    protected int edition;

    protected int publicationYear;
}