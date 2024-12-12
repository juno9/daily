<?php
// error_reporting(E_ALL);
 include "dbconnect.php";//db연결

$user_email=$_POST['user_email'];
$profile_user_email=$_POST['profile_user_email'];






$qry="INSERT INTO dailyreport.follow(following_user_email,followed_user_email) VALUES ('$user_email','$profile_user_email')";
$result=mysqli_query($con,$qry);
$emparray = array();
if($result){
echo 'following';
}
else{
echo 'not_following';
}
?>