<?php
 
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();
	 
	// json response array
	$response = array("error" => "false");
	 
	if (isset($_POST['id_sewa']) && isset($_POST['bukti_pembayaran'])) {
		$id_sewa = $_POST['id_sewa'];
		$bukti_pembayaran = $_POST['bukti_pembayaran'];
	 
		$motor = $db->updatePembayaran($id_sewa, $bukti_pembayaran);
	 
		if ($motor != false) {
			$response["error"] = "false";
			echo json_encode($response);
		} else {
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