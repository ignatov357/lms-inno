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

function getUserStoryHTML(user_story) {
    switch (user_story) {
        case "1":
            var req = new XMLHttpRequest();

            req.open("GET", "http://api.awes-projects.com/documents/getDocuments/", false);
            req.send(null);
            if (req.status == 200) {
                var json_obj = JSON.parse(req.responseText);
                var html_text = "";

                json_obj.forEach(function (value) {
                    html_text += "<a name='" + value.id + "' id='show-book'><div>" + formHTMLString("getDocument", false, JSON.stringify(value)) + "</div></a>";
                });

                return html_text;
            } else {
                alert("Server returned error: " + req.status + " " + req.statusText);
            }

    }
}

function reqGetDocuments() {
    var result = $.ajax({
        async: false,
        url: "http://api.awes-projects.com/documents/getDocuments",
        success: function (data) {
            //alert(data);
            this.responseText = data;
        }
    });
    //console.log(result.responseText);
    return result.responseText;
}

//function reqGetDocument()
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

function getDocumentsField() {
    var req = $.ajax({
        async: false,
        url: "http://api.awes-projects.com/documents/getDocuments/?availableOnly=1",
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

            if(value['type'] == 0){
                html_text+="<br/><br/><i>Was published in " + value.publicationYear + " by " + value.publisher + "</i>";
                if(value.edition == 1) {
                    html_text += "<p>" + value.edition + "st edition</p>";
                } else if(value.edition == 2){
                    html_text += "<p>" + value.edition + "nd edition</p>";
                } else if(value.edition == 3){
                    html_text += "<p>" + value.edition + "rd edition</p>";
                } else {
                    html_text += "<p>" + value.edition + "th edition</p>";
                }
            }

            if(value.bestseller == 1){
                html_text+="<div style='background: #fff123; padding: 10px'><h4>BESTSELLER</h4></div>";
            }

            if(value.reference == 1){
                html_text+="<p><i>This is reference book. You cannot checkout it</i></p></div>";
                return;
            }

            html_text += "<br/><button style='padding: 5px;' class='btn' id='checkout-book-" + value.id + "'>CHECKOUT</button><p>" + value.instockCount + " in stock</p></div>";
        });
        return html_text;
    } else return false;
}

function getAccessTokenFromCookie(){
    return document.cookie.replace("accessToken=", "");
}

function checkoutBook(book_id){
    var accessToken = getAccessTokenFromCookie();
    var req = $.ajax({
        async: false,
        url: "http://api.awes-projects.com/documents/checkOutDocument/",
        data: "documentID=" + book_id,
        type: "POST",
        dataType: "JSON",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('Access-Token', accessToken);
        },
        success: function(data) {
            alert("You have checked out a book! \nPlease, return it until " + new Date(data.returnTill * 1000).toUTCString());
        },
        //headers: {'Access-Token':accessToken},
        error: function (obj) {
            alert(obj.responseJSON.errorMessage);
            this.responseText = null;
        }
    });
}