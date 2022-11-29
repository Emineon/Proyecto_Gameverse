<?php
include '../config.php';

$retorno = array(
    'exito' => false,
    'mensaje' => "N/A",
    'usuario' => array()
);

if($_SERVER['REQUEST_METHOD'] == 'GET') {
    $get = empty($_GET) ? json_decode(file_get_contents('php://input'), true) : $_GET;

    $nombre = $get['nombre'];
    $password = $get['password'];

    $passmd5 = md5($password);

    $select = "select * from dbperfil WHERE password = '$passmd5'";

    if(filter_var($nombre, FILTER_VALIDATE_EMAIL) !== false){
        $select .= " AND email = '$nombre'";
    }else{
        $select .= " AND nombre = '$nombre'";
    }

    $resultado = mysqli_query($conexion, $select);

    if (mysqli_num_rows($resultado) == 1) {
        $usuario = array();

        $i = 0;
        while($fila = mysqli_fetch_assoc($resultado)){
            $usuario[$i]["nombre"] = $fila['nombre'];

            $i++;
        }

        $retorno['exito'] = true;
        $retorno['mensaje'] = "Existe dicha cuenta";
        $retorno['usuario'] = $usuario;
    } else {
        $retorno['mensaje'] = "El nombre/correo o la contrseña es incorrecto";
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