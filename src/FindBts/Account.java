package FindBts;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import static FindBts.Connecti.conn;
import static FindBts.helpers.Dialog.showDialog;

import FindBts.helpers.Dialog;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Account implements Initializable{
    public ComboBox<String> txtStatus;
    public ObservableList<String> txt= FXCollections.observableArrayList("Normal user","admin");
    public JFXButton addBtn;
    public JFXTextField txtName,txtSurname,txtPassword,txtEmail;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtStatus.setItems(txt);
    }
    public void handleButton(ActionEvent event){
        if (event.getSource()==addBtn){
            String status = txtStatus.getValue();
            String name=txtName.getText();
            String surname=txtSurname.getText();
            String email = txtEmail.getText();
            String password = txtPassword.getText();
            insertUser(name,surname,email,password,status);
            emptyFields();
        }
    }

    private void insertUser(String name, String surname, String email, String password, String status) {
        Connecti.connect();
        String sql ="INSERT INTO `user`(`name`, `surname`, `email`, `password`, `status`) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,surname);
            preparedStatement.setString(3,email);
            preparedStatement.setString(4,password);
            preparedStatement.setString(5,status);
            int resultSet=preparedStatement.executeUpdate();
            if (resultSet==0){
                showDialog("Error","Error message","Could not create user try later");
            }
            else {
                showDialog("Success",null,"User Created");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private void emptyFields(){
        txtName.setText("");
        txtSurname.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtStatus.setItems(txt);
    }
}
