<?php
// error_reporting(E_ALL);
 include "dbconnect.php";//db연결

$user_email=$_POST['user_email'];
$profile_user_email=$_POST['profile_user_email'];
$qry="SELECT * FROM follow WHERE following_user_email='$user_email' AND followed_user_email='$profile_user_email'";
$result=mysqli_query($con,$qry);
$emparray = array();
if($result->num_rows > 0){
echo "following";
}
else{
echo "not_following";
}
?>