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
            System.out.println("0. Salir");
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

    public void registrarMantenimiento() {                // RF3
        System.out.print("ID del equipo: ");
        String idEquipo = sc.nextLine();

        // Validar que el equipo exista
        if (gestorEquipo.buscarPorId(idEquipo) == null) {
            System.out.println("Equipo no encontrado.");
            return;
        }

        System.out.println("Tipo de mantenimiento:");
        System.out.println("1. Preventivo  2. Correctivo  3. Diagnóstico");
        System.out.print("Opción: ");
        int tipoOp = sc.nextInt(); sc.nextLine();
        String tipo = switch (tipoOp) {
            case 1 -> "Preventivo";
            case 2 -> "Correctivo";
            case 3 -> "Diagnóstico";
            default -> "Desconocido";
        };

        System.out.print("Descripción: ");
        String desc = sc.nextLine();
        System.out.print("Técnico responsable: ");
        String tecnico = sc.nextLine();

        gestorMant.registrar(idEquipo, tipo, desc, tecnico, LocalDate.now());
        System.out.println("\nMantenimiento registrado correctamente"); // RF3
    }

    public void consultarHistorial() {                    // RF4
        System.out.print("ID del equipo: ");
        String id = sc.nextLine();
        gestorMant.consultarPorEquipo(id).forEach(System.out::println);
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

    public void reportarFalla() {                         // RF5
        System.out.print("ID del equipo: ");
        String idEquipo = sc.nextLine();
        System.out.print("Descripción de la falla: ");
        String desc = sc.nextLine();
        System.out.print("Tu nombre (usuario que reporta): ");
        String usuario = sc.nextLine();

        gestorFallas.reportar(idEquipo, desc, usuario, LocalDate.now());
        System.out.println("\nFalla registrada correctamente");         // RF5
    }

    public void actualizarEstado() {                      // RF6
        System.out.print("ID del equipo: ");
        String id = sc.nextLine();
        System.out.println("Nuevo estado:");
        System.out.println("1. Operativo  2. En mantenimiento  3. Fuera de servicio");
        System.out.print("Opción: ");
        int op = sc.nextInt(); sc.nextLine();
        String estado = switch (op) {
            case 1 -> "operativo";
            case 2 -> "en mantenimiento";
            case 3 -> "fuera de servicio";
            default -> "desconocido";
        };

        gestorEquipo.actualizarEstado(id, estado);
        System.out.println("\nEstado actualizado correctamente");        // RF6
    }

    // ─── MÓDULO 4: Control y seguimiento ────────────────────────────

    public void menuReportes() {                          // RF7
        System.out.println("\n--- Reportes de Fallas ---");
        List<String> reportes = gestorReporte.consultarFallas(gestorFallas);
        if (reportes.isEmpty()) {
            System.out.println("No hay fallas registradas.");
            return;
        }
        reportes.forEach(System.out::println);
    }
}