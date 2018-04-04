<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Library main page</title>
    <script src="scripts/jquery.js"></script>
    <script src="scripts/helpers.js"></script>
    <link rel="stylesheet" href="css/style.css">
    <style>

    </style>
    <script>
        $(document).ready(function () {
            $("#req-button").click(function () {
                $("#send-request").empty();

                var request = $("#req-selector option:selected").attr("name");
                var req_html = formHTMLString(request, true);
                if (req_html){
                    $("#send-request").append(req_html);
                    $("#q-apply").click(function () {
                        switch(request){
                            case "getDocuments":
                            	var books_json = reqGetDocuments();
                            	//console.log(books_json);
                                var resp_html = formHTMLString("getDocuments", false, books_json);
                                $("#show-request").empty().append(resp_html);
                                //$("#show-request").append(resp_html);
                                $()
                                break;
                        }
                    })
                }
            });
        });
    </script>
</head>
<body>
    <div id="choose-request">
        <h3>Select your request:</h3>
        <select id="req-selector">
            <option name="getDocuments">Get All Books</option>
            <option name="getDocument">Get Particular Book</option>
            <option name="addDocument">Add New Book</option>
        </select>
        <button class="btn" id="req-button">SELECT</button>
    </div>
    <div id="send-request">

    </div>
    <div id="show-request">

    </div>
</body>
</html>