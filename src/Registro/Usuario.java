package Registro;

public class Usuario {

    private static int contadorId = 1;

    private int id;
    private String nombre;
    private String rol;       // "tecnico" | "reporter"
    private String telefono;

    public Usuario(String nombre, String rol, String telefono) {
        this.id       = contadorId++;
        this.nombre   = nombre;
        this.rol      = rol;
        this.telefono = telefono;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "[USUARIO #" + id + "] " + nombre +
                " | Rol: " + rol +
                " | Tel: " + telefono;
    }
}
