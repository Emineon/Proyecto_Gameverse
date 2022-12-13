<?php
include '../config.php';

$retorno = array(
    'exito' => true,
    'mensaje' => 'N/A',
    'lista' => array()
);

if($_SERVER['REQUEST_METHOD'] == 'GET'){
    $get = empty($_GET) ? json_decode(file_get_contents('php://input'), true) : $_GET;

    $select = "select * from dbgrupos";

    if(!empty($get['id'])){
         $select .= " where id_perfil = $get[id]";
    }

    $resultado = mysqli_query($conexion, $select);

    if($resultado){
        $grupos = array();

        $i = 0;
        while($fila = mysqli_fetch_assoc($resultado)){
                $grupos[$i]["id_grupo"] = (int) $fila['id'];
                $grupos[$i]["nombre_grupo"] = $fila['nombre_grupo'];
                $grupos[$i]["descripcion"] = $fila['descripcion'];
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
        $retorno['mensaje'] = 'Se desplego el listado de los grupos';
        $retorno['lista'] = $grupos;
    }else{
        $retorno['mensaje'] = "No se tiene ningún grupo";
    }
}else{
    $retorno['mensaje'] = 'No se encontro ninguna identificación';
}

header('Content-type: application/json');
echo json_encode($retorno);
?>