package pe.edu.upeu.tienda.control;

import jakarta.persistence.Entity;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.tienda.modelo.Cliente;
import pe.edu.upeu.tienda.modelo.Proveedor;
import pe.edu.upeu.tienda.repositorio.ClienteRepository;
import pe.edu.upeu.tienda.repositorio.ProveedorRepository;

import java.util.List;
import java.util.Optional;

@Component
public class ClienteController {
    @FXML
    public TextField txtDoc;
    @FXML
    public TextField txtRepLegal;
    @FXML
    public TextField txtNombres;
    @FXML
    public TextField txtDni;
    @FXML
    public TableView tableView;
    @FXML
    public TableColumn colDniRuc;
    @FXML
    public TableColumn colNombres;
    @FXML
    public TableColumn colRepLegal;
    @FXML
    public TableColumn colDoc;
    @Autowired
    ClienteRepository clienteRepository;
    private ObservableList<Cliente> clienteList;
    @FXML
    public void initialize() {
        configurarTabla();
        cargarDatos();
    }

    private void configurarTabla() {
        colDniRuc.setCellValueFactory(new PropertyValueFactory<>("dniruc"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colRepLegal.setCellValueFactory(new PropertyValueFactory<>("repLegal"));
        colDoc.setCellValueFactory(new PropertyValueFactory<>("tipoDocumento"));

        clienteList = FXCollections.observableArrayList();
        tableView.setItems(clienteList);
    }

    private void cargarDatos() {
        List<Cliente> clientes = clienteRepository.findAll();
        System.out.println(clientes);
        clienteList.setAll(clientes);
    }
    @FXML
    public void validarFormulario(ActionEvent actionEvent) {

        String dniruc = txtDni.getText().trim();
        String  nombres= txtNombres.getText().trim();
        String repLegal = txtRepLegal.getText().trim();
        String tipoDocumento = txtDoc.getText().trim();


        if (dniruc.isEmpty() || nombres.isEmpty() || repLegal.isEmpty() || tipoDocumento.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        try {
            Cliente cliente = new Cliente();
            cliente.setDniruc(dniruc);
            cliente.setNombres(nombres);
            cliente.setRepLegal(repLegal);
            cliente.setTipoDocumento(tipoDocumento);


            // Guarda en la base de datos
            clienteRepository.save(cliente);
            cargarDatos();

            limpiarFormulario();
            mostrarAlerta("Éxito", "Proveedor guardado correctamente.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al guardar el proveedor: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void cancelarFormulario(ActionEvent actionEvent) {
        limpiarFormulario();
    }
    @FXML
    public void editarFormulario(ActionEvent actionEvent) {
        Cliente clienteSeleccionado = (Cliente) tableView.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado == null) {
            mostrarAlerta("Error", "Selecciona un proveedor para editar.", Alert.AlertType.ERROR);
            return;
        }

        try {
            clienteSeleccionado.setDniruc(txtDni.getText().trim());
            clienteSeleccionado.setNombres(txtNombres.getText().trim());
            clienteSeleccionado.setRepLegal(txtRepLegal.getText().trim());
            clienteSeleccionado.setTipoDocumento(txtDoc.getText().trim());


            // Actualiza en la base de datos
            clienteRepository.save(clienteSeleccionado);

            cargarDatos();
            limpiarFormulario();
            mostrarAlerta("Éxito", "Proveedor editado correctamente.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al editar el proveedor: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    @FXML
    public void eliminarFormulario(ActionEvent actionEvent) {
        Cliente clienteSeleccionado = (Cliente) tableView.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado == null) {
            mostrarAlerta("Error", "Selecciona un proveedor para eliminar.", Alert.AlertType.ERROR);
            return;
        }

        clienteRepository.delete(clienteSeleccionado);
        cargarDatos();
        mostrarAlerta("Éxito", "Proveedor eliminado correctamente.", Alert.AlertType.INFORMATION);
    }
    private void limpiarFormulario() {
        txtDni.clear();
        txtNombres.clear();
        txtRepLegal.clear();
        txtDoc.clear();

    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
