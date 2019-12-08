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
			// user ditemukan
			$response["error"] = "false";
			$response["id"] = $user["id_user"];
			$response["user"]["nama"] = $user["name"];
			
			if ($user["birthdate"] === "null" || $user["birthdate"] === ""){
				$response["user"]["birthdate"] = $user["birthdate"];
			} else {
				$response["user"]["birthdate"] = "kosong";
			}
			
			if ($user["phone"] === "null" || $user["phone"] === ""){
				$response["user"]["phone"] = $user["phone"];
			} else {
				$response["user"]["phone"] = "kosong";
			}
			
			if ($user["address"] === "null" || $user["address"] === ""){
				$response["user"]["address"] = $user["address"];
			} else {
				$response["user"]["address"] = "kosong";
			}
			
			if ($user["photo_profile"] === "null" || $user["photo_profile"] === ""){
				$response["user"]["photo_profile"] = $user["photo_profile"];
			} else {
				$response["user"]["photo_profile"] = "kosong";
			}
			
			if ($user["id_card"] === "null" || $user["id_card"] === ""){
				$response["user"]["id_card"] = $user["id_card"];
			} else {
				$response["user"]["id_card"] = "kosong";
			}
			
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