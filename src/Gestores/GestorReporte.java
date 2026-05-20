package Gestores;

import Registro.Falla;
import Registro.Mantenimiento;
import java.util.List;

public class GestorReporte {

    /** RF7 — Consultar todos los reportes de fallas */
    public void consultarFallas(GestorFallas gestorFallas) {
        List<Falla> lista = gestorFallas.getLista();
        if (lista.isEmpty()) {
            System.out.println("No hay fallas registradas.");
            return;
        }
        System.out.println("\n--- Reporte de fallas ---");
        for (Falla falla : lista)
            System.out.println(falla);
    }

    /** Extra — Consultar historial completo de mantenimientos */
    public void consultarMantenimientos(GestorMantenimiento gestorMant) {
        List<Mantenimiento> lista = gestorMant.getLista();
        if (lista.isEmpty()) {
            System.out.println("No hay mantenimientos registrados.");
            return;
        }
        System.out.println("\n--- Historial de mantenimientos ---");
        for (Mantenimiento mantenimiento : lista)
            System.out.println(mantenimiento);
    }
}
