package Interfaz;

// interfaz/Sistema.java
import Modelo.*;
import Negocio.*;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import Exception.DatoInvalidoException;
import Exception.FormatoInvalidoException;
import Exception.UsuarioDuplicadoException;

public class Sistema {

    // Instancias globales de los controladores de negocio
    static GestorEquipo gestorEquipo             = new GestorEquipo();
    static GestorMantenimiento gestorMant          = new GestorMantenimiento();
    static GestorFallas gestorFallas               = new GestorFallas();
    static GestorReporte gestorReporte             = new GestorReporte();
    static GestorUsuario gestorUsuario             = new GestorUsuario(); // Inicialización corregida
    static Scanner sc = new Scanner(System.in);

    public int menu() {
        while (true) {
            try {
                System.out.println("\n====================================");
                System.out.println("  Sistema de Gestión de Mantenimiento");
                System.out.println("====================================");
                System.out.println("1. Gestión de usuarios");
                System.out.println("2. Gestión de equipos");
                System.out.println("3. Gestión de mantenimiento");
                System.out.println("4. Gestión de fallas");
                System.out.println("5. Control y seguimiento");
                System.out.print("Opción: ");
                int opcion = sc.nextInt();
                sc.nextLine(); // Limpiar búfer
                return opcion;
            } catch (InputMismatchException e) {
                System.out.println("\n[ERROR] Entrada inválida. Ingrese un número entero.");
                sc.nextLine(); // Limpiar búfer corrupto
            }
        }
    }

    /**
     * Muestra el menú de bienvenida y valida los datos campo por campo en tiempo real.
     */
    public Usuario identificarORegistrarUsuarioInicial() {
        while (true) {
            try {
                System.out.println("\n--- ACCESO AL SISTEMA ---");
                System.out.println("1. Ya estoy registrado (Iniciar Sesión)");
                System.out.println("2. Registrarse como usuario nuevo");
                System.out.print("Opción: ");

                // Leemos la opción como String para NO dejar 'Enters' flotando en el búfer
                String opcionAcceso = sc.nextLine().trim();

                switch (opcionAcceso) {
                    case "1":
                        // --- OPCIÓN 1: INICIAR SESIÓN ---
                        System.out.print("\nIngrese su Cédula/ID: ");
                        String cedulaLogin = sc.nextLine().trim(); // Limpieza estricta de espacios

                        // Forzamos validación de formato antes de buscar
                        Usuario.validarCedula(cedulaLogin);

                        Usuario usuarioExistente = gestorUsuario.buscarPorCedula(cedulaLogin);

                        if (usuarioExistente != null) {
                            return usuarioExistente; // Inicia sesión con éxito
                        } else {
                            System.out.println("\n[AVISO] La cédula '" + cedulaLogin + "' no existe en el sistema.");
                            System.out.println("Por favor, verifique el número o elija la opción 2 para registrarse.");
                        }
                        break;

                    case "2":
                        // --- OPCIÓN 2: REGISTRARSE ---
                        System.out.println("\n--- Formulario de Registro Obligatorio ---");

                        String cedulaNueva = "";
                        String nombre = "";
                        String telefono = "";
                        String cargo = "";

                        // 1. VALIDACIÓN INMEDIATA DE CÉDULA Y DUPLICADOS
                        while (true) {
                            try {
                                System.out.print("Ingrese Cédula (10 dígitos): ");
                                cedulaNueva = sc.nextLine().trim(); // Limpieza estricta de espacios

                                // Valida que sean 10 dígitos y solo números
                                Usuario.validarCedula(cedulaNueva);

                                // Verificación en caliente contra el gestor
                                if (gestorUsuario.buscarPorCedula(cedulaNueva) != null) {
                                    throw new UsuarioDuplicadoException("Esta cédula ya está registrada con otro usuario.");
                                }

                                break; // Cédula limpia y única, salimos del ciclo
                            } catch (FormatoInvalidoException | UsuarioDuplicadoException e) {
                                System.out.println("[ERROR INMEDIATO] " + e.getMessage() + " Intente de nuevo.\n");
                            }
                        }

                        // 2. VALIDACIÓN INMEDIATA DE NOMBRE
                        while (true) {
                            try {
                                System.out.print("Ingrese Nombre Completo (Solo letras): ");
                                nombre = sc.nextLine().trim();
                                Usuario.validarNombre(nombre);
                                break;
                            } catch (FormatoInvalidoException e) {
                                System.out.println("[ERROR INMEDIATO] " + e.getMessage() + " Intente de nuevo.\n");
                            }
                        }

                        // 3. VALIDACIÓN INMEDIATA DE TELÉFONO
                        while (true) {
                            try {
                                System.out.print("Ingrese Teléfono de Contacto: ");
                                telefono = sc.nextLine().trim();
                                Usuario.validarTelefono(telefono);
                                break;
                            } catch (FormatoInvalidoException e) {
                                System.out.println("[ERROR INMEDIATO] " + e.getMessage() + " Intente de nuevo.\n");
                            }
                        }

                        // 4. SELECCIÓN DE CARGO
                        while (true) {
                            System.out.println("Seleccione su cargo:");
                            System.out.println("1. Técnico\n2. Administrador\n3. Operador");
                            System.out.print("Opción: ");
                            String cargoOp = sc.nextLine().trim(); // Leemos como String para evitar InputMismatchException

                            cargo = switch (cargoOp) {
                                case "1" -> "Técnico";
                                case "2" -> "Administrador";
                                case "3" -> "Operador";
                                default -> "";
                            };

                            if (!cargo.isEmpty()) {
                                break;
                            }
                            System.out.println("[AVISO] Opción fuera de rango. Seleccione 1, 2 o 3.\n");
                        }

                        // Al llegar aquí, los datos están completamente limpios y estandarizados
                        Usuario nuevoUsuario = new Usuario(cedulaNueva, nombre, cargo, telefono);
                        gestorUsuario.registrarUsuario(nuevoUsuario);

                        System.out.println("\n✔ ¡Registro completado con éxito!");
                        return nuevoUsuario;

                    default:
                        System.out.println("[AVISO] Opción no válida. Ingrese el número 1 o 2.");
                        break;
                }

            } catch (FormatoInvalidoException e) {
                System.out.println("\n[ERROR DE FORMATO] " + e.getMessage());
            } catch (Exception e) {
                System.out.println("\n[ERROR GENERAL] Ocurrió un inconveniente: " + e.getMessage());
            }
        }
    }

