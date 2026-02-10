package junittest;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaDirecciones {

    private DireccionDAO dirDao = new DireccionDAO();
    
    private TableView<Direccion> tablaDirecciones = new TableView<>();
    private TableView<Persona> tablaHabitantes = new TableView<>();
    
    private Direccion direccionSeleccionada = null;

    public void mostrar() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Gestión de Direcciones y Habitantes");

        TableColumn<Direccion, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Direccion, String> colCalle = new TableColumn<>("Calle");
        colCalle.setCellValueFactory(new PropertyValueFactory<>("calle"));
        TableColumn<Direccion, String> colCiudad = new TableColumn<>("Ciudad");
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        
        tablaDirecciones.getColumns().addAll(colId, colCalle, colCiudad);
        tablaDirecciones.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Persona, String> colNombreHab = new TableColumn<>("Habitante");
        colNombreHab.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        tablaHabitantes.getColumns().add(colNombreHab);
        tablaHabitantes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaHabitantes.setPlaceholder(new Label("Selecciona una dirección"));

        tablaDirecciones.setItems(dirDao.obtenerTodasLasDirecciones());

        tablaDirecciones.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                direccionSeleccionada = newVal;
                tablaHabitantes.setItems(dirDao.obtenerHabitantes(newVal.getId()));
                tablaHabitantes.setPlaceholder(new Label("Nadie vive aquí aún"));
            }
        });

        TextField txtCalle = new TextField(); txtCalle.setPromptText("Calle");
        TextField txtCiudad = new TextField(); txtCiudad.setPromptText("Ciudad");
        Button btnNuevaDir = new Button("Crear Dirección");
        
        btnNuevaDir.setOnAction(e -> {
            dirDao.crearDireccion(txtCalle.getText(), txtCiudad.getText());
            tablaDirecciones.setItems(dirDao.obtenerTodasLasDirecciones());
            txtCalle.clear(); txtCiudad.clear();
        });

        TextField txtIdPersona = new TextField(); 
        txtIdPersona.setPromptText("ID Persona");
        txtIdPersona.setPrefWidth(80);
        Button btnVincular = new Button("<- Agregar Habitante");
        
        btnVincular.setOnAction(e -> {
            if (direccionSeleccionada != null && !txtIdPersona.getText().isEmpty()) {
                try {
                    int idPersona = Integer.parseInt(txtIdPersona.getText());
                    dirDao.vincularPersonaADireccion(idPersona, direccionSeleccionada.getId());
                    // Refrescar tabla derecha
                    tablaHabitantes.setItems(dirDao.obtenerHabitantes(direccionSeleccionada.getId()));
                    txtIdPersona.clear();
                } catch (NumberFormatException ex) {
                    Alert a = new Alert(Alert.AlertType.ERROR, "El ID debe ser un número");
                    a.show();
                }
            } else {
                Alert a = new Alert(Alert.AlertType.WARNING, "Selecciona una dirección primero");
                a.show();
            }
        });

        VBox panelIzquierdo = new VBox(10, new Label("1. Direcciones"), tablaDirecciones, new HBox(5, txtCalle, txtCiudad, btnNuevaDir));
        VBox panelDerecho = new VBox(10, new Label("2. Personas en esa dirección"), tablaHabitantes, new HBox(5, txtIdPersona, btnVincular));
        
        HBox.setHgrow(panelIzquierdo, Priority.ALWAYS);
        HBox.setHgrow(panelDerecho, Priority.ALWAYS);

        HBox root = new HBox(20, panelIzquierdo, panelDerecho);
        root.setPadding(new Insets(15));
        root.setPrefSize(800, 500);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
