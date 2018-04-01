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
            }

            if (value.bestseller == 1) {
                html_text += "<div style='background: #fff123; padding: 5px'><p><b>BESTSELLER</b></p></div>";
            }

            if (value.reference == 1) {
                html_text += "<p><i>This is reference book. You cannot checkout it</i></p></div>";
                return;
            }

            html_text += "<br/><button style='padding: 5px;' class='btn' id='checkout-book-" + value.id + "'><p><b>CHECKOUT</b></p></button><p><i>" + value.instockCount + " in stock</i></p></div>";
        });
        return html_text;
    } else return false;
}

function getAccessTokenFromCookie() {
    return document.cookie.replace("accessToken=", "");
}

function checkoutBook(book_id) {
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
            }

            if (value.bestseller == 1) {
                html_text += "<div style='background: #fff123; padding: 5px'><p><b>BESTSELLER</b></p></div>";
            }

            html_text += "<br/><h4><i>Return till " + new Date(val.returnTill * 1000).toUTCString() + "</i></h4></div>";
        });
        return html_text;
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

    if (req.status == 200 && JSON.parse(req.responseText).type == 2) {
        return true;
    } else {
        return false;
    }
}

function getAdmTable(function_name, additional) {
    additional = additional || null;
    var html_str = "";
    switch (function_name) {
        case "addDocument":
            //console.log("Getting table of " + function_name);
            html_str += "<table cellpadding=\"2\" border=\"2\" width=\"70%\" style=\"margin-left: 10%;\"><tr><th>#</th><th>Property Name</th><th>Value</th></tr>" +
                "<tr><td>1</td><td>Type</td><td id='type' contenteditable='true'></td></tr>" +
                "<tr><td>2</td><td>Title</td><td id='title' contenteditable='true'></td></tr>" +
                "<tr><td>3</td><td>Count</td><td id='instockCount' contenteditable='true'></td></tr>" +
                "<tr><td>4</td><td>Authors</td><td id='authors' contenteditable='true'></td></tr>" +
                "<tr><td>5</td><td>Price</td><td id='price' contenteditable='true'></td></tr>" +
                "<tr><td>6</td><td>Keywords</td><td id='keywords' contenteditable='true'></td></tr>" +
                "<tr><td>7</td><td>*Bestseller</td><td id='bestseller' contenteditable='true'></td></tr>" +
                "<tr><td>8</td><td>*Publisher</td><td id='publisher' contenteditable='true'></td></tr>" +
                "<tr><td>9</td><td>*Edition</td><td id='edition' contenteditable='true'></td></tr>" +
                "<tr><td>10</td><td>*Reference</td><td id='reference' contenteditable='true'></td> </tr>" +
                "<tr><td>11</td><td>*Publication Year</td><td id='publicationYear' contenteditable='true'></td></tr>" +
                "<tr><td>12</td><td>**Journal Title</td><td id='journalTitle' contenteditable='true'></td></tr>" +
                "<tr><td>13</td><td>**Journal Publication Date</td><td id='journalIssuePublicationDate' contenteditable='true'></td></tr>" +
                "<tr><td>14</td><td>**Journal Editors</td><td id='journalIssueEditors' contenteditable='true'></td></tr></table><br/>" +
                "<p><i>* - required only for books</i></p>" +
                "<p><i>** - required only for journals</i></p>" +
                "<br/><button class='btn' id='apply'>CREATE</button>";
            break;
        case "getDocuments":
            var docs = JSON.parse(getDocuments());
            html_str += "<table cellpadding=\"2\" border=\"2\" width=\"80%\" style=\"margin-left: 10%;\"><tr><th>#</th><th>Type</th><th>Title</th><th>Authors</th><th>Keywords</th></tr>";
            docs.forEach(function (value, index) {
                html_str += "<tr><td>" + value.id + "</td><td>" + value.type + "</td><td>" + value.title + "</td><td>" + value.authors + "</td><td>" + value.keywords + "</td></tr>";
            });
            html_str += "</table>";
            break;
        case "modifyDocument":
            var docs = JSON.parse(getDocuments());
            html_str += "<table cellpadding=\"2\" border=\"2\" width=\"80%\" style=\"margin-left: 10%;\"><tr><th>#</th><th>Type</th><th>Title</th><th>Authors</th><th>Keywords</th></tr>";
            docs.forEach(function (value, index) {
                html_str += "<tr><td>" + value.id + "</td>" +
                    "<td>" + value.type + "</td>" +
                    "<td>" + value.title + "</td>" +
                    "<td>" + value.authors + "</td>" +
                    "<td>" + value.keywords + "</td></tr>";
            });
            html_str += "</table><br/><button class='btn' id='apply'>MODIFY SELECTED</button>";
            break;
        case "modifyDocument2":
            var doc_info = JSON.parse(getDocument(additional));
            console.log(doc_info);
            html_str += "<table cellpadding=\"2\" border=\"2\" width=\"70%\" style=\"margin-left: 10%;\"><tr><th>#</th><th>Property Name</th><th>Value</th></tr>" +
                "<tr><td>2</td><td>Title</td><td id='title' contenteditable='true'>" + doc_info.title + "</td></tr>" +
                "<tr><td>3</td><td>Count</td><td id='instockCount' contenteditable='true'>" + doc_info.instockCount + "</td></tr>" +
                "<tr><td>4</td><td>Authors</td><td id='authors' contenteditable='true'>" + doc_info.authors + "</td></tr>" +
                "<tr><td>5</td><td>Price</td><td id='price' contenteditable='true'>" + doc_info.price + "</td></tr>" +
                "<tr><td>6</td><td>Keywords</td><td id='keywords' contenteditable='true'>" + doc_info.keywords + "</td></tr>" +
                "<tr><td>7</td><td>*Bestseller</td><td id='bestseller' contenteditable='true'>" + doc_info.bestseller + "</td></tr>" +
                "<tr><td>8</td><td>*Publisher</td><td id='publisher' contenteditable='true'>" + doc_info.publisher + "</td></tr>" +
                "<tr><td>9</td><td>*Edition</td><td id='edition' contenteditable='true'>"+ doc_info.edition + "</td></tr>" +
                "<tr><td>10</td><td>*Reference</td><td id='reference' contenteditable='true'>" + doc_info.reference + "</td> </tr>" +
                "<tr><td>11</td><td>*Publication Year</td><td id='publicationYear' contenteditable='true'>" + doc_info.publicationYear + "</td></tr>" +
                "<tr><td>12</td><td>**Journal Title</td><td id='journalTitle' contenteditable='true'>" + doc_info.journalTitle + "</td></tr>" +
                "<tr><td>13</td><td>**Journal Publication Date</td><td id='journalIssuePublicationDate' contenteditable='true'>" + doc_info.journalIssuePublicationDate + "</td></tr>" +
                "<tr><td>14</td><td>**Journal Editors</td><td id='journalIssueEditors' contenteditable='true'>" + doc_info.journalIssueEditors + "</td></tr></table><br/>" +
                "<br/><button class='btn' id='apply'>SAVE</button>";
            break;
        case "removeDocument":
            var docs = JSON.parse(getDocuments());
            html_str += "<table cellpadding=\"2\" border=\"2\" width=\"80%\" style=\"margin-left: 10%;\"><tr><th>#</th><th>Type</th><th>Title</th><th>Authors</th><th>Keywords</th></tr>";
            docs.forEach(function (value, index) {
                html_str += "<tr><td>" + value.id + "</td>" +
                    "<td>" + value.type + "</td>" +
                    "<td>" + value.title + "</td>" +
                    "<td>" + value.authors + "</td>" +
                    "<td>" + value.keywords + "</td></tr>";
            });
            html_str += "</table><br/><button class='btn' id='apply'>DELETE</button>";
            break;
        case "addUser":
            html_str += "<table cellpadding=\"2\" border=\"2\" width=\"70%\" style=\"margin-left: 10%;\"><tr><th>Property</th><th>Value</th></tr>" +
                "<tr><td>Name</td><td id='name' contenteditable='true'></td></tr>" +
                "<tr><td>Address</td><td id='address' contenteditable='true'></td></tr>" +
                "<tr><td>Phone</td><td id='phone' contenteditable='true'></td></tr>" +
                "<tr><td>Type</td><td id='type' contenteditable='true'></td></tr></table><button class='btn' id='apply'>CREATE</button>";
            break;
        case "modifyUser":
            var users = JSON.parse(getUsers());
            html_str += "<table cellpadding=\"2\" border=\"2\" width=\"80%\" style=\"margin-left: 10%;\"><tr><th>#</th><th>Name</th><th>Address</th><th>Phone</th><th>Type</th></tr>";
            users.forEach(function (value) {
                html_str+= "<tr><td>" + value.id + "</td>" +
                    "<td>" + value.name + "</td>" +
                    "<td>" + value.address + "</td>" +
                    "<td>" + value.phone + "</td>" +
                    "<td>" + value.type + "</td></tr>";
            });
            html_str+="</table><button class='btn' id='apply'>MODIFY SELECTED</button> ";
            break;
        case "modifyUser2":
            var user = JSON.parse(getUser(additional));
            html_str += "<table cellpadding=\"2\" border=\"2\" width=\"70%\" style=\"margin-left: 10%;\"><tr><th>Property</th><th>Value</th></tr>" +
                "<tr><td>Name</td><td id='name' contenteditable='true'>" + user.name + "</td></tr>" +
                "<tr><td>Address</td><td id='address' contenteditable='true'>" + user.address + "</td></tr>" +
                "<tr><td>Phone</td><td id='phone' contenteditable='true'>" + user.phone + "</td></tr>" +
                "<tr><td>Type</td><td id='type' contenteditable='true'>" + user.type + "</td></tr></table>" +
                "<button class='btn' id='apply'>SAVE</button><p>  </p><button class='btn' id='apply2'>GENERATE NEW PASSWORD FOR USER</button>";
            break;
        case "removeUser":
            var users = JSON.parse(getUsers());
            html_str += "<table cellpadding=\"2\" border=\"2\" width=\"80%\" style=\"margin-left: 10%;\"><tr><th>#</th><th>Name</th><th>Address</th><th>Phone</th><th>Type</th></tr>";
            users.forEach(function (value) {
                html_str+= "<tr><td>" + value.id + "</td>" +
                    "<td>" + value.name + "</td>" +
                    "<td>" + value.address + "</td>" +
                    "<td>" + value.phone + "</td>" +
                    "<td>" + value.type + "</td></tr>";
            });
            html_str+="</table><button class='btn' id='apply'>DELETE SELECTED</button> ";
            break;
        case "userDocuments":
            var users = JSON.parse(getUsers());
            html_str += "<table cellpadding=\"2\" border=\"2\" width=\"80%\" style=\"margin-left: 10%;\"><tr><th>#</th><th>Name</th><th>Address</th><th>Phone</th><th>Type</th></tr>";
            users.forEach(function (value) {
                html_str+= "<tr><td>" + value.id + "</td>" +
                    "<td>" + value.name + "</td>" +
                    "<td>" + value.address + "</td>" +
                    "<td>" + value.phone + "</td>" +
                    "<td>" + value.type + "</td></tr>";
            });
            html_str+="</table><button class='btn' id='apply'>CHECK SELECTED</button> ";
            break;
        case "userDocuments2":
            var docs = JSON.parse(getDocumentsUserCheckedOut(additional));
            html_str += "<table cellpadding=\"2\" border=\"2\" width=\"80%\" style=\"margin-left: 10%;\"><tr><th>#</th><th>Title</th><th>Authors</th><th>Keywords</th><th>Return Till</th><th>Overdue</th></tr>";
            docs.forEach(function (value) {
                if(value.documentInfo == null){
                    return;
                }
                html_str+="<tr><td>" + value.documentInfo.id + "</td>" +
                    "<td>" + value.documentInfo.title + "</td>" +
                    "<td>" + value.documentInfo.authors + "</td>" +
                    "<td>" + value.documentInfo.keywords + "</td>" +
                    "<td>" + new Date(value.returnTill * 1000).toUTCString() + "</td>" +
                    "<td>" + value.isOverdue + "</td></tr>";
            });
            html_str+="</table><button class='btn' id='apply'>RETURN SELECTED</button> ";
    }
    //console.log(html_str);
    return html_str;
}

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

