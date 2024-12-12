<?php

// 이미지, 텍스트 정보를 받아 서버에 저장하는 파일
error_reporting(E_ALL);
include "dbconnect.php";

$user_email=$_POST['user_email'];
$user_name = $_POST['user_name'];
$user_selfintro = $_POST['user_selfintro'];
$profileimg = $_POST['profile_image'];

// 파일명에 임의의 난수를 부여해(rand()) jpg 확장자로 저장한다
$filename = "IMG".rand().".jpg";
/**
 * file_put_contents() : PHP에서 데이터 / 텍스트를 파일에 쓰는 데 사용되는 함수
 * int file_put_contents ( string $filename , mixed $data [, int $flags = 0 [, resource $context ]] )
 */
file_put_contents("images/".$filename, base64_decode($profileimg));

$qry = "UPDATE  dailyreport.user SET user_name='$user_name',user_selfintro='$user_selfintro',profile_image='$filename' WHERE user_email='$user_email'";
$res = mysqli_query($con, $qry);

if ($res == true)
{
    echo "File Uploaded Successfully";
}
else
{
    echo "Couldn't upload file";
}

