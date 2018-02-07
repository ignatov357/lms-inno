<?php

    // If access token is invalid or if user doesn't have an access to this method then exit with error
    ensure_access(array(2)); // Only librarians are allowed to use this method

    // If some of the required parameters are missed then exit with error
    // If some parameter has invalid value then exit with error
    ensure_required_params(array('id', 'instockCount', 'title', 'authors', 'price', 'keywords'), $_POST);
    if ($_POST['instockCount'] < 0) {
        json_response(400, array('errorMessage' => 'Incorrect value for parameter \'instockCount\''));
    } elseif (!is_numeric($_POST['price']) || $_POST['price'] < 0) {
        json_response(400, array('errorMessage' => 'Incorrect value for parameter \'price\''));
    }

    // Get document type
    $query = $db->prepare("SELECT type FROM documents WHERE id = ?");
    $query->bind_param("i", $_POST['id']);
    $query->execute();
    $document_type = $query->get_result()->fetch_assoc()['type'];
    if ($document_type == null) {
        json_response(400, array('errorMessage' => 'Document with given id doesn\'t exist'));
    }
    unset($query);

    if ($document_type == 0) {
        ensure_required_params(array('reference', 'bestseller', 'publisher', 'edition', 'publicationYear'), $_POST);
        if(!in_array($_POST['reference'], array(0, 1))) {
            json_response(400, array('errorMessage' => 'Incorrect value for parameter \'bestseller\''));
        }
        if (!in_array($_POST['bestseller'], array(0, 1))) {
            json_response(400, array('errorMessage' => 'Incorrect value for parameter \'bestseller\''));
        }
        if ($_POST['edition'] < 1) {
            json_response(400, array('errorMessage' => 'Incorrect value for parameter \'edition\''));
        }
    } elseif ($document_type == 1) {
        ensure_required_params(array('journalTitle', 'journalIssuePublicationDate', 'journalIssueEditors'), $_POST);
    }

    $response = array('id' => $_POST['id'], 'instockCount' => $_POST['instockCount'], 'type' => $document_type, 'title' => $_POST['title'], 'authors' => $_POST['authors'], 'price' => $_POST['price'], 'keywords' => $_POST['keywords']);
    if ($document_type == 0) {
        $additional_info = array('reference' => intval($_POST['reference']), 'bestseller' => intval($_POST['bestseller']), 'publisher' => $_POST['publisher'], 'edition' => $_POST['edition'], 'publicationYear' => $_POST['publicationYear']);
    } elseif ($document_type == 1) {
        $additional_info = array('journalTitle' => $_POST['journalTitle'], 'journalIssuePublicationDate' => $_POST['journalIssuePublicationDate'], 'journalIssueEditors' => $_POST['journalIssueEditors']);
    } elseif ($document_type == 2) {
        $additional_info = array();
    }
    $response = array_merge($response, $additional_info);

    // Modify a document
    $query = $db->prepare("UPDATE documents SET instock_count = ?, title = ?, authors = ?, price = ?, keywords = ?, additional_info = ? WHERE id = ?");
    $query->bind_param("issdssi", $_POST['instockCount'], $_POST['title'], $_POST['authors'], $_POST['price'], $_POST['keywords'], json_encode($additional_info), $_POST['id']);
    $query->execute();
    unset($query);

    // Send response data to the client
    json_response(200, $response);

?>