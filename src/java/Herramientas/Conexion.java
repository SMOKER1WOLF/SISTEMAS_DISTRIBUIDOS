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

                cn = DriverManager.getConnection(url, user, pass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cn;
    }

    public void disconnect() {
        if (cn != null) {
            try {
                cn.close();
                System.out.println("Conexión terminada");
                cn = null;
            } catch (Exception ex) {
                System.out.println(ex.toString());
                System.out.println("");
            }
        }
    }
}
