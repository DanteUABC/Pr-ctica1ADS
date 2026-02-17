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
    
    private Telefono telefonoSeleccionado = null;
    
    private TextField txtNumero = new TextField();
    private Button btnGuardar = new Button("Agregar");

    public VentanaTelefonos(Persona persona) {
        this.personaActual = persona;
    }

    public void mostrar() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Teléfonos de: " + personaActual.getNombre());

        TableColumn<Telefono, String> colNumero = new TableColumn<>("Número Telefónico");
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colNumero.setMinWidth(250);

        tabla.getColumns().add(colNumero);
        actualizarTabla();

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                telefonoSeleccionado = newVal;
                txtNumero.setText(newVal.getNumero());
                btnGuardar.setText("Actualizar");
            }
        });

        txtNumero.setPromptText("Número Nuevo");

        Button btnEliminar = new Button("Eliminar");
        Button btnLimpiar = new Button("Nuevo / Limpiar");

        btnGuardar.setOnAction(e -> {
            if (txtNumero.getText().isEmpty()) return;

            if (telefonoSeleccionado == null) {
                Telefono nuevo = new Telefono(0, txtNumero.getText(), personaActual.getId());
                dao.insertar(nuevo);
            } else {
                telefonoSeleccionado.setNumero(txtNumero.getText());
                dao.actualizar(telefonoSeleccionado);
            }
            limpiar();
            actualizarTabla();
        });

        btnEliminar.setOnAction(e -> {
            Telefono seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                dao.eliminar(seleccionado.getId());
                limpiar();
                actualizarTabla();
            }
        });

        btnLimpiar.setOnAction(e -> limpiar());

        HBox controles = new HBox(10, txtNumero, btnGuardar, btnEliminar, btnLimpiar);
        VBox root = new VBox(15, new Label("Gestionando teléfonos para ID: " + personaActual.getId()), tabla, controles);
        root.setPadding(new Insets(15));

        Scene scene = new Scene(root, 550, 400);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void actualizarTabla() {
        tabla.setItems(dao.obtenerPorPersona(personaActual.getId()));
    }

    private void limpiar() {
        txtNumero.clear();
        telefonoSeleccionado = null;
        tabla.getSelectionModel().clearSelection();
        btnGuardar.setText("Agregar");
    }
}
