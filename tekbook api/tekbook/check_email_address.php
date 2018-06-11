<?php
	require "db_config.php";

	$email_address = $_POST["email"];

	$query = "SELECT user_id 
			FROM user
			WHERE `email_address` = '$email_address'";

	if(mysqli_query($con, $query)){
		$row = mysqli_fetch_array(mysqli_query($con, $query));

		if($row != null){
			    echo $row['user_id'];
		}else{
			echo "empty";
		}
	}else{
		echo "failed";
	}
?>