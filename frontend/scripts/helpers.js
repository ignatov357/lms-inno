//###########################################API_FUNCTIONS##############################################################

//-------------------------------------------DOCUMENTS_EDITING----------------------------------------------------------

function addDocument(type, title, count, authors, price, keywords, bestseller, publisher, edition, pub_year, reference, j_title, j_pub_date, j_editors) {
    var accessToken = getAccessTokenFromCookie();
    //console.log(type);

    var req = $.ajax({
        async: false,
        type: "POST",
        url: "http://api.awes-projects.com/manage/documents/addDocument/",
        headers: {"Access-Token": accessToken},
        data: {
            "type": type,
            "title": title,
            "instockCount": count,
            "authors": authors,
            "price": price,
            "keywords": keywords,
            "bestseller": bestseller,
            "publisher": publisher,
            "edition": edition,
            "publicationYear": pub_year,
            "journalTitle": j_title,
            "journalIssuePublicationDate": j_pub_date,
            "journalIssueEditors": j_editors,
            "reference": reference
        },
        success: function (data) {
            alert("Document was created!");
        },
        error: function (obj) {
            alert(obj.responseText);
        }
    });
}

function modifyDocument(id, title, count, authors, price, keywords, bestseller, publisher, edition, pub_year, reference, j_title, j_pub_date, j_editors) {
    var accessToken = getAccessTokenFromCookie();

    var req = $.ajax({
        async: false,
        type: "POST",
        url: "http://api.awes-projects.com/manage/documents/modifyDocument/",
        headers: {"Access-Token": accessToken},
        data: {
            "id": id,
            "title": title,
            "instockCount": count,
            "authors": authors,
            "price": price,
            "keywords": keywords,
            "bestseller": bestseller,
            "publisher": publisher,
            "edition": edition,
            "publicationYear": pub_year,
            "journalTitle": j_title,
            "journalIssuePublicationDate": j_pub_date,
            "journalIssueEditors": j_editors,
            "reference": reference
        },
        success: function (data) {
            alert("Document was modified!");
        },
        error: function (obj) {
            alert(obj.responseText);
        }
    });
}

function deleteDocument(doc_id) {
    var accessToken = getAccessTokenFromCookie();

    var req = $.ajax({
        async: false,
        type: "POST",
        url: "http://api.awes-projects.com/manage/documents/removeDocument/",
        headers: {"Access-Token": accessToken},
        data: {"id": doc_id},
        success: function (data) {
            alert("Document has been deleted!");
        },
        error: function (obj) {
            alert(obj.responseText);
        }
    });
}

//-------------------------------------------DOCUMENTS_INFO-------------------------------------------------------------

function getDocument(document_id) {
    var req = $.ajax({
        async: false,
        url: "http://api.awes-projects.com/documents/getDocument/",
        data: {'id': document_id},
        error: function (obj) {
            alert(obj.responseText);
        }
    });
    if (req.status == 200) {
        return req.responseText;
    }
}

function getDocuments() {
    var req = $.ajax({
        async: false,
        url: "http://api.awes-projects.com/documents/getDocuments/",
        error: function (obj) {
            alert(obj.responseText);
        }
    });
    if (req.status == 200) {
        return req.responseText;
    }
}

//------------------------------------------USERS_EDITING---------------------------------------------------------------

function addUser(name, address, phone, type) {
    var accessToken = getAccessTokenFromCookie();

    var req = $.ajax({
        async: false,
        type: "POST",
        url: "http://api.awes-projects.com/manage/users/addUser/",
        headers: {"Access-Token": accessToken},
        data: {
            "name": name,
            "address": address,
            "phone": phone,
            "type": type
        },
        success: function () {
            alert("User has been added!");
        },
        error: function (obj) {
            alert(obj.responseText);
        }
    })
}

function modifyUser(id, name, address, phone, type) {
    var accessToken = getAccessTokenFromCookie();

    var req = $.ajax({
        async: false,
        type: "POST",
        url: "http://api.awes-projects.com/manage/users/modifyUser/",
        headers: {"Access-Token": accessToken},
        data: {
            "id": id,
            "name": name,
            "address": address,
            "phone": phone,
            "type": type
        },
        success: function () {
            alert("User has been modified!");
        },
        error: function (obj) {
            alert(obj.responseText);
        }
    })
}

