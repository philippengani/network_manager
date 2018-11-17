package FindBts;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static FindBts.Connecti.conn;

public class Login {
    public JFXButton btnSignin;
    public TextField txtEmail,txtPassword;
    public Label lblerrors;
    PreparedStatement pst;
    ResultSet res;
    public static String status=null;


    public void handleButton(ActionEvent event){
        if(event.getSource()==btnSignin){
            //Login
            if (logIn().equals("Success")){

                try {
                    Node node = (Node) event.getSource();
                    Stage stage =(Stage)node.getScene().getWindow();
                    stage.close();
                    Scene scene =new Scene(FXMLLoader.load(getClass().getResource("fxml/FindBts.fxml")));
                    stage.setMaximized(true);
                    stage.setTitle("Network Manager");
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private String logIn(){

        String email= txtEmail.getText();
        String password = txtPassword.getText();
        String sql = "SELECT * FROM user WHERE email=? and password=?";
        Connecti.connect();
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1,email);
            pst.setString(2,password);
            res=pst.executeQuery();
            if (!res.next()){
                lblerrors.setTextFill(Color.TOMATO);
                lblerrors.setText("Enter correct Email/Password");
                return "Error";
            }
            else{
                String s=res.getString(6);
                if (s.equals("simple user")){
                    status = "not admin";

                }
                else{
                    status="admin";
                }
                lblerrors.setTextFill(Color.GREEN);
                lblerrors.setText("Login Successful .. Redirecting...");
                return "Success";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Exception";
        }


    }

}
