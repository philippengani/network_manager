package FindBts;


import java.sql.Connection;
import java.sql.DriverManager;

public class Connecti {
    public static Connection conn=null;

    public static void connect(){
        String url="jdbc:mysql://localhost:3306/bts";
        String user="root";
        String password="";
        try{
            conn= DriverManager.getConnection(url,user,password);
            System.out.println("connected to database");
        }catch (Exception e){
            e.printStackTrace();

        }
    }
}
