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


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import static FindBts.Connecti.conn;
import static FindBts.Connecti.connect;

public class Controller implements Initializable {
    public JFXButton search_page;
    public JFXButton home_page;
    public JFXButton home_page1;
    public JFXButton search1,account;
    public Button search;
    public Text text;
    public TextField latitude;
    public TextField longitude;
    public TextField capacity;
    public TextField latitude1;
    public TextField longitude1;
    public TextField capacity1;
    public AnchorPane search_wrapper;
    public AnchorPane container;


    //used to display the content of the ADM table
    private ObservableList<Adm> data;
    public TableView<Adm> tableADM;
    public TableColumn<Object, Object> columnID;
    public TableColumn<Object, Object> columnLocation;
    public TableColumn<Object, Object> columnLatitude;
    public TableColumn<Object, Object> columnLongitude;

    public TableView<Result> tableResult;
    public TableColumn<Object,Object> columnResID;
    public TableColumn<Object,Object> columnResLocation;
    public TableColumn<Object,Object> columnResDistance;
    public TableColumn<Object,Object> columnResSwitch;
    public TableColumn<Object,Object> columnResInterface;
    public TableColumn<Object,Object> columnResPorts1;
    public TableColumn<Object,Object> columnResPorts2;

    public TableView<EnodeB> tableShow;
    public TableColumn<Object,Object> aggreSwitchName;
    public TableColumn<Object,Object> aggreSwitchCap;
    public TableColumn<Object,Object> aggreSwitchLat;
    public TableColumn<Object,Object> aggreSwitchLong;
    public TableColumn<Object,Object> admSdhLocation;
    public TableColumn<Object,Object> admSdhLat;
    public TableColumn<Object,Object> admSdhLong;
    public TableColumn<Object,Object> admSdhCap;
    public TableColumn<Object,Object> cSwitchName;
    public TableColumn<Object,Object> cSwitchLat;
    public TableColumn<Object,Object> cSwitchLong;
    public TableColumn<Object,Object> cSwitchCap;




    private PreparedStatement pst=null;
    public Statement st=null;
    private ResultSet rs=null;

    @FXML
    private Pane searchPane,homePane;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        if (event.getSource()==search_page){
            System.out.println("search ");
            AnchorPane pane = FXMLLoader.load(getClass().getResource("Search.fxml"));
            rootPane.getChildren().setAll(pane);

        }
        else  if (event.getSource()==home_page){
            System.out.println("home");
            AnchorPane pane = FXMLLoader.load(getClass().getResource("MainHome.fxml"));
            rootPane.getChildren().setAll(pane);

        }
        else if(event.getSource()==search1){
            System.out.println("display");
            searchHome();
        }
        else if(event.getSource()==home_page1){
             AnchorPane pane = FXMLLoader.load(getClass().getResource("home.fxml"));
            rootPane.getChildren().setAll(pane);
        }
        else if (event.getSource()==account){
            AnchorPane pane = FXMLLoader.load(getClass().getResource("Account.fxml"));
            rootPane.getChildren().setAll(pane);
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Connecti.connect();
        homePane.toBack();
        data = FXCollections.observableArrayList();
        setCellTable();
        loadData();

    }
    // now for displaying the adm table from the database
    private void setCellTable(){
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        columnLatitude.setCellValueFactory(new PropertyValueFactory<>("latitude"));
        columnLongitude.setCellValueFactory(new PropertyValueFactory<>("longitude"));
    }

