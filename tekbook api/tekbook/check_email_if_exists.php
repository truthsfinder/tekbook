<?php
	require "db_config.php";

	$email = $_POST["email"];

	$query = "SELECT user_id 
			FROM user
			WHERE `email_address` = '$email'";

	if(mysqli_query($con, $query)){
		$row = mysqli_fetch_array(mysqli_query($con, $query));

		if($row != null){
			    echo "success";
		}else{
			echo "empty";
		}
	}else{
		echo "failed";
	}
?>