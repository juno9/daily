<?php 
error_reporting(E_ALL);
include "dbconnect.php";
$start_date=$_POST['start_date'];
$start_time=$_POST['start_time'];
$end_date=$_POST['end_date'];
$end_time=$_POST['end_time'];
$title=$_POST['title'];
$record_seq=$_POST['record_seq'];
$contents=$_POST['contents'];
$focus=$_POST['focus'];







$qry2="UPDATE dailyreport.record SET  start_date='$start_date', start_time='$start_time', end_date='$end_date', end_time='$end_time',  contents='$contents', focus='$focus',title='$title' WHERE record_seq='$record_seq'";
$result2=mysqli_query($con,$qry2);
      if($result2) {
        echo '3';
      } else {
        echo '4';
      }
 
?>