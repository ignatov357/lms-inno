<?php

    // If access token is invalid or if user doesn't have an access to this method then exit with error
    ensure_access(array(0)); // Only librarians are allowed to use this method

    // If some of the required parameters are missed then exit with error
    // If some parameter has invalid value then exit with error
    ensure_required_params(array('id'), $_GET);

    $response = array();

    // Get user info
    $query = $db->prepare("SELECT id, type, name, address, phone FROM users WHERE id = ?");
    $query->bind_param("i", $_GET['id']);
    $query->execute();
    $user_info = $query->get_result()->fetch_assoc();
    if($user_info === null) {
        json_response(400, array('errorMessage' => 'User with given id doesn\'t exist'));
    }
    unset($query);

    $response = $user_info;
    unset($user_info);

    // Send response data to the client
    json_response(200, $response);

?>