function deleteUser(user_id) {
    var accessToken = getAccessTokenFromCookie();

    var req = $.ajax({
        async: false,
        type: "POST",
        url: "http://api.awes-projects.com/manage/users/removeUser/",
        headers: {"Access-Token": accessToken},
        data: {"id": user_id},
        success: function (data) {
            alert("User has been deleted!");
        },
        error: function (obj) {
            alert(obj.responseText);
        }
    });
}

//-----------------------------------------USERS_INFO-------------------------------------------------------------------

function getUser(user_id) {
    var accessToken = getAccessTokenFromCookie();

    var req = $.ajax({
        async: false,
        url: "http://api.awes-projects.com/manage/users/getUser/",
        headers: {"Access-Token": accessToken},
        data: {"id": user_id},
        error: function (obj) {
            alert(obj.responseText);
        }
    });
    if (req.status == 200) {
        return req.responseText;
    }
}

function getUsers() {
    var accessToken = getAccessTokenFromCookie();

    var req = $.ajax({
        async: false,
        url: "http://api.awes-projects.com/manage/users/getUsers/",
        headers: {"Access-Token": accessToken},
        error: function (obj) {
            alert(obj.responseText);
        }
    });
    if (req.status == 200) {
        return req.responseText;
    }
}

function getDocumentsUserCheckedOut(user_id) {
    var accessToken = getAccessTokenFromCookie();

    var req = $.ajax({
        async: false,
        url: "http://api.awes-projects.com/manage/users/getDocumentsUserCheckedOut/",
        headers: {"Access-Token": accessToken},
        data: {"user_id": user_id},
        error: function (obj) {
            alert(obj.responseText);
        }
    });
    if (req.status == 200) {
        //console.log(req.responseText);
        return req.responseText;
    }
}

//-----------------------------------------UTILITY----------------------------------------------------------------------

function generateNewPassword(user_id) {
    var accessToken = getAccessTokenFromCookie();

    var req = $.ajax({
        async: false,
        type: "POST",
        url: "http://api.awes-projects.com/manage/users/generateNewPassword/",
        headers: {"Access-Token": accessToken},
        data: {"id": user_id},
        error: function (obj) {
            alert(obj.responseText);
        }
    });
    if (req.status == 200) {
        var resp = JSON.parse(req.responseText);
        alert("You renewed password for user with id=" + resp.id + "! New password is " + resp.password);
    }
}

function amILibrarian() {
    var accessToken = getAccessTokenFromCookie();

    var req = $.ajax({
        async: false,
        url: "http://api.awes-projects.com/users/getUserInfo/",
        headers: {"Access-Token": accessToken},
        error: function (obj) {
            alert(obj.responseJSON.errorMessage);
        }
    });

    if (req.status == 200 && JSON.parse(req.responseText).type == 0) {
        return true;
    } else {
        return false;
    }
}

function getAccessTokenFromCookie() {
    return document.cookie.replace("accessToken=", "");
}

function reqGetAccessToken(userid, pass) {
    var req = $.ajax({
        async: false,
        url: "http://api.awes-projects.com/users/getAccessToken/",
        data: "userID=" + userid + "&password=" + pass,
        type: "POST",
        error: function (obj, errorStatus) {
            alert("Server returned " + errorStatus);
            this.responseText = null;
        }
    });
    return req.responseText || false;
}

//----------------------------------------RETURN_SYSTEM-----------------------------------------------------------------

function checkoutDocument(document_id) {
    var accessToken = getAccessTokenFromCookie();
    var req = $.ajax({
        async: false,
        url: "http://api.awes-projects.com/documents/checkOutDocument/",
        data: "documentID=" + document_id,
        type: "POST",
        dataType: "JSON",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('Access-Token', accessToken);
        },
        success: function (data) {
            alert("You have checked out a book! \nPlease, return it until " + new Date(data.returnTill * 1000).toUTCString());
        },
        //headers: {'Access-Token':accessToken},
        error: function (obj) {
            alert(obj.responseJSON.errorMessage);
            this.responseText = null;
        }
    });
}

function returnDocument(user_id, document_id) {
    var accessToken = getAccessTokenFromCookie();
    var req = $.ajax({
        async: false,
        type: "POST",
        url: "http://api.awes-projects.com/manage/users/userReturnedDocument/",
        headers: {"Access-Token": accessToken},
        data: {
            "userID": user_id,
            "documentID": document_id
        },
        success: function () {
            alert("User has returned document!");
        },
        error: function (obj) {
            alert(obj.responseText);
        }
    })
}

