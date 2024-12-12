<?php
error_reporting(E_ALL);
 include "dbconnect.php";//db연결

$search_word=$_POST['search_word'];//날짜값 받음

$qry="SELECT user_name,user_email,profile_image FROM user WHERE user_name LIKE '%$search_word%' OR user_email LIKE '%$search_word%'";
$result=mysqli_query($con,$qry);
$emparray = array();
if($result->num_rows > 0){
    if($search_word=""){
echo "검색결과가 없습니다";
    } else

    {
while($row=mysqli_fetch_assoc($result))
{
    $emparray[] = $row;
}
echo json_encode(
    array(
        "data" => $emparray)
    );
}
}
else{
echo '기록없음';
}
?>