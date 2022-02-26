package actividadtercercorte;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/**
 *
 * @author Hugo Ruiz
 */
public class FXMLDocumentController implements Initializable {
    @FXML private Button conectar;
    @FXML
    private TextField pass;
    @FXML
    private TextField host;
    @FXML
    private TextField user;
    @FXML
    private TextField bd;
    private static Connection conn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void irProductos(ActionEvent event) throws IOException {
        conn = Connector.getConnection(host.getText(), bd.getText(), pass.getText(), user.getText());
        escrib(bd.getText());
        if(conn!=null){
            Parent parent = FXMLLoader.load(getClass().getResource("/mostrartablas/tablas.fxml"));            
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
            Stage stage2 = (Stage) conectar.getScene().getWindow();
            stage2.close(); 
           
        }
    }
    
    public static void escrib(String cadena) throws FileNotFoundException, IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\Hugo Ruiz\\Documents\\NetBeansProjects\\ActividadTercerCorte\\src\\actividadtercercorte\\datos.txt"));
        bw.write(cadena);
        bw.close();
    }
    
    public static Connection conn(){
        return conn;
    }    
}
