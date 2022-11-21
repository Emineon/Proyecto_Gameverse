<?php
include '../config.php';

$retorno = array(
    'exito' => false,
    'mensaje' => "N/A"
);

if($_SERVER['REQUEST_METHOD'] == 'GET'){
    $get = empty($_GET) ? json_decode(file_get_contents('php://input'), true) : $_GET;

    $correo = $get['correo'];

    if(!empty($correo)){
        $select = "select * from dbperfil WHERE email = '$correo'";
        $resultado = mysqli_query($conexion, $select);

        if(mysqli_num_rows($resultado) == 1) {
            $retorno['exito'] = true;
            $retorno['mensaje'] = "Se encontro el correo electronico";
        } else {
            $retorno['mensaje'] = "Error en BD";
        }
    }

    header('Content-type: application/json');
    echo json_encode($retorno);
    exit();
}

if($_SERVER['REQUEST_METHOD'] == 'PUT'){
    $get = empty($_GET) ? json_decode(file_get_contents('php://input'), true) : $_GET;

    $correo = $get['correo'];
    $password = $get['password'];

    if(!empty($correo) && !empty($password)){
        $passmd5 = md5($password);
        $update = "update dbperfil set password = '$passmd5' where email = '$correo'";

        $resultado = mysqli_query($conexion, $update);

        if($resultado){
            $retorno['exito'] = true;
            $retorno['mensaje'] = "Se modifico la contraseña";
        }else{
            $retorno['mensaje'] = "Error en BD";
        }
    }

    header('Content-type: application/json');
    echo json_encode($retorno);
    exit();
}

$retorno['mensaje'] = 'No se encontro ningun dato';

header("HTTP/1.1 400 Bad Request");
header('Content-type: application/json');
echo json_encode($retorno);
?>