<!DOCTYPE html>
<html>
<header>
    <title>Library Management</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="css/style.css">
    <script src="scripts/helpers.js"></script>
    <script src="scripts/jquery.js"></script>
    <style>
        #header {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            background: #AFB42B;
            height: 8%;

        }

        .selected {
            background: #F0F4C3;
        }
    </style>
    <script>
        $(document).ready(function () {
            var state = 'none';

            $("#table-users").click(function () {
                state = 'users';
                $("#add").text("Add user");
                $("#modify").text("Modify user");
                $("#remove").text("Remove user");

                $("#table-documents div").css("background", "#cddc39");
                $("#table-users div").css("background", "#F0F4C3");

                $(".btn").css("visibility", "visible");

                updateUsersTable($("#content-table"));

                return false
            });
            $("#table-documents").click(function () {
                state = 'documents';
                $("#add").text("Add document");
                $("#modify").text("Modify document");
                $("#remove").text("Remove document");

                $("#table-users div").css("background", "#cddc39");
                $("#table-documents div").css("background", "#F0F4C3");

                $(".btn").css("visibility", "visible");

                updateDocumentsTable($("#content-table"));

                return false
            });

            $("#add, #modify, #remove").click(function () {
                //console.log($('#content-table table tr.selected').html());
                if (state == 'documents') {
                    if ($(this).attr("id") == 'add') {
                        popupShow();
                        popupAddDocument($("#popup"));

                        return false;
                    }
                    if ($('#content-table table tr.selected').html()) {
                        var id = $("#content-table table tr.selected td:first").html();
                        switch ($(this).attr("id")) {
                            case 'modify':
                                //alert('There will be modify popup');
                                popupShow();

                                popupModifyDocument($("#popup"), id);

                                break;
                            case 'remove':
                                deleteDocument(id);
                                updateDocumentsTable($("#content-table"));

                                break;
                        }
                        return false;
                    } else {
                        alert('Choose a document first!');
                    }
                } else if (state == 'users') {
                    if ($(this).attr("id") == 'add') {
                        popupShow();
                        popupAddUser($("#popup"));

                        return false;
                    }
                    if ($('#content-table table tr.selected').html()) {
                        var id = $("#content-table table tr.selected td:first").html();
                        switch ($(this).attr("id")) {
                            case 'modify':
                                //alert('There will be modify popup');
                                popupShow();

                                popupModifyUser($("#popup"), id);

                                break;
                            case 'remove':
                                deleteUser(id);
                                updateUsersTable($("#content-table"));

                                break;
                        }
                        return false;
                    }
                } else {
                    alert('Choose a table first!');
                }
            });
        });
    </script>
</header>
<body>
<div id="header" style="text-align: center;z-index: 11">
    <a href="" id="table-users">
        <div style="position: absolute;background: #cddc39;left: 1%; top: 0; width: 10%;height:100%;">
            <h4>USERS EDITING</h4>
        </div>
    </a>
    <a href="" id="table-documents">
        <div style="position: absolute;background: #cddc39;left: 12%; top: 0; width: 10%;height:100%;">
            <h4>DOCUMENTS EDITING</h4>
        </div>
    </a>
    <a href="index.php" id="log-out">
        <div style="position: absolute;background: #cddc39;right: 1%; top: 0; width: 10%;height:100%;">
            <h4>LOG OUT</h4>
        </div>
    </a>
</div>
<div id="content-table">

</div>
<div id="hider"
     style="position: absolute; background: rgba(0,0,0,0.3); width: 100%;height: 100%;visibility: hidden;z-index: 6;">
</div>
<div id="popup"
     style="position: absolute; left: 20%; top: 20%; width: 60%; height: 60%; background: white;padding: 20px;visibility: hidden;z-index: 7; overflow: scroll">
</div>
<button id="add" style="position: absolute; top: 85%; left: 1%;visibility: hidden;padding: 10px" class="btn"></button>
<button id="modify" style="position: absolute; top: 85%; left: 8%;visibility: hidden;padding: 10px"
        class="btn"></button>
<button id="remove" style="position: absolute; top: 85%; left: 16%;visibility: hidden;padding: 10px"
        class="btn"></button>

</body>
</html>