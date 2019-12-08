<?php
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();
	 
	// json response array
	$response = array("error" => "false");
	 
	if (isset($_POST['email'])) {
		// menerima parameter POST ( email )
		$email = $_POST['email'];
	 
		// get the user by email
		$user = $db->getDataUser($email);
	 
		if ($user != false) {
			if ($user["id_card"] != NULL && $user["phone"] != NULL){
				$response["error"] = "false";
				$response["id"] = $user["id_user"];
				if ($user["address"] != NULL){
				    $response["address"] = $user["address"];
				}
				echo json_encode($response);
			} else {
				if ($user["address"] != NULL){
				    $response["address"] = $user["address"];
				}
				$response["error"] = "true";
				$response["error_msg"] = "Not active";
				echo json_encode($response);
			}
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