<?php

    // If access token is invalid or if user doesn't have an access to this method then exit with error
    ensure_access(array(-1, 0)); // Only librarians are allowed to use this method

    // If some of the required parameters are missed then exit with error
    // If some parameter has invalid value then exit with error
    ensure_required_params(array('id'), $_POST);

    if(in_array(get_user_type($_POST['id']), array(0))) {
        // Only administrators are allowed
        if(get_user_type() != -1) {
            json_response(401, array('errorMessage' => 'User associated with given Access-Token doesn\'t have permission to modify librarians'));
        }
    } else {
        // Only librarians are allowed
        if(get_user_type() != 0) {
            json_response(401, array('errorMessage' => 'User associated with given Access-Token doesn\'t have permission to modify users'));
        }
        ensure_permissions(array('remove' => 1)); // Check librarian's permissions
    }

    // Get user info before deletion
    $query = $db->prepare("SELECT type, name, address, phone FROM users WHERE id = ?");
    $query->bind_param("i", $_POST['id']);
    $query->execute();
    $user_info = $query->get_result()->fetch_assoc();
    if($user_info === null) {
        json_response(400, array('errorMessage' => 'User with given id doesn\'t exist'));
    }
    unset($query);

    // Check if this user has checked out documents
    $query = $db->prepare("SELECT * FROM booked_documents WHERE document_id = ?");
    $query->bind_param("i", $_POST['id']);
    $query->execute();
    if($query->num_rows() > 0) {
        json_response(400, array('errorMessage' => 'User couldn\'t be removed, there are documents checked out by this user'));
    }
    unset($query);

    $response = $user_info;
    unset($user_info);

    // Discarding access-tokens given to that user
    $query = $db->prepare("DELETE FROM sessions WHERE user_id = ?");
    $query->bind_param("i", $_POST['id']);
    $query->execute();
    unset($query);

    // Deleting an user
    $query = $db->prepare("DELETE FROM users WHERE id = ?");
    $query->bind_param("i", $_POST['id']);
    $query->execute();
    unset($query);

    // Send response data to the client
    json_response(200, $response);

?>