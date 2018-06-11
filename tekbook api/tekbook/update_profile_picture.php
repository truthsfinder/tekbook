<?php

	require "db_config.php";
	 
	 if($_SERVER['REQUEST_METHOD'] == 'POST')
	 {
		$image = $_POST['image'];
		$image_name = $_POST['image_name'];
		$user_id = $_POST['user_id'];

		$path = "$image_name.png";

		$actualpath = "images/$path";

		$sql = "UPDATE user SET profile_picture = '$path' WHERE user_id = $user_id";

		if (mysqli_query($con,$sql)) {
		    file_put_contents($actualpath, base64_decode($image));
		    echo $path;
		}
		mysqli_close($con);
	 }else{
	 	echo "Uploading failed";
	 }
?>