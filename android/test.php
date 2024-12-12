<?php   
    include "dbconnect.php";

   
    $user_email=$_POST['user_email'];
    $user_password=$_POST['user_password'];

    $sql = "SELECT * FROM user WHERE user_email='$user_email' AND AES_DECRYPT(UNHEX(user_password), 'a')='$user_password'";
    // $sql = "SELECT * FROM user WHERE user_email='$user_email' AND user_password='$user_password'";
  

    $result = mysqli_query($con, $sql);
    
	if($result){
        if($row=mysqli_fetch_assoc($result)){
            $user_name=$row['user_email'];
            echo $user_email;
    }
        
    }else{
        echo 0;   
    }
?>