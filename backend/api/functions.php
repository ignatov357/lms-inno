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

    function ensure_access($allowed_user_types = array()) {
        if (empty(getallheaders()['Access-Token'])) {
            json_response(401, array('errorMessage' => 'Access-Token required'));
        }

        global $db;
        $query = $db->prepare("SELECT * FROM sessions WHERE access_token = ?");
        $query->bind_param("s", getallheaders()['Access-Token']);
        $query->execute();
        $user_id = $query->get_result()->fetch_assoc()['user_id'];
        if ($user_id == NULL) {
            json_response(401, array('errorMessage' => 'The given Access-Token is invalid'));
        }
        if (!empty($allowed_user_types)) {
            $user_type = $db->query('SELECT type FROM users WHERE id = '.$user_id)->fetch_assoc()['type'];
            if(!in_array($user_type, $allowed_user_types)) {
                json_response(401, array('errorMessage' => 'The given Access-Token doesn\'t have an access to this method'));
            }
        }
    }

    function generate_password($length = 8) {
        $sets = array('abcdefghjkmnpqrstuvwxyz', 'ABCDEFGHJKMNPQRSTUVWXYZ', '1234567890');
        $all = '';
        $password = '';
        foreach($sets as $set)
        {
            $password .= $set[array_rand(str_split($set))];
            $all .= $set;
        }
        $all = str_split($all);
        for($i = 0; $i < $length - count($sets); $i++)
            $password .= $all[array_rand($all)];
        $password = str_shuffle($password);

        return $password;
    }

    function hash_password($password) {
        return md5($password.HASH_SALT);
    }