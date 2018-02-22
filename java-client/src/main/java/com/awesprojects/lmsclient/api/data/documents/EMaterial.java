package com.awesprojects.lmsclient.api.data.documents;

import com.awesprojects.lmsclient.api.internal.Responsable;
import lombok.Data;
import lombok.ToString;
import org.json.JSONObject;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
public class EMaterial extends Document implements Responsable,Serializable {

    public EMaterial(){
        super();
        setType(2);
    }

    public EMaterial(JSONObject doc) {
        super(doc);
    }

}
