<?php
include '../../config.php';

$retorno = array(
    'exito' => false,
    'mensaje' => 'N/A'
);

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $post = empty($_POST) ? json_decode(file_get_contents('php://input'), true) : $_POST;

    $id_grupo = $post['id_grupo'];
    $id_perfil = $post['id_perfil'];
    $comentario = $post['comentario'];

    $insert = "insert into dbcomentarios (id_publicacion, id_grupo, id_perfil, comentario, fecha_creacion)
		values (0, $id_grupo, $id_perfil, '$comentario', NOW())";

    $resultado = mysqli_query($conexion, $insert);

    if($resultado){
        $retorno['exito'] = true;
        $retorno['mensaje'] = "Se creo correctamente el comanentario";
    }else{
	$retorno['mensaje'] = "Error en BD";
    }

    header('Content-type: application/json');
    echo json_encode($retorno);
    exit();
}
$retorno['mensaje'] = 'No se encontro ningun dato para crear el comentario';

header('Content-type: application/json');
echo json_encode($retorno);
?>
