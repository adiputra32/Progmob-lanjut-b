<?php
 
 $server  = "localhost"; // sesuaikan alamat server anda
 $user  = "id11847976_venat_db"; // sesuaikan user web server anda
 $password = "venatpassword"; // sesuaikan password web server anda
 $database = "id11847976_venat_db"; // sesuaikan database web server anda
 
$connect = mysqli_connect("localhost", "my_user", "my_password", "my_db");

if (!$connect) {
    echo "Error: Unable to connect to MySQL." . PHP_EOL;
    echo "Debugging errno: " . mysqli_connect_errno() . PHP_EOL;
    echo "Debugging error: " . mysqli_connect_error() . PHP_EOL;
    exit;
}

echo "Success: A proper connection to MySQL was made! The my_db database is great." . PHP_EOL;
echo "Host information: " . mysqli_get_host_info($connect) . PHP_EOL;

mysqli_close($connect);
?>