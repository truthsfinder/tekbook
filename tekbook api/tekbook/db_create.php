<?php
 	
 	require "db_config.php";

 	//inserting data to user
	$username = $_POST["username"];
	$lastname = $_POST["lastname"];
	$firstname = $_POST["firstname"];
	$email_address = $_POST["email_address"];
	$password = $_POST["password"];

	$query = "INSERT INTO user (`username`, `lastname`, `firstname`, `email_address`, `password`, `reputation`, `profile_picture`) 
			  VALUES('$username', '$lastname', '$firstname', '$email_address', md5('$password'), 1, 'default.png');";

	mysqli_query($con, $query);

?>