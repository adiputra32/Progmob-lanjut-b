<?php
 
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();
	 
	// json response array
	$response = array("error" => "false");
	 
	if (isset($_POST['nama']) && isset($_POST['email']) && isset($_POST['phone'])) {
		
		$birthdate = '';
		$address = '';
		$userimage = '';
		$idcardimage = '';
		$userimage_name = '';
		$idcardimage_name = '';
	 
		$nama = $_POST['nama'];
		$email = $_POST['email'];
		$birthdate = $_POST['birthdate'];
		$phone = $_POST['phone'];
		$address = $_POST['address'];
		$userimage = $_FILES['userimage'];
		$userimage_name = basename($_FILES["userimage"]["name"]);
		$idcardimage = $_FILES['idcardimage'];
		$idcargimage_name = basename($_FILES["idcardimage"]["name"]);

		$user = $db->updateUser($nama, $email, $birthdate, $phone, $address, $userimage, $idcardimage, $userimage_name, $idcargimage_name);
		if ($user) {
			// simpan user berhasil
			$response["error"] = "false";
			$response["id"] = $user["id_user"];
			echo json_encode($response);
		} else {
			// gagal menyimpan user
			$response["error"] = "true";
			$response["error_msg"] = "Something error. Please, try again later";
			echo json_encode($response);
		}
	} else {
		$response["error"] = "true";
		$response["error_msg"] = "Parameter kurang";
		echo json_encode($response);
	}
?>