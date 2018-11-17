package FindBts;

import FindBts.tableClasses.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import static FindBts.Connecti.conn;
import static FindBts.helpers.Dialog.showDialog;

import FindBts.helpers.Dialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Account implements Initializable{
    public ComboBox<String> txtStatus;
    private ObservableList<String> txt= FXCollections.observableArrayList("simple user","admin");
    public JFXButton addBtn,btnList,btnAdd;
    public JFXTextField txtName,txtSurname,txtPassword,txtEmail;
    public Pane listPane,addPane;

    public TableView<User> tableUsers;
    @FXML
    private TableColumn<User, String> col_id,col_name,col_surname,col_email,col_password,col_status,col_select;
    @FXML
    private TableColumn<User, Button> col_update;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtStatus.setItems(txt);
        listPane.toFront();
        addPane.toBack();
        initTable();
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
        else if (event.getSource()==btnList) listPane.toFront();
        else if (event.getSource()==btnAdd) addPane.toFront();
    }

    private void initTable(){
        initColumns();
    }
    private void initColumns(){

        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        col_select.setCellValueFactory(new PropertyValueFactory<>("select"));
        col_update.setCellValueFactory(new PropertyValueFactory<>("update"));


    }
    // This one is used to make the columns editable
    private void editableCols(){
        
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
