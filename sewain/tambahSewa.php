<?php
 
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();
	 
	// json response array
	$response = array("error" => "false");
	 
	if (isset($_POST['email']) && isset($_POST['id_motor']) && isset($_POST['location']) && isset($_POST['days']) && isset($_POST['payment'])) {
	 
		// menerima parameter POST ( nama, email, password )
		$email = $_POST['email'];
		$id_motor = $_POST['id_motor'];
		$location = $_POST['location'];
		$days = $_POST['days'];
		$payment = $_POST['payment'];
	 
		$user = $db->tambahSewa($email, $id_motor, $location, $days, $payment);
		if ($user) {
			// simpan user berhasil
			$response["error"] = "false";
			echo json_encode($response);
		} else {
			// gagal menyimpan user
			$response["error"] = "true";
			$response["error_msg"] = "Something error. Please, try again later";
			echo json_encode($response);
		}
	} else {
		$response["error"] = "true";
		$response["error_msg"] = "Parameter (nama, email, phone, atau password) ada yang kurang";
		echo json_encode($response);
	}
?>