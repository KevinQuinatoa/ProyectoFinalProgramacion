package Negocio;

import Modelo.Usuario;
import Exception.DatoInvalidoException; // Ajustado a minúsculas si cambiaste el nombre del paquete
import Exception.UsuarioDuplicadoException;
import java.util.ArrayList;
import java.util.List;

public class GestorUsuario {
    private List<Usuario> usuarios = new ArrayList<>();

    public void registrarUsuario(Usuario usuario) throws UsuarioDuplicadoException, DatoInvalidoException {
        if (usuario == null) {
            throw new DatoInvalidoException("El usuario no puede ser nulo.");
        }

        // Validación interna por si se intenta meter una cédula duplicada directo al método
        if (buscarPorCedula(usuario.getCedulaId()) != null) {
            throw new UsuarioDuplicadoException("No se puede registrar. La cédula '" + usuario.getCedulaId() + "' ya existe.");
        }

        usuarios.add(usuario);
    }

    public Usuario buscarPorCedula(String cedula) {
        for (Usuario u : usuarios) {
            if (u.getCedulaId().equals(cedula)) {
                return u;
            }
        }
        return null;
    }

    // ─── NUEVO MÉTODO: BÚSQUEDA POR TELÉFONO ───
    public Usuario buscarPorTelefono(String telefono) {
        for (Usuario u : usuarios) {
            if (u.getTelefono().equals(telefono)) {
                return u; // Retorna el usuario si el teléfono ya coincide
            }
        }
        return null; // Retorna null si el teléfono está libre y no existe duplicado
    }

    public List<Usuario> listarUsuarios() {
        return usuarios;
    }
}