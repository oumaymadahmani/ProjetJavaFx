package application;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;







public class Profile extends Application {
	public static Connection connection;
    private static String url = "jdbc:mysql://localhost:3306/ensao";
    private static String username = "root";
    private static String password = "";
    
    private Etudiant p;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

	@Override

	public void start(Stage primaryStage) {

		
		

		GridPane form = new GridPane();
        //System.out.println(p.getCne());
        Label Tcne=new Label("Cne:");
        Label Tmdp=new Label("Mot de pass:");
        Label Tnom=new Label("Nom:");
        Label Tprenom=new Label("Prenom:");
        Label Temail=new Label("Email:");
        
        
        Label titre=new Label("Fiche Etudiant:");
        Label cneField = new Label();
        Label mdpField=new Label();
        Label firstNameField = new Label();
        Label lastNameField = new Label();
        Label emailField = new Label();
		
		
		
		
		
		cneField.setText(p.getCne());
		mdpField.setText(p.getMotdepass());
		firstNameField.setText(p.getFirstName());
		lastNameField.setText(p.getLastName());
		emailField.setText(p.getEmail());
       //Bouton "Retour vers le tableau des etudiants"
        Main sc=new Main();
        Button retourButton = new Button("Retour vers Liste");
        retourButton.setId("idretourButton");
        retourButton.setOnAction(event->sc.start(primaryStage));
        //Bouton "Imprimer"
        Button toprintButton = new Button("Imprimer");
        toprintButton.setId("idtoprintButton");
        toprintButton.setOnAction(event -> {
        	PrinterJob printerJob  = PrinterJob.createPrinterJob();
        	
        	if(printerJob.showPageSetupDialog(primaryStage) && printerJob.printPage(form)) {
        		printerJob.endJob();
        	}
        	
        });
        
        form.add(titre,1,0,2,2);
        form.add(Tcne,1,3);
        form.add(cneField,2,3);
        form.add(Tmdp,1,4);
        form.add(mdpField,2,4);
        form.add(Tnom,1,5);
        form.add(firstNameField,2,5);
        form.add(Tprenom,1,6);
        form.add(lastNameField,2,6);
        form.add(Temail,1,7);
        form.add(emailField,2,7);
        form.setAlignment(Pos.CENTER);
        form.setVgap(5);
        form.setHgap(10);
        HBox root1 =new HBox(20,toprintButton,retourButton);
        root1.setAlignment(Pos.CENTER);
        
        
        VBox root =new VBox(40,form,root1);
        root.setAlignment(Pos.CENTER);
        Scene scene2 = new Scene(root, 400,300);
        scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setTitle("Fiche");
        
        primaryStage.setScene(scene2);
        primaryStage.setResizable(false);

        primaryStage.show();

    }
	
	public void setPerson(Etudiant p) {
		this.p = p;
	}
	
	public static void main(String[] args) {

		launch(args);

	}

}