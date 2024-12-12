<?php //error_reporting(E_ALL);
    include "dbconnect.php";

    $user_email=$_POST['user_email'];//유저 이메일
    $user_password=$_POST['user_password'];//유저 현재 비밀번호
    $user_newpassword=$_POST['user_newpassword'];//유저 새로운 비밀번호
    
    // $qry= "INSERT INTO user(user_email, user_password, user_name) VALUES ('$_POST[user_email]',HEX(AES_ENCRYPT('$_POST[user_temp_password]','a')),'$_POST[user_name]')";
    $selectqry="SELECT * FROM user WHERE user_email='$user_email' AND AES_DECRYPT(UNHEX(user_password), 'a')='$user_password'";//조회 쿼리-이 아이디로 이 비번 쓰는거 맞는지 체크
    // $selectqry="SELECT * FROM user WHERE user_email='ppow1123@naver.com' AND AES_DECRYPT(UNHEX(user_password), 'a')='skdye1dye!'";//조회 쿼리-이 아이디로 이 비번 쓰는거 맞는지 체크
    $selectresult=mysqli_query($con, $selectqry);
  
    
    

    if($row=mysqli_fetch_assoc($selectresult)){
        $updateqry="UPDATE user SET user_password = HEX(AES_ENCRYPT('$_POST[user_newpassword]','a')) WHERE user_email = '$_POST[user_email]'";//비밀번호 재설정 쿼리
        $updateresult=mysqli_query($con, $updateqry);
        if($updateresult){
            echo 1;
        }
      
    }else {
        // 실패
        echo "비번에러";
    }
    
    
?>