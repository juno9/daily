<?php
// retrofit_simplelogin.php

if ($_SERVER['REQUEST_METHOD'] == 'POST')
{
    include_once("retrofit_config.php");

    $username = $_POST['username'];

    $password = $_POST['password'];

  

    if( $username == '' || $password == '')
    {
        echo json_encode(array(
            "status" => "false",
            "message" => "Parameter missing!",
            "case"=>"1"
        ));
    }
    else
    {
        $query = "SELECT * FROM user WHERE user_email='$username' AND AES_DECRYPT(UNHEX(user_password), 'a')='$password'";
        $result= mysqli_query($con, $query);
        
        if($result)
        {  
            $query = "SELECT * FROM user WHERE user_email='$username' AND AES_DECRYPT(UNHEX(user_password), 'a')='$password'";
            $result= mysqli_query($con, $query);
            $emparray = array();
            if($result)
            {  
                while ($row = mysqli_fetch_assoc($result))
                {
                   $emparray = $row['user_email'];
                }
            }
      
            echo json_encode(
                array(
                    "status" => "true",
                    "message" => "로그인 성공",
                    "data" => $row['user_email']
                )
            );
            }
            else
            { 
                echo json_encode(
                    array(
                        "status" => "false",
                        "case"=>"2",
                        "message" => "아이디 또는 비밀번호를 확인해 주세요")
                    );
            }
            mysqli_close($con);
    }
}
else
{
    echo json_encode(
        array(
            "status" => "false",
            "case"=>"3",
            "message" => "에러가 발생했습니다. 다시 시도해 주세요"
        )
    );
}