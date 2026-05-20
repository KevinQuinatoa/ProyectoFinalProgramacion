package Gestores;

import Registro.Falla;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestorFallas {

    private List<Falla> lista = new ArrayList<>();

    /** RF5 — Reportar una falla */
    public void reportar(String idEquipo, String descripcion,
                         String usuario, LocalDate fecha) {
        Falla falla = new Falla(idEquipo, descripcion, usuario, fecha);
        lista.add(falla);
    }

    /** RF6 — Actualizar estado de una falla por ID de equipo */
    public void actualizarEstado(String idEquipo, String nuevoEstado) {
        for (Falla falla : lista)
            if (falla.getIdEquipo().equals(idEquipo))
                falla.setEstadoFalla(nuevoEstado);
    }

    /** Devuelve toda la lista (útil para GestorReportes) */
    public List<Falla> getLista() {
        return lista;
    }
}
