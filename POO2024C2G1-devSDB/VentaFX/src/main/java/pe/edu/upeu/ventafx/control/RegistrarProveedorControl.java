package pe.edu.upeu.ventafx.control;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.ventafx.componente.ColumnInfo;
import pe.edu.upeu.ventafx.componente.TableViewHelper;
import pe.edu.upeu.ventafx.modelo.Proveedor;
import pe.edu.upeu.ventafx.servicio.ProveedorService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class RegistrarProveedorControl {

    @FXML
    private TextField txtRUC, txtNombres, txtTelefono, txtDireccion, txtRazonSocial, txtFiltroDato;
    @FXML
    private TableView<Proveedor> tableView;
    @FXML
    private Label lbnMsg;
    @FXML
    private AnchorPane miContenedor;
    Stage stage;

    @Autowired
    private ProveedorService proveedorService;

    private Validator validator;
    private ObservableList<Proveedor> listaProveedores;
    private Long idProveedorCE = 0L;

    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), event -> {
            stage = (Stage) miContenedor.getScene().getWindow();
        }));
        timeline.setCycleCount(1);
        timeline.play();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        TableViewHelper<Proveedor> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID Proveedor", new ColumnInfo("idProveedor", 100.0));
        columns.put("RUC", new ColumnInfo("dniRuc", 100.0));
        columns.put("Nombres", new ColumnInfo("nombresRaso", 200.0));
        columns.put("Teléfono", new ColumnInfo("celular", 100.0));
        columns.put("Dirección", new ColumnInfo("direccion", 200.0));
        columns.put("Razón Social", new ColumnInfo("email", 200.0));

        Consumer<Proveedor> updateAction = this::editForm;
        Consumer<Proveedor> deleteAction = proveedor -> {
            proveedorService.delete(proveedor.getIdProveedor());
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);
        listar();

        txtFiltroDato.textProperty().addListener((observable, oldValue, newValue) -> filtrarProveedores(newValue));
    }

    public void listar() {
        tableView.getItems().clear();
        listaProveedores = FXCollections.observableArrayList(proveedorService.list());
        tableView.getItems().addAll(listaProveedores);
    }

    public void clearForm() {
        txtRUC.setText("");
        txtNombres.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        txtRazonSocial.setText("");
        idProveedorCE = 0L;
        limpiarError();
    }

    @FXML
    public void cancelarAccion() {
        clearForm();
    }

    private void limpiarError() {
        txtRUC.getStyleClass().remove("text-field-error");
        txtNombres.getStyleClass().remove("text-field-error");
        txtTelefono.getStyleClass().remove("text-field-error");
        txtDireccion.getStyleClass().remove("text-field-error");
        txtRazonSocial.getStyleClass().remove("text-field-error");
    }

    private void validarCampos(Set<ConstraintViolation<Proveedor>> violaciones) {
        LinkedHashMap<String, String> errores = new LinkedHashMap<>();
        for (ConstraintViolation<Proveedor> violacion : violaciones) {
            String campo = violacion.getPropertyPath().toString();
            switch (campo) {
                case "ruc":
                    errores.put("ruc", violacion.getMessage());
                    txtRUC.getStyleClass().add("text-field-error");
                    break;
                case "nombres":
                    errores.put("nombres", violacion.getMessage());
                    txtNombres.getStyleClass().add("text-field-error");
                    break;
                case "telefono":
                    errores.put("telefono", violacion.getMessage());
                    txtTelefono.getStyleClass().add("text-field-error");
                    break;
                case "direccion":
                    errores.put("direccion", violacion.getMessage());
                    txtDireccion.getStyleClass().add("text-field-error");
                    break;
                case "razonSocial":
                    errores.put("razonSocial", violacion.getMessage());
                    txtRazonSocial.getStyleClass().add("text-field-error");
                    break;
            }
        }
        if (!errores.isEmpty()) {
            lbnMsg.setText(errores.values().iterator().next());
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
        }
    }

    @FXML
    public void validarFormulario() {
        Proveedor proveedor = new Proveedor();
        proveedor.setDniRuc(txtRUC.getText());
        proveedor.setNombresRaso(txtNombres.getText());
        proveedor.setCelular(txtTelefono.getText());
        proveedor.setDireccion(txtDireccion.getText());
        proveedor.setEmail(txtRazonSocial.getText());
        System.out.println(proveedor.toString());
        Set<ConstraintViolation<Proveedor>> violaciones = validator.validate(proveedor);
        if (violaciones.isEmpty()) {
            lbnMsg.setText("Formulario válido");
            lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
            limpiarError();

            if (idProveedorCE != 0L) {
                proveedor.setIdProveedor(idProveedorCE);
                proveedorService.update(proveedor);
            } else {
                proveedorService.save(proveedor);
            }
            clearForm();
            listar();
        } else {
            validarCampos(violaciones);
        }
    }

    private void filtrarProveedores(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tableView.getItems().clear();
            tableView.getItems().addAll(listaProveedores);
        } else {
            String lowerCaseFilter = filtro.toLowerCase();
            List<Proveedor> proveedoresFiltrados = listaProveedores.stream()
                    .filter(proveedor -> proveedor.getDniRuc().toLowerCase().contains(lowerCaseFilter) ||
                            proveedor.getNombresRaso().toLowerCase().contains(lowerCaseFilter) ||
                            proveedor.getCelular().contains(lowerCaseFilter) ||
                            proveedor.getDireccion().toLowerCase().contains(lowerCaseFilter) ||
                            proveedor.getEmail().toLowerCase().contains(lowerCaseFilter))
                    .collect(Collectors.toList());
            tableView.getItems().clear();
            tableView.getItems().addAll(proveedoresFiltrados);
        }
    }

    private void editForm(Proveedor proveedor) {
        txtRUC.setText(proveedor.getDniRuc());
        txtNombres.setText(proveedor.getNombresRaso());
        txtTelefono.setText(proveedor.getCelular());
        txtDireccion.setText(proveedor.getDireccion());
        txtRazonSocial.setText(proveedor.getEmail());
        idProveedorCE = proveedor.getIdProveedor();
        limpiarError();
    }
}