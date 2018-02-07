<?php

    // If access token is invalid or if user doesn't have an access to this method then exit with error
    ensure_access(array(2)); // Only librarians are allowed to use this method

    $response = array();

    // Get users info
    $query = $db->prepare("SELECT id, type, name, address, phone FROM users");
    $query->execute();
    $users = $query->get_result();
    while($cur_user = $users->fetch_assoc()) {
        $response[] = $cur_user;
    }
    unset($query);

    // Send response data to the client
    json_response(200, $response);

?>