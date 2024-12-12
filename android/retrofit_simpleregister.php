<?php
// retrofit_simpleregister.php
error_reporting(E_ALL);

// $_SERVER['REQUEST_METHOD'] = 'POST';
if ($_SERVER['REQUEST_METHOD'] == 'POST')
{
    include_once("retrofit_config.php");

    // $name = "이름";
    // $username = "유저이름";
    // $password = "유저비번";
    // $hobby= "취미";


    $name = $_POST['name'];
    $username = $_POST['username'];
    $password = $_POST['password'];
    $hobby= $_POST['hobby'];


    // $name = '123';
    // $username = '123';
    // $password = '123';
    // $hobby= '123';

    if($name == '' || $username == '' || $password == '' || $hobby == '')
    {
        echo json_encode(array(
            "status" => "false",
            "message" => "필수 인자가 부족합니다")
        );
    }
    else
    {
        $query= "SELECT * FROM retrofitRegister WHERE username='$username'";
        $result= mysqli_query($con, $query);

        if(mysqli_num_rows($result) > 0){  
        echo json_encode(array( "status" => "false","message" => "이미 존재하는 이름입니다") );
        }
        else
        { 
            $query = "INSERT INTO retrofitRegister (name,hobby,username,password) VALUES ('$name','$hobby','$username','$password')";
            
           
            if(mysqli_query($con,$query))
            {
                $query= "SELECT * FROM retrofitRegister WHERE username='$username'";
                $result= mysqli_query($con, $query);
                $emparray = array();
                if(mysqli_num_rows($result) > 0)
                {  
                    while ($row = mysqli_fetch_assoc($result))
                    {
                        $emparray[] = $row;
                    }
                }
                echo json_encode(
                    array(
                        "status" => "true",
                        "message" => "회원가입 성공",
                        "data" => $emparray)
                    );
            }
            else
            {
                echo json_encode(
                    array(
                    "status" => "false",
                    "message" => "에러가 발생했습니다. 다시 시도해 주세요"
                    )
                );
            }
    }
    mysqli_close($con);
    }
}