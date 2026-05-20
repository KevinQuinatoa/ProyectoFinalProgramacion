package Gestores;

import Registro.DatosEquipo;
import Registro.EquipoComputo;
import Registro.EquipoImpresion;
import Registro.EquipoProyectore;

import java.util.ArrayList;
import java.util.List;

public class GestorEquipo {

    private List<EquipoComputo> equipoComputo = new ArrayList<>();
    private List<EquipoImpresion> equipoImpresion = new ArrayList<>();
    private List<EquipoProyectore> equipoProyectore = new ArrayList<>();

    public void agregarEquipo(DatosEquipo equipo) {
        if (equipo instanceof EquipoComputo)
            equipoComputo.add((EquipoComputo) equipo);
        else if (equipo instanceof EquipoImpresion)
            equipoImpresion.add((EquipoImpresion) equipo);
        else if (equipo instanceof EquipoProyectore)
            equipoProyectore.add((EquipoProyectore) equipo);
    }

    /** Devuelve todos los equipos en una sola lista (polimorfismo) */
    public List<DatosEquipo> listarTodos() {
        List<DatosEquipo> todos = new ArrayList<>();
        todos.addAll(equipoComputo);
        todos.addAll(equipoImpresion);
        todos.addAll(equipoProyectore);
        return todos;
    }

    public DatosEquipo buscarPorId(String id) {
        for (DatosEquipo e : listarTodos())
            if (e.getId().equals(id)) return e;
        return null;
    }

    public void actualizarEstado(String id, String nuevoEstado) {
        DatosEquipo datosEquipo = buscarPorId(id);
        if (datosEquipo != null) datosEquipo.setEstado(nuevoEstado);
    }

    public List<EquipoComputo> getEquipoComputo() {
        return equipoComputo;
    }

    public List<EquipoImpresion> getEquipoImpresion() {
        return equipoImpresion;
    }

    public List<EquipoProyectore> getEquipoProyectore() {
        return equipoProyectore;
    }
}
