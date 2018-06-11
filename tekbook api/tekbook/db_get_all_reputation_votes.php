<?php
 	
 	require "db_config.php";

 	if($_SERVER['REQUEST_METHOD']=='GET'){

 		$voter_id = $_GET['voter_id'];
 		$voted_id = $_GET['voted_id'];

		$query = "SELECT * FROM `reputation_votes` WHERE voter_id = $voter_id AND voted_id = $voted_id";

		$res = mysqli_query($con, $query);

		$row = mysqli_fetch_array($res);

		if($row != NULL){
			$result = array();
			array_push($result,array(
			 "reputation_votes_id"=>$row['reputation_votes_id'],
			 "voter_id"=>$row['voter_id'],
			 "voted_id"=>$row['voted_id'],
			 "upvote"=>$row['upvote'],
			 "downvote"=>$row['downvote'],
			 "status"=>'success'
			 )
			);
			 
			echo json_encode(array("result"=>$result));
			 
			mysqli_close($con);
		}else{
			echo 'not_voted_yet';
		}
	}
?>
