package Interfaz;

// interfaz/SistemaMantenimiento.java
import Modelo.*;
import Negocio.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Sistema {

    static GestorEquipo gestorEquipo       = new GestorEquipo();
    static GestorMantenimiento gestorMant    = new GestorMantenimiento();
    static GestorFallas gestorFallas         = new GestorFallas();
    static GestorReporte gestorReporte     = new GestorReporte();
    static Scanner sc = new Scanner(System.in);

    public int menu() {
        int opcion;
            System.out.println("\n====================================");
            System.out.println("  Sistema de Gestión de Mantenimiento");
            System.out.println("====================================");
            System.out.println("1. Gestión de equipos");
            System.out.println("2. Gestión de mantenimiento");
            System.out.println("3. Gestión de fallas");
            System.out.println("4. Control y seguimiento");
            System.out.print("Opción: ");
            opcion = sc.nextInt();
        return opcion;
    }

    // ─── MÓDULO 1: Gestión de equipos ───────────────────────────────

    public void menuEquipos() {
        System.out.println("\n--- Gestión de Equipos ---");
        System.out.println("1. Registrar equipo");
        System.out.println("2. Consultar equipos");
        System.out.print("Opción: ");
        int op = sc.nextInt(); sc.nextLine();

        switch (op) {
            case 1 -> registrarEquipo();
            case 2 -> consultarEquipos();
            default -> System.out.println("Opción no válida.");
        }
    }

    public void registrarEquipo() {
        System.out.println("\nTipo de equipo:");
        System.out.println("1. Cómputo   2. Impresión   3. Proyección");
        System.out.print("Opción: ");
        int tipo = sc.nextInt(); sc.nextLine();

        // Mostrar subtipos según el tipo elegido
        List<String> subtipos = switch (tipo) {
            case 1 -> EquipoComputo.getSubTipos();
            case 2 -> EquipoImpresion.getSubTipos();
            case 3 -> EquipoProyectore.getSubtipos();
            default -> null;
        };

        if (subtipos == null) { System.out.println("Tipo no válido."); return; }

        System.out.println("Subtipo:");
        for (int i = 0; i < subtipos.size(); i++)
            System.out.println((i + 1) + ". " + subtipos.get(i));
        System.out.print("Opción: ");
        int subOp = sc.nextInt() - 1; sc.nextLine();
        String subtipo = subtipos.get(subOp);

        System.out.print("ID del equipo: ");
        String id = sc.nextLine();
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Cantidad: ");
        int cantidad = sc.nextInt(); sc.nextLine();

        DatosEquipo equipo = switch (tipo) {
            case 1 -> new EquipoComputo(id, nombre, subtipo, cantidad, LocalDate.now());
            case 2 -> new EquipoImpresion(id, nombre, subtipo, cantidad, LocalDate.now());
            case 3 -> new EquipoProyectore(id, nombre, subtipo, cantidad, LocalDate.now());
            default -> null;
        };

        gestorEquipo.agregarEquipo(equipo);
        System.out.println("\nEquipo agregado");        // RF1: mensaje éxito
    }

    public void consultarEquipos() {                      // RF2
        List<DatosEquipo> lista = gestorEquipo.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("No hay equipos registrados.");
            return;
        }
        System.out.println("\n--- Lista de equipos ---");
        for (DatosEquipo e : lista)
            System.out.println(e.obtenerFicha());
    }

    // ─── MÓDULO 2: Gestión de mantenimiento ─────────────────────────

    public void menuMantenimiento() {
        System.out.println("\n--- Gestión de Mantenimiento ---");
        System.out.println("1. Registrar mantenimiento ");
        System.out.println("2. Consultar historial");
        System.out.print("Opción: ");
        int op = sc.nextInt(); sc.nextLine();

        switch (op) {
            case 1 -> registrarMantenimiento();
            case 2 -> consultarHistorial();
            default -> System.out.println("Opción no válida.");
        }
    }

    public void registrarMantenimiento() {

        // 1. Obtener equipos registrados
        List<DatosEquipo> equipos = gestorEquipo.listarTodos();

        if (equipos.isEmpty()) {
            System.out.println("No hay equipos registrados para dar mantenimiento.");
            return;
        }

        // 2. Mostrar menú de equipos disponibles
        System.out.println("\n--- Equipos registrados ---");
        for (int i = 0; i < equipos.size(); i++) {
            System.out.println((i + 1) + ". " + equipos.get(i).getNombre()
                    + " | ID: "     + equipos.get(i).getId()
                    + " | Estado: " + equipos.get(i).getEstado());
        }

        // 3. El usuario elige por número
        System.out.print("Seleccione el equipo (número): ");
        int seleccion = sc.nextInt(); sc.nextLine();

        if (seleccion < 1 || seleccion > equipos.size()) {
            System.out.println("Selección no válida.");
            return;
        }

        DatosEquipo equipoSeleccionado = equipos.get(seleccion - 1);
        String idEquipo = equipoSeleccionado.getId();

        // 4. Elegir tipo de mantenimiento
        System.out.println("\nTipo de mantenimiento:");
        System.out.println("1. Preventivo");
        System.out.println("2. Correctivo");
        System.out.println("3. Diagnóstico");
        System.out.print("Opción: ");
        int tipoOp = sc.nextInt(); sc.nextLine();

        String tipo = switch (tipoOp) {
            case 1 -> "Preventivo";
            case 2 -> "Correctivo";
            case 3 -> "Diagnóstico";
            default -> "Desconocido";
        };

        // 5. Pedir descripción y técnico
        System.out.print("Descripción: ");
        String desc = sc.nextLine();
        System.out.print("Técnico responsable: ");
        String tecnico = sc.nextLine();

        // 6. Registrar y actualizar estado del equipo
        gestorMant.registrar(idEquipo, tipo, desc, tecnico, LocalDate.now());
        gestorEquipo.actualizarEstado(idEquipo, "en mantenimiento");

        System.out.println("\n✔ Mantenimiento registrado correctamente"); // RF3
        System.out.println("  Equipo '" + equipoSeleccionado.getNombre()
                + "' marcado como: en mantenimiento");
    }

    public void consultarHistorial() { // RF4
        List<DatosEquipo> equipos = gestorEquipo.listarTodos();

        if (equipos.isEmpty()) {
            System.out.println("No hay equipos registrados.");
            return;
        }

        System.out.println("\n--- Equipos registrados ---");
        for (int i = 0; i < equipos.size(); i++) {
            System.out.println((i + 1) + ". " + equipos.get(i).getNombre()
                    + " | ID: "     + equipos.get(i).getId()
                    + " | Estado: " + equipos.get(i).getEstado());
        }

        System.out.print("Seleccione el equipo (número): ");
        int seleccion = sc.nextInt(); sc.nextLine();

        if (seleccion < 1 || seleccion > equipos.size()) {
            System.out.println("Selección no válida.");
            return;
        }

        DatosEquipo equipoSeleccionado = equipos.get(seleccion - 1);
        String idEquipo = equipoSeleccionado.getId();

        // ── ESTO ES LO QUE FALTABA ──────────────────────────
        List<Mantenimiento> historial = gestorMant.consultarPorEquipo(idEquipo);

        if (historial.isEmpty()) {
            System.out.println("El equipo '" + equipoSeleccionado.getNombre()
                    + "' no tiene mantenimientos registrados.");
            return;
        }

        System.out.println("\n--- Historial de '" + equipoSeleccionado.getNombre() + "' ---");
        for (Mantenimiento m : historial) {
            System.out.println(m); // usa el toString() de Mantenimiento
        }
    }

    // ─── MÓDULO 3: Gestión de fallas ────────────────────────────────

    public void menuFallas() {
        System.out.println("\n--- Gestión de Fallas ---");
        System.out.println("1. Reportar falla");
        System.out.println("2. Actualizar estado del equipo");
        System.out.print("Opción: ");
        int op = sc.nextInt(); sc.nextLine();

        switch (op) {
            case 1 -> reportarFalla();
            case 2 -> actualizarEstado();
            default -> System.out.println("Opción no válida.");
        }
    }

    public void reportarFalla() {

        // 1. Obtener equipos registrados
        List<DatosEquipo> equipos = gestorEquipo.listarTodos();

        if (equipos.isEmpty()) {
            System.out.println("No hay equipos registrados para reportar falla.");
            return;
        }

        // 2. Mostrar menú de equipos disponibles
        System.out.println("\n--- Equipos registrados ---");
        for (int i = 0; i < equipos.size(); i++) {
            System.out.println((i + 1) + ". " + equipos.get(i).getNombre()
                    + " | ID: " + equipos.get(i).getId()
                    + " | Estado: " + equipos.get(i).getEstado());
        }

        // 3. El usuario elige por número, no escribe el ID
        System.out.print("Seleccione el equipo (número): ");
        int seleccion = sc.nextInt(); sc.nextLine();

        if (seleccion < 1 || seleccion > equipos.size()) {
            System.out.println("Selección no válida.");
            return;
        }

        DatosEquipo equipoSeleccionado = equipos.get(seleccion - 1);
        String idEquipo = equipoSeleccionado.getId();

        // 4. Pedir datos de la falla
        System.out.print("Descripción de la falla: ");
        String desc = sc.nextLine();
        System.out.print("Tu nombre (usuario que reporta): ");
        String usuario = sc.nextLine();

        // 5. Registrar y cambiar estado del equipo a "en mantenimiento"
        gestorFallas.reportar(idEquipo, desc, usuario, LocalDate.now());
        gestorEquipo.actualizarEstado(idEquipo, "en mantenimiento");

        System.out.println("\n✔ Falla registrada correctamente"); // RF5
        System.out.println("  Equipo '" + equipoSeleccionado.getNombre()
                + "' marcado como: en mantenimiento");
    }

    public void actualizarEstado() {

        // 1. Mostrar equipos registrados para elegir
        List<DatosEquipo> equipos = gestorEquipo.listarTodos();

        if (equipos.isEmpty()) {
            System.out.println("No hay equipos registrados.");
            return;
        }

        System.out.println("\n--- Equipos registrados ---");
        for (int i = 0; i < equipos.size(); i++) {
            System.out.println((i + 1) + ". " + equipos.get(i).getNombre()
                    + " | ID: "     + equipos.get(i).getId()
                    + " | Estado actual: " + equipos.get(i).getEstado());
        }

        System.out.print("Seleccione el equipo (número): ");
        int seleccion = sc.nextInt(); sc.nextLine();

        if (seleccion < 1 || seleccion > equipos.size()) {
            System.out.println("Selección no válida.");
            return;
        }

        String idEquipo = equipos.get(seleccion - 1).getId();

        // 2. Elegir nuevo estado
        System.out.println("\nNuevo estado:");
        System.out.println("1. Operativo");
        System.out.println("2. En mantenimiento");
        System.out.println("3. Fuera de servicio");
        System.out.print("Opción: ");
        int op = sc.nextInt(); sc.nextLine();

        String estado = switch (op) {
            case 1 -> "operativo";
            case 2 -> "en mantenimiento";
            case 3 -> "fuera de servicio";
            default -> "desconocido";
        };

        // 3. Actualizar en AMBOS gestores
        gestorEquipo.actualizarEstado(idEquipo, estado); // estado del equipo
        gestorFallas.actualizarEstado(idEquipo, estado);  // estado de sus fallas

        System.out.println("\n✔ Estado actualizado correctamente"); // RF6
    }

    // ─── MÓDULO 4: Control y seguimiento ────────────────────────────

    public void menuReportes() {
        System.out.println("\n--- Control y Seguimiento ---");
        System.out.println("1. Ver reportes de fallas");
        System.out.println("2. Ver historial de mantenimientos");
        System.out.print("Opción: ");
        int op = sc.nextInt(); sc.nextLine();

        switch (op) {
            case 1 ->
                // ya no guardamos en List, llamamos directo
                    gestorReporte.consultarFallas(gestorFallas);
            case 2 ->
                    gestorReporte.consultarMantenimientos(gestorMant);
            default ->
                    System.out.println("Opción no válida.");
        }
    }
}