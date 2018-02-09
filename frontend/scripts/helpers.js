function formHTMLString(query, query_or_response, json_str){
    var html_string = "";
    json_str = json_str || "";
    if(query_or_response) {
        switch (query) {
            case "getDocuments":
                html_string = "<p><button id='q-apply'>Get documents</button></p>";
                break;
            case "getDocument":
                html_string = "<p>Document ID: <input id='q-gd-id'></p>" +
                    "<p><button id='q-apply'>Get document</button></p>";
                break;
            case "addDocument":
                html_string = "<p>User ID:<input id='q-ad-uid'> User password: <input id='q-ad-ups'> (For Access Token)<br/></p>" +
                    "<p>Type of book: <select id='q-ad-slt'><option name='0'>Book</option><option name='1'>Journal</option><option name='2'>Media</option></select></p>" +
                    "<p>Title: <input id='q-ad-ti'> Count: <input id='q-ad-ic'> Authors: <input id='q-ad-au'> Price: <input id='q-ad-pr'></p>" +
                    "<p>Keywords: <input id='q-ad-kw'> Is Bestseller: <select id='q-ad-slbs'><option name='0'>No</option><option name='1'>Yes</option> </select></p> " +
                    "<p>Publisher: <input id='q-ad-pb'> Edition: <input id='q-ad-ed'> Publication year: <input id='q-ad-py'></p>" +
                    "<p>Journal Title: <input id='q-ad-jti'> Journal publication year: <input id='q-ad-jpy'> Journal editors: <input id='q-ad-jed'></p>" +
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

                html_string = "<h3>Title: </h3>" + json_obj.title + "" +
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