function renewDocument(document_id) {
    var req = $.ajax({
        async: false,
        type: "POST",
        url: "http://api.awes-projects.com/documents/renewDocument/",
        headers: {"Access-Token": getAccessTokenFromCookie()},
        data : {
            "documentID" : document_id
        },
        success: function () {
            alert("You have renewed document!");
        },
        error: function (obj) {
            alert(obj.responseText);
        }
    })

}

//########################################CATALOGUE#####################################################################

function formHTMLString(query, query_or_response, json_str) {
    var html_string = "";
    json_str = json_str || "";
    if (query_or_response) {
        switch (query) {
            case "getDocuments":
                html_string = "<p><button id='q-apply'>Get documents</button></p>";
                break;
            case "getDocument":
                html_string = "<p>Document ID: <input id='q-gd-id'></p>" +
                    "<p><button id='q-apply'>Get document</button></p>";
                break;
            case "addDocument":
                html_string = "<p>User ID:<input id='q-ad-uid'> User password: <input id='q-ad-ups' type='password'> (For Access Token)<br/></p>" +
                    "<p>Type of book: <select id='q-ad-slt'><option name='0'>Book</option><option name='1'>Journal</option><option name='2'>Media</option></select></p>" +
                    "<p>Title: <input id='q-ad-ti'> Count: <input id='q-ad-ic'> Authors: <input id='q-ad-au'> Price: <input id='q-ad-pr'></p>" +
                    "<p>Keywords: <input id='q-ad-kw'> Is Bestseller: <select id='q-ad-slbs'><option name='0'>No</option><option name='1'>Yes</option> </select></p> " +
                    "<p>Publisher: <input id='q-ad-pb'> Edition: <input id='q-ad-ed'> Publication year: <input id='q-ad-py'></p>" +
                    "<p>Journal Title: <input id='q-ad-jti'> Journal publication date: <input id='q-ad-jpd'> Journal editors: <input id='q-ad-jed'></p>" +
                    "<p><button id='q-apply'>Add document</button></p>";
                break;
            case "modifyDocument":
                break;
            case "removeDocument":
                break;
            case "addUser":
                break;
            case "removeUser":
                break;
            case "generateNewPassword":
                break;
            case "bookDocument":
                break;
            case "changePassword":
                break;
            case "getAccessToken":
                break;
            default:
                html_string = "Wrong request";
        }
    } else {
        switch (query) {
            case "generateNewPassword":
                break;
            case "getDocument":
                var json_obj = JSON.parse(json_str);

                html_string = "<h3>Title: " + json_obj.title + "</h3>" +
                    "<h4>Written by <i>" + json_obj.authors + "</i></h4>" +
                    "<p>Count:" + json_obj.instockCount + "<br/>Price:" + json_obj.price + "<br/>ID:" +
                    json_obj.id + "<br/>Keywords:" + json_obj.keywords + "</p>";
                break;
            case "getDocuments":
                var json_obj = JSON.parse(json_str);

                json_obj.forEach(function (element) {
                    html_string += formHTMLString("getDocument", false, JSON.stringify(element)) + "<br/>";
                });
                break;
            case "changePassword":
                break;
            case "getAccessToken":
                break;
            default:
                html_string = "Wrong request";
        }
    }

    return html_string;
}

