<?php
	define('DB_HOST', 'localhost');
	define('DB_USER', 'insight911_1');
	define('DB_PASSWORD', 'z4WL4nb15fZ2');
	define('DB_NAME', 'insight911_1');
	define('DEBUG', false);

	if(DEBUG) {
		ini_set('error_reporting', E_ALL);
		ini_set('display_errors', 1);
		ini_set('display_startup_errors', 1);
	}

	$db = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);
?>