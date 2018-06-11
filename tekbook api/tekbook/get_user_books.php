<?php
 	
 	require "db_config.php";

 	if($_SERVER['REQUEST_METHOD']=='GET'){

	 	$user_id  = $_GET['user_id'];
	 
	 	$query = "SELECT `userbooks`.`book_id`, `books`.`book_class`, `books`.`book_title`, `books`.`book_description`, `books`.`book_price`, `books`.`book_status`
	 			FROM `userbooks` 
	 			LEFT JOIN `books` ON `books`.`book_id` = `userbooks`.`book_id`
	 			LEFT JOIN `user` ON `user`.`user_id` = `userbooks`.`user_id`
	 			where `userbooks`.`user_id` = $user_id";
	 
	 	$res = mysqli_query($con,$query);
	 	
		$result = array();
		
		while($row = mysqli_fetch_array($res)){
			array_push($result,
				array('book_id'=>$row[0],
				'book_class'=>$row[1],
				'book_title'=>$row[2],
				'book_description'=>$row[3],
				'book_price'=>number_format($row[4], 2),
				'book_status'=>$row[5]
			));
		}
		 
		echo json_encode(array("result"=>$result));
		 
		mysqli_close($con);
	 
	 }
?>
