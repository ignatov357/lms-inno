<?php

	// If access token is invalid or if user doesn't have an access to this method then exit with error
	ensure_access(); // Only librarians are allowed to use this method

	// If some of the required parameters are missed then exit with error
	// If some parameter has invalid value then exit with error
	ensure_required_params(array('newPassword'), $_POST);

	$response = array('id' => get_user_id_by_access_token(getallheaders()['Access-Token']), 'password' => $_POST['newPassword']);

	$query = $db->prepare("UPDATE users SET password = ? WHERE id = ?");
    $query->bind_param("si", hash_password($response['password']), $response['id']);
    $query->execute();
    unset($query);

    json_response(200, $response);

?>
