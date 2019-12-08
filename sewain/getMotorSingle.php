<?php
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();
	 
	// json response array
	$response = array("error" => "false");
	 
	if (isset($_GET['id_motor'])) {
		$id_motor = $_GET['id_motor'];
	 
		$motor = $db->getMotorSingle($id_motor);
	 
		if ($motor != false) {
			$response["error"] = "false";
			$response["id_motor"] = $motor["id_motor"];
			$response["motor"]["gambar_motor"] = $motor["gambar_motor"];
			$response["motor"]["merk"] = $motor["merk"];
			$response["motor"]["name"] = $motor["name"];
			$response["motor"]["harga"] = $motor["harga"];
			$response["motor"]["jenis_motor"] = $motor["jenis_motor"];
			$response["motor"]["tahun"] = $motor["tahun"];
			$response["motor"]["warna"] = $motor["warna"];
			$response["motor"]["CC"] = $motor["CC"];
			$response["motor"]["bahan_bakar"] = $motor["bahan_bakar"];
			$response["motor"]["helm"] = $motor["helm"];
			$response["motor"]["detail"] = $motor["detail"];
		
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