package pe.edu.upeu.ventafx.control;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtClave;

    @FXML
    private void login() {
        String usuario = txtUsuario.getText();
        String clave = txtClave.getText();

        if (usuario.isEmpty() || clave.isEmpty()) {
            showAlert("Error", "Por favor, complete todos los campos.");
            return;
        }

        try {
            // Conectar a la base de datos
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CustomerDB", "usuario", "contraseña");

            // Consulta SQL para verificar las credenciales
            String sql = "SELECT * FROM customers WHERE email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, usuario);
            statement.setString(2, clave);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Login exitoso
                showAlert("Éxito", "Inicio de sesión exitoso.");
                // Aquí puedes redirigir al usuario a la siguiente pantalla
            } else {
                // Credenciales incorrectas
                showAlert("Error", "Credenciales incorrectas. Intenta nuevamente.");
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Ocurrió un error al intentar iniciar sesión.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}