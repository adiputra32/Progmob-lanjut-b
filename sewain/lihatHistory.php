<?php
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();
	 
	// json response array
	$response = array("error" => "false");
	 
	if (isset($_GET['id_sewa'])) {
		// menerima parameter POST ( email )
		$id_sewa = $_GET['id_sewa'];
	 
		$motor = $db->getDataHistory($id_sewa);
	 
		if ($motor != false) {
			$response["error"] = "false";
			$response["id"] = $motor["id_sewa"];
			$response["motor"]["gambar_motor"] = $motor["gambar_motor"];
			$response["motor"]["merk"] = $motor["merk"];
			$response["motor"]["name"] = $motor["name"];
			$response["motor"]["harga"] = $motor["harga"];
			$response["motor"]["jenis_motor"] = $motor["jenis_motor"];
			$response["motor"]["status_pembayaran"] = $motor["status_pembayaran"];
			
			if ($motor["bukti_pembayaran"] == "null"){
				$response["motor"]["bukti_pembayaran"] = $motor["bukti_pembayaran"];
			} else {
				$response["motor"]["bukti_pembayaran"] = "kosong";
			}			
			$response["motor"]["lokasi"] = $motor["lokasi"];
			$response["motor"]["jumlah_hari"] = $motor["jumlah_hari"];
			$response["motor"]["total_harga"] = $motor["total_harga"];
			$response["motor"]["denda"] = $motor["denda"];
			$response["motor"]["asuransi"] = $motor["asuransi"];
			
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