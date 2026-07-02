package Negocio;

import Modelo.Usuario;
import Exception.DatoInvalidoException;
import Exception.UsuarioDuplicadoException;
import java.util.ArrayList;
import java.util.List;

public class GestorUsuario {
    private List<Usuario> usuarios = new ArrayList<>();

    public void registrarUsuario(Usuario usuario) throws UsuarioDuplicadoException, DatoInvalidoException {
        if (usuario == null) {
            throw new DatoInvalidoException("El usuario no puede ser nulo.");
        }

        // Validación de duplicados en el almacenamiento
        if (buscarPorCedula(usuario.getCedulaId()) != null) {
            throw new UsuarioDuplicadoException("No se puede registrar. La cédula '" + usuario.getCedulaId() + "' ya existe.");
        }

        usuarios.add(usuario);
    }

    public Usuario buscarPorCedula(String cedula) {
        for (Usuario u : usuarios) {
            if (u.getCedulaId().equals(cedula)) return u;
        }
        return null;
    }

    public List<Usuario> listarUsuarios() {
        return usuarios;
    }
}