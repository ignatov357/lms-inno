<?php

    $response = array();

    // Get documents info
    $query_string = "SELECT * FROM documents";
    $query_where_options = array();
    if(!is_empty($_GET['availableOnly']) && $_GET['availableOnly'] == 1) {
        $query_where_options[] = "instock_count > 0";
    }
    if(!is_empty($_GET['type']) && in_array($_GET['type'], array(0, 1, 2))) {
        $query_where_options[] = "type = ".$_GET['type'];
    }
    if(!empty($query_where_options)) {
        $query_string = $query_string." WHERE ".implode(" AND ", $query_where_options);
    }
    $query = $db->prepare($query_string);
    $query->execute();
    $documents = $query->get_result();
    while($cur_document = $documents->fetch_assoc()) {
        $cur_document = array_merge(array('id' => $cur_document['id'], 'instockCount' => $cur_document['instock_count'], 'type' => $cur_document['type'], 'title' => $cur_document['title'], 'authors' => $cur_document['authors'], 'price' => $cur_document['price'], 'keywords' => $cur_document['keywords']), json_decode($cur_document['additional_info'], true));
        if($cur_document['reference'] == 0) {
            $response[] = $cur_document;
        }
    }
    unset($query);

    // Send response data to the client
    json_response(200, $response);

?>