    // ─── MÓDULO NUEVO: GESTIÓN DE USUARIOS ───────────────────────────

    public void menuUsuarios() {
        try {
            System.out.println("\n--- Gestión de Usuarios ---");
            System.out.println("1. Registrar usuario / técnico");
            System.out.println("2. Consultar usuarios");
            System.out.print("Opción: ");
            int op = sc.nextInt(); sc.nextLine();

            switch (op) {
                case 1:
                    registrarUsuario();
                    break;
                case 2:
                    consultarUsuarios();
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("[ERROR] Debe ingresar un número entero.");
            sc.nextLine();
        }
    }

    public void registrarUsuario() {
        System.out.println("\n--- Formulario de Registro de Usuario ---");

        String cedula = "";
        String nombre = "";
        String telefono = "";
        String cargo = "";

        // 1. VALIDACIÓN INMEDIATA DE CÉDULA Y DUPLICADOS (Sin dejar Enters flotando)
        while (true) {
            try {
                System.out.print("Ingrese Cédula (10 dígitos): ");
                cedula = sc.nextLine().trim();

                // Valida formato (10 dígitos, solo números)
                Usuario.validarCedula(cedula);

                // Validación en caliente de duplicados contra el gestor
                if (gestorUsuario.buscarPorCedula(cedula) != null) {
                    throw new UsuarioDuplicadoException("Esta cédula ya está registrada con otro usuario.");
                }

                break; // Si es válida y única, sale del bucle de la cédula
            } catch (FormatoInvalidoException | UsuarioDuplicadoException e) {
                System.out.println("[ERROR INMEDIATO] " + e.getMessage() + " Intente de nuevo.\n");
            }
        }

        // 2. VALIDACIÓN INMEDIATA DE NOMBRE
        while (true) {
            try {
                System.out.print("Ingrese Nombre Completo (Solo letras): ");
                nombre = sc.nextLine().trim();
                Usuario.validarNombre(nombre);
                break;
            } catch (FormatoInvalidoException e) {
                System.out.println("[ERROR INMEDIATO] " + e.getMessage() + " Intente de nuevo.\n");
            }
        }

        // 3. VALIDACIÓN INMEDIATA DE TELÉFONO
        while (true) {
            try {
                System.out.print("Ingrese Teléfono de Contacto: ");
                telefono = sc.nextLine().trim();
                Usuario.validarTelefono(telefono);
                break;
            } catch (FormatoInvalidoException e) {
                System.out.println("[ERROR INMEDIATO] " + e.getMessage() + " Intente de nuevo.\n");
            }
        }

        // 4. SELECCIÓN DE CARGO (Leído como String clásico con switch tradicional)
        while (true) {
            System.out.println("Seleccione el cargo:");
            System.out.println("1. Técnico\n2. Administrador\n3. Operador");
            System.out.print("Opción: ");
            String cargoOp = sc.nextLine().trim();

            switch (cargoOp) {
                case "1":
                    cargo = "Técnico";
                    break;
                case "2":
                    cargo = "Administrador";
                    break;
                case "3":
                    cargo = "Operador";
                    break;
                default:
                    cargo = "";
                    break;
            }

            if (!cargo.isEmpty()) {
                break; // Salimos si eligió una opción válida (1, 2 o 3)
            }
            System.out.println("[AVISO] Opción fuera de rango. Seleccione 1, 2 o 3.\n");
        }

        try {
            // Construcción e inserción 100% seguras
            Usuario nuevoUsuario = new Usuario(cedula, nombre, cargo, telefono);
            gestorUsuario.registrarUsuario(nuevoUsuario);
            System.out.println("\n✔ ¡Usuario registrado exitosamente en el sistema!");
        } catch (Exception e) {
            System.out.println("\n[ERROR GENERAL] No se pudo guardar el usuario: " + e.getMessage());
        }
    }

    public void consultarUsuarios() {
        List<Usuario> lista = gestorUsuario.listarUsuarios();
        if (lista.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        System.out.println("\n--- Lista de usuarios registrados ---");
        for (Usuario u : lista) {
            System.out.println(u); // Ejecuta el toString() limpio de Usuario
        }
    }

    // ─── MÓDULO 1: Gestión de equipos ───────────────────────────────

    public void menuEquipos() {
        try {
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
        } catch (InputMismatchException e) {
            System.out.println("[ERROR] Ingrese un número entero.");
            sc.nextLine();
        }
    }

    public void registrarEquipo() {
        try {
            System.out.println("\nTipo de equipo:");
            System.out.println("1. Cómputo   2. Impresión   3. Proyección");
            System.out.print("Opción: ");
            int tipo = sc.nextInt(); sc.nextLine();

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

            if (subOp < 0 || subOp >= subtipos.size()) {
                System.out.println("Subtipo fuera de rango.");
                return;
            }
            String subtipo = subtipos.get(subOp);

            System.out.print("ID del equipo: ");
            String id = sc.nextLine().trim();
            System.out.print("Nombre: ");
            String nombre = sc.nextLine().trim();

            System.out.print("Cantidad: ");
            int cantidad = sc.nextInt(); sc.nextLine();

            // ─── VALIDACIÓN INMEDIATA DE CANTIDAD ───
            if (cantidad <= 0) {
                throw new DatoInvalidoException("La cantidad ingresada debe ser mayor a 0. No se permiten equipos vacíos o negativos.");
            }

            DatosEquipo equipo = switch (tipo) {
                case 1 -> new EquipoComputo(id, nombre, subtipo, cantidad, LocalDate.now());
                case 2 -> new EquipoImpresion(id, nombre, subtipo, cantidad, LocalDate.now());
                case 3 -> new EquipoProyectore(id, nombre, subtipo, cantidad, LocalDate.now());
                default -> null;
            };

            gestorEquipo.agregarEquipo(equipo);
            System.out.println("\n✔ Equipo agregado exitosamente.");

        } catch (DatoInvalidoException e) {
            System.out.println("\n[ERROR DE LÓGICA] " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("\n[ERROR DE ENTRADA] Se esperaba un dato numérico entero.");
            sc.nextLine(); // Limpiar búfer
        } catch (Exception e) {
            System.out.println("\n[ERROR CONTROLADO] " + e.getMessage());
        }
    }

    public void consultarEquipos() {
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
        try {
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
        } catch (InputMismatchException e) {
            System.out.println("[ERROR] Opción inválida.");
            sc.nextLine();
        }
    }

    public void registrarMantenimiento() {
        try {
            List<DatosEquipo> equipos = gestorEquipo.listarTodos();

            if (equipos.isEmpty()) {
                System.out.println("No hay equipos registrados para dar mantenimiento.");
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

            System.out.println("\nTipo de mantenimiento:");
            System.out.println("1. Preventivo\n2. Correctivo\n3. Diagnóstico");
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

            System.out.print("Cédula del Técnico responsable: ");
            String cedulaTec = sc.nextLine();

            // Validación cruzada con el Gestor de Usuarios
            Usuario tecnico = gestorUsuario.buscarPorCedula(cedulaTec);
            if (tecnico == null) {
                throw new DatoInvalidoException("El técnico con cédula '" + cedulaTec + "' no está registrado. Regístrelo primero en el módulo 1.");
            }

            gestorMant.registrar(idEquipo, tipo, desc, tecnico.getNombre(), LocalDate.now());
            gestorEquipo.actualizarEstado(idEquipo, "en mantenimiento");

            System.out.println("\n✔ Mantenimiento asignado correctamente al técnico: " + tecnico.getNombre());
            System.out.println("  Equipo '" + equipoSeleccionado.getNombre() + "' marcado como: en mantenimiento");
        } catch (Exception e) {
            System.out.println("\n[ERROR EN OPERACIÓN] " + e.getMessage());
        }
    }

    public void consultarHistorial() {
        List<DatosEquipo> equipos = gestorEquipo.listarTodos();

        if (equipos.isEmpty()) {
            System.out.println("No hay equipos registrados.");
            return;
        }

        System.out.println("\n--- Equipos registrados ---");
        for (int i = 0; i < equipos.size(); i++) {
            System.out.println((i + 1) + ". " + equipos.get(i).getNombre()
                    + " | ID: "     + equipos.get(i).getId()
                    + " | Estado actual en sistema: " + equipos.get(i).getEstado());
        }

        System.out.print("Seleccione el equipo (número) para ver su historial: ");
        int seleccion = sc.nextInt(); sc.nextLine();

        if (seleccion < 1 || seleccion > equipos.size()) {
            System.out.println("Selección no válida.");
            return;
        }

        DatosEquipo equipoSeleccionado = equipos.get(seleccion - 1);
        String idEquipo = equipoSeleccionado.getId();

        // Obtenemos los registros de mantenimiento de este equipo
        List<Mantenimiento> historial = gestorMant.consultarPorEquipo(idEquipo);

        if (historial.isEmpty()) {
            System.out.println("El equipo '" + equipoSeleccionado.getNombre()
                    + "' no tiene mantenimientos registrados.");
            return;
        }

        System.out.println("\n=======================================================");
        System.out.println("   HISTORIAL DE MANTENIMIENTO: " + equipoSeleccionado.getNombre().toUpperCase());
        System.out.println("   ESTADO ACTUAL DEL EQUIPO: " + equipoSeleccionado.getEstado().toUpperCase()); // <-- AQUÍ MUESTRA EL ESTADO REAL EN VIVO
        System.out.println("=======================================================");

        for (Mantenimiento m : historial) {
            // Imprimimos los detalles guardados (fecha, técnico, descripción)
            System.out.println(m);
        }
        System.out.println("=======================================================");
    }

    // ─── MÓDULO 4: Gestión de fallas ────────────────────────────────

    public void menuFallas() {
        try {
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
        } catch (InputMismatchException e) {
            System.out.println("[ERROR] Ingrese un número entero.");
            sc.nextLine();
        }
    }

    public void reportarFalla() {
        List<DatosEquipo> equipos = gestorEquipo.listarTodos();

        if (equipos.isEmpty()) {
            System.out.println("No hay equipos registrados para reportar falla.");
            return;
        }

        System.out.println("\n--- Equipos registrados ---");
        for (int i = 0; i < equipos.size(); i++) {
            System.out.println((i + 1) + ". " + equipos.get(i).getNombre()
                    + " | ID: " + equipos.get(i).getId()
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

        System.out.print("Descripción de la falla: ");
        String desc = sc.nextLine();
        System.out.print("Tu nombre (usuario que reporta): ");
        String usuario = sc.nextLine();

        try {
            gestorFallas.reportar(idEquipo, desc, usuario, LocalDate.now());
            gestorEquipo.actualizarEstado(idEquipo, "en mantenimiento");

            System.out.println("\n✔ Falla registrada correctamente");
            System.out.println("  Equipo '" + equipoSeleccionado.getNombre() + "' marcado como: en mantenimiento");
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public void actualizarEstado() {
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

        System.out.println("\nNuevo estado:");
        System.out.println("1. Operativo\n2. En mantenimiento\n3. Fuera de servicio");
        System.out.print("Opción: ");
        int op = sc.nextInt(); sc.nextLine();

        String estado = switch (op) {
            case 1 -> "operativo";
            case 2 -> "en mantenimiento";
            case 3 -> "fuera de servicio";
            default -> "desconocido";
        };

        try {
            gestorEquipo.actualizarEstado(idEquipo, estado);
            gestorFallas.actualizarEstado(idEquipo, estado);

            System.out.println("\n✔ Estado actualizado correctamente.");
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ─── MÓDULO 5: Control y seguimiento ────────────────────────────

    public void menuReportes() {
        System.out.println("\n--- Control y Seguimiento ---");
        System.out.println("1. Ver reportes de fallas");
        System.out.println("2. Ver historial de mantenimientos");
        System.out.print("Opción: ");
        int op = sc.nextInt(); sc.nextLine();

        switch (op) {
            case 1 -> gestorReporte.consultarFallas(gestorFallas);
            case 2 -> gestorReporte.consultarMantenimientos(gestorMant);
            default -> System.out.println("Opción no válida.");
        }
    }
}