<?php
 
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();
	 
	// json response array
	$response = array("error" => "false");
	 
	if (isset($_POST['nama']) && isset($_POST['email']) && isset($_POST['phone']) && isset($_POST['password'])) {
	 
		// menerima parameter POST ( nama, email, password )
		$nama = $_POST['nama'];
		$email = $_POST['email'];
		$phone = $_POST['phone'];
		$password = $_POST['password'];
	 
		// Cek jika user ada dengan email yang sama
		if ($db->isUserExisted($email, $phone)) {
			// user telah ada
			$response["error"] = "true";
			$response["error_msg"] = "Email ". $email . " is already used";
			echo json_encode($response);
		} else {
			// buat user baru
			$user = $db->simpanUser($nama, $email, $phone, $password);
			if ($user) {
				// simpan user berhasil
				$response["error"] = "false";
				$response["id"] = $user["id_user"];
				$response["user"]["name"] = $user["name"];
				$response["user"]["email"] = $user["email"];
				$response["user"]["phone"] = $user["phone"];
				echo json_encode($response);
			} else {
				// gagal menyimpan user
				$response["error"] = "true";
				$response["error_msg"] = "Something error. Please, try again later";
				echo json_encode($response);
			}
		}
	} else {
		$response["error"] = "true";
		$response["error_msg"] = "Parameter (nama, email, phone, atau password) ada yang kurang";
		echo json_encode($response);
	}
?>