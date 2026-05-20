package Gestores;

import Registro.Mantenimiento;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestorMantenimiento {

    private List<Mantenimiento> lista = new ArrayList<>();

    /** RF3 — Registrar mantenimiento */
    public void registrar(String idEquipo, String tipo,
                          String descripcion, String tecnico,
                          LocalDate fecha) {
        Mantenimiento mantenimiento = new Mantenimiento(
                idEquipo, tipo, descripcion, tecnico, fecha
        );
        lista.add(mantenimiento);
    }

    /** RF4 — Consultar historial de un equipo por su ID */
    public List<Mantenimiento> consultarPorEquipo(String idEquipo) {
        List<Mantenimiento> resultado = new ArrayList<>();
        for (Mantenimiento mantenimiento : lista)
            if (mantenimiento.getIdEquipo().equals(idEquipo))
                resultado.add(mantenimiento);
        return resultado;
    }

    /** Devuelve toda la lista (útil para GestorReportes) */
    public List<Mantenimiento> getLista() {
        return lista;
    }
}
