<?php
include '../config.php';

$retorno = array(
    'exito' => true,
    'mensaje' => 'N/A',
    'lista' => array()
);

if($_SERVER['REQUEST_METHOD'] == 'GET'){
    $get = empty($_GET) ? json_decode(file_get_contents('php://input'), true) : $_GET;

    $select = "select * from dbpublicaciones";

    if(!empty($get['id_perfil'])){
	$select .= " WHERE id_perfil = $get[id_perfil]";
    }

    $select .= " ORDER BY id DESC";

    $resultado = mysqli_query($conexion, $select);

    if($resultado){
        $publicaciones = array();
        $i = 0;

        while($fila = mysqli_fetch_assoc($resultado)){
            $publicaciones[$i]["id"] = (int) $fila['id'];
            $publicaciones[$i]["titulo"] = $fila['titulo'];
            $publicaciones[$i]["descripcion"] = $fila['descripción'];

            $fecha = strtotime($fila['fecha_creacion']);
            $publicaciones[$i]["creacion"] = date("d/m/Y",$fecha);

            if($fila['archivo_url'] == NULL){
                $publicaciones[$i]["url"] = "";
            }else{
                $publicaciones[$i]["url"] = $fila['archivo_url'];
            }

            $publicaciones[$i]["perfil"] = (int) $fila['id_perfil'];
            $id_perfil = $publicaciones[$i]["perfil"];

            $usuario = "select * from dbperfil where id = $id_perfil";
            $result = mysqli_query($conexion, $usuario);

            if($result){
                $publicaciones[$i]["usuario"] = mysqli_fetch_assoc($result)['nombre'];
            }

            $i++;
        }

	$retorno['exito'] = true;
        $retorno['mensaje'] = "Se encontraron las publicaciones";
        $retorno['lista'] = $publicaciones;
    }else{
	$retorno['mensaje'] = "No se encontro ninguna publicación";
    }
}else{
    $retorno['mensaje'] = 'No se encontro ninguna identificación';
}

header('Content-type: application/json');
echo json_encode($retorno);
?>
