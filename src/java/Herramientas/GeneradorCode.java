package Herramientas;

import java.security.SecureRandom;

public class GeneradorCode {

    public static int generarCodigoPedido() {
        SecureRandom random = new SecureRandom();
        return random.nextInt(900000) + 100000; // Siempre 6 dígitos
    }
}


