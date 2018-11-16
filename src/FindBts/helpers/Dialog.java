package FindBts.helpers;

import javafx.scene.control.Alert;

public class Dialog {

    public static void showDialog(String t,String h, String co) {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(t);
        alert.setHeaderText(h);
        alert.setContentText(co);
        alert.showAndWait();
    }
}
