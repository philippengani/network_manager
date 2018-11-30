package FindBts;

import FindBts.helpers.Dialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static FindBts.Connecti.conn;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Import {
    public JFXButton browseEnoSw,browseAdm,browseCru,importCsv,goSoft;
    public JFXTextField EText,AText,CText;
    public AnchorPane pane;
    private PreparedStatement pst;
    private ResultSet res;


    public void handleButton(ActionEvent event){
        if (event.getSource() == browseEnoSw)fileBrowser("enode_sw");
        else if (event.getSource()==browseAdm) fileBrowser("adm");
        else if (event.getSource()==browseCru) fileBrowser("cru");
        else if (event.getSource()==importCsv) importData();
        else if (event.getSource()==goSoft){
            System.out.println("hmmmmmm");
            updateIni(event);

        }
    }
    private void updateIni(ActionEvent event){
        Connecti.connect();
        String sql = "UPDATE `software` SET `initialised`=? WHERE 1";
        try {
            pst = conn.prepareStatement(sql);
            pst.setInt(1,1);
            int res = pst.executeUpdate();
            if (res==0){
                System.out.println("error");
                Dialog.showDialog("Error","Error updating","Please contact the administrator");
            }
            else{
                try {
                    Node node = (Node) event.getSource();
                    Stage stage =(Stage)node.getScene().getWindow();
                    stage.close();
                    Scene scene =new Scene(FXMLLoader.load(getClass().getResource("fxml/FindBts.fxml")));
                    stage.setMaximized(true);
                    stage.setTitle("Import data");
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private void importData(){
        Connecti.connect();
        importEnodeSw();
        importAdm();
        importCru();
        Dialog.showDialog("Success","Imported successfully to database",null);
    }
    private void importEnodeSw(){
        int r=1;
        boolean a=true;
        String sql="INSERT INTO `enodeb_switch`(`id`, `enodeb_location`, `enodeb_latitude`, `enodeb_longitude`, `enodeb_capacity`," +
                " `switch_name`, `switch_capacity`, `switch_latitude`, `switch_longitude`, `adm-sdh_location`, `adm-sdh_latitude`," +
                " `adm-sdh_longitude`, `adm-sdh_capacity`, `crucial-switch_name`, `crucial-switch_latitude`, `crucial-switch_longitude`," +
                " `crucial-switch_capacity`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            pst=conn.prepareStatement(sql);
            FileInputStream fileIn = new FileInputStream(new File(EText.getText()));
            XSSFWorkbook wb = new XSSFWorkbook(fileIn);
            XSSFSheet sheet  =wb.getSheetAt(0);
            XSSFRow row;
            for(int i=1;i<=sheet.getLastRowNum();i++){
                row=sheet.getRow(i);
                pst.setInt(1,(int)row.getCell(0).getNumericCellValue());
                pst.setString(2, row.getCell(1).getStringCellValue());
                pst.setDouble(3, row.getCell(2).getNumericCellValue());
                pst.setDouble(4, row.getCell(3).getNumericCellValue());
                pst.setDouble(5, row.getCell(4).getNumericCellValue());
                pst.setString(6, row.getCell(5).getStringCellValue());
                pst.setDouble(7, row.getCell(6).getNumericCellValue());
                pst.setDouble(8, row.getCell(7).getNumericCellValue());
                pst.setDouble(9, row.getCell(8).getNumericCellValue());
                pst.setString(10, row.getCell(9).getStringCellValue());
                pst.setDouble(11, row.getCell(10).getNumericCellValue());
                pst.setDouble(12, row.getCell(11).getNumericCellValue());
                pst.setDouble(13, row.getCell(12).getNumericCellValue());
                pst.setString(14, row.getCell(13).getStringCellValue());
                pst.setDouble(15, row.getCell(14).getNumericCellValue());
                pst.setDouble(16, row.getCell(15).getNumericCellValue());
                pst.setDouble(17, row.getCell(16).getNumericCellValue());
                r =pst.executeUpdate();
                if (r==0){
                    a=false;
                }
            }
            fileIn.close();
            pst.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        if (!a){
            Dialog.showDialog("Error","Error importing","Contact Administrator");
        }
        else{
            System.out.println("Imported the enodeB successfully");
        }
    }
    private void importAdm(){
        String sql="INSERT INTO `adm`(`id`, `location`, `latitude`, `longitude`) VALUES (?,?,?,?)";

    }
    private void importCru(){
        String sql="INSERT INTO `crucial_switch`(`id`, `name`, `latitude`, `longitude`, `adm_sdh_aggre`, `adm_sdh_alat`, `adm_sdh_alon`," +
                " `adm_sdh_acap`, `cs_cap`, `adm-sdh_con`, `adm-sdh_ccap`, `int`, `ports`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }
    private void fileBrowser(String source){
        String path="";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File Dialog");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SQL", "*.sql"),
                new FileChooser.ExtensionFilter("CSV", "*.csv"),
                new FileChooser.ExtensionFilter("EXCEL", "*.xlsx")
        );
        Stage stage =(Stage)pane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file!=null){
            System.out.println("Path :"+file.getAbsolutePath());
             path = file.getAbsolutePath();
        }

        switch (source) {
            case "enode_sw":
                EText.setText(path);
                break;
            case "adm":
                AText.setText(path);
                break;
            case "cru":
                CText.setText(path);
                break;
        }


    }

}