function getDocumentsField() {
    var req = $.ajax({
        async: false,
        url: "http://api.awes-projects.com/documents/getDocuments/",
        error: function (obj, errorStatus) {
            alert("Server returned " + errorStatus);
            this.responseText = null;
        }
    });
    if (req.responseText) {
        var html_text = "";
        JSON.parse(req.responseText).forEach(function (value) {
            html_text += "<div id=book-field-" + value.id + " style='margin-left: 4%;margin-top: 4%; width: 29%;background: #e6ee9c;padding: 10px'><h3>" + value.title + "</h3><p>Written by <i>" + value.authors
                + "</i></p><br/>";
            var keywords = value.keywords.split(",");
            //console.log(JSON.stringify(value));
            keywords.forEach(function (value2) {
                html_text += "<div style='padding: 5px;border: lightskyblue solid 3px; border-radius: 4px;text-align: center;margin-left: 10px;width: 26%'>" +
                    value2 + "</div>";
            });

            if (value['type'] == 0) {
                html_text += "<br/><br/><i>Was published in " + value.publicationYear + " by " + value.publisher + "</i>";
                if (value.edition == 1) {
                    html_text += "<p>" + value.edition + "st edition</p>";
                } else if (value.edition == 2) {
                    html_text += "<p>" + value.edition + "nd edition</p>";
                } else if (value.edition == 3) {
                    html_text += "<p>" + value.edition + "rd edition</p>";
                } else {
                    html_text += "<p>" + value.edition + "th edition</p>";
                }
                if (value.bestseller == 1) {
                    html_text += "<div style='background: #fff123; padding: 5px'><p><b>BESTSELLER</b></p></div>";
                }
            }

            if(value['type'] == 1){
                html_text+="<br><br><p><i>Article in journal <b>" + value.journalTitle +"</b> published <b>" + value.journalIssuePublicationDate +"</b> edited by " + value.journalIssueEditors +"</i></p>"
            }



            if (value.reference == 1) {
                html_text += "<p><i>This is reference document. You cannot checkout it</i></p></div>";
                return;
            }

            html_text += "<br/><button style='padding: 5px;' class='btn' id='checkout-book-" + value.id + "'><p><b>CHECKOUT</b></p></button><p><i>" + value.instockCount + " in stock</i></p></div>";
        });
        $("#books-field").empty();
        $("#books-field").append(html_text);
        $(".btn").click(function () {
            var book_id = $(this).attr("id").replace("checkout-book-", "");
            checkoutDocument(book_id);
            getDocumentsField();
        });
    } else return false;
}

function getUsersDocumentsFiled() {
    var accessToken = getAccessTokenFromCookie();
    var req = $.ajax({
        async: false,
        url: "http://api.awes-projects.com/users/getCheckedOutDocuments/",
        headers: {'Access-Token': accessToken},
        error: function (obj) {
            alert(obj, responseJSON.errorMessage);
        }
    });

    if (req.status == 200) {
        var html_text = "";
        JSON.parse(req.responseText).forEach(function (val) {
            var value = val.documentInfo;
            if (value == null) {
                return;
            }
            html_text += "<div id=book-field-" + value.id + " style='margin-left: 4%;margin-top: 4%; width: 29%;background: #e6ee9c;padding: 10px'><h3>" + value.title + "</h3><p>Written by <i>" + value.authors
                + "</i></p><br/>";
            var keywords = value.keywords.split(",");
            //console.log(JSON.stringify(value));
            keywords.forEach(function (value2) {
                html_text += "<div style='padding: 5px;border: lightskyblue solid 3px; border-radius: 4px;text-align: center;margin-left: 10px;width: 26%'>" +
                    value2 + "</div>";
            });

            if (value['type'] == 0) {
                html_text += "<br/><br/><i>Was published in " + value.publicationYear + " by " + value.publisher + "</i>";
                if (value.edition == 1) {
                    html_text += "<p>" + value.edition + "st edition</p>";
                } else if (value.edition == 2) {
                    html_text += "<p>" + value.edition + "nd edition</p>";
                } else if (value.edition == 3) {
                    html_text += "<p>" + value.edition + "rd edition</p>";
                } else {
                    html_text += "<p>" + value.edition + "th edition</p>";
                }

                if (value.bestseller == 1) {
                    html_text += "<div style='background: #fff123; padding: 5px'><p><b>BESTSELLER</b></p></div>";
                }
            }

            if(value['type'] == 1){
                html_text+="<br><br><p><i>Article in journal <b>" + value.journalTitle +"</b> published <b>" + value.journalIssuePublicationDate +"</b> edited by " + value.journalIssueEditors +"</i></p>"
            }


            html_text += "<br/><h4><i>Return till " + new Date(val.returnTill * 1000).toUTCString() + "</i></h4>" +
                "<button class='renew' name='" + value.id + "'>RENEW DOCUMENT</button></div>";
        });
        $("#books-field").empty();
        $("#books-field").append(html_text);
        $(".renew").click(function () {
            renewDocument($(this).attr("name"));
            getUsersDocumentsFiled();
        });
    }
}

//#######################################LIBRARIAN######################################################################

