<!DOCTYPE html>
<html>
<head>
    <title>All books</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="css/style.css">
    <script src="scripts/helpers.js"></script>
    <script src="scripts/jquery.js"></script>
    <style>
        #header{
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            background: #AFB42B;
            height: 8%;

        }
    </style>
    <script>
        $(document).ready(function () {
            $("#load-cab").click(function () {
                $("#load-cab div").css({"background": "#F0F4C3"});
                $("#load-lib div").css({"background": "#cddc39"});
                /*$("#books-field").empty();
                $("#books-field").append(getUsersDocumentsFiled());
                $("#renew").click(function () {
                    renewDocument($("#renew").attr("name"));
                });*/
                getUsersDocumentsFiled();
                return false;
            });
            $("#load-lib").click(function () {
                $("#load-cab div").css({"background": "#cddc39"});
                $("#load-lib div").css({"background": "#F0F4C3"});

                getDocumentsField();
                return false;
            });
            $("#log-out").click(function () {
                document.cookie = "accessToken=; path=; domain=.awes-projects.com; expires=-1";
                window.location.replace("http://awes-projects.com");
            })
        })
    </script>
</head>
<body>
    <div id="header" class="centered">
        <a href="" id="load-cab"><div style="position: absolute;background: #cddc39;left: 1%; top: 0; width: 10%;height:100%;">
                <h4>MY BOOKS</h4>
            </div></a>
        <a href="" id="load-lib"><div style="position: absolute;background: #cddc39;left: 12%; top: 0; width: 10%;height:100%;">
                <h4>LIBRARY</h4>
            </div></a>
        <a href="index.php" id="log-out"><div style="position: absolute;background: #cddc39;right: 1%; top: 0; width: 10%;height:100%;">
                <h4>LOG OUT</h4>
            </div></a>
    </div>
    <div id="books-field" style="margin-top: 10%">

    </div>
</body>
</html>