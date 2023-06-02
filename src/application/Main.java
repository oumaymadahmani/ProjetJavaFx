package application;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

import application.bd.ConnexionBd;

public class Main extends Application {

    private TableView<Etudiant> tableView;
    private ObservableList<Etudiant> personList;
    private TextField cneField;
    private PasswordField mdpField;
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private Stage primaryStage;
   
    

    private Connection connection = ConnexionBd.connectToDatabase(); 
    
    
    
    //*************fct start***********
    @Override
    public void start(Stage primaryStage) {
    	
        tableView = new TableView<>();
        personList = FXCollections.observableArrayList();
        tableView.setItems(personList);
        
        //Colonne du cne
        TableColumn<Etudiant, String> cneColumn = new TableColumn<>("CNE");
        cneColumn.setCellValueFactory(new PropertyValueFactory<Etudiant,String>("cne"));
        
        //Colonne du mot de pass
        TableColumn<Etudiant, String> mdpColumn = new TableColumn<>("Mot de Pass");
        mdpColumn.setCellValueFactory(new PropertyValueFactory<Etudiant,String>("motdepass"));
        
        //Colonne du prénom
        TableColumn<Etudiant, String> firstnameColumn = new TableColumn<>("Nom");
        firstnameColumn.setCellValueFactory(new PropertyValueFactory<Etudiant,String>("firstName"));
        
        //Colonne du nom de famille
        TableColumn<Etudiant, String> lastnameColumn = new TableColumn<>("Prenom");
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<Etudiant,String>("lastName"));
        
