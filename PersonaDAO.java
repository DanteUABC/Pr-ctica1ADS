package junittest;

import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PersonaDAO {

    public ObservableList<Persona> obtenerPersonas() {
        ObservableList<Persona> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Personas";

        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Persona(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("direccion")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void insertar(Persona p) {
        String sql = "INSERT INTO Personas (nombre, direccion) VALUES (?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, p.getNombre());
            pstmt.setString(2, p.getDireccion());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizar(Persona p) {
        String sql = "UPDATE Personas SET nombre = ?, direccion = ? WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, p.getNombre());
            pstmt.setString(2, p.getDireccion());
            pstmt.setInt(3, p.getId());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM Personas WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
