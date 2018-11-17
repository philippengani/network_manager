package FindBts;

import FindBts.helpers.Calculations;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import static FindBts.Connecti.conn;


import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class Home implements Initializable {
    public String title="Parameters of the eNodeB ";
    public String title1="Parameters of the switch  ";
    public JFXButton enodeb_tab;
    public JFXButton switch_tab,cswitch_tab,hq_tab;
    public JFXButton e1;
    public JFXButton e2;
    public JFXButton e3;
    public JFXButton e4;
    public JFXButton e5;
    public JFXButton e6;
    public JFXButton e7;
    public JFXButton e8;
    public JFXButton e9;
    public JFXButton e10;
    public JFXButton e11;
    public JFXButton e12;
    public JFXButton e13;
    public JFXButton e14;
    public JFXButton e15,e16,e17,e18,e19,e20,e21,e22,e23,e24,e25;
    public JFXButton s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s11,s12,s13;
    public JFXButton cs1,cs2,cs3,cs4,cs5,cs6,cs7,cs8,cs9,cs10,cs11,cs12,cs13;
    public JFXButton h1,h2,h3,h4;
    public Label paramTitle,paramTitle1,paramTitle2,paramTitle3 ;
    public Label eLat;
    public Label eLon;
    public Label eCap;
    public Label eAdmSdh;
    public Label sLat,sLon,sCap,sAdm,sInt,sNode,eInt,csLat,csLon,csCap,csAdm,csInt,hLat,hLon,hCap,hInt,hAdm;
    @FXML
    private String[] loc,lat,lon,admSdh,cap;
    @FXML
    private String[] sNam,sLa,sLo,sAd,sCa,csNam,csLa,csLo,csAd,csIn,csPo;


    private PreparedStatement pst=null;
    private ResultSet res=null;
    @FXML
    private Pane enodeb_pane,switch_pane,cswitch_pane,hq_pane;
    @FXML
    private AnchorPane param,param1,param2,param3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadENames();
        param.setVisible(false);
        param1.setVisible(false);
        param2.setVisible(false);
        param3.setVisible(false);
        enodeb_pane.toFront();
        enodeb_tab.setButtonType(JFXButton.ButtonType.RAISED);

    }
    public void handleButton(javafx.event.ActionEvent event){
        if(event.getSource()==enodeb_tab){
            System.out.println("enode");
            enodeb_tab.setButtonType(JFXButton.ButtonType.RAISED);
            switch_tab.setButtonType(JFXButton.ButtonType.FLAT);
            enodeb_pane.toFront();
        }

        else if (event.getSource()==switch_tab){
            loadSwitch();
            System.out.println("switch");
            switch_tab.setButtonType(JFXButton.ButtonType.RAISED);
            enodeb_tab.setButtonType(JFXButton.ButtonType.FLAT);
            cswitch_tab.setButtonType(JFXButton.ButtonType.FLAT);
            hq_tab.setButtonType(JFXButton.ButtonType.FLAT);
            switch_pane.toFront();
        }
        else if (event.getSource()==cswitch_tab){
            loadCSwitch();
            System.out.println("crucial switch");
            cswitch_tab.setButtonType(JFXButton.ButtonType.RAISED);
            enodeb_tab.setButtonType(JFXButton.ButtonType.FLAT);
            switch_tab.setButtonType(JFXButton.ButtonType.FLAT);
            hq_tab.setButtonType(JFXButton.ButtonType.FLAT);

            cswitch_pane.toFront();
        }
        else if (event.getSource()==hq_tab){
            loadHQ();
            System.out.println("HQ");
            hq_tab.setButtonType(JFXButton.ButtonType.RAISED);
            enodeb_tab.setButtonType(JFXButton.ButtonType.FLAT);
            switch_tab.setButtonType(JFXButton.ButtonType.FLAT);
            cswitch_tab.setButtonType(JFXButton.ButtonType.FLAT);
            hq_pane.toFront();
        }

    }

    public void handleEnodeb(ActionEvent event){
        String t="enodeb";  //Shows that I am on the enode b tab
        param.setVisible(false);
        Connecti.connect();
        ArrayList<String> lo1=new ArrayList<String>();
        ArrayList<String> la=new ArrayList<>();
        ArrayList<String> lo=new ArrayList<String>();
        ArrayList<String> ca=new ArrayList<>();
        ArrayList<String> admSd=new ArrayList<String>();
        try {

            pst=conn.prepareStatement("SELECT `enodeb_location`, `enodeb_latitude`, `enodeb_longitude`," +
                    " `enodeb_capacity`, `adm-sdh_location`, `adm-sdh_capacity` FROM `enodeb_switch` WHERE `switch_name`='"+"'");
            res=pst.executeQuery();
            while (res.next()){
                lo1.add(res.getString(1));
                la.add(Double.toString(res.getDouble(2)));
                lo.add(Double.toString(res.getDouble(3)));
                ca.add(Double.toString(res.getDouble(4)));
                admSd.add(res.getString(5));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

         loc= lo1.toArray(new String[lo1.size()]);
         lat= la.toArray(new String[la.size()]);
         lon= lo.toArray(new String[lo.size()]);
         cap= ca.toArray(new String[ca.size()]);
         admSdh= admSd.toArray(new String[admSd.size()]);
//        System.out.println(lo1.size());

        if(event.getSource()==e1){ showParams(8,t); }
       else if(event.getSource()==e2){ showParams(9,t); }
       else if(event.getSource()==e3){
            showParams(10,t);
        }
       else if(event.getSource()==e4){
            showParams(11,t);
       }
       else if(event.getSource()==e5){
            showParams(12,t);
       }
       else if(event.getSource()==e6){
            showParams(13,t);
       }
       else if(event.getSource()==e7){
            showParams(14,t);
       }
       else if(event.getSource()==e8){
            showParams(15,t);
       }
       else if(event.getSource()==e9){
            showParams(16,t);
       }
       else if(event.getSource()==e10){
            showParams(17,t);
       }
       else if(event.getSource()==e11){
            showParams(18,t);
       }
       else if(event.getSource()==e12){
            showParams(19,t);
       }
       else if(event.getSource()==e13){
            showParams(20,t);
       }
       else if(event.getSource()==e14){
            showParams(21,t);
       }
       else if(event.getSource()==e15){
            showParams(22,t);
       }
        else if(event.getSource()==e16){
            showParams(0,t);
        }
        else if(event.getSource()==e17){
            showParams(1,t);
        }
        else if(event.getSource()==e18){
            showParams(2,t);
        }
        else if(event.getSource()==e19){
            showParams(3,t);
        }
        else if(event.getSource()==e20){
            showParams(4,t);
        }
        else if(event.getSource()==e21){
            showParams(5,t);
        }
        else if(event.getSource()==e22){
            showParams(6,t);
        }
        else if(event.getSource()==e23){
            showParams(7,t);
        }
        else if(event.getSource()==e24){
            showParams(23,t);
        }
        else if(event.getSource()==e25){
            showParams(24,t);
        }



    }
    private void loadENames(){
       Connecti.connect();
        try {
            pst=conn.prepareStatement("SELECT `enodeb_location` FROM `enodeb_switch` WHERE `switch_name`='"+"'");
            res=pst.executeQuery();

            //Storing the content of the column row by row into an arrayList
            ArrayList<String> ar=new ArrayList<String>();

            while(res.next()){
                ar.add(res.getString(1));

            }

            //Parsing arraylist to an array of string
            String[] values=ar.toArray(new String[ar.size()]);
            System.out.println(ar.size());
            System.out.println(values[8]);
            //now set the values we got to the different button
            e16.setText(values[0]);
            e17.setText(values[1]);
            e18.setText(values[2]);
            e19.setText(values[3]);
            e20.setText(values[4]);
            e21.setText(values[5]);
            e22.setText(values[6]);
            e23.setText(values[7]);
            e1.setText(values[8]);
            e2.setText(values[9]);
            e3.setText(values[10]);
            e4.setText(values[11]);
            e5.setText(values[12]);
            e6.setText(values[13]);
            e7.setText(values[14]);
            e8.setText(values[15]);
            e9.setText(values[16]);
            e10.setText(values[17]);
            e11.setText(values[18]);
            e12.setText(values[19]);
            e13.setText(values[20]);
            e14.setText(values[21]);
            e15.setText(values[22]);
            e24.setText(values[23]);
            e25.setText(values[24]);



        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void handleSwitch(ActionEvent event){
        String t="switch";
        Connecti.connect();
        ArrayList<String> na=new ArrayList<String>();
        ArrayList<String> la=new ArrayList<>();
        ArrayList<String> lo=new ArrayList<>();
        ArrayList<String> ca=new ArrayList<String>();
        ArrayList<String> ad=new ArrayList<String>();
        ArrayList en=new ArrayList();
        try {
            pst = conn.prepareStatement("SELECT `name`, `latitude`, `longitude`, `aggregate_capacity` FROM `aggregating_switch` WHERE 1");
            res=pst.executeQuery();
            while (res.next()){
                na.add(res.getString(1));
                la.add(Double.toString(res.getDouble(2)));
                lo.add(Double.toString(res.getDouble(3)));
                ca.add(Double.toString(res.getDouble(4)));
                ad.add(res.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sNam=na.toArray(new String[na.size()]);
        sLa=la.toArray(new String[la.size()]);
        sLo=lo.toArray(new String[lo.size()]);
        sCa=ca.toArray(new String[ca.size()]);
        sAd=ad.toArray(new String[ad.size()]);


        if(event.getSource()==s1) showParams(0, t);
        else if(event.getSource()==s2)showParams(1,t);
        else if(event.getSource()==s3) showParams(2,t);
        else if(event.getSource()==s4) showParams(3,t);
        else if(event.getSource()==s5) showParams(4,t);
        else if(event.getSource()==s6) showParams(5,t);
        else if(event.getSource()==s7) showParams(6,t);
        else if(event.getSource()==s8) showParams(7,t);
        else if(event.getSource()==s9) showParams(8,t);
        else if(event.getSource()==s10) showParams(9,t);
        else if(event.getSource()==s11) showParams(10,t);
        else if(event.getSource()==s12) showParams(11,t);
        else if(event.getSource()==s13) showParams(12,t);


    }
    private void loadSwitch(){
        Connecti.connect();
        ArrayList<String> s=new ArrayList<String>();
        try {
            pst=conn.prepareStatement("SELECT `name` FROM `aggregating_switch` WHERE 1");
            res=pst.executeQuery();
            while (res.next()){
                s.add(res.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] sw= s.toArray(new String[s.size()]);
        s1.setText(sw[0]);
        s2.setText(sw[1]);
        s3.setText(sw[2]);
        s4.setText(sw[3]);
        s5.setText(sw[4]);
        s6.setText(sw[5]);
        s7.setText(sw[6]);
        s8.setText(sw[7]);
        s9.setText(sw[8]);
        s10.setText(sw[9]);
        s11.setText(sw[10]);
        s12.setText(sw[11]);
        s13.setText(sw[12]);

    }

    public void handleCSwitch(ActionEvent event){
        String t="cs" ;

        if(event.getSource()== cs1){
            showParams1(cs1.getText(),t);
            //System.out.println(cs1.getText()+"|");

        }
        else if(event.getSource()==cs2){
            showParams1(cs2.getText(),t);
        }
        else if(event.getSource()==cs3){
            showParams1(cs3.getText(),t);

        }
        else if(event.getSource()==cs4) showParams1(cs4.getText(),t);

        else if(event.getSource()==cs5) showParams1(cs5.getText(),t);


        else if(event.getSource()==cs6){showParams1(cs6.getText(),t);
        }
        else if(event.getSource()==cs7){showParams1(cs7.getText(),t);
        }
        else if(event.getSource()==cs8){showParams1(cs8.getText(),t);
        }
        else if(event.getSource()==cs9){showParams1(cs9.getText(),t);
        }
        else if(event.getSource()==cs10){showParams1(cs10.getText(),t);
        }else if(event.getSource()==cs11){showParams1(cs11.getText(),t);
        }
        else if(event.getSource()==cs12){showParams1(cs12.getText(),t);
        }
        else if(event.getSource()==cs13){showParams1(cs13.getText(),t);
        }
    }

    private void loadCSwitch(){
        Connecti.connect();
        ArrayList<String> s=new ArrayList<String>();
        try {
            pst=conn.prepareStatement("SELECT `name` FROM `crucial_switch` WHERE 1");
            res=pst.executeQuery();
            String a="";
            while (res.next()){


                if(!Objects.equals(a, res.getString(1))){
                        s.add(res.getString(1));
                }
                a=res.getString(1);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] sw= s.toArray(new String[s.size()]);
        cs1.setText(sw[0]);
        cs2.setText(sw[1]);
        cs3.setText(sw[2]);
        cs4.setText(sw[3]);
        cs5.setText(sw[4]);
        cs6.setText(sw[5]);
        cs7.setText(sw[6]);
        cs8.setText(sw[7]);
        cs9.setText(sw[8]);
        cs10.setText(sw[9]);

    }
    public void handleHQ( ActionEvent event){
        String t="hq" ;
        if(event.getSource()== h1) showParams1(h1.getText(),t);
        else if(event.getSource()==h2) showParams1(h2.getText(),t);
        else if(event.getSource()==h3) showParams1(h3.getText(),t);
        else if(event.getSource()==h4) showParams1(h4.getText(),t);

    }
    private void loadHQ(){
        ArrayList<String> h=new ArrayList<>();
        Connecti.connect();
        try {
            pst=conn.prepareStatement("SELECT `name` FROM `adm_sdh_hq` WHERE 1");
            res=pst.executeQuery();
            String a="";
            while (res.next()){
                if(!Objects.equals(a,res.getString(1))){
                    h.add(res.getString(1));
                }
                a=res.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] hq= h.toArray(new String[h.size()]);
        h1.setText(hq[0]);
        h2.setText(hq[1]);
        h3.setText(hq[2]);
        h4.setText(hq[3]);
    }
    private void showParams(int position,String tab){

        if(Objects.equals(tab, "enodeb")){
            paramTitle.setText(title+loc[position]);
            eLat.setText(lat[position]);
            eLon.setText(lon[position]);
            eCap.setText(cap[position]+"Mbps");
            eInt.setText(intPort(Double.parseDouble(cap[position])));
            eAdmSdh.setText(admSdh[position]);
            param.setVisible(true);
            System.out.println(loc[position]);
        }

        else if(Objects.equals(tab,"switch")) {

            paramTitle1.setText(title1+sNam[position]);
            sLat.setText(sLa[position]);
            sLon.setText(sLo[position]);
            sCap.setText(sCa[position]);
            sCap.setText(sCa[position]);
            sAdm.setText(sAd[position]);

            //Set the interface and number of ports
            sInt.setText(intPort(Double.parseDouble(sCa[position])));
            sNode.setText(getNode(sNam[position]));
            //sInt.setText(sIn[position]);
            param1.setVisible(true);
            System.out.println(sNam[position]);

        }

    }
    private String intPort(Double cap){
        String intPo="N/A";
        if (cap<500){
            int p= Calculations.fetchPortNumber(cap);
            intPo= p +" Fast-Ethernet port";
        }
        else {
            intPo =Calculations.gigaIntPort(cap);

        }
        return intPo;
    }
    private String getNode(String s){
        Connecti.connect();
        String a ="Switch "+s;
        int i=0;
        StringBuilder sw= new StringBuilder();
        try {
            pst=conn.prepareStatement("SELECT `enodeb_location` FROM `enodeb_switch` WHERE `switch_name`='"+a+"'");
            res=pst.executeQuery();
            while (res.next()){
                sw.append(res.getString(1)).append("\n");
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(sw);
        return i+" eNodeB: "+sw.toString();
    }
    private String getAdm(String t,String op ){
        String adm="";
        int i=0;
        StringBuilder sw= new StringBuilder();
        Connecti.connect();
        if(Objects.equals(op,"cs")){
            try {
                pst=conn.prepareStatement("SELECT `adm_sdh_aggre` FROM `crucial_switch`  WHERE `name`='"+t+"'");
                res=pst.executeQuery();
                while (res.next()){
                    sw.append(res.getString(1)).append("\n");
                    i++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(sw);
        }
        else if(Objects.equals(op,"hq")){
            try {
                pst=conn.prepareStatement("SELECT `adm_sdh` FROM `adm_sdh_hq` WHERE  `name`='"+t+"'");
                res=pst.executeQuery();
                while (res.next()){
                    sw.append(res.getString(1)).append("\n");
                    i++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(sw);
        }


        return i+" Adm-sdh: "+sw.toString();

    }
    private void showParams1(String text,String tab) {
        if (Objects.equals(tab, "cs")) {
            String op="cs";
            param2.setVisible(true);
            Connecti.connect();
            try {
                pst = conn.prepareStatement("SELECT `crucial-switch_name`, `crucial-switch_latitude`," +
                        " `crucial-switch_longitude`, `crucial-switch_capacity` FROM `enodeb_switch` WHERE `crucial-switch_name`='" + text + "'");
                res = pst.executeQuery();
                while (res.next()) {
                    paramTitle2.setText(res.getString(1));
                    csLat.setText(res.getString(2));
                    csLon.setText(res.getString(3));
                    csCap.setText(res.getString(4));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            csAdm.setText(getAdm(text,op));
            getIntPort(text,op);

        }
        else if (Objects.equals(tab, "hq")){
            String op="hq";
            param3.setVisible(true);
            Connecti.connect();
            try {
                pst=conn.prepareStatement("SELECT  `name`,`latitude`, `longitude`,`total_cap` FROM `adm_sdh_hq` WHERE `name`='"+text+"'");
                res=pst.executeQuery();
                while (res.next()){
                    paramTitle3.setText(res.getString(1));
                    hLat.setText(Double.toString(res.getDouble(2)));
                    hLon.setText(Double.toString(res.getDouble(3)));
                    hCap.setText(Double.toString(res.getDouble(4)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            hAdm.setText(getAdm(text,op));
            getIntPort(text,op);
        }
    }
    private void getIntPort(String t,String op){
        if(Objects.equals(op,"cs")){
            Connecti.connect();
            try {
                pst=conn.prepareStatement("SELECT `int`, `ports` FROM `crucial_switch` WHERE `name`='"+t+"'");
                res=pst.executeQuery();
                String a="";
                while (res.next()){
                    if(!Objects.equals(a, res.getString(1))){
                        csInt.setText(res.getString(1)+" "+res.getString(2));
                    }
                    a=res.getString(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if (Objects.equals(op,"hq")){
            Connecti.connect();
            try {
                pst=conn.prepareStatement("SELECT `interface`, `ports` FROM `adm_sdh_hq` WHERE `name`='"+t+"'");
                res=pst.executeQuery();
                String a="";
                while (res.next()){
                    if(!Objects.equals(a, res.getString(1))){
                        hInt.setText(res.getString(1)+" "+res.getString(2));
                    }
                    a=res.getString(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}