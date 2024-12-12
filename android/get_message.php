<?php
    include "dbconnect.php";
    $sender_email=$_POST['sender_email'];
    $receiver_email=$_POST['receiver_email'];
    // $sender_email='a@b.com';
    // $receiver_email='ppow1123@naver.com';
    $qry2 ="SELECT * FROM dailyreport.message WHERE sender_email = '$sender_email' AND  receiver_email='$receiver_email' UNION SELECT * FROM message WHERE sender_email = '$receiver_email' AND  receiver_email='$sender_email' ORDER BY message_num";//보낸이 받는이 를 기준으로 채팅 내용을 받음
    
    // $qry2 = "SELECT * FROM dailyreport.message WHERE sender_email='a@b.com' AND receiver_email='ppow1123@naver.com' UNION SELECT * FROM dailyreport.message where sender_email='ppow1123@naver.com' AND receiver_email='a@b.com' ORDER BY message_num";
    // 결과 값을 담을 배열 생성
    
    
    $result=mysqli_query($con,$qry2);
$emparray = array();
if($result->num_rows > 0){
while($row=mysqli_fetch_assoc($result))
{
    $emparray[] = $row;
}
echo json_encode(
    array(
        "data" => $emparray)
    );
}else{
echo '메시지 없음';
}

    
 ?>




