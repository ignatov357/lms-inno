<?php
	require_once('config.php');

	// Throws error if there's database connection error
	if ($db->connect_errno) {
		http_response_code(500);
		exit(json_encode(array('errorMessage' => 'Internal Server Error')));
	}

	// Splits request uri into the array of url components
	$url_components = explode('/', trim($_SERVER['REQUEST_URI'], '/'));

	// Call method if it exists
	$method_exists = false;
	if(count($url_components) == 2 && ($url_components[0] == 'users' || $url_components[0] == 'documents')) {
		if(file_exists('methods/'.$url_components[0].'/'.$url_components[1].'.php')) {
			include('methods/'.$url_components[0].'/'.$url_components[1].'.php');
			$method_exists = true;
		}
	} elseif(count($url_components) == 3 && $url_components[0] == 'manage' && ($url_components[1] == 'users' || $url_components[1] == 'documents')) {
		if(file_exists('methods/'.$url_components[0].'/'.$url_components[1].'/'.$url_components[2].'.php')) {
			include('methods/'.$url_components[0].'/'.$url_components[1].'/'.$url_components[2].'.php');
			$method_exists = true;
		}
	}

	// Throws error if method doesn't exist
	if(!$method_exists) {
		http_response_code(404);
		exit(json_encode(array('errorMessage' => 'Invalid API method')));
	}
?>