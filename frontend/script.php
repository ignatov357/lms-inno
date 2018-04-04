<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <script src="scripts/jquery.js"></script>
    <script src="scripts/helpers.js"></script>
    <title>Library</title>
    <script>
        $(document).ready(
            function () {
                $("#try").click(
                    function () {
                        $("#det-us").empty();

                        var selected = $("#us-sel option:selected").attr("name");
                      	console.log(selected);
                        switch (selected){
                            case "1":
                                var html = getUserStoryHTML("1");
                                console.log(html);
                                $("#det-us").append(html);
                                $("#show-book").click(function () {
                                    var book_id = $(this).attr("name");
                                    var accessToken = function () {
                                        var req = new XMLHttpRequest();
                                        req.open("POST", "http://api.awes-projects.com/users/getAccessToken/", false);
                                        req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                                        req.send("userID=1&password=1234567890");

                                        if(req.status == 200){
                                            return JSON.parse(req.responseText).accessToken;
                                        }
                                        return false;
                                    };

                                    var req = new XMLHttpRequest();

                                    req.open("POST", "http://api.awes-projects.com/documents/checkOutDocument/", false);
                                    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                                    req.setRequestHeader("Access-Token", accessToken());
                                    req.send("document=" + book_id);

                                    if(req.status == 200){
                                        alert("You have ordered a book!");
                                    } else {
                                        alert("Server returned " + req.status + " " + req.statusText);
                                    }
                                })
                        }
                    }
                )
            }
        )
    </script>
</head>
<body>
    <select id="us-sel">
        <option name="1">Try User Story 1</option>
    </select>
    <button id="try">TRY</button>
    <div id="det-us">

    </div>
</body>
</html>