function updateUsersTable(object) {
    var html = "<table id='content-tab' class='adm-table'><tr class='unselectable'><th>ID</th><th>Type</th><th>Name</th><th>Address</th><th>Phone</th><th>Info</th></tr>";

    var users_table = JSON.parse(getUsers());

    users_table.forEach(function (value) {
        var type = "";
        if (value.type == 0) {
            type = "Librarian";
        } else if (value.type == 1) {
            type = "Student";
        } else if (value.type == 2) {
            type = "TA";
        } else if (value.type == 3) {
            type = "Professor";
        } else if (value.type == 4) {
            type = "Instructor";
        } else {
            type = "VP";
        }
        html += "<tr><td>" + value.id + "</td><td>" + type + "</td><td>" + value.name + "</td><td>" + value.address + "</td><td>" + value.phone + "</td><td><a href='' class='info-cell' name='" + value.id + "'>More...</a></td> </tr>";
    });

    html += "</table>";
    $(object).empty();
    $(object).append(html);
    $(".info-cell").click(function () {
        console.log("More clicked");
        var id = $(this).attr("name");
        popupShow();

        popupMoreUser($("#popup"), id);
        return false;
    });
    $("#content-tab tr:not(.unselectable)").click(function () {
        $(this).addClass("selected").siblings().removeClass("selected");
    });
}

function updateDocumentsTable(object) {
    var html = "<table id='content-tab' class='adm-table'><tr class='unselectable'><th>ID</th><th>Type</th><th>Title</th><th>Authors</th><th>Price</th><th>Info</th></tr>";

    var docs_table = JSON.parse(getDocuments());

    docs_table.forEach(function (value) {
        var type = "";
        if (value.type == 0) {
            type = "Book";
        } else if (value.type == 1) {
            type = "Article";
        } else {
            type = "Media";
        }

        html += "<tr><td>" + value.id + "</td><td>" + type + "</td><td>" + value.title + "</td><td>" + value.authors + "</td><td>" + value.price + "</td><td class='info-cell' name='" + value.id + "'><a href='#'>More...</a></td></tr>";
    });

    html += "</table>";

    $(object).empty();
    $(object).append(html);
    $(".info-cell").click(function () {
        console.log("More clicked");
        var id = $(this).attr("name");
        popupShow();

        popupMoreDocument($("#popup"), id);
        return false;
    });
    $("#content-tab tr:not(.unselectable)").click(function () {
        $(this).addClass("selected").siblings().removeClass("selected");
    });
}

function popupAddDocument(object) {
    $(object).empty();

    var html = "<select id='popup-sel'><option selected='true' disabled>Choose type of document</option><option name='0'>Book</option><option name='1'>Article</option><option name='2'>Media</option> </select>" +
        "<p>Title: <input id='title'>  Count: <input id='count' type='number'>  Authors: <input id='authors'></p>" +
        "<p>Price: <input id='price' type='number'>  Keywords: <input id='keywords'></p>" +
        "<div id='add-info'></div>";
    $(object).append(html);

    $("#popup-sel").change(function () {
        //console.log("selected " + $(this, "option:selected").attr("name"));
        $("#add-info").empty();
        switch ($("#popup-sel option:selected").attr("name")) {
            case '0':
                html = "<p><label for='bestseller'>Bestseller</label>: <input id='bestseller' type='checkbox' value='best'>  Publisher: <input id='publisher'>  Edition: <input id='edition' type='number'></p>" +
                    "<p>Publication year: <input id='pub-year' type='number' min='1900' max='2018'>  <label for='reference'>Reference</label>: <input id='reference' type='checkbox' value='ref'></p>";
                $("#add-info").append(html);
                break;
            case '1':
                html = "<p>Journal Title:  <input id='j-title'>  Publication date: <input id='j-pub-date' type='date'>  Editors: <input id='j-editors'></p>" +
                    "<p>Reference: <input id='reference' type='checkbox'></p>";
                $("#add-info").append(html);
                break;
        }
    });

    html = "<p><button id='apply'>Add</button><button id='exit'>Cancel</button>";
    $(object).append(html);

    $("#apply").click(function () {
        if ($("#popup-sel option:selected").html()) {
            var type = $("#popup-sel option:selected").attr("name");
            var title = $("#title").val();
            var count = $("#count").val();
            var authors = $("#authors").val();
            var price = $("#price").val();
            var keywords = $("#keywords").val();
            var bestseller = $("#besteller:checked").val() ? '1' : '0';
            var publisher = $("#publisher").val() || null;
            var edition = $("#edition").val() || null;
            var pub_year = $("#pub-year").val() || null;
            var reference = $("#reference:checked").val() ? '1' : '0';
            var j_title = $("#j-title").val() || null;
            var j_pub_date = $("#j-pub-date").val() || null;
            var j_editors = $("#j-editors").val() || null;

            addDocument(type, title, count, authors, price, keywords, bestseller, publisher, edition, pub_year, reference, j_title, j_pub_date, j_editors);
            $(object).empty();
            popupHide();
            updateDocumentsTable($("#content-table"));
        } else {
            alert("Choose a type!");
        }

        return false;
    });
    $("#exit, #hider").click(function () {
        $(object).empty();
        popupHide();
    });

}

