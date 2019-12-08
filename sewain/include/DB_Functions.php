<?php
 
class DB_Functions {
 
    private $conn;
 
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // koneksi ke database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
 
    // destructor
    function __destruct() {
         
    }
 
    public function simpanUser($nama, $email, $phone, $password) {
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
 
        $stmt = $this->conn->prepare("INSERT INTO tb_user(name, email, phone, password, salt) VALUES(?, ?, ?, ?, ?)");
		$stmt->bind_param("sssss", $nama, $email, $phone, $encrypted_password, $salt);
		$result = $stmt->execute();
		$stmt->close();
 
        // cek jika sudah sukses
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM tb_user WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }
	
	public function savePassword($email, $newPassword) {
        $hash = $this->hashSSHA($newPassword);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
 
        $stmt = $this->conn->prepare("UPDATE tb_user SET password = ?, salt = ? WHERE email = ?");
		$stmt->bind_param("sss", $encrypted_password, $salt, $email);
		$result = $stmt->execute();
		$stmt->close();
 
        // cek jika sudah sukses
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM tb_user WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }
	
	public function updateUser($nama, $email, $birthdate, $phone, $address, $userimage, $idcardimage, $userimage_name, $idcardimage_name) {
		// $nama = "admin3@yahoo.com";
		// $email = "admin3@yahoo.com";
		// $birthdate = "2019-11-11";
		// $phone = "0119021";
		// $address = "Tabanan";
		// $stmt = $this->conn->prepare("INSERT INTO tb_user(name, email, phone, password, salt) VALUES(?, ?, ?, ?, ?)");
		if($userimage_name != ''){
			$userimageres = $this->random_word($userimage_name);
		}
		
		if ($idcardimage_name != ''){
			$idcardimageres = $this->random_word($idcardimage_name);
		}
		
        $stmt = $this->conn->prepare("UPDATE tb_user SET name = ?, birthdate = ?, phone = ?, address = ?, id_card = ?, photo_profile = ? WHERE email = ?");
		$stmt->bind_param("sssssss", $nama, $birthdate, $phone, $address, $email, $userimage, $idcardimage);
		$result = $stmt->execute();
		$stmt->close();
 
        // cek jika sudah sukses
        if ($result) {
			if($userimageres != ""){
			    $actualpath = "/storage/ssd5/476/11594476/public_html/sewain/include/images/".$userimageres;
				file_put_contents($actualpath,base64_decode($userimage));
			}
			
			if ($idcardimageres != ""){
			    $actualpath = "/storage/ssd5/476/11594476/public_html/sewain/include/images/".$idcardimageres;
				file_put_contents($actualpath,base64_decode($idcardimage));
			}
            $stmt = $this->conn->prepare("SELECT * FROM tb_user WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }
 
    public function getUserByEmailAndPassword($email, $password) {
		$phone = $email;
 
        $stmt = $this->conn->prepare("SELECT * FROM tb_user WHERE email = ? OR phone = ?");
 
        $stmt->bind_param("ss", $email, $phone);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            // verifikasi password user
            $salt = $user['salt'];
            $encrypted_password = $user['password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // cek password jika sesuai
            if ($encrypted_password == $hash) {
                // autentikasi user berhasil
                return $user;
            }
        } else {
            return false;
        }
    }
	
    public function getDataUser($email) {
		$phone = $email;
 
        $stmt = $this->conn->prepare("SELECT * FROM tb_user WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }
 
    public function isUserExisted($email, $phone) {
        $stmt = $this->conn->prepare("SELECT email from tb_user WHERE email = ? OR phone = ?");
 
        $stmt->bind_param("ss", $email, $phone);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // user telah ada 
            $stmt->close();
            return true;
        } else {
            // user belum ada 
            $stmt->close();
            return false;
        }
    }
 
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
 
	public function random_word($userimage){
		$pool = '1234567890abcdefghijkmnopqrstuvwxyz';
		$imagename = '';
		
		for ($i = 0; $i < 20; $i++){
			$imagename .= substr($pool, mt_rand(0, strlen($pool) -1), 1);
		}
		
		$path = $_FILES['image']['name'];
        $ext = pathinfo($path, PATHINFO_EXTENSION);
		$imagename = $imagename.$ext;
		
		return $imagename;
	}
	
	public function getMotor($jumlah, $tambah, $kategori) { 
		if ($kategori == "All"){
			$stmt = $this->conn->prepare("SELECT tb_motor.id_motor,gambar_motor,merk,name,harga,jenis_motor FROM tb_motor LEFT JOIN tb_sewa ON tb_motor.id_motor = tb_sewa.id_motor JOIN tb_user_pemilik ON tb_user_pemilik.id_user_pemilik = tb_motor.id_user_pemilik JOIN tb_user ON tb_user.id_user = tb_user_pemilik.id_user WHERE id_sewa IS NULL OR status_sewa = 4 LIMIT ? OFFSET ?");
			$stmt->bind_param("ii", $tambah, $jumlah);
		} else {
			$stmt = $this->conn->prepare("SELECT tb_motor.id_motor,gambar_motor,merk,name,harga,jenis_motor FROM tb_motor LEFT JOIN tb_sewa ON tb_motor.id_motor = tb_sewa.id_motor JOIN tb_user_pemilik ON tb_user_pemilik.id_user_pemilik = tb_motor.id_user_pemilik JOIN tb_user ON tb_user.id_user = tb_user_pemilik.id_user WHERE id_sewa IS NULL OR status_sewa = 4 AND jenis_motor = ? LIMIT ? OFFSET ?");
			$stmt->bind_param("sii", $kategori, $tambah, $tambah);
		}
 
        if ($stmt->execute()) {
			$motor = mysqli_fetch_all(mysqli_stmt_get_result($stmt), MYSQLI_ASSOC);
			$result = array_column($motor, 'x2', 'x1');
            $stmt->close();
 
            return $motor;
        } else {
            return false;
        }
    }
	
	public function getMotorMostViewed($jumlah, $tambah) { 
		$stmt = $this->conn->prepare("SELECT tb_motor.id_motor,gambar_motor,merk,name,harga,jenis_motor,views FROM tb_motor 
			LEFT JOIN tb_sewa ON tb_motor.id_motor = tb_sewa.id_motor 
			JOIN tb_user_pemilik ON tb_user_pemilik.id_user_pemilik = tb_motor.id_user_pemilik 
			JOIN tb_user ON tb_user.id_user = tb_user_pemilik.id_user 
			WHERE id_sewa IS NULL OR status_sewa = 4 ORDER BY views DESC LIMIT ? OFFSET ?");
		$stmt->bind_param("ii", $tambah, $jumlah);
 
        if ($stmt->execute()) {
			$motor = mysqli_fetch_all(mysqli_stmt_get_result($stmt), MYSQLI_ASSOC);
			$result = array_column($motor, 'x2', 'x1');
            $stmt->close();
 
            return $motor;
        } else {
            return false;
        }
    }
	
	public function getMotorSingle($id_motor) { 
		$stmt = $this->conn->prepare("SELECT * FROM tb_motor 
			JOIN tb_user_pemilik ON tb_user_pemilik.id_user_pemilik = tb_motor.id_user_pemilik 
			JOIN tb_user ON tb_user.id_user = tb_user_pemilik.id_user
			WHERE id_motor = ?");
		$stmt->bind_param("s", $id_motor);
 
        if ($stmt->execute()) {
            $motor = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $motor;
        } else {
            return false;
        }
    }
	
	public function tambahSewa($email, $id_motor, $location, $days, $payment) {
        $stmt = $this->conn->prepare("INSERT INTO tb_sewa(id_motor, id_user_pemilik, id_user, jumlah_hari, pembayaran, lokasi) VALUES(?, (SELECT id_user_pemilik FROM tb_motor WHERE id_motor = ?), (SELECT id_user FROM tb_user WHERE email = ?), ?, ?, ?)");
		$stmt->bind_param("ssssss", $id_motor, $id_motor, $email, $days, $payment, $location);
		$result = $stmt->execute();
		$stmt->close();
 
        // cek jika sudah sukses
        if ($result) {
            return true;
        } else {
            return false;
        }
    }
	
	public function getDataHistory($id_sewa){
		$stmt = $this->conn->prepare("SELECT * FROM tb_sewa
			JOIN tb_motor ON tb_motor.id_motor = tb_sewa.id_motor
			JOIN tb_user_pemilik ON tb_user_pemilik.id_user_pemilik = tb_sewa.id_user_pemilik
			JOIN tb_user ON tb_user.id_user = tb_user_pemilik.id_user
			WHERE id_sewa = ?");
		$stmt->bind_param("s", $id_sewa);
 
        if ($stmt->execute()) {
            $motor = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $motor;
        } else {
            return false;
        }
	}
	
	public function getDataHistorySemua($email){
		$stmt = $this->conn->prepare("SELECT id_sewa,gambar_motor,merk,name,harga,jenis_motor,status_pembayaran FROM tb_sewa
			JOIN tb_motor ON tb_motor.id_motor = tb_sewa.id_motor
			JOIN tb_user_pemilik ON tb_user_pemilik.id_user_pemilik = tb_sewa.id_user_pemilik
			JOIN tb_user ON tb_user.id_user = tb_user_pemilik.id_user
			WHERE tb_sewa.id_user = (SELECT id_user FROM tb_user WHERE email = ?)");
		$stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
			$motor = mysqli_fetch_all(mysqli_stmt_get_result($stmt), MYSQLI_ASSOC);
			$result = array_column($motor, 'x2', 'x1');
            $stmt->close();
 
            return $motor;
        } else {
            return false;
        }
	}

	public function updateBatalSewa($id_sewa) {
        $stmt = $this->conn->prepare("UPDATE tb_sewa SET status_sewa = 4 WHERE id_sewa = ?");
		$stmt->bind_param("s", $id_sewa);
		$result = $stmt->execute();
		$stmt->close();
 
        // cek jika sudah sukses
        if ($result) {
            return true;
        } else {
            return false;
        }
    }
	
	public function updatePembayaran($id_sewa, $bukti_pembayaran) {
		$bukti_pembayaran_array = $this->random_word($bukti_pembayaran);
		
        $stmt = $this->conn->prepare("UPDATE tb_sewa SET status_sewa = 1, status_pembayaran = 1, bukti_pembayaran = ? WHERE id_sewa = ?");
		$stmt->bind_param("ss", $bukti_pembayaran_array[0], $id_sewa);
		$result = $stmt->execute();
		$stmt->close();
 
        // cek jika sudah sukses
        if ($result) {
			file_put_contents($bukti_pembayaran_array[1],base64_decode($bukti_pembayaran));
            return $result;
        } else {
            return false;
        }
    }
	
}
 
?>