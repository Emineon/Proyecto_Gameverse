<?php
include '../config.php';

$retorno = array(
    'exito' => false,
    'mensaje' => 'N/A'
);

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $post = empty($_POST) ? json_decode(file_get_contents('php://input'), true) : $_POST;

    $nombre = $post['nombre'];
    $password = $post['password'];
    $email = $post['email'];
    //$imagen = file('img/imagen_perfil.jpg');
    //$imgContenido = addslashes(file_get_contents($imagen));

    if(filter_var($email,FILTER_VALIDATE_EMAIL) !== false){
        $select = "select * from dbperfil WHERE nombre = '$nombre' AND email = '$email'";
        $verificar = mysqli_query($conexion, $select);

        if(mysqli_num_rows($verificar) == 0){
            $passmd5 = md5($password);
            $insert = "insert into dbperfil (nombre,password,email) values ('$nombre','$passmd5','$email')";

            $resultado = mysqli_query($conexion, $insert);

            if($resultado){
                $retorno['exito'] = true;
                $retorno['mensaje'] = 'Guardado correctamente';
            }else{
                $retorno['mensaje'] = 'Error en BD';
            }
        }else{
            $retorno['mensaje'] = 'El nombre o el correo de usuario ya esta ocupado';
        }
    }else{
        $retorno['mensaje'] = 'El siguiente correo es invalido';
    }

    header('Content-type: application/json');
    echo json_encode($retorno);
    exit();
}
$retorno['mensaje'] = 'No se encontro ningun registro';

header("HTTP/1.1 400 Bad Request");
header('Content-type: application/json');
echo json_encode($retorno);
?>