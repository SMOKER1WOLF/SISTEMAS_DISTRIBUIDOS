package Herramientas;

import Modelos.PedidoDAO;

public class Test {

    public static void main(String[] args) {

        Conexion conect = new Conexion();
        conect.establecerConexion();
        conect.disconnect();

//        PedidoDAO pedao = new PedidoDAO();
//        System.out.println(pedao.idPedido());
    }

}
