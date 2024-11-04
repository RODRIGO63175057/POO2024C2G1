package pe.edu.upeu.ventafx.control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label; // Asegúrate de importar Label
import pe.edu.upeu.ventafx.repositorio.UsuarioRepository;

import java.sql.Connection;

public class RegistroController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label actionTarget;
    private Button btnRegister;

    private UsuarioRepository usuarioRepository;

    @FXML
    protected void handleLogin() {

        actionTarget.setText("Login button pressed");
    }

    @FXML
    protected void handleRegister() {

        String name = nameField.getText();
        String surname = surnameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        // Aquí podrías validar los datos y registrar al usuario
        actionTarget.setText("Registration successful for " + name + " " + surname);
    }
    public RegistroController() {
        // Establece la conexión a la base de datos
        Connection connection;
    }
}