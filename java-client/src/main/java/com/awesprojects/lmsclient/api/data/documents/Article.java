package com.awesprojects.lmsclient.api.data.documents;

import com.awesprojects.lmsclient.api.internal.Responsable;
import lombok.Data;
import lombok.ToString;
import org.json.JSONObject;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
public class Article extends Document implements Responsable,Serializable{

    public Article(){
        super();
        setType(1);
    }

    public Article(JSONObject doc) {
        super(doc);
        journalTitle = doc.optString("journal_title");
        journalIssuePublicationDate = doc.optString("journal_issue_publication_date");
        journalIssueEditors = doc.optString("journal_issue_editors");
    }

    protected String journalTitle;

    protected String journalIssuePublicationDate;

    protected String journalIssueEditors;

}
