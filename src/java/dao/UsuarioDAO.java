package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import modelo.Usuario;
import util.Conexion;  // Asegúrate de tener la clase de conexión configurada correctamente

public class UsuarioDAO {

    // Método para registrar un usuario
    public boolean registrarUsuario(Usuario u) {
        boolean resultado = false;

        try {
            // Usa el driver correcto para MySQL 8+
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Usa timezone en la URL para evitar warning
            String url = "jdbc:mysql://localhost:3306/parkingsystem?useSSL=false&serverTimezone=UTC";
            Connection con = DriverManager.getConnection(url, "root", "");

            String sql = "INSERT INTO usuarios (nombre, apellido, correo, contrasena, rol, estado) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getContrasena());
            ps.setString(5, u.getRol());
            ps.setString(6, u.getEstado());  // Asignar 'estado' como "activo" si no se establece otro valor

            int filas = ps.executeUpdate();
            resultado = filas > 0;

            ps.close();
            con.close();

        } catch (SQLIntegrityConstraintViolationException dup) {
            System.out.println("⚠️ Ya existe un usuario con ese correo: " + u.getCorreo());
        } catch (SQLException sqlEx) {
            System.out.println("❌ Error SQL: " + sqlEx.getMessage());
        } catch (ClassNotFoundException cnfe) {
            System.out.println("❌ No se encontró el driver JDBC.");
        } catch (Exception e) {
            System.out.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }

        return resultado;
    }

    // Método para listar todos los usuarios
    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();

        try {
            // Usamos la clase de conexión para obtener la conexión
            Connection conn = Conexion.getConnection();
            String sql = "SELECT * FROM usuarios";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setRol(rs.getString("rol"));
                usuario.setEstado(rs.getString("estado"));  // Obtener el estado
                usuario.setFechaRegistro(rs.getString("fecha_registro"));
                lista.add(usuario);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    public boolean eliminarUsuario(int id) {
        boolean resultado = false;

        try {
            String url = "jdbc:mysql://localhost:3306/parkingsystem?useSSL=false&serverTimezone=UTC";
            Connection con = DriverManager.getConnection(url, "root", "");

            String sql = "DELETE FROM usuarios WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int filas = ps.executeUpdate();
            resultado = filas > 0;

            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultado;
    }
// ...
public boolean actualizarEstadoUsuario(int id, String nuevoEstado) {
    boolean resultado = false;

    try {
        List<String> estadosValidos = Arrays.asList("activo", "bloqueado", "inactivo");
        if (!estadosValidos.contains(nuevoEstado.toLowerCase())) {
            System.out.println("⚠️ Estado no válido: " + nuevoEstado);
            return false;
        }

        Connection conn = Conexion.getConnection();
        String sql = "UPDATE usuarios SET estado = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nuevoEstado);
        ps.setInt(2, id);

        int filasAfectadas = ps.executeUpdate();
        resultado = filasAfectadas > 0;

        ps.close();
    } catch (SQLException e) {
        System.err.println("❌ Error al actualizar el estado del usuario");
        e.printStackTrace();
    }

    return resultado;
}

}