    private void loadData(){
        try {
            pst=conn.prepareStatement("SELECT * FROM `adm` WHERE 1");
            rs=pst.executeQuery();
            while(rs.next()){
                data.add(new Adm(rs.getInt(1),rs.getString(2),rs.getDouble(3),rs.getDouble(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert=new Alert(AlertType.INFORMATION);
            alert.setTitle("Connection error");
            alert.setHeaderText("Cannot connect to database");
            alert.setContentText("Please contact the administrator of the application");
            alert.showAndWait();
        }
        tableADM.setItems(data);

    }
    //this is the havesine rule used to calculate distance between two geo coordinates

    private double measureDistance(double lat1, double lon1, double lat2, double lon2){
        //this is the havesine rule used to calculate distance between two geo coordinates
        double R =6371.0; //Earth's  radius
        double dLat=Math.toRadians(lat2-lat1);
        double dLon=Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2)* Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) *
                        Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2)*Math.sin(dLon/2);
        double c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        return R*c;
    }
    public void searchTabClicked(){
        search_page.setOpacity(0.5);
    }
    // set table for the results
    //Now for the search table

    private void setResultCellTable(){
        columnResID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnResLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        columnResDistance.setCellValueFactory(new PropertyValueFactory<>("distance"));
        columnResSwitch.setCellValueFactory(new PropertyValueFactory<>("switch_used"));
        columnResInterface.setCellValueFactory(new PropertyValueFactory<>("interface_needed"));
        columnResPorts1.setCellValueFactory(new PropertyValueFactory<>("gb_ports"));
        columnResPorts2.setCellValueFactory(new PropertyValueFactory<>("fe_ports"));
    }
    public void searchButtonClicked(){

        double lat1 =Double.parseDouble(latitude.getText());
        double lon1 =Double.parseDouble(longitude.getText());
        String capacity_needed = capacity.getText();
        double lat3=3.861645;
        double lon3=11.511191;
        text.setText(measureDistance(lat1,lon1,lat3,lon3)+" Km");
        Connecti.connect();
        ObservableList<Result> search_result = FXCollections.observableArrayList();
        setResultCellTable();
        String interface_port=switchNeeded(capacity_needed);
        //System.out.println(interface_port);
        String interface_needed = interface_port.split(":")[0];
        int gb_port_needed= Integer.parseInt(interface_port.split(":")[1]);
        int fe_port_needed= Integer.parseInt(interface_port.split(":")[2]);
        System.out.println(fe_port_needed);
        //System.out.println(port_needed);
        //This is where we search the distance between them
        try {
            int n=0;
            pst= conn.prepareStatement("SELECT * FROM `adm` WHERE 1");
            rs =pst.executeQuery();
            while (rs.next()){
                double lat2= rs.getDouble("latitude");
                double lon2= rs.getDouble("longitude");
                double distance = measureDistance(lat1,lon1,lat2,lon2);
                search_result.add(new Result(rs.getInt(1),rs.getString(2),
                        distance,"Intel switch",interface_needed,gb_port_needed,fe_port_needed));
                n++;
            }
            displayAlert(n);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableResult.setItems(search_result);

    }

    private String switchNeeded(String capacity){
        //boolean check = capacity.contains("Mbps");
        String switch_needed="";
        int port_needed=0;
        String parts="";
        if (capacity.contains("Mbps") || capacity.contains("Gbps")){
            if (capacity.contains("Mbps")){
                double cap = Double.parseDouble(capacity.replace("Mbps",""));
                if (cap<=500){
                    switch_needed = "Fast-Ethernet only";
                    port_needed=Calculations.fetchPortNumber(cap);
                     parts="0:"+port_needed;

                }else {
                    port_needed=1;
                    parts=port_needed+":0";
                    switch_needed ="Gigabit only";

                }
            }
            else{

                String aa=capacity.replace("Gbps","");
               // System.out.println("|"+aa+"|");
                double ab=Double.parseDouble(aa);
                //String ab=aa.split(".")[0]; //Take the whole number part of the number

                int p1=(int)ab;
                double t=ab%1;
                double r =Math.round(t*100)/100D;
                System.out.println(r);

               // String ac=aa.split(".")[1];
                if ((r*1000)>500){
                    parts=(p1+1)+":0";
                    switch_needed="Gigabit only";
                }
                else {
                    parts=p1+":"+Calculations.fetchPortNumber(r*1000);
                    if (r==0){
                        switch_needed="Gigabit only";

                    }
                    else{
                        switch_needed="Gigabit and Fast-Ethernet";

                    }
                }
               //double ca = Double.parseDouble(aa);
                /*if((ca%1)==0){
                    int a=(int)(ca/1);
                    port_needed = a;
                }else {
                    String an = (ca/1)+"";
                    System.out.println(an);
                    int a=(int)(ca/1);
                    port_needed =a+1;
                }*/
            }
        }
        else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Check this");
            alert.setHeaderText("Format error");
            alert.setContentText("Please enter the capacity in the format sizeMbps or sizeGbps\n"+"Example : 100Mbps" );
            alert.showAndWait();
        }
        return switch_needed+":"+parts;
    }
    private void displayAlert(int results){
        Alert alert =new Alert(AlertType.INFORMATION);
        alert.setTitle("Search Results");
        alert.setHeaderText(null);
        alert.setContentText(""+results +" ADM have been found, check the result table" );
        alert.showAndWait();
    }

/*    private int fetchPortNumber(double ca){
        int b=0;
        if((ca % 100)==0){
            int a=(int)(ca/100);
            if(a>5){
                b=5;
            }else {
                b=a;
            }
        }
        else {
            int a=(int)(ca/100);
            if(a!=5){
                b=a+1;
            }
            else {
                b=a;
            }
        }
        return b;
    }
*/

    private double extractCapacity(String cap){
        double capacity=0;
        if (cap.contains("Mbps")|| cap.contains("Gbps") ){
            if(cap.contains("Mbps")) capacity=Double.parseDouble(cap.replace("Mbps",""));
            else capacity=Double.parseDouble(cap.replace("Gbps",""));
        }
        else {
            Alert alert=new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Format Error");
            alert.setContentText("Please enter the capacity in the format sizeMbps or sizeGbps\n"+"Example : 100Mbps" );
            alert.showAndWait();
        }
        return capacity;
    }

    private void setShowCellTable(){

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
    private void searchHome(){
        double capacity=extractCapacity(capacity1.getText());
        double latitude=Double.parseDouble(latitude1.getText());
        double longitude=Double.parseDouble(longitude1.getText());
        Connecti.connect();
        ObservableList<EnodeB> show = FXCollections.observableArrayList();
        setShowCellTable();

        try {
            pst=conn.prepareStatement("SELECT * FROM enodeb_switch WHERE enodeb_latitude='"+latitude+"'and enodeb_longitude='"+longitude+"'");
            rs=pst.executeQuery();
            int n=0;
            while (rs.next()){
                show.add(new EnodeB(rs.getString(2),rs.getDouble(3),rs.getDouble(4),rs.getDouble(5),
                        rs.getString(6),rs.getDouble(7),rs.getDouble(8),rs.getDouble(9),
                        rs.getString(10), rs.getDouble(11),rs.getDouble(12),rs.getDouble(13),
                        rs.getString(14),rs.getDouble(15),rs.getDouble(16),rs.getDouble(17)));
           n++;
            }
            System.out.println(n);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableShow.setItems(show);


    }
}
