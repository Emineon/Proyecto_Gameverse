<?php
include '../config.php';

$retorno = array(
    'exito' => false,
    'mensaje' => 'N/A',
    'lista' => array()
);

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $post = empty($_POST) ? json_decode(file_get_contents('php://input'), true) : $_POST;

    $id_perfil = $post['id'];
    $select = "select * from dbgrupos WHERE id_perfil != $id_perfil AND ";

    if(!empty($post['buscar'])){
        $nombre = $post['buscar'];
        $select .= "nombre_grupo LIKE '%$nombre%' AND ";
    }

    $xbox = $post['xbox'];
    $select .= "xbox = $xbox AND ";

    $playstation = $post['playstation'];
    $select .= "playstation = $playstation AND ";

    $nintendo = $post['nintendo'];
    $select .= "nintendo = $nintendo";

    if(!empty($post['genero'])) {
        $genero = $post['genero'];
        $select .= " AND genero = '$genero'";
    }

    $resultado = mysqli_query($conexion, $select);

    if($resultado){
        $grupos = array();
        $i = 0;

        while($fila = mysqli_fetch_assoc($resultado)){
            $grupos[$i]["id"] = (int) $fila['id'];
            $grupos[$i]["nombre"] = $fila['nombre_grupo'];
            $grupos[$i]["descripcion"] = $fila['descripción'];
            $grupos[$i]["icono"] = $fila['icono_url'];

            $grupos[$i]["perfil"] = (int) $fila['id_perfil'];
            $id_perfil = $grupos[$i]["perfil"];

            $usuario = "select * from dbperfil where id = $id_perfil";
            $result = mysqli_query($conexion, $usuario);

            if($result){
                 $grupos[$i]["usuario"] = mysqli_fetch_assoc($result)['nombre'];
            }

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
