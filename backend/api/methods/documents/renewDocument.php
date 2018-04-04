<?php

    // If access token is invalid or if user doesn't have an access to this method then exit with error
    ensure_access();

    // If some of the required parameters are missed then exit with error
    // If some parameter has invalid value then exit with error
    ensure_required_params(array('documentID'), $_POST);

    $response = array();

    // Get user info
    $user_id = get_user_id();
    $query = $db->prepare("SELECT * FROM users WHERE id = ?");
    $query->bind_param("i", $user_id);
    $query->execute();
    $user_info = $query->get_result()->fetch_assoc();
    if($user_info === null) {
        // If user doesn't exist then exit with error
        json_response(400, array('errorMessage' => 'User with given id doesn\'t exist'));
    }
    unset($query);
    $response['userID'] = $user_id;

    // Get document info
    $query = $db->prepare("SELECT * FROM documents WHERE id = ?");
    $query->bind_param("i", $_POST['documentID']);
    $query->execute();
    $document_info = $query->get_result()->fetch_assoc();
    if($document_info === null) {
        // If document doesn't exist then exit with error
        json_response(400, array('errorMessage' => 'Document with given id doesn\'t exist'));
    }
    $document_info = array_merge(array('id' => $document_info['id'], 'instock_count' => $document_info['instock_count'], 'type' => $document_info['type'], 'title' => $document_info['title'], 'authors' => $document_info['authors'], 'price' => $document_info['price'], 'keywords' => $document_info['keywords']), json_decode($document_info['additional_info'], true));
    unset($query);
    $response['documentID'] = $_POST['documentID'];

    // Checking if a copy of this document is checked out by user
    $query = $db->prepare("SELECT * FROM booked_documents WHERE user_id = ? AND document_id = ?");
    $query->bind_param("ii", $user_id, $_POST['documentID']);
    $query->execute();
    $booking_info = $query->get_result()->fetch_assoc();
    if($booking_info === null) {
        // If user didn't check out this document then exit with error
        json_response(400, array('errorMessage' => 'You didn\'t check out this document'));
    }
    unset($query);

    // If user was asked to return the document then exit with error
    if($booking_info['asked_to_return'] == 1) {
        json_response(400, array('errorMessage' => 'You were asked to return the document due to the outstanding request'));
    }

    // If user has already renewed the document and user isn't a Visiting Professor
    if($booking_info['renewed'] == 1 && $user_info['type'] != 5) {
        json_response(400, array('errorMessage' => 'You have already renewed the document'));
    }

    // If the document is a reference book, then exit with error
    if(in_array($document_info['type'], array(0, 1)) && $document_info['reference'] == 1) {
        // If there's no available copies of the document in library then exit with error
        json_response(400, array('errorMessage' => 'Reference books and magazines are not available to be renewed'));
    }

    // Calculate the return period
    if($user_info['type'] == 5) {
        $return_period = 1;
    } elseif($document_info['type'] == 0) {
        $return_period = 3;
        if(in_array($user_info['type'], array(2, 3, 4))) {
            $return_period = 4;
        } elseif($document_info['bestseller'] == 1) {
            $return_period = 2;
        }
    } elseif($document_info['type'] == 1 || $document_info['type'] == 2) {
        $return_period = 2;
    }
    $response['returnPeriod'] = $return_period;
    $response['returnTill'] = $booking_info['return_till'] + $return_period * 604800;
    $response['renewed'] = 1;

    // Book a document
    $query = $db->prepare("UPDATE booked_documents SET return_till = ?, renewed = 1 WHERE user_id = ? and document_id = ?");
    $query->bind_param("iii", $response['returnTill'], $user_id, $_POST['documentID']);
    $query->execute();
    unset($query);

    json_response(200, $response);

?>