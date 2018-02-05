<?php

    $response = array();

    // Get documents info
    $query = $db->prepare("SELECT * FROM documents");
    $query->execute();
    $documents = $query->get_result();
    while($cur_document = $documents->fetch_assoc()) {
        $response[] = array_merge(array('id' => $cur_document['id'], 'instockCount' => $cur_document['instock_count'], 'type' => $cur_document['type'], 'title' => $cur_document['title'], 'authors' => $cur_document['authors'], 'price' => $cur_document['price'], 'keywords' => $cur_document['keywords']), json_decode($cur_document['additional_info'], true));
    }
    unset($query);

    // Send response data to the client
    json_response(200, $response);

?>