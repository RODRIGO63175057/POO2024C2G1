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
import pe.edu.upeu.tienda.dto.RegistroVista;
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
    private TableView<RegistroVista> tableView;

    @FXML
    private TableColumn<RegistroVista, String> colCategoria;

    @FXML
    private TableColumn<RegistroVista, String> colSubCategoria;

    @FXML
    private TableColumn<RegistroVista, String> colMarca;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private UnidadMedidaService subCategoriaService;

    @Autowired
    private MarcaService marcaService;

    private ObservableList<RegistroVista> registros;

    @FXML
    public void initialize() {
        // Configurar columnas de la tabla
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colSubCategoria.setCellValueFactory(new PropertyValueFactory<>("subCategoria"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));

        // Cargar datos iniciales
        cargarDatos();
    }

    private void cargarDatos() {
        registros = FXCollections.observableArrayList();

        List<Categoria> categorias = categoriaService.list();
        List<UnidadMedida> subCategorias = subCategoriaService.list();
        List<Marca> marcas = marcaService.list();

        // Mezclar datos dinámicamente en la interfaz (sin afectar la base de datos)
        int maxSize = Math.max(Math.max(categorias.size(), subCategorias.size()), marcas.size());
        for (int i = 0; i < maxSize; i++) {
            String categoria = i < categorias.size() ? categorias.get(i).getNombre() : "";
            String subCategoria = i < subCategorias.size() ? subCategorias.get(i).getNombreMedida() : "";
            String marca = i < marcas.size() ? marcas.get(i).getNombre() : "";

            registros.add(new RegistroVista(categoria, subCategoria, marca));
        }

        tableView.setItems(registros);
    }

    @FXML
    public void validarFormulario(ActionEvent event) {
        String categoria = txtCategoria.getText();
        String subCategoria = txtSubCategoria.getText();
        String marca = txtMarca.getText();

        // Guardar en tablas independientes
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
        RegistroVista seleccion = tableView.getSelectionModel().getSelectedItem();

        if (seleccion != null) {
            // Editar la categoría
            if (!seleccion.getCategoria().isEmpty()) {
                Categoria categoria = categoriaService.repo.findByNombre(seleccion.getCategoria());
                if (categoria != null) {
                    categoria.setNombre(txtCategoria.getText());
                    categoriaService.save(categoria); // Actualizar en la base de datos
                }
            }

            // Editar la subcategoría
            if (!seleccion.getSubCategoria().isEmpty()) {
                UnidadMedida subCategoria = subCategoriaService.repo.findByNombreMedida(seleccion.getSubCategoria());
                if (subCategoria != null) {
                    subCategoria.setNombreMedida(txtSubCategoria.getText());
                    subCategoriaService.save(subCategoria); // Actualizar en la base de datos
                }
            }

            // Editar la marca
            if (!seleccion.getMarca().isEmpty()) {
                Marca marca = marcaService.repo.findByNombre(seleccion.getMarca());
                if (marca != null) {
                    marca.setNombre(txtMarca.getText());
                    marcaService.save(marca); // Actualizar en la base de datos
                }
            }

            // Actualizar la tabla
            cargarDatos();
            limpiarCampos();
        } else {
            mostrarAlerta("Debe seleccionar una fila para editar.");
        }
    }

    @FXML
    public void eliminarFormulario(ActionEvent event) {
        RegistroVista seleccion = tableView.getSelectionModel().getSelectedItem();
        if (seleccion != null) {
            // Eliminar de las tablas correspondents
            if (!seleccion.getCategoria().isEmpty()) {
                Categoria categoria = categoriaService.repo.findByNombre(seleccion.getCategoria());
                if (categoria != null) categoriaService.delete(categoria.getIdCategoria());
            }
            if (!seleccion.getSubCategoria().isEmpty()) {
                UnidadMedida subCategoria = subCategoriaService.repo.findByNombreMedida(seleccion.getSubCategoria());
                if (subCategoria != null) subCategoriaService.delete(subCategoria.getIdUnidad());
            }
            if (!seleccion.getMarca().isEmpty()) {
                Marca marca = marcaService.repo.findByNombre(seleccion.getMarca());
                if (marca != null) marcaService.delete(marca.getIdMarca());
            }

            cargarDatos();
        } else {
            mostrarAlerta("Debe seleccionar una fila para eliminar.");
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
