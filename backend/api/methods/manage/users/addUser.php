<?php

    // If access token is invalid or if user doesn't have an access to this method then exit with error
    ensure_access(array(2)); // Only librarians are allowed to use this method

    // If some of the required parameters are missed then exit with error
    // If some parameter has invalid value then exit with error
    if(empty($_POST['name']) || empty($_POST['address']) || empty($_POST['phone']) || empty($_POST['type'])) {
        json_response(400, array('errorMessage' => 'Some required parameters are missed'));
    } elseif(!in_array($_POST['type'], array(0, 1, 2))) {
        json_response(400, array('errorMessage' => 'Incorrect value for parameter \'type\''));
    }

    $response = array();

    // Create an user
    $response['id'] = NULL;
    $response['password'] = generate_password();
    $query = $db->prepare("INSERT INTO users (name, address, phone, password, type) VALUES (?, ?, ?, ?, ?)");
    $query->bind_param("ssssi", $_POST['name'], $_POST['address'], $_POST['phone'], hash_password($response['password']), $_POST['type']);
    $query->execute();
    $response['id'] = $db->insert_id;
    unset($query);

    // Send response data to the client
    json_response(200, $response);

?>