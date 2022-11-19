<?php
include 'config.php';

$retorno = array(
    'exito' => false,
    'mensaje' => 'N/A'
);

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    //Decodificar los datos recibidos
    $post = empty($_POST) ? json_decode(file_get_contents('php://input'), true) : $_POST;

    $nombre = $post['nombre']; //$nombre = 'Emi';
    $password = $post['password']; //$password = 'S1st';
    $email = $post['email']; //$email = '201960417@ucc.mx';
    //$imagen = file('img/imagen_perfil.jpg');
    //$imgContenido = addslashes(file_get_contents($imagen));

    $passmd5 = md5($password);

    $insert = "insert into dbperfil (nombre,password,email) values ('$nombre','$passmd5','$email')";

    $resultado = mysqli_query($conexion, $insert);

    if($resultado){
        $retorno['exito'] = true;
        $retorno['mensaje'] = 'Guardado correctamente';
    }else{
        $retorno['mensaje'] = 'Error en BD';
    }

    header('Content-type: application/json');
    echo json_encode($retorno);
    exit();
}
$retorno['mensaje'] = 'No se encontro ningun registro';

header('Content-type: application/json');
echo json_encode($retorno);
?>