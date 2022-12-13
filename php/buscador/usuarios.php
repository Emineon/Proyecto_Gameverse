<?php
include '../config.php';

$retorno = array(
    'exito' => false,
    'mensaje' => 'N/A',
    'lista' => array()
);

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $post = empty($_POST) ? json_decode(file_get_contents('php://input'), true) : $_POST;

    $id = $post['id'];

    $select = "select * from dbperfil where id != $id";

    if(!empty($post['buscar'])){
        $nombre = $post['buscar'];
        $select .= " AND nombre LIKE '%$nombre%'";
    }

    $resultado = mysqli_query($conexion, $select);

    if($resultado){
        $perfiles = array();
        $i = 0;

        while($fila = mysqli_fetch_assoc($resultado)){
            $perfiles[$i]["id"] = (int) $fila['id'];
            $perfiles[$i]["nombre"] = $fila['nombre'];
            $perfiles[$i]["email"] = $fila['email'];

            if($fila['descripcion'] == NULL){
                $perfiles[$i]["descripcion"] = "";
            }else{
            	$perfiles[$i]["descripcion"] = $fila['descripcion'];
            }

            if($fila['videojuego'] == NULL){
                $perfiles[$i]["videojuego"] = "Desconocido";
            }else{
            	$perfiles[$i]["videojuego"] = $fila['videojuego'];
            }

	    if($fila['imagen_url'] == NULL){
	    	$perfiles[$i]["url"] = "";
	    }else{
		$perfiles[$i]["url"] = $fila['imagen_url'];
            }

            $i++;
        }

        $retorno['exito'] = true;
        $retorno['mensaje'] = "Se encontraron los perfiles seleccionados";
        $retorno['lista'] = $perfiles;
    }else{
        $retorno['mensaje'] = "No se encontro ningún grupo";
    }
}

header('Content-type: application/json');
echo json_encode($retorno);
?>
