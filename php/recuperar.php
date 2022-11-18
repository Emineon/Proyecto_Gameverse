<?php
include 'config.php';

$retorno = array(
    'exito' => false,
    'mensaje' => "N/A"
);

if(!empty($_GET)){
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

    if(!empty($correo) && !empty($post['password'])){
        $password = $get['password'];
    }

    header('Content-type: application/json');
    echo json_encode($retorno);
    exit();
}
?>