        //Colonne de l'email
        TableColumn<Etudiant, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<Etudiant,String>("email"));

        tableView.getColumns().add(cneColumn);
        tableView.getColumns().add(mdpColumn);
        tableView.getColumns().add(firstnameColumn);
		tableView.getColumns().add(lastnameColumn);
		tableView.getColumns().add(emailColumn);
		
		cneField = new TextField();
		mdpField=new PasswordField();
        firstNameField = new TextField();
        lastNameField = new TextField();
        emailField = new TextField();
        
        //Bouton "Ajouter"
        Button addButton = new Button("Ajouter");
        addButton.setId("idaddButton");
        addButton.setOnAction(event -> addPerson());
        
        //Bouton "Modifier"
        Button updateButton = new Button("Modifier");
        updateButton.setId("idupdateButton");
        updateButton.setOnAction(event -> updatePerson());
        
        //Bouton "Supprimer"
        Button deleteButton = new Button("Supprimer");
        deleteButton.setId("iddeleteButton");
        deleteButton.setOnAction(event -> deletePerson());
        
        //Bouton "Quitter"
        Button quittButton = new Button("Quitter");
        quittButton.setId("idquittButton");
        quittButton.setOnAction(event -> System.exit(0));
        
        //Bouton "Voir Profile"
        Button profileButton = new Button("Voir Profile");
        profileButton.setId("idprofileButton");
        
        profileButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent t) {
				Etudiant selectedPerson = tableView.getSelectionModel().getSelectedItem();
		        if (selectedPerson != null) {
		        	Profile prf = new Profile();
		        	prf.setPerson(selectedPerson);
		        	prf.start(primaryStage);
				primaryStage.show();}
		        else {
		        	Alert alert = new Alert(AlertType.WARNING);
		    	    alert.setTitle("aucun etudiant sélectionné");
		    	    alert.setHeaderText(null);
		    	    alert.setContentText("Sélectionner un etudiant ");
		    	    alert.showAndWait();
		        }
		        }
        });
      
        Label Tcne=new Label("Cne:");
        Label Tmdp=new Label("Mot de pass:");
        Label Tnom=new Label("Nom:");
        Label Tprenom=new Label("Prenom:");
        Label Temail=new Label("Email:");

        
        GridPane form = new GridPane();
        form.add(Tcne,1,0);
        form.add(cneField,2,0);
        form.add(Tmdp,3,0);
        form.add(mdpField,4,0);
        form.add(Tnom,1,2);
        form.add(firstNameField,2,2);
        form.add(Tprenom,3,2);
        form.add(lastNameField,4,2);
        form.add(Temail,1,3);
        form.add(emailField,2,3);
        form.setVgap(5);
        form.setHgap(10);
        HBox hbox1=new HBox(30,addButton,updateButton,deleteButton);
        HBox hbox2=new HBox(30,quittButton,profileButton);
        VBox vbox1 = new VBox(10,hbox1,hbox2);
        
        HBox inputBox= new HBox(30,form, vbox1);
        
        
        VBox root = new VBox(10, tableView, inputBox);
        Scene scene = new Scene(root, 800, 550);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Liste des Etudiants de L'ENSAO");
        primaryStage.setResizable(false);
        
        
        primaryStage.show();
        
        //Chargement des etudiants depuis la base de données
        loadpersons();
    	}
    
    
        //***********fct loadpersons**********    
        private void loadpersons() {
        try {
         //Creation d'une instruction sql
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM students");
        //Efface la liste des personnes existantes
            personList.clear();
        //Remplit la liste des personnes avec les données du résultat de la requete
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String cne = resultSet.getString("cne");
                String mdp = resultSet.getString("mdp");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String email = resultSet.getString("email");
                
         //Ajout d'une nouvelle personne a la liste
                personList.add(new Etudiant(id,cne,mdp, firstname, lastname, email));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error loading students from database.");
            e.printStackTrace();
        }
    }
    
    
    
        private boolean checkPerson(String cne) {
    	
    	try {
    		            Statement statement = connection.createStatement();
    		            ResultSet resultSet = statement.executeQuery("SELECT * FROM students where cne = '"+cne+"'");
    		            while (resultSet.next()) {
    		                return true;
    		            }

    		            resultSet.close();
    		            statement.close();
    		        } catch (SQLException e) {
    		            System.out.println("Error loading students from database.");
    		            e.printStackTrace();
    		        }
    	
    	
    	             return false;
    }
    
        //********fct addperson***********
        private void addPerson() {
        //Récupération des informationsde la personne a ajouter depuis les champs de saisie
    	String cne = cneField.getText();
    	String motdepass = mdpField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        

         if((!this.checkPerson(cne)) && (!cne.isEmpty())) {
    	  try {
    		             //Preparation de la requete Insert
    		             PreparedStatement statement = connection.prepareStatement("INSERT INTO students (cne,mdp,firstname, lastname, email) VALUES (?,?,?, ?, ?)");
    		             statement.setString(1, cne);
    		             statement.setString(2, motdepass);
    		             statement.setString(3, firstName);
    		             statement.setString(4, lastName);
    		             statement.setString(5, email);
    		             //Execution de la requete
    		             statement.executeUpdate();
    		             statement.close();
    		             clearFields();
    		             loadpersons();
    		         } catch (SQLException e) {
    		             System.out.println("Error adding student to database.");
    		             e.printStackTrace();
    		         }
       }
       else {
    	   
    	   Alert alert = new Alert(AlertType.WARNING);
    	    alert.setTitle("Verifier CNE");
    	    alert.setHeaderText(null);
    	    alert.setContentText("cne deja existe ou vide");
    	    alert.showAndWait();
    	}
       }
    

    
    
        //**********fct updateperson************
        private void updatePerson() {
        Etudiant selectedPerson = tableView.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            int id = selectedPerson.getId();
            String cne = cneField.getText();
            String motdepass = mdpField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();

            if((!this.checkPerson(cne)) && (!cne.isEmpty())) {
            	try {
                	//Preparation de la requete Update
                    PreparedStatement statement = connection.prepareStatement("UPDATE students SET cne = ?,mdp = ?, firstname = ?, lastname = ?, email = ? WHERE id = ?");
                    statement.setString(1, cne);
                    statement.setString(2, motdepass);
                    statement.setString(3, firstName);
                    statement.setString(4, lastName);
                    statement.setString(5, email);
                    statement.setInt(6, id);
              //Execution de la requete
                    statement.executeUpdate();
                    statement.close();
                    clearFields();
                    loadpersons();
                } catch (SQLException e) {
                    System.out.println("Error updating person in database.");
                    e.printStackTrace();
                }
            	
            } else {
            	
            	Alert alert = new Alert(AlertType.WARNING);
        	    alert.setTitle("Verifier CNE");
        	    alert.setHeaderText(null);
        	    alert.setContentText("cne deja existe ou vide");
        	    alert.showAndWait();
            	
            }
        }
        else{
        	Alert alert = new Alert(AlertType.WARNING);
    	    alert.setTitle("aucun etudiant sélectionné");
    	    alert.setHeaderText(null);
    	    alert.setContentText("Sélectionner un etudiant ");
    	    alert.showAndWait();
        }
    }

    
    
    
    
        //*********fct deleteperson***********
        private void deletePerson() {
        Etudiant selectedPerson = tableView.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            int id = selectedPerson.getId();

            try {
                //Execution de la requete Delete
                PreparedStatement statement = connection.prepareStatement("DELETE FROM students WHERE id = ?");
                statement.setInt(1, id);
                //Exécution de la requete de suppression
                statement.executeUpdate();
                statement.close();
                clearFields();
                loadpersons();
            } catch (SQLException e) {
                System.out.println("Error deleting person from database.");
                e.printStackTrace();
            }
        }
        else {
        	Alert alert = new Alert(AlertType.WARNING);
    	    alert.setTitle("aucun etudiant sélectionné");
    	    alert.setHeaderText(null);
    	    alert.setContentText("Sélectionner un etudiant ");
    	    alert.showAndWait();
        }
    }
    
        //*********fct clearFields*************
        private void clearFields() {
        //Effacement des champs de saisie
    	cneField.clear();
    	mdpField.clear();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
    }

     //**********fct main*********
    public static void main(String[] args) {
    	
    	//Point d'entée de l'application
        launch(args);
    }
}