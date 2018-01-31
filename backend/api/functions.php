<?php
    function init() {
        remove_expired_access_tokens();
    }

    function remove_expired_access_tokens() {
        global $db;
        $db->query("DELETE FROM sessions WHERE expiry_date < ".time());
    }

    function json_response($code, $response) {
        http_response_code($code);
        exit(json_encode($response));
    }