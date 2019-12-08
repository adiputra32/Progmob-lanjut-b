<?php
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();
	 
	// json response array
	$response = array("error" => "false");
	 
	if (isset($_POST['email']) && isset($_POST['password'])) {
	 
		// menerima parameter POST ( email dan password )
		$email = $_POST['email'];
		$password = $_POST['password'];
	 
		// get the user by email and password
		// get user berdasarkan email dan password
		$user = $db->getUserByEmailAndPassword($email, $password);
	 
		if ($user != false) {
			// user ditemukan
			$response["error"] = "false";
			$response["id"] = $user["id_user"];
			$response["user"]["nama"] = $user["name"];
			$response["user"]["email"] = $user["email"];
			echo json_encode($response);
		} else {
			// user tidak ditemukan password/email salah
			$response["error"] = "true";
			$response["error_msg"] = "Login failed! Password/Email is incorrect.";
			echo json_encode($response);
		}
	} else {
		$response["error"] = "true";
		$response["error_msg"] = "Parameter (email atau password) ada yang kurang";
		echo json_encode($response);
	}
?>