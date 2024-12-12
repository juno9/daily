<?php  
    include "dbconnect.php";


$checkqry="SELECT * FROM user WHERE user_email='$_POST[user_email]'";
$checkresult=mysqli_query($con, $checkqry);


if($checkresult->num_rows > 0){//만약에 같은 이메일로 가입이 되어 있으면
    echo "already";
    } 
    else{
    $qry= "INSERT INTO user(user_email, user_password, user_name,profile_image) VALUES ('$_POST[user_email]',HEX(AES_ENCRYPT('$_POST[user_password]','a')),'$_POST[user_name]',null)";
    // $qry= "INSERT INTO user(user_email, user_password, user_name) VALUES ('$_POST[user_email]','$_POST[user_password]','$_POST[user_name]')";
    $result=mysqli_query($con, $qry);
    mysqli_close($con);
    if($result){
        // 성공
        echo 1;
    }else {
        // 실패
        echo 0;
    }  
    }

  

?>