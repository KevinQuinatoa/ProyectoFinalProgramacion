// Main.java
import Interfaz.Sistema;
import Modelo.Usuario;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args) {
        Sistema sis = new Sistema();
        Scanner sc = new Scanner(System.in);

        int opc1 = 0;
        int opc2 = 2;

        System.out.println("=======================================================");
        System.out.println("  BIENVENIDO AL SISTEMA DE GESTIÓN DE MANTENIMIENTO    ");
        System.out.println("=======================================================");

        // FLUJO OBLIGATORIO: Identificar o registrar al usuario antes de entrar al menú
        Usuario usuarioActivo = sis.identificarORegistrarUsuarioInicial();

        System.out.println("\n[SESIÓN INICIADA]: Bienvenido(a), " + usuarioActivo.getNombre() + " (" + usuarioActivo.getCargo() + ")");

        do {
            // Llama al menú principal
            opc1 = sis.menu();

            // Evaluamos la opción con la estructura tradicional de case y break
            switch (opc1) {
                case 1:
                    sis.menuUsuarios();      // Permite ver la lista de usuarios o registrar otros
                    break;
                case 2:
                    sis.menuEquipos();       // Gestión de Equipos
                    break;
                case 3:
                    sis.menuMantenimiento(); // Gestión de Mantenimientos
                    break;
                case 4:
                    sis.menuFallas();        // Gestión de Fallas
                    break;
                case 5:
                    sis.menuReportes();      // Control y Seguimiento
                    break;
                default:
                    System.out.println("[AVISO] Opción fuera de rango. Seleccione del 1 al 5.");
                    break;
            }

            // Validación para continuar en el sistema
            while (true) {
                try {
                    System.out.println("\n¿Desea seleccionar otra opción del sistema?");
                    System.out.print("1. Sí / 2. No: ");
                    opc2 = sc.nextInt();
                    sc.nextLine(); // Limpiar búfer

                    if (opc2 == 1 || opc2 == 2) {
                        break;
                    }
                    System.out.println("[AVISO] Por favor, ingrese únicamente 1 para Sí o 2 para No.");
                } catch (InputMismatchException e) {
                    System.out.println("\n[ERROR] Entrada inválida. Debe introducir un número (1 o 2).");
                    sc.nextLine(); // Limpiar el búfer
                }
            }

        } while (opc2 == 1);

        System.out.println("\n=======================================================");
        System.out.println("  ¡Sesión finalizada con éxito! Cierre de sistema.    ");
        System.out.println("=======================================================");
        sc.close();
    }
}