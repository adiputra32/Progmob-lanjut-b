<?php
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();
	 
	// json response array
	$response = array("error" => "false");
	 
	if (isset($_GET['email'])) {
		// menerima parameter POST ( email )
		$email = $_GET['email'];
	 
		$motor = $db->getDataHistorySemua($email);
	 
		if ($motor != false) {
			$response["error"] = "false";
			$response["motor"] = $motor;	
			echo json_encode($response);
		} else {
			// user tidak ditemukan email salah
			$response["error"] = "true";
			$response["error_msg"] = "Something error.";
			echo json_encode($response);
		}
	} else {
		$response["error"] = "true";
		$response["error_msg"] = "Parameter kurang";
		echo json_encode($response);
	}
?>