<?php 
//error_reporting(E_ALL);
include "dbconnect.php";
$user_email=$_POST['user_email'];
$start_date=$_POST['start_date'];
$start_time=$_POST['start_time'];
$end_date=$_POST['end_date'];
$end_time=$_POST['end_time'];
$title=$_POST['title'];
$contents=$_POST['contents'];
$focus=$_POST['focus'];




//순서를 정리해보자.
//1. 입력값들을 받는다.
//2. 이메일과 시간을 기준으로 기존에 입력된 값이 있는지 select문을 돌려 확인한다
//3. 셀렉트문의 결과 값이 이메일과 시간이 충족되는 행이 있다면 업데이트문으로 내용, 카테고리, 집중도 만 업데이트 한다.
//4. 셀렉트문의 결과 이메일과 시간이 충족되는 행이 없다면 인서트 문을 돌려 새 값들을 넣는다.


    $qry3="INSERT INTO dailyreport.record(user_email, start_date, start_time, end_date, end_time, title, contents,focus) VALUES ('$user_email','$start_date','$start_time','$end_date','$end_time','$title','$contents','$focus')";//새로 데이터를 삽입하는 쿼리
    $result3=mysqli_query($con,$qry3);
    if($result3){
    echo '1';
    }else{
    echo '2';
    }


?>