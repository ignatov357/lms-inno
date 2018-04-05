<?php

// If access token is invalid or if user doesn't have an access to this method then exit with error
ensure_access(array(0)); // Only librarians are allowed to use this method

// If some of the required parameters are missed then exit with error
// If some parameter has invalid value then exit with error
ensure_required_params(array('documentID'), $_GET);

$response = array();

// Check if document exists
$query = $db->prepare("SELECT id FROM documents WHERE id = ?");
$query->bind_param("i", $_GET['documentID']);
$query->execute();
if ($query->get_result()->fetch_assoc() === null) {
    json_response(400, array('errorMessage' => 'Document with given id doesn\'t exist'));
}
unset($query);

// Get queue for document
$response = get_queue_for_document($_GET['documentID']);

// Send response data to the client
json_response(200, $response);

?>