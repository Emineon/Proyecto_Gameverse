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
        $grupo = $post['nombre_grupo'];
        $descripcion = $post['descripcion'];
        $xbox = $post['xbox'];
        $playstation = $post['playstation'];
        $nintendo = $post['nintendo'];
        $genero = $post['genero'];
        $icono = $post['icono'];

        $insert = "insert into dbgrupos (id_perfil, nombre_grupo, descripcion, xbox, playstation, nintendo, genero, fecha_creacion, icono_url) 
                values ($id_perfil, '$grupo', '$descripcion', $xbox, $playstation, $nintendo, '$genero', NOW(), '$icono')";

        $resultado = mysqli_query($conexion, $insert);

        if($resultado){
            $retorno['exito'] = true;
            $retorno['mensaje'] = "Se creo una nuevo grupo";
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
