import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Locale;
import java.util.ResourceBundle;

public class UIDemo extends Application {
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/fxdemo";
    private static final String DB_USER = "appuser";
    private static final String DB_PASSWORD = "password";
    private ResourceBundle bundle;
    private ComboBox<String> languageComboBox;
    private Locale locale;
    private Label label;
    private Button saveButton;
    private Label firstNameLabel;
    private Label lastNameLabel;
    private Label emailLabel;
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;


    @Override
    public void start(Stage primaryStage) {
        locale = new Locale("en", "US");
        bundle = ResourceBundle.getBundle("Bundle", locale);


        // Create UI components
        label = new Label(bundle.getString("language_label"));
        saveButton = new Button(bundle.getString("button"));
        saveButton.setOnAction(event -> saveUserData());
        languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("English", "Farsi", "Japanese");
        languageComboBox.setOnAction(event -> changeLanguage(primaryStage));

        firstNameLabel = new Label(bundle.getString("firstname_label"));
        lastNameLabel = new Label(bundle.getString("lastname_label"));
        emailLabel = new Label(bundle.getString("email_label"));

        firstNameField = new TextField();
        firstNameField.setPromptText(bundle.getString("firstname_placeholder"));
        lastNameField = new TextField();
        lastNameField.setPromptText(bundle.getString("lastname_placeholder"));
        emailField = new TextField();
        emailField.setPromptText(bundle.getString("email_placeholder"));

        // Layout
//        VBox root = new VBox(10);
//        root.getChildren().addAll(label, languageComboBox, firstNameLabel, firstNameField, lastNameLabel, lastNameField, emailLabel, emailField, saveButton);
        GridPane root = new GridPane();
        root.add(label, 0, 0);
        root.add(languageComboBox, 1, 0);
        root.add(firstNameLabel, 0, 1);
        root.add(firstNameField, 1, 1);
        root.add(lastNameLabel, 0, 2);
        root.add(lastNameField, 1, 2);
        root.add(emailLabel, 0, 3);
        root.add(emailField, 1, 3);
        root.add(saveButton, 1, 4);

        // Set up the scene
        Scene scene = new Scene(root, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle(bundle.getString("app_name"));
        primaryStage.show();
    }

    private void changeLanguage(Stage primaryStage) {
        String selectedLanguage = languageComboBox.getValue();

        switch (selectedLanguage) {
            case "Farsi":
                locale = new Locale("fa", "IR");
                break;
            case "Japanese":
                locale = new Locale("ja", "JP");
                break;
            case "English":
                locale = new Locale("en", "US");
                break;
            default:
                locale = new Locale("en", "US");
        }

        bundle = ResourceBundle.getBundle("Bundle", locale);

        // Update UI components
        updateUI(primaryStage);
    }

    private void updateUI(Stage primaryStage) {
        label.setText(bundle.getString("language_label"));
        saveButton.setText(bundle.getString("button"));
        firstNameLabel.setText(bundle.getString("firstname_label"));
        lastNameLabel.setText(bundle.getString("lastname_label"));
        emailLabel.setText(bundle.getString("email_label"));
        firstNameField.setPromptText(bundle.getString("firstname_placeholder"));
        lastNameField.setPromptText(bundle.getString("lastname_placeholder"));
        emailField.setPromptText(bundle.getString("email_placeholder"));
        primaryStage.setTitle(bundle.getString("app_name"));
        // Update other UI components as needed
    }
    private void saveUserData() {
        // Retrieve field values
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();

        // Construct INSERT statement based on the selected language
        String tableName;
        switch (languageComboBox.getValue()) {
            case "Farsi":
                tableName = "employee_fa";
                break;
            case "Japanese":
                tableName = "employee_ja";
                break;
            case "English":
            default:
                tableName = "employee_en";
                break;
        }

        // Establish database connection and execute INSERT statement
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO " + tableName + " (first_name, last_name, email) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, email);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle any potential database errors
        }
    }
}