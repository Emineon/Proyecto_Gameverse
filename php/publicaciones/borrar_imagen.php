<?php
include '../config.php';

$retorno = array(
    'exito' => false,
    'mensaje' => 'N/A'
);

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $post = empty($_POST) ? json_decode(file_get_contents('php://input'), true) : $_POST;

    if(!empty($post)) {
        $id = $post['id_publicaciones'];
        $nombre_imagen = $post['nombre_imagen'];
        $imagen = $post['imagen'];

        $update = "update dbpublicaciones set nombre_imagen = '$nombre_imagen',
                archivo_url = '$imagen',
                fecha_actualizacion = NOW()
                where id = $id";

        $resultado = mysqli_query($conexion, $update);

        if($resultado){
            $retorno['exito'] = true;
            $retorno['mensaje'] = "Se modifico la publicación";
        }else{
            $retorno['mensaje'] = "Error en el BD";
        }

        header('Content-type: application/json');
        echo json_encode($retorno);
        exit();
    }
}

$retorno['mensaje'] = 'No se encontro ningun dato';

header("HTTP/1.1 400 Bad Request");
header('Content-type: application/json');
echo json_encode($retorno);
?>