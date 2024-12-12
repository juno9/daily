<?php  
    include "dbconnect.php";

    $result=mysqli_query($con, "INSERT INTO user(user_email, user_password, user_name) VALUES ('$_POST[id]','$_POST[pw]','$_POST[nickname]')");
    mysqli_close($con);

    if($result){
        // 성공
        echo 1;
    }else {
        // 실패
        echo 0;
    }
    
    
?>