package projet_poo;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.chart.PieChart;

public class GestionnaireFichierGUI extends Application {

    // Vibrant color palette - Modern & Colorful
    private final String PRIMARY_COLOR = "#cfb8f4";     // Light purple for sidebar
    private final String SECONDARY_COLOR = "#60009b";   // Deep blue for buttons
    private final String DELETE_COLOR = "#ff0000";      // Red for delete buttons
    private final String SAVE_COLOR = "#28A745";        // Green for save buttons
    private final String BACK_COLOR = "#6C757D";        // Gray for back buttons
    private final String ACCENT_COLOR = "#ff3e00";      // Bright orange for highlights
    private final String BACKGROUND_COLOR = "#ffffff";  // Clean white background
    private final String CARD_COLOR = "#FFFFFF";        // White for cards
    private final String TEXT_COLOR = "#000000";        // Dark gray for text
    private final String BORDER_COLOR = "#DFE6E9";      // Soft gray for borders

    // Modern typography
    private final String FONT_FAMILY = "Inter, Segoe UI, sans-serif";

    private Stage primaryStage;
    private BorderPane mainLayout;
    private VBox sidebar;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        DataBaseManager.testConnection();
        DataBaseManager.initDatabase();

        primaryStage.setTitle("FileSync: Gestionnaire");

        // Layout principal
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        // Sidebar
        sidebar = createSidebar();
        mainLayout.setLeft(sidebar);

        // Afficher la scène d'accueil
        showAccueilScene();

