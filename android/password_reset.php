<?php  

//error_reporting(E_ALL);
    include "dbconnect.php";
    
    // $qry= "INSERT INTO user(user_email, user_password, user_name) VALUES ('$_POST[user_email]',HEX(AES_ENCRYPT('$_POST[user_temp_password]','a')),'$_POST[user_name]')";
    $qry2="UPDATE user SET user_password = HEX(AES_ENCRYPT('$_POST[user_temp_password]','a')) WHERE user_email = '$_POST[user_email]'";
    // $qry2="UPDATE user SET user_password = '$_POST[user_temp_password]' WHERE user_email = '$_POST[user_email]'";
    // $qry2="UPDATE user SET user_password = HEX(AES_ENCRYPT('djklasjd11sl','a')) WHERE user_email = 'ppow112@naver.com'";
    $result=mysqli_query($con, $qry2);
    mysqli_close($con);
    
    

    if($result){
        // 성공
        echo 1;
    }else {
        // 실패
        echo 0;
    }
    
    
?>