<?php
$host = 'localhost';
$username = 'root';
$password = 'gameverse';
$db = 'proyecto_db';

$conexion = mysqli_connect($host, $username, $password, $db);

if(!$conexion){
    exit(mysqli_connect_error());
}

mysqli_set_charset($conexion,'utf8');
?>
