<?php

    // If some of the required parameters are missed then exit with error
    // If some parameter has invalid value then exit with error
    ensure_required_params(array('id'), $_GET);

    $response = array();

    // Get document info
    $query = $db->prepare("SELECT * FROM documents WHERE id = ?");
    $query->bind_param("i", $_GET['id']);
    $query->execute();
    $document_info = $query->get_result()->fetch_assoc();
    if($document_info == null) {
        json_response(400, array('errorMessage' => 'Document with given id doesn\'t exist'));
    }
    unset($query);

    $response = array_merge(array('id' => $document_info['id'], 'instockCount' => $document_info['instock_count'], 'type' => $document_info['type'], 'title' => $document_info['title'], 'authors' => $document_info['authors'], 'price' => $document_info['price'], 'keywords' => $document_info['keywords']), json_decode($document_info['additional_info'], true));
    unset($document_info);

    // Send response data to the client
    json_response(200, $response);

?>