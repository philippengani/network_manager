package FindBts;

import FindBts.tableClasses.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.ObjectExpression;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Account implements Initializable{
    public ComboBox<String> txtStatus;
    private ObservableList<String> txt= FXCollections.observableArrayList("simple user","admin");
    public JFXButton addBtn,btnList,btnAdd,btnDel,btnUp;
    public JFXTextField txtName,txtSurname,txtPassword,txtEmail;
    public Label lblerror;
    public Pane listPane,addPane;
    private ObservableList<User> data;
    public TableView<User> tableUsers;
    @FXML
    private TableColumn<User, String> col_id,col_name,col_surname,col_email,col_password,col_status,col_select;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtStatus.setItems(txt);
        listPane.toFront();
        addPane.toBack();
        tableUsers.setEditable(true);
        initTable();
        loadData();
    }
    public void handleButton(ActionEvent event){
        if (event.getSource()==addBtn){
            txtStatus.setItems(txt);
            String status = txtStatus.getValue();
            String name=txtName.getText();
            String surname=txtSurname.getText();
            String email = txtEmail.getText();
            String password = txtPassword.getText();
            insertUser(name,surname,email,password,status);
            emptyFields();
        }
        else if (event.getSource()==btnList){
            loadData();
            listPane.toFront();
        }
        else if (event.getSource()==btnAdd) addPane.toFront();
        else if(event.getSource()==btnDel) userAction("delete");
        else if(event.getSource()==btnUp) userAction("update");
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
        editableCols();
    }
    // This one is used to make the columns editable
    private void editableCols(){
        col_name.setCellFactory(TextFieldTableCell.forTableColumn());
        col_name.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setName(e.getNewValue());
        });
        col_surname.setCellFactory(TextFieldTableCell.forTableColumn());
        col_surname.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setName(e.getNewValue());
        });
        col_email.setCellFactory(TextFieldTableCell.forTableColumn());
        col_email.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setName(e.getNewValue());
        });
        col_password.setCellFactory(TextFieldTableCell.forTableColumn());
        col_password.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setName(e.getNewValue());
        });

    }
    private void loadData(){
         data = FXCollections.observableArrayList();
        Connecti.connect();
        String sql="SELECT * FROM `user` WHERE 1";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet res = pst.executeQuery();
            while (res.next()){
                data.add(new User(res.getInt(1),res.getString(2),res.getString(3),
                        res.getString(4),res.getString(5),res.getString(6),
                        new CheckBox()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableUsers.setItems(data);
    }

    private void userAction(String action) {
        // To delete, we get the selected row from the data
        ObservableList<User> dataList = FXCollections.observableArrayList();
        if (action.equals("delete")){
            for(User user:data){
                if (user.getSelect().isSelected()){
                    dataList.add(user);
                    removeUser(user.getId());
                }
            }
            data.removeAll(dataList);
        }
        else if(action.equals("update")){
            for(User user:data){
                dataList.add(user);
                updateUsers(user);
            }

        }


    }
    //This will remove the user from the database
    private void removeUser(int id){
        Connecti.connect();
        String sql = "DELETE FROM `user` WHERE id=?";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1,id);
            int r = pst.executeUpdate();
            if (r==0){
                showLog("Deleting ","error");
            }
            else{
                showLog("Deleting ","success");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void updateUsers(User user){
        Connecti.connect();
        String sql = "UPDATE `user` SET `id`=?,`name`=?,`surname`=?," +
                "`email`=?,`password`=?,`status`=? WHERE id=?";
        try {
            PreparedStatement pst=conn.prepareStatement(sql);
            pst.setInt(1,user.getId());
            pst.setString(2,user.getName());
            pst.setString(3,user.getSurname());
            pst.setString(4,user.getEmail());
            pst.setString(5,user.getPassword());
            pst.setString(6,user.getStatus());
            pst.setInt(7,user.getId());
            int resultSet =pst.executeUpdate();
            if (resultSet==0){
                showLog("Updated ","error");
            }
            else{
                showLog("Updated ","success");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void showLog(String mes,String res){
        if (res.equals("error")){
            lblerror.setTextFill(Color.TOMATO);
            lblerror.setText("Error" +mes +"user");
            System.out.println("error deleting");
        }
        else{
            lblerror.setTextFill(Color.GREEN);
            lblerror.setText(mes +"successfully");
            System.out.println("Updated");
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