function popupModifyDocument(object, id) {
    $(object).empty();

    var doc = JSON.parse(getDocument(id));

    var html = "<p>Title: <input id='title' value='" + doc.title + "'>  Count: <input id='count' type='number' value='" + doc.instockCount + "'>" +
        "  Authors: <input id='authors' value='" + doc.authors + "'></p>" +
        "<p>Price: <input id='price' type='number' value='" + doc.price + "'>  Keywords: <input id='keywords' value='" + doc.keywords + "'></p>";
    //$(object).append(html);


    switch (doc.type) {
        case 0:
            var refer = doc.reference == '1' ? "checked" : "";
            var bestsell = doc.bestseller == '1' ? "checked" : "";
            html += "<p><label for='bestseller'>Bestseller</label>: <input id='bestseller' type='checkbox' value='best' " + bestsell + ">" +
                "  Publisher: <input id='publisher' value='" + doc.publisher + "'>  Edition: <input id='edition' type='number' value='" + doc.edition + "'></p>" +
                "<p>Publication year: <input id='pub-year' type='number' min='1900' max='2018' value='" + doc.publicationYear + "'>  " +
                "<label for='reference'>Reference</label>: <input id='reference' type='checkbox' value='ref' " + refer + "></p>";
            $("#add-info").append(html);
            break;
        case 1:
            var refer = doc.reference == '1' ? "checked" : "";
            html += "<p>Journal Title:  <input id='j-title' value='" + doc.journalTitle + "'>  Publication date: <input id='j-pub-date' type='date' value='" + doc.journalIssuePublicationDate + "'>" +
                "  Editors: <input id='j-editors' value='" + doc.journalIssueEditors + "'> Reference: <input id='reference' type='checkbox' " + refer +"></p>";
            $("#add-info").append(html);
            break;
    }

    html += "<p><button id='apply'>Save</button><button id='exit'>Cancel</button>";
    $(object).append(html);

    $("#apply").click(function () {
        var title = $("#title").val();
        var count = $("#count").val();
        var authors = $("#authors").val();
        var price = $("#price").val();
        var keywords = $("#keywords").val();
        var bestseller = $("#besteller:checked").val() ? '1' : '0';
        var publisher = $("#publisher").val() || null;
        var edition = $("#edition").val() || null;
        var pub_year = $("#pub-year").val() || null;
        var reference = $("#reference:checked").val() ? '1' : '0';
        var j_title = $("#j-title").val() || null;
        var j_pub_date = $("#j-pub-date").val() || null;
        var j_editors = $("#j-editors").val() || null;

        modifyDocument(id, title, count, authors, price, keywords, bestseller, publisher, edition, pub_year, reference, j_title, j_pub_date, j_editors);
        $(object).empty();
        popupHide();
        updateDocumentsTable($("#content-table"));

        return false;
    });
    $("#exit, #hider").click(function () {
        $(object).empty();
        popupHide();
    });
}

function popupMoreDocument(object, id) {
    $(object).empty();
    var doc = JSON.parse(getDocument(id));

    var type = doc.type == 0 ? 'Book' : (doc.type == 1 ? 'Article' : 'Media');
    var edition = doc.edition == 1 ? doc.edition + 'st' : (doc.edition == 2 ? doc.edition + 'nd' : (doc.edition == 3 ? doc.edition + 'rd' : doc.edition + 'th'));
    var descr = doc.type == 1 ? '(published ' + doc.journalIssuePublicationDate + ')' + ' in journal <b>' + doc.journalTitle + '</b> edited by <i>' + doc.journalIssueEditors + '</i>' : (doc.type == 0 ? 'published in ' + doc.publicationYear + ' (' + edition + ' edition)' : '');

    var bestseller = doc.bestseller == 1 ? "<div style='padding: 10px; background: yellow'>BESTSELLER</div> " : "";
    var reference = doc.reference == 1 ? "<div style='padding: 10px; background: red'>REFERENCE</div> " : "";

    var html = "<h2>" + doc.title + "</h2>" +
        "<p><i>by " + doc.authors + "</i></p></br></br>" +
        "<p><b>" + type + "</b> " + descr + "</p>" +
        "</br><p>" + doc.instockCount + " copies left (price: " + doc.price + ")</p>" +
        "<p>Keywords: <i>" + doc.keywords + "</i></p>" +
        bestseller + "</br>" + reference;

    $(object).append(html);

    $("#hider").click(function () {
        popupHide();
    });
}

