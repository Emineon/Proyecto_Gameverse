<?php
include '../config.php';

$retorno = array(
    'exito' => true,
    'mensaje' => 'N/A',
    'lista' => array()
);

if($_SERVER['REQUEST_METHOD'] == 'GET'){
    $get = empty($_GET) ? json_decode(file_get_contents('php://input'), true) : $_GET;

    $id = $get['id_perfil'];

    $select = "select * from dbpublicaciones where id_perfil = $id";
    $resultado = mysqli_query($conexion, $select);

    if($resultado){
        $publicaciones = array();

        $indice = 0;
        while($fila = mysqli_fetch_assoc($resultado)){
            $publicaciones[$indice]["id"] = (int) $fila['id'];
            $publicaciones[$indice]["titulo"] = $fila['titulo'];
            $publicaciones[$indice]["descripcion"] = $fila['descripción'];
            $publicaciones[$indice]["xbox"] = $fila['op_xbox'] == 1;
            $publicaciones[$indice]["playstation"] = $fila['op_playstation'] == 1;
            $publicaciones[$indice]["nintendo"] = $fila['op_nintendo'] == 1;
            $publicaciones[$indice]["genero"] = $fila['genero'];
            $publicaciones[$indice]["imagen"] = $fila['archivo_url'];

            $indice++;
        }

        $retorno['exito'] = true;
        $retorno['mensaje'] = 'Se desplego el listado de las publicaciones de usuario especifico';
        $retorno['lista'] = $publicaciones;
    }else{
        $retorno['mensaje'] = "No tienes ninguna publicacion";
    }
}else{
    $retorno['mensaje'] = 'No se encontro ninguna identificación';
}

header('Content-type: application/json');
echo json_encode($retorno);
?>