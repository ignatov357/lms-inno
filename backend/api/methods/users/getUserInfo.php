<?php

    // If access token is invalid or if user doesn't have an access to this method then exit with error
    ensure_access();

    $response = array();

    // Get user info
    $query = $db->prepare("SELECT id, type, name, address, phone FROM users WHERE id = ?");
    $query->bind_param("i", get_user_id());
    $query->execute();
    $user_info = $query->get_result()->fetch_assoc();
    unset($query);

    $response = $user_info;
    unset($user_info);

    // Send response data to the client
    json_response(200, $response);

?>