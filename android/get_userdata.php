<?php
//error_reporting(E_ALL);
 include "dbconnect.php";//db연결

$user_email=$_POST['user_email'];//날짜값 받음

$qry="SELECT user_name, user_selfintro, profile_image, user_email FROM user WHERE user_email='$user_email'";
$result=mysqli_query($con,$qry);
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
}
else{
echo '기록없음';
}
?>