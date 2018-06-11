<?php
 	
 	require "db_config.php";

 	if($_SERVER['REQUEST_METHOD']=='GET'){
 		$user_id = $_GET['user_id'];
	 
	 	$query = "SELECT `user`.`user_id`, `user`.`firstname`, `user`.`lastname`, `user`.`contact_number`, `user`.`reputation`, `user`.`email_address`, `user`.`profile_picture`
	 			FROM `user` 
                WHERE `user`.`user_id` = $user_id";
	 
	 	$res = mysqli_query($con,$query);
	 	
		$result = array();
		
		while($row = mysqli_fetch_array($res)){
			array_push($result,
				array('user_id'=>$row['user_id'],
				'firstname'=>$row['firstname'],
				'lastname'=>$row['lastname'],
				'contact_number'=>$row['contact_number'],
				'reputation'=>$row['reputation'],
				'email_address'=>$row['email_address'],
				'profile_picture'=>$row['profile_picture'],
				'status'=>'success'
			));
		}
		 
		echo json_encode(array("result"=>$result));
		 
		mysqli_close($con);
	 
	 }
?>
