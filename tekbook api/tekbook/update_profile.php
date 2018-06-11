<?php
 	
 	require "db_config.php";

 	//inserting data to userbooks
	$user_id = $_POST["user_id"];
	$contact_number = $_POST["contact_number"];
	$birthdate = $_POST["birthdate"];
	$email_address = $_POST["email_address"];
	$password = $_POST["password"];
	$gender = $_POST["gender"];

	$query = "UPDATE user SET contact_number = '$contact_number', birthdate = '$birthdate', email_address = '$email_address', gender = '$gender', password = md5('$password')
		WHERE user_id = $user_id";

	mysqli_query($con, $query);
	

?>