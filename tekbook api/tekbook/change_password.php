<?php
    require "db_config.php";
    
    $user_id = $_POST['user_id'];
    $password = $_POST['password'];
    
    $query = "UPDATE user SET password = md5('$password') WHERE user_id = $user_id";
    
    if(mysqli_query($con, $query)){
        echo "success";
    }else{
        echo "failed";
    }
?>