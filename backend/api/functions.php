<?php
    function init() {
        header('Access-Control-Allow-Origin: http://awes-projects.com');
        if($_SERVER['REQUEST_METHOD'] == "OPTIONS") {
            header('Access-Control-Allow-Headers: Access-Token');
            exit();
        }
        remove_expired_access_tokens();
    }

    function remove_expired_access_tokens() {
        global $db;
        $db->query("DELETE FROM sessions WHERE expiration_date < ".time());
    }

    function json_response($code, $response) {
        http_response_code($code);
        exit(json_encode($response));
    }

    function get_user_id() {
        global $db;

        $query = $db->prepare("SELECT * FROM sessions WHERE access_token = ?");
        $query->bind_param("s", getallheaders()['Access-Token']);
        $query->execute();

        return $query->get_result()->fetch_assoc()['user_id'];
    }

    function get_user_type($user_id = null) {
        if($user_id === null) {
            $user_id = get_user_id();
        }

        global $db;

        $query = $db->prepare("SELECT type FROM users WHERE id = ?");
        $query->bind_param("i", $user_id);
        $query->execute();

        return $query->get_result()->fetch_assoc()['type'];
    }

    function get_permissions($user_id = null) {
        if($user_id === null) {
            $user_id = get_user_id();
        }

        global $db;

        $query = $db->prepare("SELECT * FROM librarians_permissions WHERE librarian_id = ?");
        $query->bind_param("i", $user_id);
        $query->execute();
        $permissions = $query->get_result()->fetch_assoc();
        unset($permissions['librarian_id']);
        unset($query);

        return $permissions;
    }

    function create_notification($user_id, $notification) {
        global $db;
        $query = $db->prepare("INSERT INTO notifications (user_id, text) VALUES (?, ?)");
        $query->bind_param("is", $user_id, $text);
        $query->execute();
    }

	function get_queue_for_document($document_id, $user_ids_only = false) {
        $queue = array();

        global $db;
		$users_by_types = array();

        $query = $db->query("SELECT DISTINCT type FROM users");
		while($user_type = $query->fetch_assoc()) {
			$users_by_types[$user_type['type']] = array();
		}
		unset($query);

		if($user_ids_only) {
            $query = $db->prepare("SELECT users.id, users.type FROM users INNER JOIN queue ON users.id = queue.user_id WHERE queue.document_id = ?");
        } else {
            $query = $db->prepare("SELECT users.id, users.type, users.name, users.address, users.phone FROM users INNER JOIN queue ON users.id = queue.user_id WHERE queue.document_id = ?");
        }
        $query->bind_param("i", $document_id);
        $query->execute();
		$query = $query->get_result();
		while($user = $query->fetch_assoc()) {
		    if($user_ids_only) {
                $users_by_types[$user['type']][] = $user['id'];
            } else {
                $users_by_types[$user['type']][] = $user;
            }
		}
		unset($query);

		foreach(explode(',', PRIORITY_OF_USER_TYPES_IN_QUEUE) as $user_type) {
		    if(array_key_exists($user_type, $users_by_types)) {
                $queue = array_merge($queue, $users_by_types[$user_type]);
                unset($users_by_types[$user_type]);
            }
		}
		foreach($users_by_types as $users) {
			$queue = array_merge($queue, $users);
		}

		return $queue;
	}

    function ensure_access($allowed_user_types = array()) {
        if (empty(getallheaders()['Access-Token'])) {
            json_response(401, array('errorMessage' => 'Access-Token required'));
        }

        $user_id = get_user_id();
        if ($user_id === null) {
            json_response(401, array('errorMessage' => 'Given Access-Token is invalid'));
        }
        if (!empty($allowed_user_types)) {
            global $db;
            $user_type = $db->query('SELECT type FROM users WHERE id = '.$user_id)->fetch_assoc()['type'];
            if(!in_array($user_type, $allowed_user_types)) {
                json_response(401, array('errorMessage' => 'User associated with given Access-Token doesn\'t have an access to this method'));
            }
        }
    }

    function ensure_permissions($required_permissions = array()) {
        global $db;

        $permissions = get_permissions();
        if($permissions === null) {
            json_response(401, array('errorMessage' => 'Librarian associated with given Access-Token doesn\'t have any permissions'));
        }

        foreach($required_permissions as $key => $value) {
            if($value == 1 && (!isset($permissions[$key]) || $permissions[$key] != 1)) {
                json_response(401, array('errorMessage' => 'Librarian associated with given Access-Token doesn\'t have required permissions to use this method'));
            }
        }
    }

    function ensure_required_params($params, $request_data) {
        foreach($params as $param) {
            if(is_empty($request_data[$param])) {
                json_response(400, array('errorMessage' => 'Some required parameters are missed'));
            }
        }
    }

    function is_empty($object) {
        return empty($object) && ($object == NULL || !in_array($object, array("0", 0, 0.0)));
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
