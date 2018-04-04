<?php

    // If some of the required parameters are missed then exit with error
    ensure_required_params(array('userID', 'password'), $_POST);

    $response = array();

    // Checking if the given credentials are correct
    $query = $db->prepare("SELECT password FROM users WHERE id = ?");
    $query->bind_param("i", $_POST['userID']);
    $query->execute();
    if($query->get_result()->fetch_assoc()['password'] != hash_password($_POST['password'])) {
        // If the given credentials are incorrect then exit with error
        json_response(400, array('errorMessage' => 'User ID or password is incorrect'));
    }
    unset($query);

    // Checking if an access token for this user is already generated
    $query = $db->prepare("SELECT access_token FROM sessions WHERE user_id = ?");
    $query->bind_param("i", $_POST['userID']);
    $query->execute();
    $response['accessToken'] = $query->get_result()->fetch_assoc()['access_token'];
    unset($query);

    // If an access token is already generated then change an expiry date
    if($response['accessToken'] !== null) {
        $response['expirationDate'] = time() + ACCESS_TOKEN_LIFETIME;
        $query = $db->prepare("UPDATE sessions SET expiration_date = ? WHERE access_token = ?");
        $query->bind_param("is", $response['expirationDate'], $response['accessToken']);
        $query->execute();
        unset($query);
    }

    // If an access token isn't already generated then generate it
    if($response['accessToken'] === null) {
        $response['accessToken'] = hash('sha256', $_POST['userID'].':'.$_POST['password'].':'.time());
        $response['expirationDate'] = time() + ACCESS_TOKEN_LIFETIME;
        $query = $db->prepare("INSERT INTO sessions (user_id, access_token, expiration_date) VALUES (?, ?, ?)");
        $query->bind_param("isi", $_POST['userID'], $response['accessToken'], $response['expirationDate']);
        $query->execute();
        unset($query);
    }

    // Send response data to the client
    json_response(200, $response);

?>
