package junittest;

import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TelefonoDAO {

    public ObservableList<Telefono> obtenerTelefonos(int personaId) {
        ObservableList<Telefono> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Telefonos WHERE personaId = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, personaId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Telefono(
                    rs.getInt("id"),
                    rs.getString("telefono"),
                    rs.getInt("personaId")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void insertar(String numero, int personaId) {
        String sql = "INSERT INTO Telefonos (telefono, personaId) VALUES (?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, numero);
            pstmt.setInt(2, personaId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM Telefonos WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
