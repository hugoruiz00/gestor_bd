package mostrartablas;

import actividadtercercorte.Connector;
import actividadtercercorte.FXMLDocumentController;
import static actividadtercercorte.FXMLDocumentController.escrib;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Hugo Ruiz
 */
public class ControllerTabla implements Initializable {
    @FXML TableView<Consulta> tabla;
    @FXML TextField consulta;
    @FXML TextField actual;
    @FXML GridPane grid;
    @FXML Button cerrarSesion;
    @FXML Label nombreBD;
    Button[] btn;
    Connection conn;
    int tamanio;
    
    @FXML
    private void crearBotones() throws IOException {
        tamanio = 0;
        String nameBD = leerArchivo("C:\\Users\\Hugo Ruiz\\Documents\\NetBeansProjects\\ActividadTercerCorte\\src\\actividadtercercorte\\datos.txt");
        nombreBD.setText(nameBD);
        btn = new Button[15];
        int posicion = 0;
        int posBtn = 0;
        conn = FXMLDocumentController.conn();//FXMLDocumentController.conn();            
        String query = "show full tables from " + nameBD;
        Statement st;
        ResultSet rs;
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                System.out.println(rs.getString("Tables_in_" + nameBD));
                btn[posBtn] = new Button(rs.getString("Tables_in_" + nameBD));
                btn[posBtn].setMinSize(130, 40);
                btn[posBtn].setTextFill(Color.WHITE);
                btn[posBtn].setStyle("-fx-background-color: #2c44cc;");
                btn[posBtn].setFont(Font.font(14));
                //grid.add(btn[posBtn++], 0, posicion++);
                grid.addRow(posicion++, btn[posBtn++]);
                tamanio++;
            }
        } catch (Exception e) {
            System.out.println("Error");
        }

        for (int x = 0; x < tamanio; x++) {
            btn[x].setOnAction(new EventHandler<ActionEvent>() {
                @Override               
                public void handle(ActionEvent event) {   
                    tabla.getItems().clear();
                    consulta.clear();
                    tabla.getColumns().clear();
                    String value = ((Button) event.getSource()).getText();
                    actual.setText(value);
                    TableColumn[] columnas = new TableColumn[20];                   
                    int posicion = 0;
                    int posCol = 0;                    
                    String nameBD="";
                    String columnsFile="C:\\Users\\Hugo Ruiz\\Documents\\NetBeansProjects\\ActividadTercerCorte\\src\\mostrartablas\\columnas.txt";  
                    try {
                        nameBD = leerArchivo("C:\\Users\\Hugo Ruiz\\Documents\\NetBeansProjects\\ActividadTercerCorte\\src\\actividadtercercorte\\datos.txt");
                    } catch (IOException ex) {
                        Logger.getLogger(ControllerTabla.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    conn = FXMLDocumentController.conn();//FXMLDocumentController.conn();            
                    String query = "show columns from " +nameBD +"."+value;
                    Statement st;
                    ResultSet rs;
                    try {
                        TableColumn nombre = new TableColumn("Nombre");
                        st = conn.createStatement();
                        rs = st.executeQuery(query);                      
                        int num=0;
                        escrib();
                        while (rs.next()) {
                            String atributo="c";
                            atributo=atributo+String.valueOf(++num);
                            System.out.println(atributo);
                            System.out.println(rs.getString("Field"));
                            escribirTexto(columnsFile,rs.getString("Field"));
                            columnas[posCol] = new TableColumn(rs.getString("Field"));
                            columnas[posCol].setPrefWidth(100);
                            columnas[posCol].setCellValueFactory(new PropertyValueFactory(atributo));
                            tabla.getColumns().add(posicion++, columnas[posCol++]);
                        }
                    } catch (Exception e) {
                        System.out.println("Error");
                    }
                }
            });
        }
    }

    public static String leerArchivo(String nombreArchivo) throws FileNotFoundException, IOException {
        String texto;
        String nameBD = "";
        FileReader archivo = new FileReader(nombreArchivo);//Busca el archivo
        BufferedReader contenedor = new BufferedReader(archivo);//Almacenar el contenido del archivo
        while ((texto = contenedor.readLine()) != null) {//Leer linea por linea
            nameBD = texto;//Imprimir linea
        }
        contenedor.close();
        return nameBD;
    }
    
    public static String leerColumnas(String nombreArchivo) throws FileNotFoundException, IOException {
        String texto;
        String columnNames = "";
        FileReader archivo = new FileReader(nombreArchivo);//Busca el archivo
        BufferedReader contenedor = new BufferedReader(archivo);//Almacenar el contenido del archivo
        while ((texto = contenedor.readLine()) != null) {//Leer linea por linea
            columnNames += texto+",";//Imprimir linea
        }
        contenedor.close();
        return columnNames;
    }
    
    @FXML
    public void consultas() throws IOException{
        String columnsFile="C:\\Users\\Hugo Ruiz\\Documents\\NetBeansProjects\\ActividadTercerCorte\\src\\mostrartablas\\columnas.txt";  
        ObservableList<Consulta> producList = FXCollections.observableArrayList();      
        String[] nombreColumnas = leerColumnas(columnsFile).split(",");
        System.out.println(leerColumnas(columnsFile));       
        System.out.println(nombreColumnas.length);          
        String query = consulta.getText().toUpperCase();    
        String[] evaluacion = query.split(" ");
        System.out.println(query); 
        
        if(evaluacion[0].equals("SELECT")){ 
            if(evaluacion[1].equals("*")){
                System.out.println("select");   
                sentenciaSelect(nombreColumnas,query,producList);
            }else{
                String[] aux = consulta.getText().split(" ");
                String[] nombres = aux[1].split(",");
                selectName(nombres,query,producList);
            }           
        }
        
        else if(evaluacion[0].equals("DELETE")){
            System.out.println("delete");   
            executeQuery(query);
            sentenciaSelect(nombreColumnas,"Select * from "+evaluacion[2],producList);
        }
        else if(evaluacion[0].equals("UPDATE")){ 
            executeQuery(consulta.getText());
            System.out.println("update");   
            System.out.println(evaluacion[1]);   
            sentenciaSelect(nombreColumnas,"Select * from "+evaluacion[1],producList);
        }
         else if(evaluacion[0].equals("INSERT")){ 
            executeQuery(consulta.getText());
            System.out.println("insert");             
            sentenciaSelect(nombreColumnas,"Select * from "+evaluacion[2],producList);
        }
        else{
            System.out.println("null");
            Alert dialogo = new Alert(Alert.AlertType.INFORMATION);
            dialogo.setTitle("ERROR");
            dialogo.setHeaderText(null);
            dialogo.setContentText("Verifique su consulta");
            dialogo.initStyle(StageStyle.UTILITY);
            dialogo.showAndWait();
        }
    }
    
    public void sentenciaSelect(String[] nombreColumnas, String query,ObservableList<Consulta> producList){
        conn = FXMLDocumentController.conn();
        Statement st;
        ResultSet rs;
        try {         
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Consulta consul;           
            while(rs.next()){
                System.out.println("while select");   
                int num=1;
                 System.out.println(nombreColumnas.length);  
                if(nombreColumnas.length==1){
                    consul = new Consulta(rs.getObject(num++));
                    producList.add(consul);
                }
                else if(nombreColumnas.length==2){
                    consul = new Consulta(rs.getObject(num++),rs.getObject(num++));
                    producList.add(consul);
                }
                else if(nombreColumnas.length==3){
                    consul = new Consulta(rs.getObject(num++),rs.getObject(num++),rs.getObject(num++));
                    producList.add(consul);
                }
                else if(nombreColumnas.length==4){
                    consul = new Consulta(rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++));
                    producList.add(consul);
                }
                else if(nombreColumnas.length==5){
                    consul = new Consulta(rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++));
                    producList.add(consul);
                }
                else if(nombreColumnas.length==6){
                    consul = new Consulta(rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++));
                    producList.add(consul);
                }
                else if(nombreColumnas.length==7){
                    consul = new Consulta(rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++));
                    producList.add(consul);
                }
                else if(nombreColumnas.length==8){
                    consul = new Consulta(rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++));
                    producList.add(consul);
                }
                else if(nombreColumnas.length==9){
                    consul = new Consulta(rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++));
                    producList.add(consul);
                }
                else if(nombreColumnas.length==10){
                    consul = new Consulta(rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++));
                    producList.add(consul);
                }            
            }
            tabla.setItems(producList);
        }catch(Exception e) {
            tabla.getColumns().clear();
            Alert dialogo = new Alert(Alert.AlertType.INFORMATION);
            dialogo.setTitle("ERROR");
            dialogo.setHeaderText(null);
            dialogo.setContentText("Verifique su consulta");
            dialogo.initStyle(StageStyle.UTILITY);
            dialogo.showAndWait();
        }
    }
    
    public void selectName(String[] nombreColumnas, String query,ObservableList<Consulta> producList){
        TableColumn[] columnas = new TableColumn[20];  
        tabla.getColumns().clear();
        conn = FXMLDocumentController.conn();
        Statement st;
        ResultSet rs;
        try {   
            int posicion=0;
            int posCol=0;
            for(int x=0;x<nombreColumnas.length;x++){
                String auxil="c";
                auxil+=String.valueOf(x+1);
                columnas[x] = new TableColumn(nombreColumnas[x]);
                columnas[x].setPrefWidth(100);
                columnas[x].setCellValueFactory(new PropertyValueFactory(auxil));
                tabla.getColumns().add(posCol++, columnas[x]);
            }
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Consulta consul;             
            while(rs.next()){                                     
                System.out.println("while select "+nombreColumnas[0]);   
                int num=1;
                if(nombreColumnas.length==1){
                    consul = new Consulta(rs.getObject(nombreColumnas[0]));
                    producList.add(consul);
                }
                else if(nombreColumnas.length==2){
                    consul = new Consulta(rs.getObject(nombreColumnas[0]),rs.getObject(nombreColumnas[1]));
                    producList.add(consul);
                }
                else if(nombreColumnas.length==3){
                    consul = new Consulta(rs.getObject(num++),rs.getObject(num++),rs.getObject(num++));
                    producList.add(consul);
                }
                else if(nombreColumnas.length==4){
                    consul = new Consulta(rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++));
                    producList.add(consul);
                }
                else if(nombreColumnas.length==5){
                    consul = new Consulta(rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++),rs.getObject(num++));
                    producList.add(consul);
                }        
            }
            tabla.setItems(producList);          
        }catch(Exception e) {
            tabla.getColumns().clear();
            Alert dialogo = new Alert(Alert.AlertType.INFORMATION);
            dialogo.setTitle("ERROR");
            dialogo.setHeaderText(null);
            dialogo.setContentText("Verifique su consulta.");
            dialogo.initStyle(StageStyle.UTILITY);
            dialogo.showAndWait();          
        }
    }
    
     public static void escribirTexto(String nombre, String nuevas)throws FileNotFoundException,IOException
    {
        FileWriter escribir;
        PrintWriter escrib; 
        escribir = new FileWriter(nombre, true);
        escrib = new PrintWriter(escribir); 
        escrib.print(nuevas+" ");
        escrib.print("\n");
        escrib.close();    
    }
     
     public static void escrib() throws FileNotFoundException, IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\Hugo Ruiz\\Documents\\NetBeansProjects\\ActividadTercerCorte\\src\\mostrartablas\\columnas.txt"));
        bw.write("");
        bw.close();
    }
     
    public void executeQuery(String query) {
        conn = FXMLDocumentController.conn();
        Statement st;
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Erro de exceu query");   
            Alert dialogo = new Alert(Alert.AlertType.INFORMATION);
            dialogo.setTitle("ERROR");
            dialogo.setHeaderText(null);
            dialogo.setContentText("Verifique su consulta");
            dialogo.initStyle(StageStyle.UTILITY);
            dialogo.showAndWait();
        }
    }
    
    @FXML
    private void cerrarSesion(ActionEvent event) throws IOException, SQLException {
        conn.close();     
        Parent parent = FXMLLoader.load(getClass().getResource("/actividadtercercorte/FXMLDocument.fxml"));            
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
        Stage stage2 = (Stage) cerrarSesion.getScene().getWindow();
        stage2.close();      
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            crearBotones();
        } catch (IOException ex) {
            Logger.getLogger(ControllerTabla.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
