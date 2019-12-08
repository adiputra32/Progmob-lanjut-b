<?php
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();
	 
	// json response array
	$response = array("error" => "false");
	 
	if (isset($_GET['jumlah']) && isset($_GET['tambah'])) {
		$jumlah = $_GET['jumlah'];
		$tambah = $_GET['tambah'];
	 
		$motor = $db->getMotorMostViewed($jumlah, $tambah);
	 
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