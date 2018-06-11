<?php
 	
 	require "db_config.php";

 	//inserting data to userbooks
	$user_id = $_POST["user_id"];
	$book_class = $_POST["book_class"];
	$book_title = $_POST["book_title"];
	$book_price = $_POST["book_price"];

	$query = "INSERT INTO books (`book_class`, `book_title`, `book_price`, `book_status`) 
			  VALUES('$book_class', '$book_title', $book_price, 'active')";

	if(mysqli_query($con, $query)){
		$book_id = mysqli_insert_id($con);

		date_default_timezone_set("Asia/Kuala_Lumpur");
		$date_added = date("Y-m-d h:i:s");

		$query2 = "INSERT INTO userbooks (`book_id`, `user_id`, `date_added`) 
			  VALUES($book_id, $user_id, '$date_added')";

		mysqli_query($con, $query2);
	}else{
		echo "Error: " . $query . "<br>" . mysqli_error($con);
	}

?>