        Scene scene = new Scene(mainLayout, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Créer la barre latérale
    private VBox createSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: " + PRIMARY_COLOR + "; -fx-min-width: 220; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 0 1 0 0;");

        Label title = new Label("FileSync");
        title.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 22));
        title.setTextFill(Color.web(BACKGROUND_COLOR));

        Button addButton = createSidebarButton("➕ Ajouter", SECONDARY_COLOR);
        addButton.setOnAction(e -> showAddFichierScene());
        addButton.setTooltip(new Tooltip("Ajouter un fichier favori"));

        Button viewButton = createSidebarButton("📂 Voir", SECONDARY_COLOR);
        viewButton.setOnAction(e -> showViewFichiersScene(false));
        viewButton.setTooltip(new Tooltip("Voir tous les fichiers"));

        Button updateButton = createSidebarButton("📝 Modifier", SECONDARY_COLOR);
        updateButton.setOnAction(e -> showViewFichiersScene(true));
        updateButton.setTooltip(new Tooltip("Modifier ou supprimer"));

        Button searchButton = createSidebarButton("🔍 Rechercher", SECONDARY_COLOR);
        searchButton.setOnAction(e -> showSearchScene());
        searchButton.setTooltip(new Tooltip("Rechercher un fichier"));

        Button statsButton = createSidebarButton("📊 Propriétés", SECONDARY_COLOR);
        statsButton.setOnAction(e -> showStatsScene());
        statsButton.setTooltip(new Tooltip("Voir les statistiques"));

        sidebar.getChildren().addAll(title, addButton, viewButton, updateButton, searchButton, statsButton);
        sidebar.setAlignment(Pos.TOP_CENTER);
        return sidebar;
    }

    // Créer un bouton de sidebar
    private Button createSidebarButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXT_COLOR + "; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 14px; -fx-padding: 12 15; -fx-background-radius: 8; -fx-alignment: CENTER_LEFT;");
        button.setMaxWidth(Double.MAX_VALUE);

        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: " + lightenColor(color, 0.95) + "; -fx-text-fill: " + TEXT_COLOR + "; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 14px; -fx-padding: 12 15; -fx-background-radius: 8;");
        });
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXT_COLOR + "; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 14px; -fx-padding: 12 15; -fx-background-radius: 8;");
        });
        return button;
    }

    // Éclaircir une couleur
    private String lightenColor(String hexColor, double factor) {
        Color color = Color.web(hexColor);
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255 + (255 - color.getRed() * 255) * factor),
                (int) (color.getGreen() * 255 + (255 - color.getGreen() * 255) * factor),
                (int) (color.getBlue() * 255 + (255 - color.getBlue() * 255) * factor));
    }

    // SCÈNE D'ACCUEIL
    private void showAccueilScene() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(40));
        content.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1;");
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(700);

        Label welcome = new Label("Bienvenue sur FileSync");
        welcome.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 28));
        welcome.setTextFill(Color.web(SECONDARY_COLOR));

        Label subtitle = new Label("Gérez vos fichiers favoris avec simplicité ! ");
        subtitle.setFont(Font.font(FONT_FAMILY, FontWeight.NORMAL, 16));
        subtitle.setTextFill(Color.web(TEXT_COLOR));

        // Ajout de l'image
        ImageView imageView = new ImageView();

        try {
        // Debug the classpath root
        System.out.println("Classpath root: " + getClass().getResource("/"));
        // Load the image using getResource to get the URL
        URL resourceUrl = getClass().getResource("/img.png");
        if (resourceUrl == null) {
            throw new Exception("Image file 'img.png' not found in resources. Check src/ or build/classes/.");
        }
        System.out.println("Image URL: " + resourceUrl);
        String imagePath = resourceUrl.toExternalForm();
        Image image = new Image(imagePath);
        if (image.isError()) {
            throw new Exception("Image loading failed: " + image.getException().getMessage());
        }
        imageView.setImage(image);
        imageView.setFitWidth(700);
        imageView.setFitHeight(600);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-background-radius: 12;");
        } catch (Exception e) {
            // Instead of showing an error label, hide the ImageView and log the error
            imageView.setVisible(false);
            System.err.println("Failed to load image: " + e.getMessage());
        }
        content.getChildren().addAll(welcome, subtitle, imageView);
        VBox.setVgrow(content, Priority.ALWAYS);
        mainLayout.setCenter(content);
    }
    // SCÈNE D'AJOUT DE FICHIER

    private void showAddFichierScene() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1;");
        content.setAlignment(Pos.TOP_CENTER);
        content.setMaxWidth(800);

        Label title = new Label("Marquage de fichiers favoris");
        title.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 24));
        title.setTextFill(Color.web(SECONDARY_COLOR));
        title.setAlignment(Pos.CENTER);

        // Form layout with labels on the left
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(15);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.TOP_LEFT);

        // Labels and fields
        Label cheminLabel = createFormLabel("Chemin");
        TextField pathField = createTextField("Chemin du fichier");
        Button browseButton = createActionButton("Parcourir", SECONDARY_COLOR);
        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                pathField.setText(selectedFile.getAbsolutePath());
            }
        });
        HBox pathBox = new HBox(10, pathField, browseButton);

        Label titreLabel = createFormLabel("Titre*");
        TextField titleField = createTextField("Titre");

        Label auteurLabel = createFormLabel("Auteur");
        TextField authorField = createTextField("Auteur");

        Label tagsLabel = createFormLabel("Tags*");
        TextField tagsField = createTextField("Tags (séparés par , ou ;)");

        Label resumeLabel = createFormLabel("Résumé");
        TextArea resumeArea = createTextArea("Résumé");

        Label commentsLabel = createFormLabel("Commentaires");
        TextArea commentsArea = createTextArea("Commentaires");

        // Add to grid
        form.add(cheminLabel, 0, 0);
        form.add(pathBox, 1, 0);
        form.add(titreLabel, 0, 1);
        form.add(titleField, 1, 1);
        form.add(auteurLabel, 0, 2);
        form.add(authorField, 1, 2);
        form.add(tagsLabel, 0, 3);
        form.add(tagsField, 1, 3);
        form.add(resumeLabel, 0, 4);
        form.add(resumeArea, 1, 4);
        form.add(commentsLabel, 0, 5);
        form.add(commentsArea, 1, 5);

        Label requiredLabel = new Label("* Champs obligatoires");
        requiredLabel.setStyle("-fx-text-fill: " + DELETE_COLOR + "; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 15px;");

        Button saveButton = createActionButton("Enregistrer", SAVE_COLOR);
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String path = pathField.getText();
                String title = titleField.getText();
                String author = authorField.getText();
                String resume = resumeArea.getText();
                String comments = commentsArea.getText();
                String tagsText = tagsField.getText();

                if (path.isEmpty() || title.isEmpty() || tagsText.isEmpty()) {
                    showAlert("Erreur", "Les champs obligatoires doivent être remplis.");
                    return;
                }

                List<String> tags = Arrays.stream(tagsText.split("[;,]+"))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());

                try {
                    DataBaseManager.ajouterFichierAvecTags(path, author, title, tags, resume, comments);
                    showAlert("Succès", "Fichier ajouté avec succès !");
                    showAccueilScene();
                } catch (SQLException ex) {
                    showAlert("Erreur", "Erreur lors de l'ajout : " + ex.getMessage());
                }
            }
        });

        Button backButton = createActionButton("Retour", BACK_COLOR);
        backButton.setOnAction(e -> showAccueilScene());

        HBox buttonBox = new HBox(10, saveButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        content.getChildren().addAll(title, form, requiredLabel, buttonBox);
        mainLayout.setCenter(content);
    }

    // SCÈNE D'AFFICHAGE/MISE À JOUR
    private void showViewFichiersScene(boolean forUpdate) {
        VBox content = new VBox(15);
        content.setPadding(new Insets(25));
        content.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1;");
        content.setAlignment(Pos.TOP_CENTER);

        Label title = new Label(forUpdate ? "Modifier Fichiers" : "Vos Fichiers");
        title.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 24));
        title.setTextFill(Color.web(SECONDARY_COLOR));

        TableView<Fichier> tableView = new TableView<>();
        tableView.setStyle("-fx-background  -fx-background-color: " + CARD_COLOR + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8;");

        String cellStyle = "-fx-text-fill: " + TEXT_COLOR + "; -fx-alignment: CENTER_LEFT; -fx-padding: 10; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 13px;";

        TableColumn<Fichier, String> titleCol = new TableColumn<>("Titre");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        titleCol.setStyle(cellStyle);
        titleCol.setPrefWidth(150);

        TableColumn<Fichier, String> pathCol = new TableColumn<>("Chemin");
        pathCol.setCellValueFactory(new PropertyValueFactory<>("chemin"));
        pathCol.setStyle(cellStyle);
        pathCol.setPrefWidth(250);

        TableColumn<Fichier, String> authorCol = new TableColumn<>("Auteur");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        authorCol.setStyle(cellStyle);
        authorCol.setPrefWidth(120);

        TableColumn<Fichier, String> tagsCol = new TableColumn<>("Tags");
        tagsCol.setCellValueFactory(cellData -> {
            try {
                List<String> tags = DataBaseManager.getTagsParFichierId(String.valueOf(cellData.getValue().getId()));
                return new SimpleStringProperty(String.join(", ", tags));
            } catch (SQLException e) {
                return new SimpleStringProperty("Erreur");
            }
        });
        tagsCol.setStyle(cellStyle);
        tagsCol.setPrefWidth(150);

        TableColumn<Fichier, String> resumeCol = new TableColumn<>("Résumé");
        resumeCol.setCellValueFactory(new PropertyValueFactory<>("resume"));
        resumeCol.setStyle(cellStyle);
        resumeCol.setPrefWidth(200);

        TableColumn<Fichier, String> commentsCol = new TableColumn<>("Commentaires");
        commentsCol.setCellValueFactory(new PropertyValueFactory<>("commentaires"));
        commentsCol.setStyle(cellStyle);
        commentsCol.setPrefWidth(200);

        tableView.getColumns().addAll(titleCol, pathCol, authorCol, tagsCol, resumeCol, commentsCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableView.setStyle("-fx-control-inner-background: " + CARD_COLOR + "; -fx-table-cell-border-color: " + BORDER_COLOR + "; -fx-table-header-background: " + lightenColor(SECONDARY_COLOR, 0.95) + ";");
        tableView.lookupAll(".column-header").forEach(node
                -> node.setStyle("-fx-background-color: " + lightenColor(SECONDARY_COLOR, 0.95) + "; -fx-text-fill: " + TEXT_COLOR + "; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-weight: BOLD; -fx-padding: 12;"));

        try {
            List<Fichier> fichiers = DataBaseManager.getFichiersFavoris();
            tableView.setItems(FXCollections.observableArrayList(fichiers));
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement : " + e.getMessage());
        }

        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setPadding(new Insets(15));

        if (forUpdate) {
            Button updateButton = createActionButton("Modifier", SECONDARY_COLOR);
            updateButton.setOnAction(e -> {
                Fichier selected = tableView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    showUpdateFichierScene(selected);
                } else {
                    showAlert("Erreur", "Sélectionnez un fichier à modifier.");
                }
            });

            Button deleteButton = createActionButton("Supprimer", DELETE_COLOR);
            deleteButton.setOnAction(e -> {
                Fichier selected = tableView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    try {
                        deleteFichier(selected);
                    } catch (SQLException ex) {
                        showAlert("Erreur", "Erreur lors de la suppression : " + ex.getMessage());
                    }
                } else {
                    showAlert("Erreur", "Sélectionnez un fichier à supprimer.");
                }
            });

            Button backButton = createActionButton("Retour", BACK_COLOR);
            backButton.setOnAction(e -> showAccueilScene());

            buttonsBox.getChildren().addAll(updateButton, deleteButton, backButton);
        } else {
            Button viewButton = createActionButton("Ouvrir", SECONDARY_COLOR);
            viewButton.setOnAction(e -> {
                Fichier selected = tableView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    try {
                        File file = new File(selected.getChemin());
                        if (file.exists()) {
                            java.awt.Desktop.getDesktop().open(file);
                        } else {
                            showAlert("Erreur", "Fichier introuvable : " + selected.getChemin());
                        }
                    } catch (IOException ex) {
                        showAlert("Erreur", "Impossible d'ouvrir : " + ex.getMessage());
                    }
                } else {
                    showAlert("Erreur", "Sélectionnez un fichier à ouvrir.");
                }
            });

            Button backButton = createActionButton("Retour", BACK_COLOR);
            backButton.setOnAction(e -> showAccueilScene());

            buttonsBox.getChildren().addAll(viewButton, backButton);
        }

        Button exportButton = createActionButton("Exporter", TEXT_COLOR);
        exportButton.setOnAction(e -> exportFichiersList());

        buttonsBox.getChildren().add(exportButton);

        content.getChildren().addAll(title, tableView, buttonsBox);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        mainLayout.setCenter(content);
    }

    private void showUpdateFichierScene(Fichier fichier) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(25));
        content.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1;");
        content.setAlignment(Pos.TOP_CENTER);
        content.setMaxWidth(800); // Increased from 600 to 800 for wider layout

        // Title Section
        Label title = new Label("Modifier Fichier");
        title.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 24));
        title.setTextFill(Color.web(SECONDARY_COLOR));
        title.setAlignment(Pos.CENTER);

        // Form Section with GridPane
        GridPane form = new GridPane();
        form.setHgap(50); // Increased from 10 to 50 for more horizontal spacing
        form.setVgap(15);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.TOP_LEFT);

        // Set column constraints to control width
        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setPrefWidth(100); // Fixed width for labels
        ColumnConstraints fieldColumn = new ColumnConstraints();
        fieldColumn.setPrefWidth(600); // Wider column for input fields
        fieldColumn.setHgrow(Priority.ALWAYS); // Allow fields to grow with window
        form.getColumnConstraints().addAll(labelColumn, fieldColumn);

        // Form fields
        Label cheminLabel = createFormLabel("Chemin");
        TextField pathField = createTextField("Chemin du fichier");
        pathField.setText(fichier.getChemin());
        pathField.setDisable(true);

        Label titreLabel = createFormLabel("Titre");
        TextField titleField = createTextField("Titre*");
        titleField.setText(fichier.getTitre());

        Label auteurLabel = createFormLabel("Auteur");
        TextField authorField = createTextField("Auteur");
        authorField.setText(fichier.getAuteur());

        Label tagsLabel = createFormLabel("Tags");
        TextField tagsField = createTextField("Tags (séparés par , ou ;)*");
        try {
            List<String> tags = DataBaseManager.getTagsParFichierId(String.valueOf(fichier.getId()));
            tagsField.setText(String.join(", ", tags));
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des tags : " + e.getMessage());
        }

        Label resumeLabel = createFormLabel("Résumé");
        TextArea resumeArea = createTextArea("Résumé");
        resumeArea.setText(fichier.getResume());

        Label commentsLabel = createFormLabel("Commentaires");
        TextArea commentsArea = createTextArea("Commentaires");
        commentsArea.setText(fichier.getCommentaires());

        // Add to grid
        form.add(cheminLabel, 0, 0);
        form.add(pathField, 1, 0);
        form.add(titreLabel, 0, 1);
        form.add(titleField, 1, 1);
        form.add(auteurLabel, 0, 2);
        form.add(authorField, 1, 2);
        form.add(tagsLabel, 0, 3);
        form.add(tagsField, 1, 3);
        form.add(resumeLabel, 0, 4);
        form.add(resumeArea, 1, 4);
        form.add(commentsLabel, 0, 5);
        form.add(commentsArea, 1, 5);

        // Required Fields Label
        Label requiredLabel = new Label("* Champs obligatoires");
        requiredLabel.setStyle("-fx-text-fill: " + ACCENT_COLOR + "; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 12px;");
        requiredLabel.setAlignment(Pos.CENTER_LEFT);

        // Button Section
        Button saveButton = createActionButton("Enregistrer", SAVE_COLOR);
        saveButton.setOnAction(e -> {
            String titleText = titleField.getText();
            String author = authorField.getText();
            String resume = resumeArea.getText();
            String comments = commentsArea.getText();
            String tagsText = tagsField.getText();

            if (titleText.isEmpty() || tagsText.isEmpty()) {
                showAlert("Erreur", "Les champs obligatoires doivent être remplis.");
                return;
            }

            List<String> tags = Arrays.stream(tagsText.split("[;,]+"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            try {
                boolean success = DataBaseManager.updateFichier(fichier.getId(), titleText, author, resume, comments);
                DataBaseManager.updateTagsForFichier(fichier.getId(), tags);
                if (success) {
                    showAlert("Succès", "Fichier mis à jour avec succès !");
                    showViewFichiersScene(true);
                } else {
                    showAlert("Erreur", "Mise à jour échouée.");
                }
            } catch (SQLException ex) {
                showAlert("Erreur", "Erreur lors de la mise à jour : " + ex.getMessage());
            }
        });

        Button backButton = createActionButton("Retour", BACK_COLOR);
        backButton.setOnAction(e -> showViewFichiersScene(true));

        HBox buttonBox = new HBox(20, saveButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        // Add components to content
        content.getChildren().addAll(title, form, requiredLabel, buttonBox);

        mainLayout.setCenter(content);
    }

    // SCÈNE DE RECHERCHE
    private void showSearchScene() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(25));
        content.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1;");
        content.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Rechercher");
        title.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 24));
        title.setTextFill(Color.web(SECONDARY_COLOR));

        HBox searchBox = new HBox(10);
        searchBox.setPadding(new Insets(12));
        searchBox.setStyle("-fx-background-color: " + lightenColor(SECONDARY_COLOR, 0.95) + "; -fx-background-radius: 8;");

        ComboBox<String> searchTypeComboBox = new ComboBox<>();
        searchTypeComboBox.getItems().addAll("Titre", "Auteur", "Tag");
        searchTypeComboBox.setValue("Titre");
        searchTypeComboBox.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-text-fill: " + TEXT_COLOR + "; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 13px; -fx-background-radius: 6;");

        TextField searchField = createTextField("Recherche");
        Button searchButton = createActionButton("Rechercher", SECONDARY_COLOR);

        searchBox.getChildren().addAll(searchTypeComboBox, searchField, searchButton);
        searchBox.setAlignment(Pos.CENTER);

        TableView<Fichier> resultsTable = new TableView<>();
        resultsTable.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8;");

        TableColumn<Fichier, String> titleCol = new TableColumn<>("Titre");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        titleCol.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-alignment: CENTER_LEFT; -fx-padding: 10; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 13px;");
        titleCol.setPrefWidth(200);

        TableColumn<Fichier, String> pathCol = new TableColumn<>("Chemin");
        pathCol.setCellValueFactory(new PropertyValueFactory<>("chemin"));
        pathCol.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-alignment: CENTER_LEFT; -fx-padding: 10; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 13px;");
        pathCol.setPrefWidth(400);

        resultsTable.getColumns().addAll(titleCol, pathCol);
        resultsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        resultsTable.setStyle("-fx-control-inner-background: " + CARD_COLOR + "; -fx-table-cell-border-color: " + BORDER_COLOR + "; -fx-table-header-background: " + lightenColor(SECONDARY_COLOR, 0.95) + ";");
        resultsTable.lookupAll(".column-header").forEach(node
                -> node.setStyle("-fx-background-color: " + lightenColor(SECONDARY_COLOR, 0.95) + "; -fx-text-fill: " + TEXT_COLOR + "; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-weight: BOLD; -fx-padding: 12;"));

        searchButton.setOnAction(e -> {
            String searchText = searchField.getText().trim();
            String searchType = searchTypeComboBox.getValue();

            if (searchText.isEmpty()) {
                showAlert("Erreur", "Entrez un terme de recherche.");
                return;
            }

            List<Fichier> results;
            try {
                switch (searchType) {
                    case "Titre":
                        results = DataBaseManager.getFichiersFavoris().stream()
                                .filter(f -> f.getTitre().toLowerCase().contains(searchText.toLowerCase()))
                                .collect(Collectors.toList());
                        break;
                    case "Auteur":
                        results = DataBaseManager.getFichiersFavoris().stream()
                                .filter(f -> f.getAuteur() != null && f.getAuteur().toLowerCase().contains(searchText.toLowerCase()))
                                .collect(Collectors.toList());
                        break;
                    case "Tag":
                        results = DataBaseManager.searchByTag(searchText);
                        break;
                    default:
                        results = new ArrayList<>();
                }
                resultsTable.setItems(FXCollections.observableArrayList(results));
            } catch (SQLException ex) {
                showAlert("Erreur", "Erreur lors de la recherche : " + ex.getMessage());
            }
        });

        Button backButton = createActionButton("Retour", BACK_COLOR);
        backButton.setOnAction(e -> showAccueilScene());

        content.getChildren().addAll(title, searchBox, resultsTable, backButton);
        VBox.setVgrow(resultsTable, Priority.ALWAYS);
        mainLayout.setCenter(content);
    }

    // SCÈNE DES STATISTIQUES
    private void showStatsScene() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(25));
        content.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-background-radius: 12; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1;");
        content.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Statistiques");
        title.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 24));
        title.setTextFill(Color.web(SECONDARY_COLOR));

        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton screenOption = new RadioButton("Afficher");
        screenOption.setToggleGroup(toggleGroup);
        screenOption.setSelected(true);
        screenOption.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 13px;");

        RadioButton fileOption = new RadioButton("Exporter");
        fileOption.setToggleGroup(toggleGroup);
        fileOption.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 13px;");

        HBox optionsBox = new HBox(15, screenOption, fileOption);
        optionsBox.setAlignment(Pos.CENTER);
        optionsBox.setStyle("-fx-background-color: " + lightenColor(SECONDARY_COLOR, 0.95) + "; -fx-background-radius: 8; -fx-padding: 12;");

        Button generateButton = createActionButton("Générer", SECONDARY_COLOR);
        generateButton.setTooltip(new Tooltip("Générer les statistiques"));

        TextArea resultsArea = new TextArea();
        resultsArea.setEditable(false);
        resultsArea.setPrefRowCount(15);
        resultsArea.setWrapText(true);
        resultsArea.setStyle("-fx-control-inner-background: " + CARD_COLOR + "; -fx-text-fill: " + TEXT_COLOR + "; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 13px; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8;");

        // Ajout du graphique (PieChart pour le nombre de fichiers par tag)
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Répartition des fichiers par tag");
        pieChart.setStyle("-fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 13px;");
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);

        generateButton.setOnAction(e -> {
            try {
                String stats = DataBaseManager.genererStatistiques();
                if (fileOption.isSelected()) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Exporter Stats");
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers texte", "*.txt"));
                    File file = fileChooser.showSaveDialog(primaryStage);
                    if (file != null) {
                        DataBaseManager.exporterStatistiques(file.getAbsolutePath());
                        showAlert("Succès", "Stats exportées : " + file.getAbsolutePath());
                    }
                } else {
                    resultsArea.setText(stats);

                    // Mettre à jour le graphique
                    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
                    // Simuler des données de tags (remplacer par une méthode réelle dans DataBaseManager)
                    List<String> allTags = DataBaseManager.getAllTags(); // Supposons que cette méthode existe
                    for (String tag : allTags) {
                        int count = DataBaseManager.getFilesCountByTag(tag); // Supposons que cette méthode existe
                        if (count > 0) {
                            pieChartData.add(new PieChart.Data(tag + " (" + count + ")", count));
                        }
                    }
                    pieChart.setData(pieChartData);
                }
            } catch (SQLException | IOException ex) {
                showAlert("Erreur", "Erreur lors de la génération : " + ex.getMessage());
            }
        });

        Button backButton = createActionButton("Retour", BACK_COLOR);
        backButton.setOnAction(e -> showAccueilScene());

        content.getChildren().addAll(title, optionsBox, generateButton, resultsArea, pieChart, backButton);
        VBox.setVgrow(pieChart, Priority.ALWAYS);
        mainLayout.setCenter(content);
    }

    // MÉTHODES UTILITAIRES
    private TextField createTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-text-fill: " + TEXT_COLOR + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 6; -fx-padding: 10; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 13px; -fx-background-radius: 6;");
        field.setMaxWidth(500);
        return field;
    }

    private TextArea createTextArea(String prompt) {
        TextArea area = new TextArea();
        area.setPromptText(prompt);
        area.setPrefRowCount(4);
        area.setWrapText(true);
        area.setStyle("-fx-control-inner-background: " + CARD_COLOR + "; -fx-text-fill: " + TEXT_COLOR + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 6; -fx-padding: 10; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 13px; -fx-background-radius: 6;");
        area.setMaxWidth(500);
        return area;
    }

    private Label createFormLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-weight: BOLD; -fx-font-size: 14px;");
        return label;
    }

    private Button createActionButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-weight: BOLD; -fx-padding: 10 20; -fx-background-radius: 8;");
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: " + darkenColor(color, 0.15) + "; -fx-text-fill: white; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-weight: BOLD; -fx-padding: 10 20; -fx-background-radius: 8;");
        });
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-weight: BOLD; -fx-padding: 10 20; -fx-background-radius: 8;");
        });
        button.setTooltip(new Tooltip(text));
        return button;
    }

    private String darkenColor(String hexColor, double factor) {
        Color color = Color.web(hexColor);
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255 * (1 - factor)),
                (int) (color.getGreen() * 255 * (1 - factor)),
                (int) (color.getBlue() * 255 * (1 - factor)));
    }

    private void deleteFichier(Fichier fichier) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer Fichier");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce fichier ?");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-font-family: '" + FONT_FAMILY + "';");
        dialogPane.lookupAll(".label").forEach(node
                -> ((Label) node).setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 13px;"));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = DataBaseManager.supprimerFichierParId(fichier.getId());
            if (success) {
                showAlert("Succès", "Fichier supprimé avec succès !");
                showViewFichiersScene(true);
            } else {
                showAlert("Erreur", "La suppression a échouée.");
            }
        }
    }

    private void exportFichiersList() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter Liste");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers texte", "*.txt"));
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            try {
                DataBaseManager.listerFichiersFavoris(false, file.getAbsolutePath());
                showAlert("Succès", "Liste exportée : " + file.getAbsolutePath());
            } catch (SQLException | IOException ex) {
                showAlert("Erreur", "Erreur lors de l'exportation : " + ex.getMessage());
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + CARD_COLOR + "; -fx-font-family: '" + FONT_FAMILY + "';");
        dialogPane.lookupAll(".label").forEach(node
                -> ((Label) node).setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-font-family: '" + FONT_FAMILY + "'; -fx-font-size: 13px;"));

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
