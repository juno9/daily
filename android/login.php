<?php
    include "dbconnect.php";
    $user_email=$_POST['user_email'];
    $user_password=$_POST['user_password'];
    $qry2 ="SELECT * FROM user WHERE user_email = '$user_email' AND  AES_DECRYPT(UNHEX(user_password), 'a')='$user_password'";//id가 있는지 확인 
    $result= mysqli_query($con,$qry2);
    if($row=mysqli_fetch_assoc($result)){
      echo $user_email;
    } else{
        echo '로그인 실패';
    }

    //비번 안맞을 때랑 다 맞춰야 한다.
 ?>