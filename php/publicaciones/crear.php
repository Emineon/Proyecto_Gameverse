<?php
include '../config.php';

$retorno = array(
    'exito' => false,
    'mensaje' => 'N/A'
);

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $post = empty($_POST) ? json_decode(file_get_contents('php://input'), true) : $_POST;

    if(!empty($post)){
        $id_perfil = $post['id'];
        $titulo = $post['titulo'];
        $descripcion = $post['descripcion'];
        $xbox = $post['xbox'];
        $playstation = $post['playstation'];
        $nintendo = $post['nintendo'];
        $genero = $post['genero'];
        $nombre_imagen = $post['nombre_imagen'];
        $imagen = $post['imagen'];

        $insert = "insert into dbpublicaciones (id_perfil, titulo, descripción, op_xbox, op_playstation, op_nintendo, genero, nombre_imagen, archivo_url, fecha_creacion, fecha_actualizacion) 
                values ($id_perfil, '$titulo', '$descripcion', $xbox, $playstation, $nintendo, '$genero', '$nombre_imagen', '$imagen', NOW(), NOW())";

        $resultado = mysqli_query($conexion, $insert);

        if($resultado){
            $retorno['exito'] = true;
            $retorno['mensaje'] = "Se creo una nueva publicación";
        }else{
            $retorno['mensaje'] = "Error en el BD";
        }

        header('Content-type: application/json');
        echo json_encode($retorno);
        exit();
    }
}
$retorno['mensaje'] = 'No se encontro ningun dato para crear el mensaje';

header("HTTP/1.1 400 Bad Request");
header('Content-type: application/json');
echo json_encode($retorno);
?>