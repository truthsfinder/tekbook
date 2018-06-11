<?php
	require "db_config.php";

	$username = $_POST["username"];

	$query = "SELECT user_id 
			FROM user
			WHERE `username` = '$username'";

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