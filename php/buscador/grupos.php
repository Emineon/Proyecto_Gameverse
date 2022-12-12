<?php
include '../config.php';

$retorno = array(
    'exito' => false,
    'mensaje' => 'N/A',
    'lista' => array()
);

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $post = empty($_POST) ? json_decode(file_get_contents('php://input'), true) : $_POST;

    $select = "select * from dbgrupos";

    if(!empty($post['buscar'])){
        $nombre = $post['buscar'];
        $select .= " where nombre_grupo LIKE '%$nombre%'";
    }

    $resultado = mysqli_query($conexion, $select);

    if($resultado){
        $grupos = array();
        $i = 0;

        while($fila = mysqli_fetch_assoc($resultado)){
            $grupos[$i]["id"] = (int) $fila['id'];
            $grupos[$i]["nombre"] = $fila['nombre_grupo'];
            $grupos[$i]["descripcion"] = $fila['descripción'];
            $grupos[$i]["url"] = $fila['icono_url'];

            $i++;
        }

        $retorno['exito'] = true;
        $retorno['mensaje'] = "Se encontraron los grupos seleccionados";
        $retorno['lista'] = $grupos;
    }else{
        $retorno['mensaje'] = "No se encontro ningún grupo";
    }
}

header('Content-type: application/json');
echo json_encode($retorno);
?>
