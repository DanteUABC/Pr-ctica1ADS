package junittest;

import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TelefonoDAO implements CrudDAO<Telefono> {
    
    public ObservableList<Telefono> obtenerPorPersona(int personaId) {
        ObservableList<Telefono> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Telefonos WHERE persona_id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, personaId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Telefono(rs.getInt("id"), rs.getString("numero"), rs.getInt("persona_id")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public ObservableList<Telefono> obtenerTodos() {
        return FXCollections.observableArrayList(); 
    }

    @Override
    public void insertar(Telefono t) {
        String sql = "INSERT INTO Telefonos (numero, persona_id) VALUES (?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, t.getNumero());
            pstmt.setInt(2, t.getPersonaId());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void actualizar(Telefono t) {
        String sql = "UPDATE Telefonos SET numero = ? WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, t.getNumero());
            pstmt.setInt(2, t.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM Telefonos WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
