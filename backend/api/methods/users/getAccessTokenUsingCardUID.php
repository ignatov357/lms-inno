<?php

    // If some of the required parameters are missed then exit with error
    ensure_required_params(array('cardUID'), $_POST);

    $response = array();

    // Checking if the card UID is correct
    $query = $db->prepare("SELECT id FROM users WHERE card_UID = ?");
    $query->bind_param("s", $_POST['cardUID']);
    $query->execute();
    $user_id = $query->get_result()->fetch_assoc()['id'];
    if($user_id == null) {
        // If the given card UID doesn't exit in the system, then exit with error
        json_response(400, array('errorMessage' => 'User wasn\'t found'));
    }
    unset($query);

    // Checking if an access token for this user is already generated
    $query = $db->prepare("SELECT access_token FROM sessions WHERE user_id = ?");
    $query->bind_param("i", $user_id);
    $query->execute();
    $response['accessToken'] = $query->get_result()->fetch_assoc()['access_token'];
    unset($query);

    // If an access token is already generated then change an expiry date
    if($response['accessToken'] != null) {
        $response['expirationDate'] = time() + ACCESS_TOKEN_LIFETIME;
        $query = $db->prepare("UPDATE sessions SET expiration_date = ? WHERE access_token = ?");
        $query->bind_param("is", $response['expirationDate'], $response['accessToken']);
        $query->execute();
        unset($query);
    }

    // If an access token isn't already generated then generate it
    if($response['accessToken'] == null) {
        $response['accessToken'] = hash('sha256', $user_id.':'.$_POST['cardUID'].':'.time());
        $response['expirationDate'] = time() + ACCESS_TOKEN_LIFETIME;
        $query = $db->prepare("INSERT INTO sessions (user_id, access_token, expiration_date) VALUES (?, ?, ?)");
        $query->bind_param("isi", $user_id, $response['accessToken'], $response['expirationDate']);
        $query->execute();
        unset($query);
    }

    // Send response data to the client
    json_response(200, $response);

?>
