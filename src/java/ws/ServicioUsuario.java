package ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import modelo.Usuario;
import dao.UsuarioDAO;
import java.util.List;

@WebService
public class ServicioUsuario {

    // Método para registrar un usuario
    @WebMethod
    public String registrar(String nombre, String apellido, String correo, String contrasena, String rol) {
        try {
            System.out.println("==> INVOCANDO REGISTRO");
            System.out.println("Datos recibidos: " + nombre + ", " + apellido + ", " + correo + ", " + rol);

            Usuario u = new Usuario();
            u.setNombre(nombre);
            u.setApellido(apellido);
            u.setCorreo(correo);
            u.setContrasena(contrasena);
            u.setRol(rol);
            u.setEstado("activo");  // Asignamos el valor "activo" por defecto

            UsuarioDAO dao = new UsuarioDAO();
            boolean exito = dao.registrarUsuario(u);

            return exito ? "Usuario registrado correctamente" : "Error al registrar usuario";
        } catch (Exception e) {
            e.printStackTrace();  // Mostrará en el log del servidor
            return "Error interno en el servidor: " + e.getMessage();
        }
    }

    // Método para listar todos los usuarios
    @WebMethod
    public List<Usuario> listarUsuarios() {
        try {
            UsuarioDAO dao = new UsuarioDAO();
            return dao.listarUsuarios();  // Llamada al DAO para obtener la lista de usuarios
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // O manejarlo de acuerdo a tu lógica de manejo de excepciones
        }
    }
    @WebMethod
    public String eliminarUsuario(int id) {
        try {
            UsuarioDAO dao = new UsuarioDAO();
            boolean exito = dao.eliminarUsuario(id);  // Llamada al DAO para eliminar al usuario
            return exito ? "Usuario eliminado correctamente" : "Error al eliminar usuario";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al eliminar usuario: " + e.getMessage();
        }
    }
    @WebMethod
public String cambiarEstadoUsuario(int id, String nuevoEstado) {
    try {
        UsuarioDAO dao = new UsuarioDAO();
        boolean exito = dao.actualizarEstadoUsuario(id, nuevoEstado);
        return exito ? "Estado actualizado correctamente" : "No se pudo actualizar el estado";
    } catch (Exception e) {
        e.printStackTrace();
        return "Error al cambiar el estado: " + e.getMessage();
    }
}


}
