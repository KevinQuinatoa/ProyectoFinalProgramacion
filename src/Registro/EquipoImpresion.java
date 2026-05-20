package Registro;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EquipoImpresion extends DatosEquipo{

    private static final List<String> subTipo = new ArrayList<>();

    static {
            subTipo.add("Impresora de tinta");
            subTipo.add("Impresora láser");
            subTipo.add("Multifuncional");
            subTipo.add("Escáner");
    }


    private String subTipos;



    public EquipoImpresion(String id, String nombre, String tipo,
                           int cantidad, LocalDate fechaIngreso, String subTipos) {
        super(id, nombre, "Impresion", cantidad, fechaIngreso);
        this.subTipos=subTipos;
    }

    @Override
    public String obtenerFicha() {
        return "[IMPRESIÓN] " + getNombre() +
                " | Subtipo: " + subTipos +
                " | Estado: " + getEstado() +
                " | Cant.: " + getCantidad();
    }

    public static List<String> getSubTipo(){
        return subTipo;
    }
    public String getSubTipos() {
        return subTipos;
    }

    public void setSubTipos(String subTipos) {
        this.subTipos = subTipos;
    }
}
