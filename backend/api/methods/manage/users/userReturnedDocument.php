<?php

    // If access token is invalid or if user doesn't have an access to this method then exit with error
    ensure_access(array(0)); // Only librarians are allowed to use this method

    // If some of the required parameters are missed then exit with error
    // If some parameter has invalid value then exit with error
    ensure_required_params(array('userID', 'documentID'), $_POST);

    // If this user didn't check out this document then exit with error
    $query = $db->prepare("SELECT * FROM booked_documents WHERE user_id = ? AND document_id = ?");
    $query->bind_param("ii", $_POST['userID'], $_POST['documentID']);
    $query->execute();
    if($query->get_result()->fetch_assoc() === null) {
        json_response(400, array('errorMessage' => 'User with given id didn\'t check out the document with given id'));
    }
    unset($query);

    $response = array('userID' => $_POST['userID'], 'documentID' => $_POST['documentID'], 'documentReturned' => 1);

    // Increase the count of available copies of this document
    $query = $db->prepare("UPDATE documents SET instock_count = instock_count + 1 WHERE id = ?");
    $query->bind_param("i", $_POST['documentID']);
    $query->execute();
    unset($query);

    // Remove the document from list of the documents user checked
    $query = $db->prepare("DELETE FROM booked_documents WHERE user_id = ? AND document_id = ?");
    $query->bind_param("ii", $_POST['userID'], $_POST['documentID']);
    $query->execute();
    unset($query);

    // Send response data to the client
    json_response(200, $response);

?>