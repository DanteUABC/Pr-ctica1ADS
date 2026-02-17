package junittest;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

    public class Agenda extends Application {

    private PersonaDAO dao = new PersonaDAO();
    
    private TableView<Persona> tabla = new TableView<>();
    private TextField txtNombre = new TextField();
    private TextField txtDireccion = new TextField();
    
    private Persona personaSeleccionada = null;

    @Override
    public void start(Stage stage) {
        
        TableColumn<Persona, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Persona, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setMinWidth(150);

        TableColumn<Persona, String> colDir = new TableColumn<>("Dirección");
        colDir.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colDir.setMinWidth(200);

        tabla.getColumns().addAll(colId, colNombre, colDir);
        cargarDatos();
        txtNombre.setPromptText("Nombre completo");
        txtDireccion.setPromptText("Dirección");

        Button btnGuardar = new Button("Guardar");
        Button btnEliminar = new Button("Eliminar");
        Button btnLimpiar = new Button("Limpiar");
        Button btnModuloDirecciones = new Button("Módulo Direcciones");
        
        Button btnTelefonos = new Button("Ver teléfonos");
        btnTelefonos.setDisable(true);

        
        
        btnModuloDirecciones.setOnAction(e -> {
            VentanaDirecciones ventanaDir = new VentanaDirecciones();
            ventanaDir.mostrar();
        });

        HBox buttonLayout = new HBox(10, btnGuardar, btnEliminar, btnLimpiar, btnTelefonos, btnModuloDirecciones);
        
        
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                personaSeleccionada = newVal;
                txtNombre.setText(newVal.getNombre());
                txtDireccion.setText(newVal.getDireccion());
                
                btnTelefonos.setDisable(false);
            } else {
                btnTelefonos.setDisable(true);
            }
        });

        btnTelefonos.setOnAction(e -> {
            if (personaSeleccionada != null) {
                VentanaTelefonos ventana = new VentanaTelefonos(personaSeleccionada);
                ventana.mostrar();
            }
        });

        btnGuardar.setOnAction(e -> {
            String nombre = txtNombre.getText();
            String direccion = txtDireccion.getText();

            if (personaSeleccionada == null) {
                Persona nueva = new Persona(0, nombre, direccion);
                dao.insertar(nueva);
            } else {
                personaSeleccionada.setNombre(nombre);
                personaSeleccionada.setDireccion(direccion);
                dao.actualizar(personaSeleccionada);
            }
            limpiarFormulario(btnTelefonos);
            cargarDatos();
        });

        btnEliminar.setOnAction(e -> {
            if (personaSeleccionada != null) {
                dao.eliminar(personaSeleccionada.getId());
                limpiarFormulario(btnTelefonos);
                cargarDatos();
            } else {
                mostrarAlerta("Seleccione una persona para eliminar");
            }
        });
        
        btnLimpiar.setOnAction(e -> limpiarFormulario(btnTelefonos));

        HBox inputLayout = new HBox(10, txtNombre, txtDireccion);
        
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(new Label("Personas"), tabla, inputLayout, buttonLayout);

        Scene scene = new Scene(root, 650, 500);
        stage.setTitle("Agenda Telefónica");
        stage.setScene(scene);
        stage.show();
    }

    private void cargarDatos() {
        tabla.setItems(dao.obtenerTodos());
    }

    private void limpiarFormulario(Button btnTelefonos) {
        txtNombre.clear();
        txtDireccion.clear();
        personaSeleccionada = null;
        tabla.getSelectionModel().clearSelection();
        btnTelefonos.setDisable(true);
    }
    
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atención");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
