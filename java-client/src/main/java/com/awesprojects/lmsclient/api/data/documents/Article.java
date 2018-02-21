package com.awesprojects.lmsclient.api.data.documents;

import lombok.Data;
import lombok.ToString;
import org.json.JSONObject;

@Data
@ToString(callSuper = true)
public class Article extends Document{

    public Article(JSONObject doc) {
        super(doc);
        journalTitle = doc.optString("journal_title");
        journalIssuePublicationDate = doc.optString("journal_issue_publication_date");
        journalIssueEditors = doc.optString("journal_issue_editors");
    }

    public String journalTitle;

    public String journalIssuePublicationDate;

    public String journalIssueEditors;

}