function addUser(name, address, phone, type) {
    var accessToken = getAccessTokenFromCookie();

    var req = $.ajax({
        async: false,
        type: "POST",
        url: "http://api.awes-projects.com/manage/users/addUser/",
        headers:{"Access-Token":accessToken},
        data:{
            "name":name,
            "address":address,
            "phone":phone,
            "type":type
        },
        success: function () {
            alert("User has been added!");
        },
        error: function (obj) {
            alert(obj.responseText);
        }
    })
}

function getUsers() {
    var accessToken = getAccessTokenFromCookie();

    var req = $.ajax({
        async: false,
        url: "http://api.awes-projects.com/manage/users/getUsers/",
        headers: {"Access-Token":accessToken},
        error: function (obj) {
            alert(obj.responseText);
        }
    });
    if(req.status == 200){
        return req.responseText;
    }
}

function getUser(user_id){
    var accessToken = getAccessTokenFromCookie();

    var req = $.ajax({
        async: false,
        url: "http://api.awes-projects.com/manage/users/getUser/",
        headers: {"Access-Token":accessToken},
        data:{"id":user_id},
        error: function (obj) {
            alert(obj.responseText);
        }
    });
    if(req.status == 200){
        return req.responseText;
    }
}

function modifyUser(id, name, address, phone, type) {
    var accessToken = getAccessTokenFromCookie();

    var req = $.ajax({
        async: false,
        type: "POST",
        url: "http://api.awes-projects.com/manage/users/modifyUser/",
        headers:{"Access-Token":accessToken},
        data:{
            "id":id,
            "name":name,
            "address":address,
            "phone":phone,
            "type":type
        },
        success: function () {
            alert("User has been modified!");
        },
        error: function (obj) {
            alert(obj.responseText);
        }
    })
}

