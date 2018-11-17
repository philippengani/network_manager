package FindBts;

import FindBts.helpers.Calculations;
import FindBts.tableClasses.Adm;
import FindBts.tableClasses.Result;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static FindBts.Connecti.conn;

public class Search implements Initializable {

    public Button search;
    public Text text;
    public TextField latitude;
    public TextField longitude;
    public TextField capacity;
    public AnchorPane search_wrapper;


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

    private PreparedStatement pst=null;
    private ResultSet rs=null;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Connecti.connect();
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
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
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
                    port_needed= Calculations.fetchPortNumber(cap);
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Check this");
            alert.setHeaderText("Format error");
            alert.setContentText("Please enter the capacity in the format sizeMbps or sizeGbps\n"+"Example : 100Mbps" );
            alert.showAndWait();
        }
        return switch_needed+":"+parts;
    }
    private void displayAlert(int results){
        Alert alert =new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Search Results");
        alert.setHeaderText(null);
        alert.setContentText(""+results +" ADM have been found, check the result table" );
        alert.showAndWait();
    }



}
