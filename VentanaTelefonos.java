package junittest;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaTelefonos {

    private TelefonoDAO dao = new TelefonoDAO();
    private TableView<Telefono> tabla = new TableView<>();
    private Persona personaActual;

    public VentanaTelefonos(Persona persona) {
        this.personaActual = persona;
    }

    public void mostrar() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.setTitle("Teléfonos de: " + personaActual.getNombre());

        TableColumn<Telefono, String> colNumero = new TableColumn<>("Número Telefónico");
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colNumero.setMinWidth(200);

        tabla.getColumns().add(colNumero);
        actualizarTabla();

        TextField txtNumero = new TextField();
        txtNumero.setPromptText("Teléfono");

        Button btnAgregar = new Button("Agregar");
        Button btnEliminar = new Button("Eliminar");

        btnAgregar.setOnAction(e -> {
            if (!txtNumero.getText().isEmpty()) {
                dao.insertar(txtNumero.getText(), personaActual.getId());
                txtNumero.clear();
                actualizarTabla();
            }
        });

        btnEliminar.setOnAction(e -> {
            Telefono seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                dao.eliminar(seleccionado.getId());
                actualizarTabla();
            }
        });

        HBox controles = new HBox(10, txtNumero, btnAgregar, btnEliminar);
        VBox root = new VBox(15, new Label("Teléfonos"), tabla, controles);
        root.setPadding(new Insets(15));

        Scene scene = new Scene(root, 450, 300);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void actualizarTabla() {
        tabla.setItems(dao.obtenerTelefonos(personaActual.getId()));
    }
}