function popupShow() {
    $("#hider, #popup").css("visibility", "visible");
}

function popupHide() {
    $("#hider, #popup").css("visibility", "hidden");
}

function popupAddUser(object) {
    $(object).empty();

    var html = "<select id='popup-sel'><option selected='true' disabled>Choose a type of user</option><option name='0'>Librarian</option><option name='1'>Student</option><option name='2'>TA</option><option name='3'>Professor</option>" +
        "<option name='4'>Instructor</option><option name='5'>VP</option> </select>" +
        "<p>Name:</br> <input id='name'><br>  Address:</br> <input id='address'> <br> Phone number:</br> <input id='phone'></p></br>" +
        "<p><button id='apply'>ADD</button>  <button id='exit'>CANCEL</button></p>";

    $(object).append(html);

    $("#apply").click(function () {
        if ($("#popup-sel option:selected").html()) {
            var type = $("#popup-sel option:selected").attr("name");
            var name = $("#name").val();
            var address = $("#address").val();
            var phone = $("#phone").val();

            addUser(name, address, phone, type);
            $(object).empty();
            popupHide();
            updateUsersTable($("#content-table"));
        } else {
            alert("Choose a type!");
        }

        return false;
    });
    $("#exit, #hider").click(function () {
        $(object).empty();
        popupHide();
    });
}

function popupModifyUser(object, id) {
    $(object).empty();

    var userInfo = JSON.parse(getUser(id));

    var sel0 = userInfo.type == 0 ? "selected='true'" : "";
    var sel1 = userInfo.type == 1 ? "selected='true'" : "";
    var sel2 = userInfo.type == 2 ? "selected='true'" : "";
    var sel3 = userInfo.type == 3 ? "selected='true'" : "";
    var sel4 = userInfo.type == 4 ? "selected='true'" : "";
    var sel5 = userInfo.type == 5 ? "selected='true'" : "";

    var html = "<select id='popup-sel'><option selected='true' disabled>Choose a type of user</option><option name='0' " + sel0 + ">Librarian</option><option name='1' " + sel1 + ">Student</option><option name='2' " + sel2 + ">TA</option>" +
        "<option name='3' " + sel3 + ">Professor</option><option name='4' " + sel4 + ">Instructor</option><option name='5' " + sel5 + ">VP</option> </select>" +
        "<p>Name:</br> <input id='name' value='" + userInfo.name + "'> <br> Address:</br> <input id='address' value='" + userInfo.address + "'> <br> Phone number:</br> <input id='phone' value='" + userInfo.phone + "'></p></br>" +
        "<p><button id='apply'>SAVE</button>  <button id='exit'>CANCEL</button></p>";

    $(object).append(html);

    $("#apply").click(function () {
        if ($("#popup-sel option:selected").html()) {
            var type = $("#popup-sel option:selected").attr("name");
            var name = $("#name").val();
            var address = $("#address").val();
            var phone = $("#phone").val();

            modifyUser(id, name, address, phone, type);
            $(object).empty();
            popupHide();
            updateUsersTable($("#content-table"));
        } else {
            alert("Choose a type!");
        }

        return false;
    });
    $("#exit, #hider").click(function () {
        $(object).empty();
        popupHide();
    });
}

