<?php

    // If access token is invalid or if user doesn't have an access to this method then exit with error
    ensure_access(array(-1, 0)); // Only librarians and administrators are allowed to use this method

    // If some of the required parameters are missed then exit with error
    // If some parameter has invalid value then exit with error
    ensure_required_params(array('name', 'address', 'phone', 'type'), $_POST);

    if(!in_array($_POST['type'], array(0, 1, 2, 3, 4, 5))) {
        json_response(400, array('errorMessage' => 'Incorrect value for parameter \'type\''));
    }
    if(in_array($_POST['type'], array(0))) {
        // Only administrators are allowed
        if(get_user_type() != -1) {
            json_response(401, array('errorMessage' => 'User associated with given Access-Token doesn\'t have permission to add librarians'));
        }

        ensure_required_params(array('permissions'), $_POST);
        if(!in_array($_POST['permissions']['add'], array(0, 1))) {
            json_response(400, array('errorMessage' => 'Incorrect value for permission \'add\''));
        } elseif(!in_array($_POST['permissions']['modify'], array(0, 1))) {
            json_response(400, array('errorMessage' => 'Incorrect value for permission \'modify\''));
        } elseif(!in_array($_POST['permissions']['remove'], array(0, 1))) {
            json_response(400, array('errorMessage' => 'Incorrect value for permission \'remove\''));
        }
    } else {
        // Only librarians are allowed
        if(get_user_type() != 0) {
            json_response(401, array('errorMessage' => 'User associated with given Access-Token doesn\'t have permission to add users'));
        }
        ensure_permissions(array('add' => 1)); // Check librarian's permissions
    }

    $response = array('id' => NULL, 'password' => generate_password());

    // Create an user
    $query = $db->prepare("INSERT INTO users (name, address, phone, password, type) VALUES (?, ?, ?, ?, ?)");
    $query->bind_param("ssssi", $_POST['name'], $_POST['address'], $_POST['phone'], hash_password($response['password']), $_POST['type']);
    $query->execute();
    $response['id'] = $db->insert_id;
    unset($query);

    if($_POST['type'] == 0) {
        // Add permissions
        $query = $db->prepare("INSERT INTO librarians_permissions (librarian_id, add, modify, remove) VALUES (?, ?, ?, ?)");
        $query->bind_param("iiii", $response['id'], $_POST['permissions']['add'], $_POST['permissions']['modify'], $_POST['permissions']['remove']);
        $query->execute();
        unset($query);
    }

    // Send response data to the client
    json_response(200, $response);

?>