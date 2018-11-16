package FindBts;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import FindBts.helpers.Dialog;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import static FindBts.Connecti.conn;
import static FindBts.helpers.Dialog.showDialog;

import java.sql.*;






    public class MainHome {

        public JFXButton search1,delete;
        public TextField latitude1;
        public TextField longitude1;
        public TextField capacity1;




        public TableView<EnodeB> tableShow;
        public TableColumn<Object, Object> aggreSwitchName;
        public TableColumn<Object, Object> aggreSwitchCap;
        public TableColumn<Object, Object> aggreSwitchLat;
        public TableColumn<Object, Object> aggreSwitchLong;
        public TableColumn<Object, Object> admSdhLocation;
        public TableColumn<Object, Object> admSdhLat;
        public TableColumn<Object, Object> admSdhLong;
        public TableColumn<Object, Object> admSdhCap;
        public TableColumn<Object, Object> cSwitchName;
        public TableColumn<Object, Object> cSwitchLat;
        public TableColumn<Object, Object> cSwitchLong;
        public TableColumn<Object, Object> cSwitchCap;


        private PreparedStatement pst = null;
        public Statement st = null;
        private ResultSet rs = null;

        @FXML
        private Pane searchPane, homePane;
        @FXML
        private AnchorPane rootPane;
        @FXML
        private void handleButtonAction(ActionEvent event) throws IOException {
            if(event.getSource()==search1){
                System.out.println("display");
                searchHome();
            }
            else if(event.getSource()==delete) deleteInfo();
        }

        private double extractCapacity(String cap) {
            double capacity = 0;
            if (cap.contains("Mbps") || cap.contains("Gbps")) {
                if (cap.contains("Mbps")) capacity = Double.parseDouble(cap.replace("Mbps", ""));
                else capacity = Double.parseDouble(cap.replace("Gbps", ""));
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Format Error");
                alert.setContentText("Please enter the capacity in the format sizeMbps or sizeGbps\n" + "Example : 100Mbps");
                alert.showAndWait();
            }
            return capacity;
        }

        private void setShowCellTable() {

            aggreSwitchName.setCellValueFactory(new PropertyValueFactory<>("switch_name"));
            aggreSwitchCap.setCellValueFactory(new PropertyValueFactory<>("switch_capacity"));
            aggreSwitchLat.setCellValueFactory(new PropertyValueFactory<>("switch_latitude"));
            aggreSwitchLong.setCellValueFactory(new PropertyValueFactory<>("switch_longitude"));
            admSdhLocation.setCellValueFactory(new PropertyValueFactory<>("admSdh_location"));
            admSdhLat.setCellValueFactory(new PropertyValueFactory<>("admSdh_latitude"));
            admSdhLong.setCellValueFactory(new PropertyValueFactory<>("admSdh_longitude"));
            admSdhCap.setCellValueFactory(new PropertyValueFactory<>("admSdh_capacity"));
            cSwitchName.setCellValueFactory(new PropertyValueFactory<>("crucialSwitch_name"));
            cSwitchLat.setCellValueFactory(new PropertyValueFactory<>("crucialSwitch_latitude"));
            cSwitchLong.setCellValueFactory(new PropertyValueFactory<>("crucialSwitch_longitude"));
            cSwitchCap.setCellValueFactory(new PropertyValueFactory<>("crucialSwitch_capacity"));
        }

        private void searchHome() {
            double capacity = extractCapacity(capacity1.getText());
            double latitude = Double.parseDouble(latitude1.getText());
            double longitude = Double.parseDouble(longitude1.getText());
            Connecti.connect();
            ObservableList<EnodeB> show = FXCollections.observableArrayList();
            setShowCellTable();

            try {
                pst = conn.prepareStatement("SELECT * FROM enodeb_switch WHERE enodeb_latitude='" + latitude + "'and enodeb_longitude='" + longitude + "'");
                rs = pst.executeQuery();
                int n = 0;
                while (rs.next()) {
                    show.add(new EnodeB(rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5),
                            rs.getString(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9),
                            rs.getString(10), rs.getDouble(11), rs.getDouble(12), rs.getDouble(13),
                            rs.getString(14), rs.getDouble(15), rs.getDouble(16), rs.getDouble(17)));
                    n++;
                }
                if (n==0){
                    showDialog("Error",null,"No eNode was found please try another");
                }
                System.out.println(n);

            } catch (SQLException e) {
                e.printStackTrace();
            }
            tableShow.setItems(show);


        }

        private void deleteInfo(){
            int ro=0;
            double latitude = Double.parseDouble(latitude1.getText());
            double longitude = Double.parseDouble(longitude1.getText());
            Connecti.connect();
            String sql="DELETE FROM `enodeb_switch` WHERE enodeb_latitude=? and enodeb_longitude=?";
            try {
                pst =conn.prepareStatement(sql);
                pst.setDouble(1,latitude);
                pst.setDouble(2,longitude);
                ro=pst.executeUpdate();
                if (ro==0){
                    showDialog("Error","Error message","Could not delete information");
                }
                else{
                    System.out.println("Deleted");
                    showDialog("Success",null,"Information was deleted");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }
