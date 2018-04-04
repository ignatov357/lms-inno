package com.awesprojects.lmsclient.api.data.documents;

import com.awesprojects.lmsclient.api.internal.Responsable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.json.JSONObject;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Article extends Document implements Responsable,Serializable{

    public Article(){
        super();
        setType(1);
    }

    public Article(JSONObject doc) {
        super(doc);
        journalTitle = doc.optString("journal_title",null);
        journalIssuePublicationDate = doc.optString("journal_issue_publication_date", null);
        journalIssueEditors = doc.optString("journal_issue_editors", null);
        if (journalTitle==null)
            journalTitle = doc.optString("journalTitle");
        if (journalIssuePublicationDate==null)
            journalIssuePublicationDate = doc.optString("journalIssuePublicationDate");
        if (journalIssueEditors==null)
            journalIssueEditors = doc.optString("journalIssueEditors");
    }

    protected String journalTitle;

    protected String journalIssuePublicationDate;

    protected String journalIssueEditors;

}
