<?php

    // If access token is invalid or if user doesn't have an access to this method then exit with error
    ensure_access();

    $response = array();

    // Get unread notifications
    $query = $db->prepare("SELECT id, text AS notification FROM notifications WHERE user_id = ? AND unread = 1");
    $query->bind_param("i", get_user_id());
    $query->execute();
    $query = $query->get_result();
    while($notification = $query->fetch_assoc()) {
        $response[] = $notification;

        // Mark notification as read
        $mark_read_query = $db->prepare("UPDATE notifications SET unread = 0 WHERE id = ?");
        $mark_read_query->bind_param("i", $notification['id']);
        $mark_read_query->execute();
        unset($mark_read_query);
    }
    unset($query);

    // Send response data to the client
    json_response(200, $response);

?>