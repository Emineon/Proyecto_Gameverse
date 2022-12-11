<?php
include "../config.php";

$retorno = array(
    'exito' => false,
    'mensaje' => 'N/A',
    'lista' => array()
);

if($_SERVER['REQUEST_METHOD'] == 'GET'){
    $get = empty($_GET) ? json_decode(file_get_contents('php://input'), true) : $_GET;

    $nombre = $get['nombre'];

    $select = "select * from dbperfil where nombre = '$nombre'";
    $resultado = mysqli_query($conexion, $select);

    if($resultado){
        $perfil = array();

        $indice = 0;
        while($fila = mysqli_fetch_assoc($resultado)){
            $perfil[$indice]["id"] = (int) $fila['id'];
	    $perfil[$indice]["nombre"] = $fila['nombre'];

            $indice++;
        }

        $retorno['exito'] = true;
        $retorno['mensaje'] = 'Se encontro el numero de identificación';
        $retorno['lista'] = $perfil;
    }else{
        $retorno['mensaje'] = "Error en BD";
    }
}elseif($_SERVER['REQUEST_METHOD'] == 'POST'){
    $post = empty($_POST) ? json_decode(file_get_contents('php://input'), true) : $_POST;

    $id = $post['id'];

    $select = "select * from dbperfil where id = $id";
    $resultado = mysqli_query($conexion, $select);

    if($resultado){
        $perfil = array();

        $indice = 0;
        while($fila = mysqli_fetch_assoc($resultado)){
	    $perfil[$indice]["email"] = $fila['email'];

            $fecha = $fila['fecha_nacimiento'];

            if($fila['descripcion'] == NULL){
		$perfil[$indice]["descripcion"] = "";
	    }else{
		$perfil[$indice]["descripcion"] = $fila['descripcion'];
	    }

	    if($fila['videojuego'] == NULL){
            	$perfil[$indice]["videojuego"] = "";
	    }else{
	    	$perfil[$indice]["videojuego"] = $fila['videojuego'];
            }

	    if($fecha == NULL || $fecha == '0000-00-00'){
	    	$perfil[$indice]["fecha"] = "";
	    }else{
	        $fecha_format = strtotime($fecha);
                $perfil[$indice]["fecha"] = date('d/m/Y',$fecha_format);
            }

	    if($fila['imagen_url'] == NULL){
		$perfil[$indice]["imagen"] = "";
            }else{
		$perfil[$indice]["imagen"] = $fila['imagen_url'];
            }
            $indice++;
        }

        $retorno['exito'] = true;
        $retorno['mensaje'] = 'Se encontro el numero de identificación';
        $retorno['lista'] = $perfil;
     }
}else{
    $retorno['mensaje'] = 'No se encontro ningun dato';
}

header('Content-type: application/json');
echo json_encode($retorno);
?>
