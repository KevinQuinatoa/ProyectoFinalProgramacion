import java.util.Scanner;
import Modelo.*;
import Interfaz.Sistema;
import Negocio.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Sistema sis = new Sistema();
        Scanner sc = new Scanner(System.in);
        int opc1 = 0, opc2 = 0;

        do {
            opc1 = sis.menu();

            switch (opc1) {
                case 1:
                    sis.menuEquipos();
                    break;
                case 2:
                    sis.menuMantenimiento();
                    break;
                case 3:
                    sis.menuFallas();
                    break;
                case 4:
                    sis.menuReportes();
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }

            System.out.println("Desea seleccionar otra opcion: ");
            System.out.print("1.Si / 2.No: ");
            opc2 = sc.nextInt();

        } while (opc2 == 1);

        System.out.println("Hasta luego.");

    }
}