<?php
 	
 	require "db_config.php";

 	if($_SERVER['REQUEST_METHOD']=='GET'){

	 	$book_id  = $_GET['book_id'];
	 
	 	$query = "UPDATE `books` SET `book_status` = 'sold' WHERE `books`.`book_id` = $book_id";
	 
	 	if(mysqli_query($con,$query)){
	 	    echo "success";
	 	}else{
	 	    echo "failed";
	 	}
	 }
?>
