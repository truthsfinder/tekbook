<?php
 	
 	require "db_config.php";

 	//inserting data to user
	$voter_id = $_POST["voter_id"];
	$voted_id = $_POST["voted_id"];
	$upvote = $_POST["upvote"];
	$downvote = $_POST["downvote"];
	$action = $_POST["action"];
	$reputation = $_POST["reputation"];
	$vote = $_POST["vote"];

	if($action == "update"){
		$query = "UPDATE reputation_votes SET upvote = '$upvote', downvote = '$downvote' 
			WHERE voter_id = $voter_id AND voted_id = $voted_id";
		if($vote == "upvote"){
			$query2 = "UPDATE user SET reputation = $reputation
			WHERE user_id = $voted_id";
		}else{
			$query2 = "UPDATE user SET reputation = $reputation 
			WHERE user_id = $voted_id";
		}

		mysqli_query($con, $query);
		mysqli_query($con, $query2);
	}else{
		$query = "INSERT INTO reputation_votes (`voter_id`, `voted_id`, `upvote`, `downvote`) 
			  VALUES($voter_id, $voted_id, '$upvote', '$downvote');";

		if($vote == "upvote"){
			$query2 = "UPDATE user SET reputation = $reputation
			WHERE user_id = $voted_id";
		}else{
			$query2 = "UPDATE user SET reputation = $reputation 
			WHERE user_id = $voted_id";
		}

		mysqli_query($con, $query);
		mysqli_query($con, $query2);
	}

	

?>