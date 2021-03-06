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

    // If the document is a reference book, then exit with error
    if(in_array($document_info['type'], array(0, 1)) && $document_info['reference'] == 1) {
        // If there's no available copies of the document in library then exit with error
        json_response(400, array('errorMessage' => 'Reference books and magazines are not available for checking out'));

    }

    // If user already checked out copy of this document, then exit with error
    $query = $db->prepare("SELECT * FROM booked_documents WHERE document_id = ? and user_id = ?");
    $query->bind_param("ii", $_POST['documentID'], $user_id);
    $query->execute();
    if($query->get_result()->fetch_assoc() !== null) {
        json_response(400, array('errorMessage' => 'You already checked out a copy of this document'));
    }
    unset($query);

    // If there's no available copies of the document in library at this moment then exit with error
    if($document_info['instock_count'] <= 0 || $db->query("SELECT * FROM queue WHERE document_id = " . intval($_POST['documentID']))->fetch_assoc() !== null) {
        // Put user to the queue
        $query = $db->prepare("SELECT * FROM queue WHERE user_id = ? AND document_id = ?");
        $query->bind_param("ii", $user_id, $_POST['documentID']);
        $query->execute();
        if($query->get_result()->fetch_assoc() === null) {
            $queue_add_query = $db->prepare("INSERT INTO queue (user_id, document_id) VALUES (?, ?)");
            $queue_add_query->bind_param("ii", $user_id, $_POST['documentID']);
            $queue_add_query->execute();
            unset($queue_add_query);
        }
        unset($query);

        // If some users renewed this document ask them to return it
        $query = $db->prepare("SELECT user_id FROM booked_documents WHERE document_id = ? AND renewed = 1");
        $query->bind_param("i", $_POST['documentID']);
        $query->execute();
		$query = $query->get_result();
        while($cur_user_id = $query->fetch_assoc()['user_id']) {
            $update_booked_documents_query = $db->prepare("UPDATE booked_documents SET asked_to_return = 1, return_till = ? WHERE user_id = ? AND document_id = ?");
            $update_booked_documents_query->bind_param("iii", time(), $cur_user_id, $_POST['documentID']);
            $update_booked_documents_query->execute();
            unset($update_booked_documents_query);

            create_notification($cur_user_id, 'Outstanding request received. Please return the document "'.$document_info['title'].'" immediately.');
        }
        unset($query);
		
		$queue = get_queue_for_document($_POST['documentID'], true);
		$position_in_queue = array_search($user_id, $queue) + 1;

        json_response(400, array('errorMessage' => 'There\'re no available copies at this moment, your request is #' . $position_in_queue . ' in queue'));
    }

    // Decrease the count of available copies of this document
    $query = $db->prepare("UPDATE documents SET instock_count = instock_count - 1 WHERE id = ?");
    $query->bind_param("i", $_POST['documentID']);
    $query->execute();
    unset($query);

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
    $response['returnTill'] = time() + $return_period * 604800;

    // Book a document
    $query = $db->prepare("INSERT INTO booked_documents (user_id, document_id, return_till) VALUES (?, ?, ?)");
    $query->bind_param("iii", $user_id, $_POST['documentID'], $response['returnTill']);
    $query->execute();
    unset($query);

    json_response(200, $response);

?>
