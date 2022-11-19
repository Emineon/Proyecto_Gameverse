<?php
include 'config.php';

$retorno = array(
    'exito' => false,
    'mensaje' => "N/A"
);

if($_SERVER['REQUEST_METHOD'] == 'GET') {
    $get = empty($_GET) ? json_decode(file_get_contents('php://input'), true) : $_GET;

    $nombre = $get['nombre'];
    $password = $get['password'];

    $passmd5 = md5($password);

    $sel = "select * from dbperfil WHERE nombre = '$nombre' AND password = '$passmd5'";
    $resultado = mysqli_query($conexion, $sel);

    if (mysqli_num_rows($resultado) == 1) {
        $retorno['exito'] = true;
        $retorno['mensaje'] = "Existe dicha cuenta";
    } else {
        $retorno['mensaje'] = "Error en BD";
    }

    header('Content-type: application/json');
    echo json_encode($retorno);
    exit();
}
$retorno['mensaje'] = 'No se encontro ningun dato';

header('Content-type: application/json');
echo json_encode($retorno);
?>