package com.awesprojects.lmsclient.api.data.documents;

import lombok.Data;
import lombok.ToString;
import org.json.JSONObject;

@Data
@ToString(callSuper = true)
public class EMaterial extends Document {

    public EMaterial(JSONObject doc) {
        super(doc);
    }

}
