package junittest;

import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DireccionDAO {

    public ObservableList<Direccion> obtenerTodasLasDirecciones() {
        ObservableList<Direccion> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Direcciones";
        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Direccion(rs.getInt("id"), rs.getString("calle"), rs.getString("ciudad")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public void crearDireccion(String calle, String ciudad) {
        String sql = "INSERT INTO Direcciones (calle, ciudad) VALUES (?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, calle);
            pstmt.setString(2, ciudad);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void vincularPersonaADireccion(int personaId, int direccionId) {
        String sql = "INSERT INTO Personas_Direcciones (persona_id, direccion_id) VALUES (?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, personaId);
            pstmt.setInt(2, direccionId);
            pstmt.executeUpdate();
        } catch (SQLException e) { 
            System.out.println("Es probable que esa persona ya esté en esa dirección.");
            e.printStackTrace(); 
        }
    }

    public ObservableList<Persona> obtenerHabitantes(int direccionId) {
        ObservableList<Persona> lista = FXCollections.observableArrayList();
        String sql = "SELECT p.* FROM Personas p " +
                     "JOIN Personas_Direcciones pd ON p.id = pd.persona_id " +
                     "WHERE pd.direccion_id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, direccionId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Persona(rs.getInt("id"), rs.getString("nombre"), rs.getString("direccion")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}
