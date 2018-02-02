<?php

    // If access token is invalid or if user doesn't have an access to this method then exit with error
    ensure_access(array(2)); // Only librarians are allowed to use this method

    // If some of the required parameters are missed then exit with error
    // If some parameter has invalid value then exit with error
    if(empty($_POST['id']) || empty($_POST['name']) || empty($_POST['address']) || empty($_POST['phone']) || empty($_POST['type'])) {
        json_response(400, array('errorMessage' => 'Some required parameters are missed'));
    } elseif(!in_array($_POST['type'], array(0, 1, 2))) {
        json_response(400, array('errorMessage' => 'Incorrect value for parameter \'type\''));
    }

    $response = array('id' => $_POST['id'], 'type' => $_POST['type'], 'name' => $_POST['name'], 'address' => $_POST['address'], 'phone' => $_POST['phone']);

    // Modify an user
    $query = $db->prepare("UPDATE users SET type = ?, name = ?, address = ?, phone = ? WHERE id = ?");
    $query->bind_param("isssi", $_POST['type'], $_POST['name'], $_POST['address'], $_POST['phone'], $_POST['id']);
    $query->execute();
    unset($query);

    // Send response data to the client
    json_response(200, $response);

?>