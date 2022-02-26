package actividadtercercorte;

import java.sql.DriverManager;
import java.sql.Connection;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

public class Connector {
    public static Connection getConnection(String host, String bd, String passw, String user){
        Connection conn;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://"+host+"/"+bd+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatatimeCode=false&serverTimezone=UTC",user,passw);
            System.out.println("Conectado");
            return conn;
        }
        catch(Exception e){
            Alert dialogo = new Alert(Alert.AlertType.INFORMATION);
            dialogo.setTitle("ERROR");
            dialogo.setHeaderText(null);
            dialogo.setContentText("Error en la conexion");
            dialogo.initStyle(StageStyle.UTILITY);
            dialogo.showAndWait();
            return null;
        }
    }
}

