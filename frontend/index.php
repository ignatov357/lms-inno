<?php
	if(!empty($_COOKIE['accessToken'])) {
		header('Location: catalogue.php');
	}
?>
<!DOCTYPE html>
<html>
<head>
    <title>InnoLib</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="css/style.css">
    <script src="scripts/jquery.js"></script>
    <script src="scripts/helpers.js"></script>
    <style>
        #panel-login {
            background: #e6ee9c;
            position: absolute;
            left: 23.4%;
            top: 23.8%;
            right: 23.4%;
            padding: 1%;
        }

        button {
            width: 26%;
            padding: 10px;
        }
    </style>
    <script>
        $(document).ready(function () {
            $("button.btn").click(function () {
                var id = "userID=" + $("input[name='card-id']").val();
                var pass = "password=" + $("input[name='password']").val();

                $.ajax({
                    url: "http://api.awes-projects.com/users/getAccessToken/",
                    type: "POST",
                    dataType: "JSON",
                    data: id+"&"+pass,
                    success: function (result) {
                        var date = new Date(+result.expirationDate * 1000);

                        document.cookie = "accessToken=" +
                            result.accessToken +
                            "; path=; domain=.awes-projects.com; expires=" +
                            date.toUTCString();
                        window.location.replace('catalogue.php');
                    },
                    error: function (data) {
                        alert(data.responseJSON.errorMessage);
                    }
                });
                return false;
            });
        });
    </script>
</head>
<body>
<div id="panel-login">
    <div class="centered">
        <h3>AUTHORIZE USING YOUR CARD NUMBER</h3>
    </div>
    <br/>
    <form id="form-login">
        <h3>CARD ID: <input name="card-id" required></h3>
        <h3>PASSWORD:<input name="password" type="password" required></h3>
        <div class="centered">
            <button class="btn">LOG IN</button>
        </div>
    </form>
</div>
</body>
</html>