function generateNewPassword(user_id) {
    var accessToken = getAccessTokenFromCookie();

    var req = $.ajax({
        async: false,
        type: "POST",
        url: "http://api.awes-projects.com/manage/users/generateNewPassword/",
        headers: {"Access-Token":accessToken},
        data:{"id":user_id},
        error: function (obj) {
            alert(obj.responseText);
        }
    });
    if(req.status == 200){
        var resp = JSON.parse(req.responseText);
        alert("You renewed password for user with id=" + resp.id + "! New password is " + resp.password);
    }
}

function deleteUser(user_id){
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

function getDocumentsUserCheckedOut(user_id){
    var accessToken = getAccessTokenFromCookie();

    var req = $.ajax({
        async: false,
        url: "http://api.awes-projects.com/manage/users/getDocumentsUserCheckedOut/",
        headers: {"Access-Token":accessToken},
        data:{"user_id":user_id},
        error: function (obj) {
            alert(obj.responseText);
        }
    });
    if(req.status == 200){
        //console.log(req.responseText);
        return req.responseText;
    }
}

function returnDocument(user_id, document_id){
    var accessToken = getAccessTokenFromCookie();
    var req = $.ajax({
        async: false,
        type: "POST",
        url: "http://api.awes-projects.com/manage/users/userReturnedDocument/",
        headers: {"Access-Token": accessToken},
        data: {
            "userID": user_id,
            "documentID":document_id
        },
        success:function () {
            alert("User has returned document!");
        },
        error: function (obj) {
            alert(obj.responseText);
        }
    })
}