function popupMoreUser(object, id) {
    $(object).empty();
    var user = JSON.parse(getUser(id));

    var type = "";
    switch (user.type){
        case 0:
            type = "Librarian";
            break;
        case 1:
            type = "Student";
            break;
        case 2:
            type = "TA";
            break;
        case 3:
            type = "Professor";
            break;
        case 4:
            type = "Instructor";
            break;
        case 5:
            type = "VP";
            break;
    }

    var html = "<h2>" + user.name + "</h2>" +
        "<p><i>" + type + "</i></p></br>" +
        "<p>Phone number: " + user.phone + "</p>" +
        "<p>Address: " + user.address + "</p></br>" +
        "<div id='usr-doc-table' style='position: absolute; top: 50%; left: 0; width: 100%; height: 40%; overflow-y: scroll; '></div></br>";

    $(object).append(html);
    var checker = updateUserCheckedOutDocumentsTable($("#usr-doc-table"), id);

    if (checker) {
        html = "<button id='return' style='position: absolute; top: 92%; left: 1%;padding: 10px;' class='btn'>RETURN DOCUMENT</button>  <button id='exit' style='position: absolute; top: 92%; left: 50%;padding: 10px;' class='btn'>CANCEL</button>";
        $(object).append(html);
    }



    $("#return").click(function () {
        var doc_id = $("#usr-doc tr.selected td:first").html();

        returnDocument(id, doc_id);
        $("#usr-doc-table").empty();
        updateUserCheckedOutDocumentsTable($("#usr-doc-table"), id);
    });
    $("#exit, #hider").click(function () {
        $(object).empty();
        popupHide();
    });

}

function updateUserCheckedOutDocumentsTable(object, id) {
    var html = "<table id='usr-doc' class='adm-table'><tr class='unselectable'><th>ID</th><th>Type</th><th>Title</th><th>Authors</th><th>Return till</th><th>Info</th></tr>";

    var docs_table = JSON.parse(getDocumentsUserCheckedOut(id));
    if (docs_table == (null || undefined) || (docs_table != null && docs_table.length == '1' && docs_table[0].documentInfo == (undefined || null)) || docs_table.length == '0') {
        $(object).append("<p>No checked out documents</p>");
        return false;
    }
    console.log("docs_table is " + docs_table + ", docs_table.lenght is " + docs_table.length )
    console.log("docs_table[0].documentInfo is " + docs_table[0].documentInfo);

    docs_table.forEach(function (val) {
        var value = val.documentInfo;
        if (value == null) {
            return;
        }
        var type = "";
        if (value.type == 0) {
            type = "Book";
        } else if (value.type == 1) {
            type = "Article";
        } else {
            type = "Media";
        }

        html += "<tr><td>" + value.id + "</td><td>" + type + "</td><td>" + value.title + "</td><td>" + value.authors + "</td><td>" + new Date(val.returnTill * 1000).toUTCString() + "</td><td class='info-cell-2' name='" + value.id + "'><a href='#'>More...</a></td></tr>";
    });

    html += "</table>";

    $(object).empty();
    $(object).append(html);
    $(".info-cell-2").click(function () {
        console.log("More clicked");
        var doc_id = $(this).attr("name");
        $("#hider-2, #popup-2").css("visibility", "visible");

        popupMoreUserDocument($("#popup-2"), doc_id);
        return false;
    });
    $("#usr-doc tr:not(.unselectable)").click(function () {
        $(this).addClass("selected").siblings().removeClass("selected");
    });

    return true;
}

function popupMoreUserDocument(object, id) {
    $(object).empty();
    var doc = JSON.parse(getDocument(id));

    var type = doc.type == 0 ? 'Book' : (doc.type == 1 ? 'Article' : 'Media');
    var edition = doc.edition == 1 ? doc.edition + 'st' : (doc.edition == 2 ? doc.edition + 'nd' : (doc.edition == 3 ? doc.edition + 'rd' : doc.edition + 'th'));
    var descr = doc.type == 1 ? '(published ' + doc.journalIssuePublicationDate + ')' + ' in journal <b>' + doc.journalTitle + '</b> edited by <i>' + doc.journalIssueEditors + '</i>' : (doc.type == 0 ? 'published in ' + doc.publicationYear + ' (' + edition + ' edition)' : '');

    var bestseller = doc.bestseller == 1 ? "<div style='padding: 10px; background: yellow'>BESTSELLER</div> " : "";
    var reference = doc.reference == 1 ? "<div style='padding: 10px; background: red'>REFERENCE</div> " : "";

    var html = "<h2>" + doc.title + "</h2>" +
        "<p><i>by " + doc.authors + "</i></p></br></br>" +
        "<p><b>" + type + "</b> " + descr + "</p>" +
        "</br><p>" + doc.instockCount + " copies left (price: " + doc.price + ")</p>" +
        "<p>Keywords: <i>" + doc.keywords + "</i></p>" +
        bestseller + "</br>" + reference;

    $(object).append(html);

    $("#hider-2").click(function () {
        $("#hider-2, #popup-2").css("visibility", "hidden");
    });
}