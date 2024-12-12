<?php
// error_reporting(E_ALL);
 include "dbconnect.php";//db연결

$user_email=$_POST['user_email'];

$qry="SELECT user_email,user_name,profile_image FROM user WHERE user_email in (SELECT followed_user_email FROM follow WHERE following_user_email='$user_email')";
//결과로 내가 팔로우하고 있는 유저들 이메일 주소를 받을 것. 
//이 주소 하나하나를 바탕으로 각 유저의 정보를 받아와야 한다. 
//이메일 주소를 pk로 쓰고 조인을 쓰면 쿼리 하나로 다 받아올 수 있다. 
//그러려면 이메일 주소를 pk로 사용할 수 있게 db구조를 바꿔야 한다.
//그리고 또 한가지, 안드로이드 상에서 주로 활용하는 데이터가 바로 이메일 주소이다. user_seq는 누가 거들떠나 보는가
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