package Herramientas;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {

    private static Connection cn;

    public static Connection establecerConexion() {
        if (cn == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                String url = System.getenv("DB_URL");
                String user = System.getenv("DB_USER");
                String pass = System.getenv("DB_PASS");

//                String url = "jdbc:mysql://centerbeam.proxy.rlwy.net:37177/railway?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
//                String user = "root";
//                String pass = "NfiRUMprCyZhzdxvLDtllIpgIapWVwaU";

                cn = DriverManager.getConnection(url, user, pass);
                System.out.println(">>> Conexión a Railway exitosa!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cn;
    }

    public static void disconnect() {
        if (cn != null) {
            try {
                cn.close();
                System.out.println("Conexión terminada");
                cn = null;
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }
    }
}
