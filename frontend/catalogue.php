<!DOCTYPE html>
<html>
<head>
    <title>All books</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="css/style.css">
    <script src="scripts/helpers.js"></script>
    <script src="scripts/jquery.js"></script>
    <style>

    </style>
    <script>
        $(document).ready(function () {
            $("#books-field").empty();

            $("#books-field").append(getDocumentsField());
            $(".btn").click(function () {
                var book_id = $(this).attr("id").replace("checkout-book-", "");
                checkoutBook(book_id);
                $(document).ready();
            })
        })
    </script>
</head>
<body>
    <div id="header" class="centered" style="background: greenyellow;">
        <h1>ALL BOOKS</h1>
    </div>
    <div id="books-field">

    </div>
</body>
</html>