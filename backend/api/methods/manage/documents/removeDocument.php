<?php

    // If access token is invalid or if user doesn't have an access to this method then exit with error
    ensure_access(array(0)); // Only librarians are allowed to use this method
    ensure_permissions(array('remove' => 1)); // Check librarian's permissions

    // If some of the required parameters are missed then exit with error
    // If some parameter has invalid value then exit with error
    ensure_required_params(array('id'), $_POST);

    // Get document info before deletion
    $query = $db->prepare("SELECT type, title, authors, price, additional_info, keywords, instock_count FROM documents WHERE id = ?");
    $query->bind_param("i", $_POST['id']);
    $query->execute();
    $document_info = $query->get_result()->fetch_assoc();
    if ($document_info === null) {
        json_response(400, array('errorMessage' => 'Document with given id doesn\'t exist'));
    }
    unset($query);

    // Check if some copies of this document are checked out
    $query = $db->prepare("SELECT * FROM booked_documents WHERE document_id = ?");
    $query->bind_param("i", $_POST['id']);
    $query->execute();
    if($query->num_rows() > 0) {
        json_response(400, array('errorMessage' => 'Document couldn\'t be removed, because some copies of this document are checked out'));
    }
    unset($query);

    $response = array_merge(array('id' => $document_info['id'], 'instockCount' => $document_info['instock_count'], 'type' => $document_info['type'], 'title' => $document_info['title'], 'authors' => $document_info['authors'], 'price' => $document_info['price'], 'keywords' => $document_info['keywords']), json_decode($document_info['additional_info'], true));;
    unset($document_info);

    // Delete a document
    $query = $db->prepare("DELETE FROM documents WHERE id = ?");
    $query->bind_param("i", $_POST['id']);
    $query->execute();
    unset($query);

    // Send response data to the client
    json_response(200, $response);

?>