package pe.edu.upeu.ventafx.control;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.ventafx.modelo.Cliente;
import pe.edu.upeu.ventafx.servicio.ClienteService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RegistroController {

    @FXML
    private TextField txtNombre, txtApellido, txtFono, txtCorreo;
    @FXML
    private PasswordField txtContra;
    @FXML
    private TextArea registroArea;
    @FXML
    Label actionTarget;
    @FXML
    AnchorPane miContenedor;
    public Stage stage;

    @Autowired
    private ClienteService clienteService;

    private Validator validator;
    private ObservableList<Cliente> listaClientes;
    private String dnirucCE;

    public void initialize() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        listarClientes();
        txtNombre.textProperty().addListener((observable, oldValue, newValue) -> filtrarClientes(newValue));
    }

    public void listarClientes() {
        listaClientes = FXCollections.observableArrayList(clienteService.list());
        registroArea.clear();
        listaClientes.forEach(cliente -> registroArea.appendText(cliente.toString() + "\n"));
    }

    public void clearForm() {
        txtNombre.clear();
        txtApellido.clear();
        txtFono.clear();
        txtCorreo.clear();
        txtContra.clear();
        dnirucCE = null;
        limpiarError();
    }

    private void limpiarError() {
        txtNombre.getStyleClass().remove("text-field-error");
        txtApellido.getStyleClass().remove("text-field-error");
        txtFono.getStyleClass().remove("text-field-error");
        txtCorreo.getStyleClass().remove("text-field-error");
    }

    private void validarCampos(Set<ConstraintViolation<Cliente>> violaciones) {
        LinkedHashMap<String, String> errores = new LinkedHashMap<>();
        for (ConstraintViolation<Cliente> violacion : violaciones) {
            String campo = violacion.getPropertyPath().toString();
            switch (campo) {
                case "nombres":
                    errores.put("nombres", violacion.getMessage());
                    txtNombre.getStyleClass().add("text-field-error");
                    break;
                case "repLegal":
                    errores.put("repLegal", violacion.getMessage());
                    txtApellido.getStyleClass().add("text-field-error");
                    break;
                case "tipoDocumento":
                    errores.put("tipoDocumento", violacion.getMessage());
                    txtFono.getStyleClass().add("text-field-error");
                    break;
                case "dniruc":
                    errores.put("dniruc", violacion.getMessage());
                    txtCorreo.getStyleClass().add("text-field-error");
                    break;
            }
        }
        if (!errores.isEmpty()) {
            actionTarget.setText(errores.values().iterator().next());
            actionTarget.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
        }
    }

    @FXML
    public void GuadarFormulario() {
        Cliente cliente = new Cliente();
        cliente.setNombres(txtNombre.getText());
        cliente.setRepLegal(txtApellido.getText());
        cliente.setTipoDocumento(txtFono.getText());
        cliente.setDniruc(txtCorreo.getText());

        Set<ConstraintViolation<Cliente>> violaciones = validator.validate(cliente);
        if (violaciones.isEmpty()) {
            actionTarget.setText("Formulario válido");
            actionTarget.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
            limpiarError();

            if (dnirucCE != null) {
                cliente.setDniruc(dnirucCE);
                clienteService.update(cliente, dnirucCE);
            } else {
                clienteService.save(cliente);
            }
            clearForm();
            listarClientes();
        } else {
            validarCampos(violaciones);
        }
    }

    @FXML
    public void CancelarGFormulario() {
        clearForm();
    }

    private void filtrarClientes(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            listarClientes();
        } else {
            String lowerCaseFilter = filtro.toLowerCase();
            List<Cliente> clientesFiltrados = listaClientes.stream()
                    .filter(cliente -> cliente.getNombres().toLowerCase().contains(lowerCaseFilter) ||
                            cliente.getRepLegal().toLowerCase().contains(lowerCaseFilter) ||
                            cliente.getTipoDocumento().contains(lowerCaseFilter) ||
                            cliente.getDniruc().toLowerCase().contains(lowerCaseFilter))
                    .collect(Collectors.toList());
            registroArea.clear();
            clientesFiltrados.forEach(cliente -> registroArea.appendText(cliente.toString() + "\n"));
        }
    }
}