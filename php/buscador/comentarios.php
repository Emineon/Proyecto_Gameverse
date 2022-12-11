<?php
include '../config.php';

$retorno = array(
    'exito' => false,
    'mensaje' => 'N/A',
    'lista' => array()
);

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $post = empty($_POST) ? json_decode(file_get_contents('php://input'), true) : $_POST;

    $id_publicacion = $post['id_publicacion'];
    //$id_perfil = $post['id_perfil'];

    $select = "select * from dbcomentarios where id_publicacion = $id_publicacion";

    $resultado = mysqli_query($conexion, $select);

    if($resultado){
	$comentarios = array();
        $i = 0;

        while($fila = mysqli_fetch_assoc($resultado)){
            $comentarios[$i]["comentario"] = $fila['comentario'];

            $fecha = strtotime($fila['fecha_creacion']);
            $comentarios[$i]["creacion"] = date('d/m/Y',$fecha);

            $id_perfil = (int) $fila['id_perfil'];

            $usuario = "select * from dbperfil where id = $id_perfil";
            $adicional = mysqli_query($conexion, $usuario);

            if($adicional){
                $comentarios[$i]["usuario"] = mysqli_fetch_assoc($adicional)['nombre'];
            }

	    $extra = mysqli_query($conexion, $usuario);

	    if($extra){
	        $imagen = mysqli_fetch_assoc($extra)['imagen_url'];
                if($imagen != NULL){
                        $comentarios[$i]["icono"] = $imagen;
                }else{
                        $comentarios[$i]["icono"] = "";
                }
            }

            $i++;
        }

        $retorno['exito'] = true;
        $retorno['mensaje'] = "Se encontraron los comentarios en dicha publicación";
        $retorno['lista'] = $comentarios;
    }else{
	$retorno['mensaje'] = "No se encontro ningún comentario en dicha publicación";
    }
}

header('Content-type: application/json');
echo json_encode($retorno);
?>
