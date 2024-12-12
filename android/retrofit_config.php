<?php
    $host = '127.0.0.1';
    $username = 'root'; # MySQL 계정 아이디
    $password = 'Skdye1dye!@#'; # MySQL 계정 패스워드
    $dbname = 'dailyreport';  # DATABASE 이름


    $con=mysqli_connect('127.0.0.1','root','Skdye1dye!@#',$dbname);
        if (!$con) {
            
        die("서버와의 연결 실패! : ".mysqli_connect_error());
        } else{
            
        }

?>