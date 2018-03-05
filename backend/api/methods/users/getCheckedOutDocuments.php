<?php

    // If access token is invalid or if user doesn't have an access to this method then exit with error
    ensure_access();

    $response = array();

    // Get documents user checked out
    $query_string = "SELECT document_id, return_till FROM booked_documents WHERE user_id = ?";
    if(!is_empty($_GET['overdueOnly']) && $_GET['overdueOnly'] == 1) {
        $query_string = $query_string." AND return_till < ".time();
    }
    $query = $db->prepare($query_string);
    $query->bind_param("i", get_user_id());
    $query->execute();
    $entries = $query->get_result();
    for($i = 0; $cur_entry = $entries->fetch_assoc(); $i++) {
        // Get document info
        $document_query = $db->prepare("SELECT * FROM documents WHERE id = ?");
        $document_query->bind_param("i", $cur_entry['document_id']);
        $document_query->execute();
        $document_info = $document_query->get_result()->fetch_assoc();
        $response[$i]['documentInfo'] = array_merge(array('id' => $document_info['id'], 'instockCount' => $document_info['instock_count'], 'type' => $document_info['type'], 'title' => $document_info['title'], 'authors' => $document_info['authors'], 'price' => $document_info['price'], 'keywords' => $document_info['keywords']), json_decode($document_info['additional_info'], true));
        unset($document_info);
        unset($document_query);

        $response[$i]['returnTill'] = $cur_entry['return_till'];

        $response[$i]['isOverdue'] = false;
        if($response[$i]['returnTill'] < time()) {
            $response[$i]['isOverdue'] = true;
        }

        $response[$i]['fine'] = 0;
        if($response[$i]['isOverdue']) {
            // Calculating fine
            $response[$i]['fine'] = min(ceil((time() - $response[$i]['returnTill']) / 86400), $response[$i]['documentInfo']['price']);
        }
    }
    unset($query);

    // Send response data to the client
    json_response(200, $response);

?>