<?php
 	
 	require "db_config.php";

 	if($_SERVER['REQUEST_METHOD']=='GET'){
	 
	 	$query = "SELECT `user`.`user_id`, `user`.`firstname`, `user`.`lastname`, `books`.`book_class`, `books`.`book_price`, `books`.`book_status`
	 			FROM `userbooks` 
	 			LEFT JOIN `user` ON `user`.`user_id` = `userbooks`.`user_id`
	 			LEFT JOIN `books` ON `userbooks`.`book_id` = `books`.`book_id`
	 			ORDER BY `books`.`book_price`";
	 
	 	$res = mysqli_query($con,$query);
	 	
		$result = array();
		
		while($row = mysqli_fetch_array($res)){
			array_push($result,
				array('user_id'=>$row[0],
				'firstname'=>$row[1],
				'lastname'=>$row[2],
				'book_class'=>$row[3],
				'book_price'=>number_format($row[4], 2),
				'book_status'=>$row[5]
			));
		}
		 
		echo json_encode(array("result"=>$result));
		 
		mysqli_close($con);
	 
	 }
?>
