<!DOCTYPE html>
<html>
<header>
    <title>Library Management</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="css/style.css">
    <script src="scripts/helpers.js"></script>
    <script src="scripts/jquery.js"></script>
    <style>
        .selected{
            background: #cddc39;
        }
    </style>
    <script>
        $(document).ready(function () {
            $(".t-clickable-1, .t-clickable-2, .t-clickable-3, .t-clickable-4, .t-clickable-5, .t-clickable-6, .t-clickable-7, .t-clickable-8").click(function () {
                //console.log($(this).html());
                $(".t-clickable-1,.t-clickable-2, .t-clickable-3, .t-clickable-4, .t-clickable-5, .t-clickable-6, .t-clickable-7, .t-clickable-8").css("background", "#f9fbe7");
                $("."+$(this).attr("class")).css("background", "#cddc39");
                var method = $("."+$(this).attr("class")+"[id='c-2']").html();
                //console.log(method);

                $("#adm-fields").empty();
                $("#adm-fields").append(getAdmTable(method));
                switch (method){
                    case "addDocument":
                        $("#apply").click(function () {
                            var type = $("#type").html();
                            var title = $("#title").html();
                            var count = $("#instockCount").html();
                            var authors = $("#authors").html();
                            var price = $("#price").html();
                            var keywords = $("#keywords").html();
                            var bestseller = $("#bestseller").html();
                            var publisher = $("#publisher").html();
                            var edition = $("#edition").html();
                            var pub_year = $("#publicationYear").html();
                            var j_title = $("#journalTitle").html();
                            var j_pub_date = $("#journalIssuePublicationDate").html();
                            var j_editors = $("#journalIssueEditors").html();

                            addDocument(type,title,count,authors,price,keywords,bestseller,publisher,edition,pub_year,j_title,j_pub_date,j_editors);
                        });
                        break;
                    case "getDocuments":
                        break;
                    case "modifyDocument":
                        $("#adm-fields table tr").click(function () {
                            $(this).addClass('selected').siblings().removeClass('selected');
                        });
                        $("#apply").click(function () {
                            var id = $("#adm-fields table tr.selected td:first").html();
                            //console.log("ID is " + id);
                            $("#adm-fields").empty().append(getAdmTable("modifyDocument2", id));
                            $("#apply").click(function () {
                                var title = $("#title").html();
                                var count = $("#instockCount").html();
                                var authors = $("#authors").html();
                                var price = $("#price").html();
                                var keywords = $("#keywords").html();
                                var bestseller = $("#bestseller").html();
                                var publisher = $("#publisher").html();
                                var edition = $("#edition").html();
                                var pub_year = $("#publicationYear").html();
                                var j_title = $("#journalTitle").html();
                                var j_pub_date = $("#journalIssuePublicationDate").html();
                                var j_editors = $("#journalIssueEditors").html();

                                modifyDocument(id, title,count,authors,price,keywords,bestseller,publisher,edition,pub_year,j_title,j_pub_date,j_editors);
                            });
                        });
                        break;
                    case "removeDocument":
                        $("#adm-fields table tr").click(function () {
                            $(this).addClass('selected').siblings().removeClass('selected');
                        });
                        $("#apply").click(function () {
                            var id = $("#adm-fields table tr.selected td:first").html();
                            deleteDocument(id);
                            $("#adm-fields").empty();
                        });
                        break;
                    case "addUser":
                        $("#apply").click(function () {
                            var name = $("#name").html();
                            var address = $("#address").html();
                            var phone = $("#phone").html();
                            var type = $("#type").html();

                            addUser(name,address,phone,type);
                        });
                        break;
                    case "modifyUser":
                        $("#adm-fields table tr").click(function () {
                            $(this).addClass('selected').siblings().removeClass('selected');
                        });
                        $("#apply").click(function () {
                            var id = $("#adm-fields table tr.selected td:first").html();
                            $("#adm-fields").empty().append(getAdmTable("modifyUser2", id));
                            $("#apply").click(function () {
                                var name = $("#name").html();
                                var address = $("#address").html();
                                var phone = $("#phone").html();
                                var type = $("#type").html();

                                modifyUser(id, name, address, phone, type);
                            });
                            $("#apply2").click(function () {
                                generateNewPassword(id);
                            })
                        });
                        break;
                    case "removeUser":
                        $("#adm-fields table tr").click(function () {
                            $(this).addClass('selected').siblings().removeClass('selected');
                        });
                        $("#apply").click(function () {
                            var id = $("#adm-fields table tr.selected td:first").html();
                            deleteUser(id);
                            $("#adm-fields").empty();
                        });
                        break;
                    case "userDocuments":
                        $("#adm-fields table tr").click(function () {
                            $(this).addClass('selected').siblings().removeClass('selected');
                        });
                        $("#apply").click(function () {
                            var id = $("#adm-fields table tr.selected td:first").html();
                            $("#adm-fields").empty().append(getAdmTable("userDocuments2", id));
                            $("#adm-fields table tr").click(function () {
                                $(this).addClass('selected').siblings().removeClass('selected');
                            });
                            $("#apply").click(function () {
                                var b_id = $("#adm-fields table tr.selected td:first").html();
                                returnDocument(id, b_id);
                                $("#adm-fields").empty();
                            });
                        });
                        break;

                }
            });
            return false;
        });
    </script>
</header>
<body>
<div id="functions-window">
    <table cellpadding="2" border="2" width="50%" style="margin-left: 10%;">
        <tr>
            <td>#</td>
            <th>Function name</th>
            <th>Description</th>
        </tr>
        <tr>
            <td class="t-clickable-1" id="c-1">1</td>
            <td class="t-clickable-1" id="c-2">addDocument</td>
            <td class="t-clickable-1" id="c-3">Add new document</td>
        </tr>
        <tr>
            <td class="t-clickable-2" id="c-1">2</td>
            <td class="t-clickable-2" id="c-2">modifyDocument</td>
            <td class="t-clickable-2" id="c-3">Modify existing document</td>
        </tr>
        <tr>
            <td class="t-clickable-3" id="c-1">3</td>
            <td class="t-clickable-3" id="c-2">removeDocument</td>
            <td class="t-clickable-3" id="c-3">Remove existing document</td>
        </tr>
        <tr>
            <td class="t-clickable-4" id="c-1">4</td>
            <td class="t-clickable-4" id="c-2">getDocuments</td>
            <td class="t-clickable-4" id="c-3">View all existing documents</td>
        </tr>
        <tr>
            <td class="t-clickable-5" id="c-1">5</td>
            <td class="t-clickable-5" id="c-2">addUser</td>
            <td class="t-clickable-5" id="c-3">Add new user</td>
        </tr>
        <tr>
            <td class="t-clickable-6" id="c-1">6</td>
            <td class="t-clickable-6" id="c-2">modifyUser</td>
            <td class="t-clickable-6" id="c-3">Modify existing user</td>
        </tr>
        <tr>
            <td class="t-clickable-7" id="c-1">7</td>
            <td class="t-clickable-7" id="c-2">removeUser</td>
            <td class="t-clickable-7" id="c-3">Remove existing user</td>
        </tr>
        <tr>
            <td class="t-clickable-8" id="c-1">8</td>
            <td class="t-clickable-8" id="c-2">userDocuments</td>
            <td class="t-clickable-8" id="c-3">Check user's documents</td>
        </tr>
    </table>
    <br/>
    <div id="adm-fields">

    </div>
</div>
</body>
</html>