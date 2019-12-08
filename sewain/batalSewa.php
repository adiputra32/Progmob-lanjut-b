<?php
 
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();
	 
	// json response array
	$response = array("error" => "false");
	 
	if (isset($_POST['id_sewa'])) {
		$id_sewa = $_POST['id_sewa'];
	 
		$motor = $db->updateBatalSewa($id_sewa);
	 
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