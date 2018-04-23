<?php

    // SYSTEM CONFIG BEGIN
	define('DB_HOST', 'localhost');
	define('DB_USER', 'insight911_1');
	define('DB_PASSWORD', 'z4WL4nb15fZ2');
	define('DB_NAME', 'insight911_1');
	define('DEBUG', false);

	$db = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

    function catch_fatal_error() {
        // Getting Last Error
        $error =  error_get_last();

        // Check if Last error is of type FATAL
        if(isset($error['type']) && $error['type'] == E_ERROR) {
            if(DEBUG) {
                json_response(500, array('errorMessage' => $error['message'].' in '.$error['file'].' on line '.$error['line']));
            } else {
                json_response(500, array('errorMessage' => 'Internal Server Error'));
            }
        }

    }
    register_shutdown_function('catch_fatal_error');
    //SYSTEM CONFIG END

    define('HASH_SALT', '96YNzk5DBx5ABLJV');
    define('ACCESS_TOKEN_LIFETIME', 259200);
    define('OVERDUE_FINE_IN_RUB_PER_DAY', 100);
    define('PRIORITY_OF_USER_TYPES_IN_QUEUE', '1,4,2,5,3');

?>