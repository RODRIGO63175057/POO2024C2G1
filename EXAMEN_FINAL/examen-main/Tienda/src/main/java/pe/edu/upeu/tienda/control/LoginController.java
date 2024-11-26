package pe.edu.upeu.tienda.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pe.edu.upeu.tienda.componente.StageManager;
import pe.edu.upeu.tienda.componente.Toast;
import pe.edu.upeu.tienda.dto.SessionManager;
import pe.edu.upeu.tienda.modelo.Usuario;
import pe.edu.upeu.tienda.servicio.UsuarioService;

import java.io.IOException;

@Component
public class LoginController {

    @Autowired
    UsuarioService us;

    @Autowired
     ApplicationContext context;

    @FXML
    TextField txtUsuario;
    @FXML
    PasswordField txtClave;
    @FXML
    Button btnIngresar;
    @FXML
    ComboBox<String> cmbTipoUsuario;
    @FXML
    private ImageView dogImage;

    // Imágenes del perro
    Image normalDogImage;
    Image lookAtUserImage;
    Image coverEyesImage;


    @FXML
    public void initialize() {
        cmbTipoUsuario.getItems().addAll("Administrador", "Cliente");

        // Cargar las imágenes del perro
        normalDogImage = new Image(getClass().getResource("/imagen/normal_dog.png").toString());
        lookAtUserImage = new Image(getClass().getResource("/imagen/look_at_user_dog.png").toString());
        coverEyesImage = new Image(getClass().getResource("/imagen/cover_eyes_dog.png").toString());

        // Establecer la imagen inicial del perro
        dogImage.setImage(normalDogImage);

        // Listener para cambios en el campo de usuario
        txtUsuario.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                dogImage.setImage(lookAtUserImage); // Mirando al usuario
            } else {
                dogImage.setImage(normalDogImage); // Estado normal
            }
        });

        // Listener para detectar si el campo de contraseña está enfocado
        txtClave.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                dogImage.setImage(coverEyesImage); // Tapándose los ojos
            } else {
                dogImage.setImage(normalDogImage); // Estado normal
            }
        });
    }

    @FXML
    public void login(ActionEvent event) throws IOException {
        // Validar que se haya seleccionado un tipo de usuario
        if (cmbTipoUsuario.getValue() == null) {
            Stage stage = (Stage) btnIngresar.getScene().getWindow();
            Toast.showToast(stage, "Debe seleccionar un tipo de usuario", 2000,0,0);
            return;
        }

        try {
            Usuario usu = us.loginUsuario(txtUsuario.getText(), new String(txtClave.getText()));
            System.out.println(usu.toString());
            System.out.println("esta  bien: " + usu.getClave());
            if (usu != null) {
                // Validar que el tipo de usuario coincida
                String tipoSeleccionado = cmbTipoUsuario.getValue();
                System.out.println("tipoSeleccionado: "+tipoSeleccionado.toString());
                String tipoUsuario = usu.getIdPerfil().getNombre();

                System.out.println("tipoUsuario: "+tipoUsuario);
                if (!tipoSeleccionado.equalsIgnoreCase(tipoUsuario)) {
                    Toast.showToast((Stage) btnIngresar.getScene().getWindow(), "El usuario no corresponde al perfil seleccionado", 2000,0,0);
                    return;
                }

                // Resto del código de inicio de sesión existente
                SessionManager.getInstance().setUserId(usu.getIdUsuario());
                SessionManager.getInstance().setUserName(usu.getUser());
                SessionManager.getInstance().setNombrePerfil(tipoUsuario);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/guimainfx.fxml"));
                System.out.println("confirmo");
                loader.setControllerFactory(context::getBean);
                Parent mainRoot = loader.load();
                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getBounds();
                Scene mainScene = new Scene(mainRoot, bounds.getWidth(), bounds.getHeight()-30);

                mainScene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getResource("/imagen/store.png").toExternalForm()));
                stage.setScene(mainScene);
                System.out.println("correcto");
                stage.setTitle("Pantalla Principal");
                stage.setX(bounds.getMinX());
                stage.setY(bounds.getMinY());
                stage.setResizable(true);
                StageManager.setPrimaryStage(stage);
                stage.setWidth(bounds.getWidth());
                stage.setHeight(bounds.getHeight());

                stage.show();
            } else {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                double width = stage.getWidth() * 2;
                double height = stage.getHeight() / 2;
                Toast.showToast(stage, "Credencial inválida, intente nuevamente", 2000, width, height);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}