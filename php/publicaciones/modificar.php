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
        $titulo = $post['titulo'];
        $descripcion = $post['descripcion'];
        $xbox = $post['xbox'];
        $playstation = $post['playstation'];
        $nintendo = $post['nintendo'];
        $genero = $post['genero'];
        $nombre_imagen = $post['nombre_imagen'];
        $imagen = $post['imagen'];

        $update = "update dbpublicaciones set titulo = '$titulo',
                descripción = '$descripcion',
                op_xbox = $xbox,
                op_playstation = $playstation,
                op_nintendo = $nintendo,
                genero = '$genero',
                nombre_archivo = '$nombre_imagen',
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

if($_SERVER['REQUEST_METHOD'] == 'DELETE'){
    $get = empty($_GET) ? json_decode(file_get_contents('php://input'), true) : $_GET;

    if(!empty($get)){
        $id = $get['id_publicaciones'];

        $delete = "delete FROM dbpublicaciones where id = $id";

        $resultado = mysqli_query($conexion, $delete);

        if($resultado){
            $retorno['exito'] = true;
            $retorno['mensaje'] = "Se elimino la publicación";
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
