<?php
    try {
        require_once('config.php');
        require_once('functions.php');

        // Throws error if there's database connection error
        if ($db->connect_errno) {
            throw new RuntimeException('Connection to the database couldn\'t be established');
        }

        init();

        // Splits request uri into the array of url components
        $url_components = explode('/', trim(explode('?', $_SERVER['REQUEST_URI'])[0], '/'));

        // Call method if it exists
        $method_exists = false;
        if (count($url_components) == 2 && ($url_components[0] == 'users' || $url_components[0] == 'documents')) {
            if (file_exists('methods/' . $url_components[0] . '/' . $url_components[1] . '.php')) {
                include('methods/' . $url_components[0] . '/' . $url_components[1] . '.php');
                $method_exists = true;
            }
        } elseif (count($url_components) == 3 && $url_components[0] == 'manage' && ($url_components[1] == 'users' || $url_components[1] == 'documents')) {
            if (file_exists('methods/' . $url_components[0] . '/' . $url_components[1] . '/' . $url_components[2] . '.php')) {
                include('methods/' . $url_components[0] . '/' . $url_components[1] . '/' . $url_components[2] . '.php');
                $method_exists = true;
            }
        }

        // Throws error if method doesn't exist
        if(!$method_exists) {
            json_response(404, array('errorMessage' => 'Invalid API method'));
        }

    } catch(Exception $e) {
        if(DEBUG) {
            json_response(500, array('errorMessage' => $e->getMessage()));
        } else {
            json_response(500, array('errorMessage' => 'Internal Server Error'));
        }
    }
?>