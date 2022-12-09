<?php
include '../config.php';

$retorno = array(
    'exito' => false,
    'mensaje' => 'N/A'
);

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $post = empty($_POST) ? json_decode(file_get_contents('php://input'), true) : $_POST;

    $id_perfil = $post['id'];
    $accion = $post['accion'];

    switch($accion) {
	case "Datos":
        	$nombre = $post['nombre'];
        	$descripcion = $post['descripcion'];
        	$fecha = $post['nacimiento'];
        	$videojuego = $post['videojuego'];

        	$fecha_format = strtotime($fecha);
        	$date = date('Y-m-d',$fecha_format);

        	$update = "update dbperfil set
                	nombre = '$nombre', fecha_nacimiento = '$date', descripcion = '$descripcion', videojuego = '$videojuego' WHERE id = $id_perfil";
        	$resultado = mysqli_query($conexion, $update);

        	if($resultado) {
            		$retorno['exito'] = true;
            		$retorno['mensaje'] = "Se actualizo el usuario correctamente";
        	}else{
            		$retorno['mensaje'] = "Sucedio un error en la actualización";
        	}
		break;
    	case "Foto":
		$imagen = $post['imagen'];

                $update = "update dbperfil set
                        imagen_url = '$imagen' WHERE id = $id_perfil";
                $resultado = mysqli_query($conexion, $update);

                if($resultado) {
                        $retorno['exito'] = true;
                        $retorno['mensaje'] = "Se actualizo el usuario correctamente";
                }else{
                        $retorno['mensaje'] = "Sucedio un error en la actualización";
                }
                break;

    	}
}else{
    $retorno['mensaje'] = "No se encontro los datos";
}

header('Content-type: application/json');
echo json_encode($retorno);
?>
