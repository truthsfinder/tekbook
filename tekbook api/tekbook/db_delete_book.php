<?php
 	
 	require "db_config.php";

 	if($_SERVER['REQUEST_METHOD']=='GET'){

	 	$user_id  = $_GET['user_id'];
	 	$book_id  = $_GET['book_id'];
	 
	 	$query = "DELETE FROM `userbooks`
	 			WHERE `userbooks`.`user_id` = $user_id AND `userbooks`.`book_id` = $book_id";

	 	$query2 = "DELETE FROM `books`
	 			WHERE `books`.`book_id` = $book_id";
	 
	 	mysqli_query($con,$query);
	 	mysqli_query($con,$query2);
		 
		mysqli_close($con);
	 
	 }
?>
