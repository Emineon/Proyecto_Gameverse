<?php
include "config.php";

$retorno = array(
    'exito' => false,
    'mensaje' => 'N/A',
    'lista' => array()
);

if($_SERVER['REQUEST_METHOD'] == 'GET'){
    $get = empty($_GET) ? json_decode(file_get_contents('php://input'), true) : $_GET;

    $nombre = $get['nombre'];

    $select = "select * from dbperfil where nombre = '$nombre'";
    $resultado = mysqli_query($conexion, $select);

    if($resultado){
        $perfil = array();

        $indice = 0;
        while($fila = mysqli_fetch_assoc($resultado)){
            $perfil[$indice]["id"] = (int) $fila['id'];
            $perfil[$indice]["fecha"] = $fila['fecha_nacimiento'];
            $perfil[$indice]["descripcion"] = $fila['descripcion'];
            $perfil[$indice]["videojuego"] = $fila['videojuego'];

            $indice++;
        }

        $retorno['exito'] = true;
        $retorno['mensaje'] = 'Se encontro el numero de identificación';
        $retorno['lista'] = $perfil;
    }else{
        $retorno['mensaje'] = "Error en BD";
    }
}else{
    $retorno['mensaje'] = 'No se encontro ningun dato';
}

header('Content-type: application/json');
echo json_encode($retorno);
?>