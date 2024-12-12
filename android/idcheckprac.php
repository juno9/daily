<?php

    include "dbconnect.php";
//

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $json = file_get_contents('php://input');
    $data = json_decode($json, true);
    
    $memberId = $data['memberId'];

    // $user_email 값을 이용한 로직 처리
    // ...
}

    
    
   
$checkqry="SELECT * FROM user WHERE user_email='$memberId'";
 
    $res = mysqli_query($con,$checkqry);
 
 
    if($res->num_rows >= 1){
        echo json_encode(array('res'=>'bad'));
    }else{
        echo json_encode(array('res'=>'good'));
    }
?>