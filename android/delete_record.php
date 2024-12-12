<?php 
// error_reporting(E_ALL);
include "dbconnect.php";

$record_seq=$_POST['record_seq'];



$qry2="DELETE FROM dailyreport.record WHERE record_seq='$record_seq'";
$result2=mysqli_query($con,$qry2);
      if($result2) {
        echo '1';
      } else {
        echo '2';
      }
 
?>