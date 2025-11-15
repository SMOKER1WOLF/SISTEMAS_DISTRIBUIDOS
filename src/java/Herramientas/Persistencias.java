/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Herramientas;

import Entidades.Usuario;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author USUARIO
 */
public class Persistencias {

    private static Usuario user;
    private static String fecha;
    private static String hora;

    public static String getFecha() {
        Calendar date = new GregorianCalendar();
        // Sumar 1 al mes porque Calendar.MONTH es base 0
        Persistencias.fecha = String.format("%d-%02d-%02d",
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH) + 1, // ¡Aquí está la corrección!
                date.get(Calendar.DAY_OF_MONTH));
        return fecha;
    }

    public static String getHora() {
        Calendar date = new GregorianCalendar();
        Persistencias.hora = String.format("%02d:%02d:%02d",
                date.get(Calendar.HOUR_OF_DAY), // Hora en 24h
                date.get(Calendar.MINUTE),
                date.get(Calendar.SECOND));
        return hora;
    }

    public static Usuario getUser() {
        return user;
    }

    public static void setUser(Usuario user) {
        Persistencias.user = user;
    }

}
