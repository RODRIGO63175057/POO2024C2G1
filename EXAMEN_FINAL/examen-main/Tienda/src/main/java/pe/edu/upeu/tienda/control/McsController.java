package pe.edu.upeu.tienda.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.tienda.dto.ComboBoxOption;
import pe.edu.upeu.tienda.modelo.Categoria;
import pe.edu.upeu.tienda.modelo.Marca;
import pe.edu.upeu.tienda.modelo.Categoria;
import pe.edu.upeu.tienda.modelo.UnidadMedida;
import pe.edu.upeu.tienda.servicio.CategoriaService;
import pe.edu.upeu.tienda.servicio.MarcaService;
import pe.edu.upeu.tienda.servicio.UnidadMedidaService;
import java.util.List;
@Component
public class McsController {
    @FXML
    private TextField txtCategoria;

    @FXML
    private TextField txtSubCategoria;

    @FXML
    private TextField txtMarca;

    @FXML
    private TableView<Categoria> tableView;

    @FXML
    private TableColumn<Categoria, String> colCategoria;

    @FXML
    private TableColumn<UnidadMedida, String> colSubCategoria;

    @FXML
    private TableColumn<Marca, String> colMarca;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private UnidadMedidaService subCategoriaService;

    @Autowired
    private MarcaService marcaService;

    private ObservableList<Categoria> categorias;

    @FXML
    public void initialize() {
        // Configurar columnas de la tabla
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colSubCategoria.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        // Cargar datos iniciales
        cargarDatos();
    }

    private void cargarDatos() {
        categorias = FXCollections.observableArrayList(categoriaService.list());
        tableView.setItems(categorias);
    }

    @FXML
    public void validarFormulario(ActionEvent event) {
        String categoria = txtCategoria.getText();
        String subCategoria = txtSubCategoria.getText();
        String marca = txtMarca.getText();

        if (!categoria.isEmpty()) {
            Categoria nuevaCategoria = new Categoria();
            nuevaCategoria.setNombre(categoria);
            categoriaService.save(nuevaCategoria);
        }

        if (!subCategoria.isEmpty()) {
            UnidadMedida nuevaSubCategoria = new UnidadMedida();
            nuevaSubCategoria.setNombreMedida(subCategoria);
            subCategoriaService.save(nuevaSubCategoria);
        }

        if (!marca.isEmpty()) {
            Marca nuevaMarca = new Marca();
            nuevaMarca.setNombre(marca);
            marcaService.save(nuevaMarca);
        }

        limpiarCampos();
        cargarDatos();
    }

    @FXML
    public void cancelarFormulario(ActionEvent event) {
        limpiarCampos();
    }

    @FXML
    public void editarFormulario(ActionEvent event) {
        Categoria seleccion = tableView.getSelectionModel().getSelectedItem();
        if (seleccion != null) {
            seleccion.setNombre(txtCategoria.getText());
            categoriaService.save(seleccion); // Actualizar en la base de datos
            cargarDatos();
            limpiarCampos();
        } else {
            mostrarAlerta("Debe seleccionar una categoría para editar.");
        }
    }

    @FXML
    public void eliminarFormulario(ActionEvent event) {
        Categoria seleccion = tableView.getSelectionModel().getSelectedItem();
        if (seleccion != null) {
            categoriaService.delete(seleccion.getIdCategoria());
            cargarDatos();
        } else {
            mostrarAlerta("Debe seleccionar una categoría para eliminar.");
        }
    }

    private void limpiarCampos() {
        txtCategoria.clear();
        txtSubCategoria.clear();
        txtMarca.clear();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
