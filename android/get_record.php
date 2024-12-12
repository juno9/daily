<?php
// error_reporting(E_ALL);
 include "dbconnect.php";//db연결
$start_date=$_POST['start_date'];//날짜값 받음
$user_email=$_POST['user_email'];
$qry="SELECT * FROM record WHERE start_date='$start_date' AND user_email='$user_email' ORDER BY start_time asc";
// $qry="SELECT * FROM record WHERE user_email='$user_email'";
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
}else{
echo '기록없음';
}
?>