<?php
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();
	 
	// json response array
	$response = array("error" => "false");
	 
	if (isset($_POST['email']) && isset($_POST['oldPassword']) && isset($_POST['newPassword'])) {
	 
		$email = $_POST['email'];
		$oldPassword = $_POST['oldPassword'];
		$newPassword = $_POST['newPassword'];
	 
		$user = $db->getUserByEmailAndPassword($email, $oldPassword);
	 
		if ($user != false) {
			$user2 = $db->savePassword($email, $newPassword);
			if ($user2 != false) {
				$response["error"] = "false";
				$response["id"] = $user2["id_user"];
				echo json_encode($response);
			} else {
				$response["error"] = "true";
				$response["error_msg"] = "Something wrong, please try again later.";
				echo json_encode($response);
			}
		} else {
			$response["error"] = "true";
			$response["error_msg"] = "Password invalid";
			echo json_encode($response);
		}
	} else {
		$response["error"] = "true";
		$response["error_msg"] = "Something missing!";
		echo json_encode($response);
	}
?>