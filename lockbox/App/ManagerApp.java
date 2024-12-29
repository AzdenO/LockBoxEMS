package org.vigilance.lockbox.App;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;
import javafx.util.Duration;
import org.vigilance.lockbox.abstracts.definitions.*;
import org.vigilance.lockbox.abstracts.enums.DataType;
import org.vigilance.lockbox.datapersistance.FileStore;
import org.vigilance.lockbox.datapersistance.SaveConfigurations;
import org.vigilance.lockbox.gui.DataButton;
import org.vigilance.lockbox.gui.GroupButton;
import org.vigilance.lockbox.organisation.Group;
import org.vigilance.lockbox.utils.checks.InputCheck;
import org.vigilance.lockbox.utils.checks.SecurityChecks;
import org.vigilance.lockbox.utils.configs.AccessLog;
import org.vigilance.lockbox.utils.enums.Origin;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.vigilance.lockbox.abstracts.enums.DataType.*;

/**
 * Public javaFX application class that is the main thread of execution
 */
public class ManagerApp extends Application {

    private Object dragData;
    private boolean dragWait = false;

    private boolean tempOpen = false;

    private boolean longPress = false;

    String selectedPath=null;

    private double[] nodeCoordinates;
    private double[] nodeOrigin;
    private final Manager lockbox = new Manager();
    private final PasswordField entry = new PasswordField();
    private final Dictionary<String, Image> images = new Hashtable<>();
    private final DataType[] enumeration = {ACCOUNTS,BANKS,CODES,FILES,IMAGES,NOTES};

    private Scene main;

    private final HBox groupSelect = new HBox();
    private final FlowPane dataSelect = new FlowPane();
    private final HBox headerSelections = new HBox();

    private final ArrayList<Button> headerButtons=new ArrayList<>();
    private final ArrayList<MenuButton> menus = new ArrayList<>();
    private final ArrayList<MenuItem> menuItems = new ArrayList<>();
    private ArrayList<Button> groupButtons=new ArrayList<>();
    private ArrayList<Button> dataButtons = new ArrayList<>();

    private AnchorPane root=new AnchorPane();
    private Stage locker;
    private String groupSelected;
    private HBox search;
    private HBox header;
    private TextField searchBar;
    private ScrollPane groups = new ScrollPane();
    ScrollPane dataScroller;
    private FadeTransition dataFade;
    private FadeTransition groupFade;
    private final ColorPicker colourSelect = new ColorPicker();
    private final Button binButton = new Button();
    private final Button secureButton = new Button();

    Button addG = new Button();

    VBox newGroup = new VBox();

    public Label timeLabel = new Label();

    HBox timeContainer = new HBox();

    private boolean viewEncrypted = true;

    private BorderPane binPane = new BorderPane();

    private BorderPane keyPane = new BorderPane();

    private HBox panelContainer = new HBox();

    private String globalTypeSelect;
    private String globalGroupSelect;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void start(Stage stage){
        lockbox.loadWrapper();
        locker = stage;
        setup();
        locker.setTitle("LockBox");
        locker.setResizable(false);
        locker.getIcons().add(new Image("LockBoxLogo.png"));
        if(!lockbox.initialisationCheck()){
            locker.setScene(getMain(locker));
            new Utils().set(lockbox.config.backgroundPath);

        }else{
            locker.setScene(getLogin(locker));
        }
        locker.show();
        if(!lockbox.initialisationCheck()){
            new InputOutputWindow().runNewKeyIO(false);
        }


    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        launch(args);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setup(){

        fetchMedia();
        dataScroller=new ScrollPane();
        dataScroller.setLayoutX(20);
        dataScroller.setLayoutY(60);
        dataScroller.setMaxWidth(1080);
        dataScroller.setMaxHeight(580);
        dataScroller.setMinHeight(580);
        dataSelect.setMaxWidth(1200);
        dataSelect.setMinWidth(1200);
        dataSelect.setId("dataPane");
        dataSelect.setHgap(20);
        dataSelect.setPadding(new Insets(10,10,10,80));
        dataSelect.setVgap(20);
        groups.setOpacity(0.0);

        nodeCoordinates = new double[2];
        nodeOrigin = new double[2];
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void fetchMedia(){
        images.put("Logo",new Image("LockBoxLogo.png"));
        images.put("bin",new Image("bin2.png"));
        images.put("key",new Image("key.png"));
        images.put("databorder",new Image("databorder.png"));
        images.put("backIcon",new Image("backicon.png"));
        images.put("paint",new Image("paintbrush.png"));
        images.put("keyicon",new Image("keyicon.png"));
        images.put("logicon",new Image("logicon.png"));
        images.put("bankicon", new Image("bankcard.png"));
        images.put("noteicon",new Image("noteicon.png"));
        images.put("fileicon",new Image("fileicon.png"));
        images.put("codeicon",new Image("codeicon.png"));
        images.put("accounticon",new Image("accounticon.png"));
        images.put("deleteicon",new Image("deleteicon.png"));
        images.put("historyicon",new Image("historyicon.png"));
        images.put("foldericon",new Image("foldericon.png"));
        images.put("customicon",new Image("customicon.png"));
        images.put("machineicon",new Image("machineicon.png"));
        images.put("accountborder",new Image("accountborder.png"));
        images.put("bankborder",new Image("bankborder.png"));
        images.put("codeborder",new Image("codeborder.png"));
        images.put("fileborder",new Image("fileborder.png"));
        images.put("imageborder",new Image("imageborder.png"));
        images.put("noteborder",new Image("noteborder.png"));
        images.put("addicon",new Image("addicon.png"));
        images.put("addgroup",new Image("addgroupicon.png"));
        images.put("searchicon",new Image("searchIcon.png"));
        images.put("power",new Image("powericon.png"));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Method for constructing the initial login window
     * @param stage
     * @return
     */
    private Scene getLogin(Stage stage){
        setup();
        AnchorPane loginroot = new AnchorPane();
        loginroot.setId("rootnode");
        if(lockbox.config.colourHex==null){
            lockbox.config.colourHex="orange";
        }
        entry.setStyle("-fx-border-color: "+lockbox.config.colourHex);
        if(lockbox.config.backgroundPath==null){
            try{
                lockbox.config.backgroundPath=getClass().getResource("/default.png").toURI().toURL().toExternalForm();
            }catch(Exception e){
                System.out.println("x");
            }
        }
        BackgroundImage backImg = new BackgroundImage(new Image(lockbox.config.backgroundPath), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        loginroot.setBackground(new Background(backImg));
        Scene login = new Scene(loginroot, 1280, 720);

        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(4);
        shadow.setOffsetY(4);
        shadow.setColor(Color.GRAY);

        entry.setId("entry");
        entry.setLayoutX(490);
        entry.setLayoutY(300);
        entry.setMaxWidth(300);
        entry.setMinWidth(300);
        entry.setMaxHeight(35);
        entry.setMinHeight(35);
        entry.setEffect(shadow);
        entry.setFont(Font.font(20));
        loginroot.getChildren().add(entry);

        Button submit = new Button("Unlock");
        submit.setId("event");
        submit.setLayoutX(590);
        submit.setLayoutY(390);
        submit.setMinHeight(40);
        submit.setMinWidth(100);
        submit.setMaxWidth(100);
        submit.setMaxHeight(40);
        submit.setTextAlignment(TextAlignment.CENTER);
        loginroot.getChildren().add(submit);

        EventHandler<ActionEvent> submitted = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(lockbox.verify(entry.getText())){
                    lockbox.decryptAllGroupNames();
                    submit.setVisible(false);
                    lockbox.config.compileLogs();
                    FadeTransition progress = new FadeTransition(Duration.seconds(2), loginroot);
                    progress.setToValue(0.0);
                    progress.setCycleCount(1);
                    progress.setAutoReverse(false);
                    progress.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            stage.setScene(getMain(stage));
                            new Utils().set(lockbox.config.backgroundPath);
                            setPrimaryColours();
                            FadeTransition progress2 = new FadeTransition(Duration.seconds(2), stage.getScene().getRoot());
                            progress2.setToValue(1);
                            progress2.setCycleCount(1);
                            progress2.setAutoReverse(false);
                            progress2.play();
                        }
                    });
                    progress.play();
                }else{
                    entry.setText("");
                }
            }
        };
        submit.setOnAction(submitted);

        URL mainCss = this.getClass().getResource("/login.css");
        login.getStylesheets().add(mainCss.toExternalForm());

        return login;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private Scene getMain(Stage locker){

        if(lockbox.initialisationCheck()){
            root.setOpacity(0.0);
            lockbox.config.checkWipe();
        }
        root.setId("rootnode");
        main = new Scene(root, 1280, 720);

        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        shadow.setColor(Color.GRAY);

        DropShadow headerShadow = new DropShadow();
        headerShadow.setOffsetX(-5);
        headerShadow.setOffsetY(5);
        headerShadow.setColor(Color.valueOf("#514b5e"));

        dataFade = new FadeTransition(Duration.seconds(1), root);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        header = new HBox();
        header.setEffect(headerShadow);
        timeContainer.getChildren().add(timeLabel);
        timeLabel.setId("timelabel");
        timeContainer.setPadding(new Insets(5,0,0,35));
        header.setMaxWidth(1200);
        header.setMinWidth(1200);
        header.setId("header");
        header.setSpacing(5);
        header.setMinHeight(42);
        header.setMaxHeight(42);
        header.setPadding(new Insets(0,0,0,20));

        header.setLayoutY(20);
        header.setLayoutX(40);

        header.getChildren().add(headerSelections);
        headerSelections.setPadding(new Insets(4,0,3,0));
        headerSelections.setId("headerselect");
        headerSelections.setAlignment(Pos.TOP_CENTER);
        String[] wrapperButtnLabels = {"Accounts", "Cards", "Passcodes", "Files", "Images","Notes"};
        for(int x=0; x<6;x++){
            Button wrapperSelection = new Button(wrapperButtnLabels[x]);
            wrapperSelection.setOpacity(0.75);
            wrapperSelection.setPadding(new Insets(0,5,0,5));
            wrapperSelection.setMinHeight(27);
            wrapperSelection.setMaxHeight(27);
            //wrapperSelection.setMaxWidth(65);
            wrapperSelection.setMinWidth(65);
            headerButtons.add(wrapperSelection);
            final int lambdaCount = x;
            wrapperSelection.setOnAction(e ->{
                populateGroupSelect(enumeration[lambdaCount]);
                globalTypeSelect = wrapperButtnLabels[lambdaCount];
                viewEncrypted=true;
            });
            wrapperSelection.setId("data_select");
            headerSelections.getChildren().add(wrapperSelection);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                timeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss '|' EEE")));
            }
        };
        timer.start();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        VBox circleContainer = new VBox();
        circleContainer.setLayoutY(400);
        circleContainer.setLayoutX(25);
        circleContainer.setSpacing(25);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        panelContainer.setId("panelContainer");
        panelContainer.setLayoutX(210);
        panelContainer.setLayoutY(540);
        panelContainer.setSpacing(50);
        panelContainer.getChildren().add(binPane);
        panelContainer.getChildren().add(keyPane);
        panelContainer.setOpacity(0);
        binPane.setId("buttonPanels");
        binPane.setOpacity(0.7);
        ImageView binn =new ImageView(images.get("bin"));
        binn.setFitHeight(50);
        binn.setFitWidth(50);
        binPane.setCenter(binn);
        binPane.hoverProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                dragWait = true;
                if(!(dragData instanceof String)){
                    dragData = lockbox.lockSingle((Credential) dragData, true);
                }
                if(dragData!=null){
                    String group=null;
                    if(!(dragData instanceof String)){
                        group = ((Credential)dragData).group;
                    }
                    if(dragData instanceof Account){
                        lockbox.deleteData(ACCOUNTS,dragData);
                        if(globalGroupSelect==null){
                            populateDataSelect("default",ACCOUNTS);
                        }else{
                            populateDataSelect(group,ACCOUNTS);
                        }
                        dragData=null;
                        dragWait=false;
                    }
                    if(dragData instanceof BankCard){
                        lockbox.deleteData(BANKS,dragData);
                        if(globalGroupSelect==null){
                            populateDataSelect("default",BANKS);
                        }else{
                            populateDataSelect(group,BANKS);
                        }
                        dragData=null;
                        dragWait=false;
                    }
                    if(dragData instanceof Code){
                        lockbox.deleteData(CODES,dragData);
                        if(globalGroupSelect==null){
                            populateDataSelect("default",CODES);
                        }else{
                            populateDataSelect(group,CODES);
                        }
                        dragData=null;
                        dragWait=false;
                    }
                    if(dragData instanceof UserFile){
                        if(!(tempOpen)){
                            new Utils().deleteFile((UserFile) dragData, null);
                        }
                    }
                    if(dragData instanceof Note){
                        lockbox.deleteData(NOTES, dragData);
                        if(globalGroupSelect==null){
                            populateDataSelect("default",NOTES);
                        }else{
                            populateDataSelect(group,NOTES);
                        }
                        dragData=null;
                        lockbox.save();
                    }
                    if(dragData instanceof String){
                        String checker = (String) dragData;
                        if(checker.equals("All")){

                        }else{
                            new Utils().deleteGroupWindow((String) dragData);
                            dragData=null;
                            lockbox.save();
                        }
                    }
                    lockbox.save();
                    root.getChildren().remove(panelContainer);
                    main.setCursor(new ImageCursor());

                }
            }
        });
        ///////////////////////////////////////////////////////////
        keyPane.setId("buttonPanels");
        keyPane.setOpacity(0.7);
        ImageView keyy =new ImageView(images.get("key"));
        keyy.setFitHeight(50);
        keyy.setFitWidth(50);
        keyPane.setCenter(keyy);
        keyPane.hoverProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                dragWait=true;
                for(Button button: dataButtons){
                    if(((DataButton<Credential>)button).data==dragData){
                        lockbox.lockSingle(((DataButton<Credential>)button).data,((DataButton<Credential>)button).data.encrypted);
                        button.setText(((DataButton<Credential>)button).data.toDialog());
                    }
                }
                dragWait=false;
                dragData=null;
                root.getChildren().remove(panelContainer);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        secureButton.setId("circleButton");
        root.getChildren().add(secureButton);
        secureButton.setLayoutY(643);
        secureButton.setLayoutX(30);
        ImageView keyView = new ImageView(images.get("keyicon"));
        keyView.setFitHeight(25);
        keyView.setFitWidth(25);
        secureButton.setGraphic(keyView);
        secureButton.setEffect(shadow);
        ScaleTransition scale2 = new ScaleTransition();
        scale2.setNode(secureButton);
        secureButton.hoverProperty().addListener(e -> {
            scale2.setByX(0.25);
            scale2.setByY(0.25);
            scale2.setDuration(Duration.millis(500));
            scale2.setCycleCount(2);
            scale2.setAutoReverse(true);
            scale2.play();
        });
        secureButton.setOnMouseClicked(e -> {
            lockbox.lockFromCollection(dataButtons,viewEncrypted);
            lockbox.lockFromCollection(dataButtons,viewEncrypted);
            viewEncrypted = !viewEncrypted;
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        root.getChildren().add(header);
        groups = new ScrollPane();
        groups.setOpacity(0);
        groups.setLayoutX(100);
        groups.setLayoutY(648);
        groups.setMinWidth(120);
        groups.setMaxWidth(1080);
        groups.setId("header");
        groups.setContent(groupSelect);
        groupSelect.setId("headerselect");
        groupSelect.setOnScroll(e -> {
            double delta = e.getDeltaY()*0.005;
            groups.setHvalue(groups.getHvalue() - delta);
        });
        groups.setPadding(new Insets(0,15,0,15));
        groups.setPannable(true);
        groups.setFitToHeight(true);
        groups.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        groups.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.getChildren().add(groups);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        addG.setId("addG");
        ImageView addgGraphic = new ImageView(images.get("addgroup"));
        addgGraphic.setSmooth(true);
        addG.setGraphic(addgGraphic);
        root.getChildren().add(addG);
        addG.setLayoutX(1210);
        addG.setLayoutY(645);
        addG.setOnAction(e ->{
            if(root.getChildren().contains(newGroup)){
                root.getChildren().remove(newGroup);
                return;
            }
            newGroup=new VBox();
            newGroup.setOpacity(0);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300));
            fadeIn.setCycleCount(1);
            fadeIn.setToValue(1);
            fadeIn.setNode(newGroup);
            newGroup.setId("addGPopUp");
            newGroup.setStyle("-fx-border-color: "+lockbox.config.colourHex);
            root.getChildren().add(newGroup);
            newGroup.setLayoutX(1070);
            newGroup.setSpacing(5);
            newGroup.setLayoutY(490);
            newGroup.setPadding(new Insets(10));
            newGroup.getChildren().add(new Label("New Group"));
            TextField groupIn = new TextField();
            groupIn.setStyle("-fx-border-color: "+lockbox.config.colourHex);
            groupIn.setId("groupIn");
            newGroup.getChildren().add(groupIn);
            String[] enuemrate = {"ACCOUNTS","CODES","BANKS","FILES","IMAGES","NOTES"};
            ComboBox<String> groupSelection = new ComboBox(FXCollections.observableArrayList(enuemrate));
            groupSelection.setId("groupSelectionBox");
            groupSelection.setStyle("-fx-border-color: "+lockbox.config.colourHex);
            newGroup.getChildren().add(groupSelection);
            Button subG = new Button("Add");
            subG.setId("subG");
            subG.setOnAction(et->{
                DataType localType;
                String newGroupString = groupIn.getText();
                if(newGroupString=="" |groupSelection.getValue() ==null){
                    root.getChildren().remove(newGroup);
                }else{
                    localType = DataType.valueOf(groupSelection.getValue());
                    ArrayList<String> existingNames = lockbox.groupNamesFromType(localType);
                    for(String name: existingNames){
                        if(name.equals(newGroupString)){
                            groupIn.setText("Already Exists");
                            return;
                        }
                    }
                    lockbox.newGroup(localType, newGroupString);
                    populateGroupSelect(localType);
                    lockbox.save();
                    root.getChildren().remove(newGroup);
                }
            });
            newGroup.getChildren().add(subG);
            fadeIn.play();

        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        dataScroller.setId("dataScroller");
        dataScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        dataScroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        dataScroller.setFitToHeight(true);
        dataScroller.setContent(dataSelect);
        dataScroller.setMaxWidth(1280);
        dataScroller.setMinWidth(1280);
        dataScroller.setPannable(true);
        root.getChildren().add(dataScroller);
        root.getChildren().add(circleContainer);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        MenuButton addData = new MenuButton("New");
        ImageView addGraphic = new ImageView(images.get("addicon"));
        addGraphic.setSmooth(true);
        addData.setGraphic(addGraphic);
        addData.setMaxHeight(27);
        addData.setPadding(new Insets(0));
        addData.setMinHeight(27);
        addData.setOpacity(0.75);
        menus.add(addData);
        addData.setAlignment(Pos.CENTER);
        addData.setId("data_select");
        for(int x=0; x<6; x++){
            ImageView graphic = new ImageView();
            graphic.setFitHeight(20);
            graphic.setFitWidth(20);
            graphic.setSmooth(true);
            MenuItem add = new MenuItem(wrapperButtnLabels[x]);
            if(enumeration[x]==NOTES){
                graphic.setImage(images.get("noteicon"));
                add.setGraphic(graphic);
                add.setOnAction(e->{
                    new NoteWindow().run(new Note(), true);
                });
            }else{
                if(enumeration[x]==BANKS){
                    graphic.setImage(images.get("bankicon"));
                    add.setGraphic(graphic);
                }
                if(enumeration[x]==ACCOUNTS){
                    graphic.setImage(images.get("accounticon"));
                    add.setGraphic(graphic);
                }
                if(enumeration[x]==CODES){
                    graphic.setImage(images.get("codeicon"));
                    add.setGraphic(graphic);
                }
                if(enumeration[x]==FILES){
                    graphic.setImage(images.get("fileicon"));
                    add.setGraphic(graphic);
                }
                if(enumeration[x]==IMAGES){
                    graphic.setImage(images.get("backIcon"));
                    add.setGraphic(graphic);
                }
                add.setOnAction(newDataInput(enumeration[x], false, false));
            }
            menuItems.add(add);
            add.setId("dropdowns");
            addData.getItems().add(add);

        }


        //////////////////////////////////////////////////////
        MenuButton settings = new MenuButton("Options");
        ImageView secGraphic = new ImageView(images.get("machineicon"));
        secGraphic.setFitWidth(20);
        secGraphic.setFitHeight(20);
        secGraphic.setSmooth(true);
        settings.setGraphic(secGraphic);
        settings.setOpacity(0.75);
        settings.setPadding(new Insets(0,3,0,5));
        menus.add(settings);
        settings.setId("data_select");
        settings.setMaxHeight(27);
        settings.setMinHeight(27);
        /////////////////////////////////////////////////////
        MenuItem accessLogs = new MenuItem("Logs Config");
        menuItems.add(accessLogs);
        ImageView logGraphic = new ImageView(images.get("logicon"));
        logGraphic.setFitHeight(20);
        logGraphic.setFitWidth(20);
        logGraphic.setSmooth(true);
        accessLogs.setGraphic(logGraphic);
        accessLogs.setOnAction(e ->{
            new InputOutputWindow().runConfigIO();
        });
        accessLogs.setId("dropdowns");
        settings.getItems().add(accessLogs);
        /////////////////////////////////////////////////////
        MenuItem newPass = new MenuItem("New Password");
        newPass.setOnAction(e ->{
            new InputOutputWindow().runNewKeyIO(lockbox.initialisationCheck());
        });
        menuItems.add(newPass);
        ImageView passGraphic = new ImageView(images.get("keyicon"));
        passGraphic.setFitHeight(20);
        passGraphic.setFitWidth(20);
        passGraphic.setSmooth(true);
        newPass.setGraphic(passGraphic);
        newPass.setId("dropdowns");
        settings.getItems().add(newPass);
        ////////////////////////////////////////////////////
        MenuItem colour = new MenuItem("Colour");
        menuItems.add(colour);
        ImageView colourGraphic = new ImageView(images.get("paint"));
        colourGraphic.setFitHeight(20);
        colourGraphic.setFitWidth(20);
        colourGraphic.setSmooth(true);
        colour.setId("dropdowns");
        colour.setGraphic(colourGraphic);
        colour.setOnAction(e->{
            new ColourWindow().run();
        });
        settings.getItems().add(colour);
        ///////////////////////////////////////////////////
        MenuItem background = new MenuItem("Background");
        ImageView backGraphic = new ImageView(images.get("backIcon"));
        backGraphic.setFitHeight(20);
        backGraphic.setFitWidth(20);
        backGraphic.setSmooth(true);
        menuItems.add(background);
        background.setId("dropdowns");
        background.setGraphic(backGraphic);
        settings.getItems().add(background);
        background.setOnAction(new Utils().setBackground());
        ////////////////////////////////////////////////////
        MenuItem destruct = new MenuItem("Wipe LockBox");
        ImageView destructGraphic = new ImageView(images.get("deleteicon"));
        destructGraphic.setFitHeight(20);
        destructGraphic.setFitWidth(20);
        destructGraphic.setSmooth(true);
        destruct.setGraphic(destructGraphic);
        menuItems.add(destruct);
        destruct.setId("dropdowns");
        settings.getItems().add(destruct);
        destruct.setOnAction(e->{
            new DestructWindow().run();
        });
        ////////////////////////////////////////////////////
        MenuItem exitbtn = new MenuItem("Exit");
        ImageView delGraphic = new ImageView(images.get("power"));
        delGraphic.setFitHeight(20);
        delGraphic.setFitWidth(20);
        delGraphic.setSmooth(true);
        menuItems.add(exitbtn);
        exitbtn.setId("dropdowns");
        exitbtn.setGraphic(delGraphic);
        settings.getItems().add(exitbtn);
        exitbtn.setOnAction(e->{
            System.exit(0);
        });
        ///////////////////////////////////////////////////


        addData.setMaxHeight(27);
        addData.setMinHeight(27);

        settings.setMaxHeight(27);
        settings.setMinHeight(27);

        HBox meuBtns = new HBox();
        meuBtns.setPadding(new Insets(0,0,0,10));

        headerSelections.getChildren().add(meuBtns);
        meuBtns.getChildren().add(addData);
        meuBtns.getChildren().add(settings);
        URL mainCss = this.getClass().getResource("/main.css");
        main.getStylesheets().add(mainCss.toExternalForm());
        search = new HBox();
        search.setId("search");
        search.setAlignment(Pos.CENTER);
        search.setSpacing(5);

        searchBar = new TextField();
        searchBar.setId("label");
        searchBar.setMinWidth(190);
        searchBar.setMaxWidth(190);
        searchBar.setMaxHeight(30);
        searchBar.setMinHeight(30);
        searchBar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                searchBar.setText("");
            }
        });
        searchBar.setText("Search");
        search.getChildren().add(searchBar);

        Button searchEnter = new Button(">");
        headerButtons.add(searchEnter);
        searchEnter.setId("search_enter");
        searchEnter.setMaxHeight(30);
        //searchEnter.setPadding(new Insets(1));
        searchEnter.setMinHeight(30);
        searchEnter.setTextAlignment(TextAlignment.CENTER);
        searchEnter.setOnAction(e->{
            String arg = searchBar.getText();
            ArrayList<Credential> results = lockbox.search(arg);
            populateDataFromCollection(results);
        });
        search.getChildren().add(searchEnter);


        Button accessLogBtn = new Button("History");
        accessLogBtn.setOpacity(0.75);
        headerButtons.add(accessLogBtn);
        accessLogBtn.setOnAction(e->{
            new Logwindow().run();
        });
        accessLogBtn.setId("data_select");
        accessLogBtn.setMaxHeight(27);
        accessLogBtn.setMinHeight(27);
        accessLogBtn.setPadding(new Insets(0,5,0,5));
        meuBtns.getChildren().add(accessLogBtn);
        meuBtns.setSpacing(5);
        new Utils().set(lockbox.config.backgroundPath);
        header.getChildren().add(search);
        header.getChildren().add(timeContainer);

        HBox helpContainer = new HBox();
        Button help = new Button("?");
        help.setOnAction(e->{
            new HelpWindow().run();
        });
        help.setId("search_enter");
        helpContainer.setPadding(new Insets(2.5,0,0,45));
        helpContainer.getChildren().add(help);
        headerButtons.add(help);
        header.getChildren().add(helpContainer);
        setPrimaryColours();

        return main;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void populateGroupSelect(DataType typeSelected){
        DropShadow dataShadow = new DropShadow();
        dataShadow.setOffsetX(3);
        dataShadow.setOffsetY(3);
        dataShadow.setColor(Color.GRAY);
        groupFade = new FadeTransition(Duration.millis(1000), this.groupSelect);
        groupSelect.setAlignment(Pos.CENTER_LEFT);
        final ArrayList<String> groupS = new ArrayList<>();
        switch(typeSelected) {
            case ACCOUNTS:
                groupS.addAll(lockbox.accounts.getGroupNames());
                break;

            case BANKS:
                groupS.addAll(lockbox.banks.getGroupNames());
                break;

            case CODES:
                groupS.addAll(lockbox.codes.getGroupNames());
                break;

            case FILES:
                groupS.addAll(lockbox.files.getGroupNames());
                break;

            case IMAGES:
                groupS.addAll(lockbox.images.getGroupNames());
                break;

            case NOTES:
                groupS.addAll(lockbox.notes.getGroupNames());
                break;

        }
        groupSelect.setOpacity(0.0);
        groupButtons.clear();
        groupSelect.getChildren().clear();
        for(int x=0;x< groupS.size();x++){
            groups.setOpacity(1);
            String group = groupS.get(x);
            GroupButton groupSelection;
            if(group.equals("default")){
                groupSelection = new GroupButton();
                groupSelection.setText("All");
            }else{
                groupSelection = new GroupButton();
                groupSelection.setText(group);
            }
            groupSelection.setOpacity(0.75);
            groupSelection.type = typeSelected;
            groupFade.setToValue(1.0);
            groupFade.setCycleCount(1);
            groupFade.setAutoReverse(false);
            groupButtons.add(groupSelection);
            groupSelection.setMinWidth(100);
            final int lambdaCount = x;
            groupSelection.setOnMouseClicked(e -> {
                if(e.getButton().equals(MouseButton.PRIMARY)){
                    populateDataSelect(groupButtons.get(lambdaCount).getText(),typeSelected);
                    globalGroupSelect = groupSelection.getText();
                }
            });
            groupSelection.setMinHeight(40);
            groupSelection.setMaxHeight(40);
            groupSelection.setId("data_select");

            initGroupButton(groupSelection);

            groupSelect.getChildren().add(groupSelection);
        }
        groupFade.play();
        setPrimaryColours();
        populateDataSelect("default", typeSelected);


    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void initGroupButton(Button groupSelection){
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        FadeTransition panelFade = new FadeTransition(Duration.millis(1000));
        panelFade.setAutoReverse(false);
        panelFade.setCycleCount(1);
        panelFade.setToValue(0.7);
        panelFade.setNode(panelContainer);
        /////////////////////////////////////
        /*
        groupSelection.hoverProperty().addListener(e -> {
            if(dragData!=null){
                if(!(dragData instanceof String)){
                    lockbox.lockSingle((Credential) dragData, true);
                    String sourceGroup = ((Credential) dragData).group;
                    lockbox.migrateData(groupSelection.getText(),dragData, Utils.retrieveEquivalent(globalTypeSelect));
                    populateDataSelect(sourceGroup,Utils.retrieveEquivalent(globalTypeSelect));
                    lockbox.save();


                }
            }
        });
        */
        ///////////////////////////////////
        groupSelection.setOnMousePressed(e -> {
            if(e.getButton().equals(MouseButton.SECONDARY)){
                WritableImage buttonShot = groupSelection.snapshot(params,null);
                main.setCursor(new ImageCursor(buttonShot));
                groupSelection.setOpacity(0);
                if((root.getChildren().contains(panelContainer))){
                    root.getChildren().remove(panelContainer);
                }
                root.getChildren().add(panelContainer);
                panelFade.setToValue(0.7);
                panelFade.play();
                dragData = groupSelection.getText();
            }

        });
        //////////////////////////////////
        groupSelection.setOnMouseDragged(e -> {
        });
        //////////////////////////////////
        groupSelection.setOnMouseReleased(e -> {
            main.setCursor(new ImageCursor());
            groupSelection.setOpacity(0.9);
            new DragClearer().start();
        });
        //////////////////////////////////
        /*
        groupSelection.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {
            long startTime;
            @Override
            public void handle(MouseEvent event) {
                if(event.getEventType().equals(MouseEvent.MOUSE_PRESSED)){
                    startTime = System.currentTimeMillis();
                }else if(event.getEventType().equals(MouseEvent.MOUSE_RELEASED)){
                    if(System.currentTimeMillis()-startTime>150){
                        longPress=true;
                    }else{
                        longPress=false;
                    }
                }
            }
        });
        */
        ///////////////////////////////////

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setPrimaryColours(){
        if(lockbox.config.colourHex==null){
            lockbox.config.colourHex = "orange";
        }
        header.setStyle("-fx-border-color: "+lockbox.config.colourHex);
        groups.setStyle("-fx-border-color: "+lockbox.config.colourHex);
        searchBar.setStyle("-fx-border-color: "+lockbox.config.colourHex);
        binButton.setStyle("-fx-background-color: "+lockbox.config.colourHex);



        BackgroundFill filler = new BackgroundFill(Color.WHITE, new CornerRadii(20),Insets.EMPTY);
        Background backer = new Background(filler);
        //header.setBackground(backer);
        //groups.setBackground(backer);
        //groupSelect.setBackground(backer);

        searchBar.setBackground(backer);
        search.setBackground(backer);



        new Utils().attachAll();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void populateDataSelect(String group, DataType type){

        DropShadow dataShadow = new DropShadow();
        dataShadow.setOffsetX(3);
        dataShadow.setOffsetY(3);
        dataShadow.setColor(Color.GRAY);

        dataFade = new FadeTransition(Duration.millis(300));
        groupSelected = group;
        lockbox.lockFromCollection(dataButtons,false);
        viewEncrypted=true;
        dataButtons.clear();
        dataSelect.getChildren().clear();



        switch(type){
            case ACCOUNTS:
                Group<Account> fetched;
                if(groupSelected.equals("default") || groupSelected==null){
                    fetched = lockbox.accounts.getAll();
                }else{
                    if(groupSelected.equals("All")){
                        fetched = lockbox.accounts.getAll();
                    }else{
                        fetched = lockbox.accounts.getGroup(groupSelected);
                    }
                }
                for(int dataIndex=0; dataIndex< fetched.size();dataIndex++){

                    Account data = fetched.get(dataIndex);
                    lockbox.lockSingle(data,false);
                    DataButton<Account> dataButton = new DataButton<>();
                    dataButton.data=data;
                    dataButton.setText(data.toDialog());
                    initDataButton(dataButton, dataButton.data);
                    Bounds rootBound = dataButton.getLayoutBounds();
                    Bounds absoluteBound = dataButton.localToScene(rootBound);
                    dataButton.origin[0] = absoluteBound.getMinX();
                    dataButton.origin[1] = absoluteBound.getMinY();
                }
                break;
            /////////////////////////////////////////////////////////////////////////
            case BANKS:
                Group<BankCard> bfetched;
                if(groupSelected.equals("default")|| groupSelected==null){
                    bfetched = lockbox.banks.getAll();
                }else{
                    if(groupSelected.equals("All")){
                        bfetched = lockbox.banks.getAll();
                    }else{
                        bfetched = lockbox.banks.getGroup(groupSelected);
                    }
                }
                for(int dataIndex=0; dataIndex< bfetched.size();dataIndex++){
                    BankCard data = bfetched.get(dataIndex);
                    lockbox.lockSingle(data,false);
                    DataButton<BankCard> dataButton = new DataButton<>();
                    dataButton.data=data;
                    dataButton.setText(data.toDialog());
                    initDataButton(dataButton, dataButton.data);
                    Bounds rootBound = dataButton.getLayoutBounds();
                    Bounds absoluteBound = dataButton.localToScene(rootBound);
                    dataButton.origin[0] = absoluteBound.getMinX();
                    dataButton.origin[1] = absoluteBound.getMinY();

                }
                break;
            //////////////////////////////////////////////////////////////////////
            case CODES:
                Group<Code> cfetched;
                if(groupSelected.equals("default")|| groupSelected==null){
                    cfetched = lockbox.codes.getAll();
                }else{
                    if(groupSelected.equals("All")){
                        cfetched = lockbox.codes.getAll();
                    }else{
                        cfetched = lockbox.codes.getGroup(groupSelected);
                    }
                }
                for(int dataIndex=0; dataIndex< cfetched.size();dataIndex++){
                    Code data = cfetched.get(dataIndex);
                    lockbox.lockSingle(data,false);
                    DataButton<Code> dataButton = new DataButton<>();
                    dataButton.data=data;
                    dataButton.setText(data.toDialog());
                    initDataButton(dataButton, dataButton.data);
                    Bounds rootBound = dataButton.getLayoutBounds();
                    Bounds absoluteBound = dataButton.localToScene(rootBound);
                    dataButton.origin[0] = absoluteBound.getMinX();
                    dataButton.origin[1] = absoluteBound.getMinY();
                }
                break;
            ///////////////////////////////////////////////////////////////////////
            case FILES:
                Group<UserFile> ffetched=null;
                if(groupSelected.equals("default")|| groupSelected==null){
                    ffetched = lockbox.files.getAll();
                }else{
                    if(groupSelected.equals("All")){
                        ffetched = lockbox.files.getAll();
                    }else{
                        ffetched = lockbox.files.getGroup(groupSelected);
                    }
                }
                for(int dataIndex=0; dataIndex< ffetched.size();dataIndex++){
                    UserFile data = ffetched.get(dataIndex);
                    lockbox.lockSingle(data,false);
                    DataButton<UserFile> dataButton = new DataButton<>();
                    dataButton.data=data;
                    dataButton.setText(data.toDialog());
                    initDataButton(dataButton, dataButton.data);
                    Bounds rootBound = dataButton.getLayoutBounds();
                    Bounds absoluteBound = dataButton.localToScene(rootBound);
                    dataButton.origin[0] = absoluteBound.getMinX();
                    dataButton.origin[1] = absoluteBound.getMinY();
                }
                break;
            //////////////////////////////////////////////////////////////////////////
            case IMAGES:
                Group<UserFile> ifetched;
                if(groupSelected.equals("default") || groupSelected.equals("All")|| groupSelected==null){
                    ifetched = lockbox.images.getAll();
                }else{
                    ifetched = lockbox.images.getGroup(groupSelected);
                }
                for(int dataIndex=0; dataIndex< ifetched.size();dataIndex++){
                    UserFile data = ifetched.get(dataIndex);
                    lockbox.lockSingle(data,false);
                    DataButton<UserFile> dataButton = new DataButton<>();
                    dataButton.data=data;
                    dataButton.setText(data.toDialog());
                    initDataButton(dataButton, dataButton.data);
                    Bounds rootBound = dataButton.getLayoutBounds();
                    Bounds absoluteBound = dataButton.localToScene(rootBound);
                    dataButton.origin[0] = absoluteBound.getMinX();
                    dataButton.origin[1] = absoluteBound.getMinY();
                }
                break;
            ///////////////////////////////////////////////////////////////////////////
            case NOTES:
                Group<Note> nfetched;
                if(groupSelected.equals("default")||groupSelected==null || groupSelected=="All"){
                    nfetched = lockbox.notes.getAll();
                }else{
                    nfetched = lockbox.notes.getGroup(groupSelected);
                }
                for(int dataIndex=0; dataIndex< nfetched.size();dataIndex++){
                    Note data = nfetched.get(dataIndex);
                    lockbox.lockSingle(data,false);
                    DataButton<Note> dataButton = new DataButton<>();
                    dataButton.data=data;
                    dataButton.setText(data.toDialog());
                    initDataButton(dataButton, dataButton.data);
                    Bounds rootBound = dataButton.getLayoutBounds();
                    Bounds absoluteBound = dataButton.localToScene(rootBound);
                    dataButton.origin[0] = absoluteBound.getMinX();
                    dataButton.origin[1] = absoluteBound.getMinY();
                }
                break;
            ///////////////////////////////////////////////////////////////////////////
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void initDataButton(Button dataButton, Object data){
        DropShadow dataShadow = new DropShadow();
        ImageView graphic=new ImageView();
        if(data instanceof Account){
            graphic.setImage(images.get("accountborder"));
        }else if(data instanceof BankCard){
            graphic.setImage(images.get("bankborder"));
        }else if(data instanceof Code){
            graphic.setImage(images.get("codeborder"));
        }else if(data instanceof Note){
            graphic.setImage(images.get("noteborder"));
        }else{
            if(((UserFile)data).dataType==IMAGES){
                graphic.setImage(images.get("imageborder"));
            }else{
                graphic.setImage(images.get("fileborder"));
            }
        }
        dataButton.setGraphic(graphic);
        dataButton.setAlignment(Pos.BASELINE_LEFT);
        dataShadow.setOffsetX(3);
        dataShadow.setOffsetY(3);
        dataShadow.setColor(Color.GRAY);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        FadeTransition panelFade = new FadeTransition(Duration.millis(1000));
        panelFade.setAutoReverse(false);
        panelFade.setCycleCount(1);
        panelFade.setToValue(0.7);
        panelFade.setNode(panelContainer);
        dataFade = new FadeTransition(Duration.millis(300));

        dataButton.setOnMousePressed(e -> {
            DataType type = null;
            if(data instanceof UserFile && e.getButton()==MouseButton.PRIMARY) {
                if (globalGroupSelect == null) {
                    globalGroupSelect = "default";
                }
                if (((UserFile) data).dataType == IMAGES) {
                    lockbox.lockSingle((Credential) data,true);
                    lockbox.config.logs.add(new AccessLog(Origin.APPLICATION,IMAGES,((Credential)data).group,((Credential)data).domain));
                    lockbox.config.saveLogs();
                    new ImageWindow(lockbox.images.getGroup(((Credential)data).group), data);
                    return;
                }else{
                    type=FILES;
                }
            }
            if(data instanceof Note && e.getButton()==MouseButton.PRIMARY){
                lockbox.lockSingle((Credential) data,true);
                lockbox.config.logs.add(new AccessLog(Origin.APPLICATION,NOTES,((Credential)data).group,((Credential)data).domain));
                lockbox.config.saveLogs();
                new NoteWindow().run((Note)data, false);
                return;
            }
            if(e.getButton().equals(MouseButton.PRIMARY) && type !=IMAGES && type != NOTES){
                openDataPanel((Credential) data);
                return;
            }else{
                WritableImage buttonShot = dataButton.snapshot(params,null);
                main.setCursor(new ImageCursor(buttonShot));
                dataButton.setOpacity(0);
                if((root.getChildren().contains(panelContainer))){
                    root.getChildren().remove(panelContainer);
                }
                root.getChildren().add(panelContainer);
                panelFade.setToValue(0.7);
                panelFade.play();

                dragData = data;
            }
        });
        dataButton.setOnMouseDragged(e -> {

        });
        dataButton.setOnMouseReleased(e -> {
            if(e.getButton().equals(MouseButton.SECONDARY)){
                main.setCursor(new ImageCursor());
                dataButton.setOpacity(0.9);
                new DragClearer().start();
            }
        });
        dataButton.setOpacity(0.0);
        dataFade = new FadeTransition(Duration.millis(300), dataButton);
        dataFade.setToValue(0.9);
        dataFade.setAutoReverse(false);
        dataFade.setCycleCount(1);
        dataButtons.add(dataButton);
        if(!(data instanceof Account || data instanceof BankCard)){
            dataButton.setId("other_select");
        }else{
            dataButton.setId("object_select");
        }
        dataButton.setEffect(dataShadow);
        dataSelect.getChildren().add(dataButton);
        dataFade.play();

        setPrimaryColours();

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void populateDataFromCollection(ArrayList<Credential> results){
        dataButtons.clear();
        viewEncrypted=false;
        if(results==null){
            return;
        }
        dataSelect.getChildren().clear();
        for(Credential data: results){
            DataButton<Credential> dataButton = new DataButton<>();
            dataButton.data=data;
            dataButton.setText(data.toDialog());
            initDataButton(dataButton,data);
        }
        globalTypeSelect="default";
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void openDataPanel(Credential data){
        String group;
        String domain;
        if(data.encrypted){
            group = lockbox.security.decrypt(data.group);
            domain = lockbox.security.decrypt(data.domain);
        }else{
            group = data.group;
            domain = data.domain;
        }
        AccessLog newLog = new AccessLog(Origin.APPLICATION, new Utils().classAssignment(data),group,domain);
        lockbox.config.logs.add(newLog);
        lockbox.config.saveLogs();
        lockbox.lockSingle(data,true);
        new InputOutputWindow(data).run();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private EventHandler<ActionEvent> newDataInput(DataType type, boolean groupIn, boolean colourIn){
        EventHandler<ActionEvent> openInput;
        openInput = event -> {
            new InputOutputWindow(type, groupIn, colourIn).run();
        };
        return openInput;

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class Utils{

        public EventHandler<ActionEvent> setBackground(){

            EventHandler<ActionEvent> handler = event -> {

                Stage temp = new Stage();
                temp.initOwner(locker);
                temp.initModality(Modality.WINDOW_MODAL);
                temp.setTitle("Select Background");
                temp.setResizable(false);
                temp.getIcons().add(images.get("Logo"));

                BorderPane background = new BorderPane();
                background.setId("root");
                Scene tempScene = new Scene(background, 640,480);
                tempScene.getStylesheets().add(this.getClass().getResource("/background.css").toExternalForm());
                temp.setScene(tempScene);

                VBox containment = new VBox();
                containment.setSpacing(10);
                ImageView view = new ImageView();
                view.setId("view");
                view.setFitWidth(640);
                view.setFitHeight(360);
                containment.getChildren().add(view);
                background.setCenter(containment);
                HBox path = new HBox();
                path.setPadding(new Insets(0,0,0,100));
                path.setSpacing(10);
                path.setLayoutX(150);
                path.setLayoutY(310);
                containment.getChildren().add(path);
                TextField pathInput = new TextField();
                path.getChildren().add(pathInput);
                Button select = new Button("select");
                select.setId("select");
                select.setOnAction(e->{
                    FileChooser chooser = new FileChooser();
                    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Images",new String[]{"*.jpg","*.png"});
                    chooser.getExtensionFilters().add(filter);
                    File selection = chooser.showOpenDialog(locker);
                    if(!(selection==null)){
                        try{
                            view.setImage(new Image(new File(selection.getAbsolutePath()).toURI().toURL().toExternalForm()));
                            selectedPath=new File(selection.getAbsolutePath()).toURI().toURL().toExternalForm();
                            pathInput.setText(selectedPath);
                        }catch(Exception ex){

                        }
                    }
                });
                Button submit = new Button("Set");
                submit.setOnAction(e->{
                    if(!(selectedPath==null)){
                        set(selectedPath);
                        lockbox.config.saveLogs();
                        temp.close();
                        selectedPath=null;
                    }
                });
                submit.setId("select");
                path.getChildren().add(select);
                path.getChildren().add(submit);
                pathInput.setId("input");
                pathInput.setStyle("-fx-border-color: "+lockbox.config.colourHex);
                Label info = new Label("• Background Image should be 1280x720 or in a 16:9 Aspect Ratio");
                info.setId("label");
                containment.getChildren().add(info);
                /////////////////////////////////////////////////////////
                temp.show();
            };
            return  handler;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public void set(String selection){
            if(selection==null){
                selection = "/default.png";
            }
            BackgroundImage backImg=null;
            if(selection.equals("/default.png")){
                try{
                    backImg = new BackgroundImage(new Image(getClass().getResource(selection).toURI().toString(),1280,720,false,false), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
                }catch(Exception e){

                }
            }else{
                backImg = new BackgroundImage(new Image(selection,1280,720,false,false), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
            }

            root.setBackground(new Background(backImg));
            lockbox.config.backgroundPath = selection;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public DataType retrieveEquivalent(String equiv){
            if(equiv==null){
                return ACCOUNTS;
            }
            if(equiv.equals("Accounts")){
                return ACCOUNTS;
            }else if(equiv.equals("Cards")){
                return BANKS;
            }else if(equiv.equals("Passcodes")){
                return CODES;
            }else if(equiv.equals("Files")){
                return FILES;
            }else if(equiv.equals("Images")){
                return IMAGES;
            }else{
                return NOTES;
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private void attachColours(ArrayList<Button> buttons){
            for(Button btn: buttons){
                btn.styleProperty().bind(
                        Bindings
                                .when(btn.hoverProperty())
                                .then(
                                        new SimpleStringProperty("-fx-background-color: "+lockbox.config.colourHex)
                                )
                                .otherwise(
                                        new SimpleStringProperty("-fx-background-color: white")
                                )
                );
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public void attachAll(){
            attachColours(groupButtons);
            attachColours(dataButtons);
            attachColours(headerButtons);

            for(MenuButton btn: menus){
                btn.styleProperty().bind(
                        Bindings
                                .when(btn.hoverProperty())
                                .then(
                                        new SimpleStringProperty("-fx-background-color: "+lockbox.config.colourHex)
                                )
                                .otherwise(
                                        new SimpleStringProperty("-fx-background-color: white")
                                )
                );
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public boolean checkForContents(TextField field){
            if(field.getText().equals("") || field.getText()==null){
                return false;
            }
            return true;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public DataType classAssignment(Object data){
            if(data instanceof Account){
                return ACCOUNTS;
            }else if(data instanceof BankCard){
                return BANKS;
            }else if(data instanceof Code){
                return CODES;
            }else if(data instanceof UserFile){
                if(((UserFile)data).dataType.equals(FILES)){
                    return FILES;
                }else{
                    return IMAGES;
                }
            }else if(data instanceof Note){
                return NOTES;
            }
            return  DEFAULT;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public void deleteGroupWindow(String removing){
            Stage temp = new Stage();
            temp.initOwner(locker);
            temp.initModality(Modality.WINDOW_MODAL);
            temp.setTitle("Delete Group");
            temp.setResizable(false);
            temp.getIcons().add(images.get("Logo"));

            AnchorPane tempRoot = new AnchorPane();
            tempRoot.setStyle("-fx-background-color: #514b5e");
            Scene tempScene = new Scene(tempRoot, 150,75);
            temp.setScene(tempScene);

            HBox outerContainer = new HBox();
            outerContainer.setSpacing(10);
            tempRoot.getChildren().add(outerContainer);
            outerContainer.setPadding(new Insets(10,10,10,10));

            VBox container = new VBox();
            container.setSpacing(10);
            container.setPadding(new Insets(5,5,5,5));
            outerContainer.getChildren().add(container);

            RadioButton keep = new RadioButton("Keep Data");
            keep.setTextFill(Color.WHITE);
            RadioButton destroy = new RadioButton("Discard");
            destroy.setTextFill(Color.WHITE);
            keep.setSelected(true);

            ToggleGroup toggler = new ToggleGroup();

            keep.setToggleGroup(toggler);
            destroy.setToggleGroup(toggler);

            container.getChildren().add(keep);
            container.getChildren().add(destroy);

            HBox buttonContainer = new HBox();
            buttonContainer.setPadding(new Insets(15,10,10,5));

            Button submit = new Button("✓");
            submit.styleProperty().bind(Bindings.when(submit.hoverProperty()).then(new SimpleStringProperty("-fx-background-color: grey")).otherwise(new SimpleStringProperty("-fx-background-color: "+lockbox.config.colourHex)));
            submit.setOnAction(e -> {
                lockbox.deleteGroup(removing,retrieveEquivalent(globalTypeSelect),getToggleResult(toggler));
                populateGroupSelect(retrieveEquivalent(globalTypeSelect));
                temp.close();
            });
            buttonContainer.getChildren().add(submit);
            outerContainer.getChildren().add(buttonContainer);

            temp.show();


        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public boolean getToggleResult(ToggleGroup toggler){
            RadioButton retrieved = (RadioButton) toggler.getSelectedToggle();
            if(retrieved.getText().equals("Discard")){
                return false;
            }else{
                return true;
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public void deleteFile(UserFile data, Stage io){
            tempOpen=true;
            dragWait=true;
            Stage temp = new Stage();
            temp.initOwner(locker);
            temp.initModality(Modality.WINDOW_MODAL);
            temp.setTitle("Delete File");
            temp.setResizable(false);
            temp.getIcons().add(images.get("Logo"));

            AnchorPane tempRoot = new AnchorPane();
            tempRoot.setStyle("-fx-background-color: #514b5e");
            Scene tempScene = new Scene(tempRoot, 150,75);
            temp.setScene(tempScene);

            HBox outerContainer = new HBox();
            outerContainer.setSpacing(10);
            tempRoot.getChildren().add(outerContainer);
            outerContainer.setPadding(new Insets(10,10,10,10));

            VBox container = new VBox();
            container.setSpacing(10);
            container.setPadding(new Insets(5,5,5,5));
            outerContainer.getChildren().add(container);

            RadioButton keep = new RadioButton("Export");
            keep.setTextFill(Color.WHITE);
            RadioButton destroy = new RadioButton("Discard");
            destroy.setTextFill(Color.WHITE);
            keep.setSelected(true);

            ToggleGroup toggler = new ToggleGroup();

            keep.setToggleGroup(toggler);
            destroy.setToggleGroup(toggler);

            container.getChildren().add(keep);
            container.getChildren().add(destroy);

            HBox buttonContainer = new HBox();
            buttonContainer.setPadding(new Insets(15,10,10,5));

            Button submit = new Button("✓");
            submit.styleProperty().bind(Bindings.when(submit.hoverProperty()).then(new SimpleStringProperty("-fx-background-color: grey")).otherwise(new SimpleStringProperty("-fx-background-color: "+lockbox.config.colourHex)));
            submit.setOnAction(e->{
                DataType fileT = data.dataType;
                if(getToggleResult(toggler)){
                    DirectoryChooser dirChoose = new DirectoryChooser();
                    File destination = dirChoose.showDialog(locker);
                    if(destination==null){
                        dragWait=false;
                        return;
                    }else{
                        data.destination=destination.getAbsolutePath();
                        lockbox.deleteData(fileT,dragData);
                        populateDataSelect("default",fileT);
                        temp.close();
                        tempOpen=false;
                        lockbox.save();
                        dragWait=false;
                    }
                }else{
                    lockbox.lockSingle(data,true);
                    File removing = new File(data.generateInternalPath());
                    removing.delete();
                    if(dragData!=null){
                        lockbox.deleteData(fileT,dragData);
                    }else{
                        lockbox.deleteData(fileT,data);
                    }
                    populateDataSelect("default",fileT);
                    temp.close();
                    tempOpen=false;
                    lockbox.save();
                    dragWait=false;
                }
                if(io!=null){
                    io.close();
                }
            });

            buttonContainer.getChildren().add(submit);
            outerContainer.getChildren().add(buttonContainer);

            temp.show();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class DestructWindow{

        Button activator;

        Stage destructStage;

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public void run(){
            destructStage = new Stage();

            DropShadow dataShadow = new DropShadow();
            dataShadow.setOffsetX(-4);
            dataShadow.setOffsetY(4);
            dataShadow.setColor(Color.BLACK);

            destructStage.setTitle("LockBox Data Wipe");
            destructStage.setResizable(false);
            destructStage.getIcons().add(images.get("Logo"));
            destructStage.initOwner(locker);
            destructStage.initModality(Modality.WINDOW_MODAL);

            BorderPane rootP = new BorderPane();
            rootP.setId("rootP");
            rootP.getStylesheets().add(this.getClass().getResource("/destruct.css").toExternalForm());
            Scene mainScene = new Scene(rootP,500,300);
            rootP.setBackground(new Background(new BackgroundFill(Color.valueOf("#514b5e"),CornerRadii.EMPTY,Insets.EMPTY)));
            destructStage.setScene(mainScene);

            VBox inputContainer = new VBox();
            rootP.setCenter(inputContainer);
            inputContainer.setAlignment(Pos.CENTER);
            inputContainer.setSpacing(20);
            Label header = new Label("Confirm Master Password");
            header.setId("label");
            inputContainer.getChildren().add(header);
            PasswordField input1 = new PasswordField();
            input1.setStyle("-fx-border-color: "+lockbox.config.colourHex);
            input1.setEffect(dataShadow);
            inputContainer.getChildren().add(input1);
            input1.setId("passwordIn");
            PasswordField input2 = new PasswordField();
            input2.setEffect(dataShadow);
            input2.setStyle("-fx-border-color: "+lockbox.config.colourHex);
            inputContainer.getChildren().add(input2);
            input2.setId("passwordIn");
            activator=new Button("Destroy");
            activator.setOnAction(e->{
                if(input1.getText().equals(input2.getText()) && lockbox.verify(input1.getText())){
                    String userdir = System.getenv("APPDATA");
                    File savedir = new File(userdir+"\\LockData\\Safe\\Creds");
                    File[] wrappers = savedir.listFiles();
                    for(File wrapper: wrappers){
                        wrapper.delete();
                    }
                    File filedir = new File(userdir+"\\LockData\\Safe\\UserF");
                    File[] filedirs = filedir.listFiles();
                    for(File userf: filedirs){
                        userf.delete();
                    }
                    filedir.delete();
                    savedir.delete();
                    File top = new File(userdir+"\\LockData\\Safe");
                    top.delete();
                    File root = new File(userdir+"\\LockData\\log.bin");
                    root.delete();
                    File finish = new File(userdir+"\\LockData");
                    finish.delete();
                    System.exit(0);
                }
            });
            activator.setId("confirm");
            activator.setEffect(dataShadow);
            inputContainer.getChildren().add(activator);

            destructStage.show();

        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class Logwindow{

        DatePicker dateSelection;

        VBox logsContainer;
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public void run(){
            Stage logStage = new Stage();

            DropShadow dataShadow = new DropShadow();
            dataShadow.setOffsetX(-4);
            dataShadow.setOffsetY(4);
            dataShadow.setColor(Color.BLACK);

            logStage.setTitle("History");
            logStage.setResizable(false);
            logStage.getIcons().add(images.get("Logo"));
            logStage.initOwner(locker);
            logStage.initModality(Modality.WINDOW_MODAL);

            AnchorPane rootPane = new AnchorPane();
            rootPane.setId("root");
            Scene logOutput = new Scene(rootPane, 660, 480);
            logOutput.getStylesheets().add(this.getClass().getResource("/logOutput.css").toExternalForm());
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            HBox logHeader = new HBox();
            logHeader.setId("logHeader");
            logHeader.setEffect(dataShadow);
            logHeader.setMaxHeight(40);
            logHeader.setMinHeight(40);
            HBox innerHeader = new HBox();
            logHeader.getChildren().add(innerHeader);
            logHeader.setPadding(new Insets(0,10,0,10));
            innerHeader.setAlignment(Pos.TOP_CENTER);
            logHeader.setStyle("-fx-border-color: "+lockbox.config.colourHex);
            innerHeader.setSpacing(2);
            innerHeader.setPadding(new Insets(4,0,15,0));
            rootPane.getChildren().add(logHeader);
            rootPane.setId("root");
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            VBox column1 = new VBox();
            Label header = new Label("Access History");
            header.setId("header");
            HBox row2 = new HBox();
            row2.getChildren().add(header);
            column1.getChildren().add(row2);
            HBox row1 = new HBox();
            column1.getChildren().add(row1);
            column1.setSpacing(7);
            column1.setPadding(new Insets(0,0,0,20));
            dateSelection = new DatePicker();
            //dateSelection.setEffect(dataShadow);
            dateSelection.getEditor().setEditable(false);
            row1.setSpacing(15);
            row1.setPadding(new Insets(0,0,0,50));
            innerHeader.getChildren().add(column1);
            Button searchDateenter = new Button();
            ImageView enterGraphic = new ImageView(images.get("searchicon"));
            searchDateenter.setGraphic(enterGraphic);
            searchDateenter.setId("searchData_enter");
            //searchDateenter.setEffect(dataShadow);
            HBox searchBox = new HBox();
            searchBox.getChildren().add(dateSelection);
            searchBox.getChildren().add(searchDateenter);
            row1.getChildren().add(searchBox);
            searchDateenter.setOnAction(e->{
                searchLogs();
            });

            Label nextwipe = new Label("Next Wipe: "+lockbox.config.nextLogWipe);
            nextwipe.setLayoutX(240);
            nextwipe.setLayoutY(30);
            nextwipe.setId("label");
            row1.setSpacing(25);
            row1.getChildren().add(nextwipe);

            Label interval = new Label("Interval: "+lockbox.config.interval);
            interval.setId("label");
            row1.getChildren().add(interval);


            ScrollPane scroller = new ScrollPane();
            scroller.setOpaqueInsets(new Insets(10));
            scroller.setLayoutX(25);
            scroller.setLayoutY(80);
            scroller.setId("scrollLog");
            scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scroller.setMaxWidth(640);
            scroller.setMinWidth(640);
            scroller.setMaxHeight(400);
            scroller.setMinHeight(400);
            rootPane.getChildren().add(scroller);

            logsContainer = new VBox();
            logsContainer.setSpacing(10);
            logsContainer.setId("container");
            logsContainer.setPadding(new Insets(50,50,50,20));
            logsContainer.setAlignment(Pos.CENTER_LEFT);
            scroller.setContent(logsContainer);

            build(lockbox.config.logs);

            logStage.setScene(logOutput);
            logStage.show();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private void searchLogs(){
            ArrayList<AccessLog> result=new ArrayList<>();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            if(dateSelection.getValue()==null){
                return;
            }
            String selectedDate = dateSelection.getValue().format(format);
            for(AccessLog log: lockbox.config.logs){
                String[] dateFetch = log.getAccessTime().split("\\s");
                if(dateFetch[0].equals(selectedDate)){
                    result.add(log);
                }
            }
            build(result);

        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private void build(ArrayList<AccessLog> logs){
            DropShadow dataShadow2 = new DropShadow();
            dataShadow2.setOffsetX(-4);
            dataShadow2.setOffsetY(4);
            dataShadow2.setColor(Color.BLACK);
            logsContainer.getChildren().clear();
            logsContainer.setSpacing(30);
            for(AccessLog log: logs){
                HBox logLine = new HBox();
                logLine.setSpacing(15);
                logLine.setEffect(dataShadow2);
                logLine.setPadding(new Insets(15,0,0,10));
                logLine.setStyle("-fx-border-color: "+lockbox.config.colourHex);
                logLine.setId("liner");
                ImageView graphic = new ImageView();
                if(log.getAccessType()==ACCOUNTS){
                    graphic.setImage(images.get("accounticon"));
                }else if(log.getAccessType()==CODES){
                    graphic.setImage(images.get("codeicon"));
                }else if(log.getAccessType()==BANKS){
                    graphic.setImage(images.get("bankicon"));
                }else if(log.getAccessType()==NOTES){
                    graphic.setImage(images.get("noteicon"));
                }else if(log.getAccessType()==IMAGES){
                    graphic.setImage(images.get("backIcon"));
                }else if(log.getAccessType()==FILES){
                    graphic.setImage(images.get("fileicon"));
                }
                logLine.getChildren().add(graphic);

                VBox datamain = new VBox();
                datamain.setSpacing(5);
                logLine.getChildren().add(datamain);
                Label dataname = new Label(log.getAccessData());
                dataname.setId("subheader");
                datamain.getChildren().add(dataname);
                Label group = new Label("Group: "+log.getAccessGroup());
                group.setId("group");
                datamain.getChildren().add(group);

                Label time = new Label(log.getAccessTime());
                time.setId("time");
                logLine.getChildren().add(time);

                logsContainer.getChildren().add(logLine);
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class ColourWindow{

        private Stage window;

        private GridPane selector;

        private String selected="#ff9900";
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public void run(){
            ////////////////////////////////////////////////////
            AnchorPane colourRoot = new AnchorPane();
            colourRoot.setId("root");
            Scene colourScene = new Scene(colourRoot,400,400);
            window = new Stage();
            window.getIcons().add(images.get("Logo"));
            window.setResizable(false);
            window.setTitle("Colour");
            window.setScene(colourScene);
            window.initOwner(locker);
            window.initModality(Modality.WINDOW_MODAL);
            colourScene.getStylesheets().add(this.getClass().getResource("/colourwindow.css").toExternalForm());
            ////////////////////////////////////////////////////
            selector=new GridPane();
            selector.setLayoutX(90);
            selector.setLayoutY(100);
            selector.setHgap(10);
            selector.setVgap(10);
            colourRoot.getChildren().add(selector);
            int x=0;
            int y=0;
            final String[] hexColours={"#ff0000","#cc0000","#0000cc","#0000ff","#00cc00","#006600","#ff9900","#ff8000","#ff00ff","#cc00cc","#6600cc","#6600ff"};
            for(String hex: hexColours){
                Button c = new Button();
                c.setId("button");
                c.setStyle("-fx-background-color: "+hex);
                selector.add(c,x,y);
                c.setOnAction(e->{
                    selected=hex;
                });
                if(x==3){
                    x=0;
                    y++;
                    continue;
                }
                x++;

            }
            Button submit = new Button("Set");
            submit.setId("select");
            submit.setLayoutY(300);
            submit.setLayoutX(185);
            submit.setOnAction(e->{
                lockbox.config.colourHex=selected;
                setPrimaryColours();
                lockbox.config.saveLogs();
                window.close();
            });
            colourRoot.getChildren().add(submit);
            window.show();

        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class NoteWindow{

        private Stage window;

        private TextArea editor;

        private Note data;

        private TextField noteName;

        private ComboBox<String> groupIn;

        private boolean newNote;

        private boolean bulleted=false;

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public void run(Note data, boolean New){
            this.data = data;
            this.newNote=New;

            AnchorPane noteRoot = new AnchorPane();
            noteRoot.setId("root");
            noteRoot.setStyle("-fx-background-color: "+lockbox.config.colourHex);
            noteRoot.setOpacity(0.8);
            Scene noteScene = new Scene(noteRoot, 400,200);
            window = new Stage();
            window.setOnCloseRequest(e->{
                if(data.domain==null || data.text==null){

                }else{
                    lockbox.lockSingle(data,false);
                    save();
                    globalTypeSelect="Notes";
                }
            });
            window.setFullScreen(false);
            window.setMaximized(false);
            window.getIcons().add(images.get("Logo"));
            window.setTitle("Note");
            window.setMinWidth(400);
            window.setMaxWidth(400);
            window.setScene(noteScene);
            noteScene.getStylesheets().add(this.getClass().getResource("/note.css").toExternalForm());
            window.maximizedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue)
                    window.setMaximized(false);
            });

            VBox innerWindow = new VBox();
            noteRoot.getChildren().add(innerWindow);
            /////////////////////////////////////////////////////////
            HBox details = new HBox();
            details.setId("details");
            details.setOpacity(0.85);
            innerWindow.getChildren().add(details);
            noteName = new TextField();
            if(data.domain!=null){
                noteName.setText(data.domain);
            }
            noteName.setId("detailIn");
            Button save = new Button("Save");
            save.setOnAction(e->{
                save();
            });
            save.setId("save");
            details.setSpacing(10);
            ArrayList<String> groups = lockbox.notes.getGroupNames();
            groupIn = new ComboBox<>(FXCollections.observableArrayList(groups));
            details.getChildren().add(noteName);
            details.getChildren().add(groupIn);
            details.getChildren().add(save);
            groupIn.setId("groupIn");
            if(data.group==null){

            }else{
                groupIn.setValue(data.group);
            }
            ///////////////////////////////////////////////////////
            editor = new TextArea();
            editor.setId("editor");
            editor.setWrapText(true);
            editor.maxHeightProperty().bind(noteRoot.heightProperty());
            editor.minHeightProperty().bind(noteRoot.heightProperty());
            if(data.text!=null){
                editor.setText(data.text);
            }
            innerWindow.getChildren().add(editor);

            if(data.group==null || data.group.equals("")){
                data.group="default";
            }


            window.show();
            editor.lookup(".content").setStyle("-fx-background-color: "+lockbox.config.colourHex);
            ScrollBar bar = (ScrollBar) editor.lookup(".scroll-bar:vertical");
            bar.setDisable(true);
            bar.setVisible(false);
            bar.setOpacity(0);

        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public void save(){
            lockbox.lockSingle(data,true);
            if(noteName.getText()==null || noteName.getText().equals("")){
                noteName.setText("Provide Name");
                return;
            }
            data.domain = noteName.getText();

            if(editor.getText()==null || editor.getText().equals("")){
                editor.setText("Empty Note");
                return;
            }
            data.text = editor.getText();
            if(newNote){
                if(groupIn.getItems()==null){
                    data.group="default";
                }else{
                    data.group=groupIn.getValue();
                }
                Group<Note> selectedGroup = lockbox.notes.getGroup(data.group);
                selectedGroup.add(data);
                populateGroupSelect(NOTES);
                populateDataSelect(groupIn.getValue(),NOTES);
                newNote=false;
            }else{
                if(!(data.group.equals(groupIn.getValue()))){
                    lockbox.migrateData(groupIn.getValue(),data,NOTES);
                }
                populateGroupSelect(NOTES);
                globalTypeSelect="Notes";
                populateDataSelect(groupIn.getValue(),NOTES);
            }
            lockbox.save();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class HelpWindow{

        private Stage window;

        private ScrollPane content;
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public void run(){

            AnchorPane helpRoot = new AnchorPane();
            helpRoot.setId("root");
            Scene helpScene = new Scene(helpRoot,800,400);
            window = new Stage();
            window.getIcons().add(images.get("Logo"));
            window.setResizable(false);
            window.setTitle("Help");
            window.setScene(helpScene);
            window.initOwner(locker);
            window.initModality(Modality.WINDOW_MODAL);
            helpScene.getStylesheets().add(this.getClass().getResource("/help.css").toExternalForm());

            content = new ScrollPane();
            content.setLayoutX(200);
            content.setLayoutY(0);
            content.setId("content");
            content.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            content.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            helpRoot.getChildren().add(content);

            VBox contentSelection = new VBox();
            helpRoot.getChildren().add(contentSelection);
            contentSelection.setId("contentSelector");
            contentSelection.setLayoutX(0);
            contentSelection.setLayoutY(0);
            String[] sectionText = {"How It Works","Deleting","Groups","Security","History","Customisation"};
            for(String section: sectionText){
                ImageView graphic=new ImageView();
                graphic.setFitHeight(20);
                graphic.setFitWidth(20);
                graphic.setSmooth(true);
                Button sectionB = new Button(section);
                sectionB.setAlignment(Pos.BASELINE_LEFT);
                switch (section){
                    case "Security":
                        graphic.setImage(images.get("keyicon"));
                        sectionB.setGraphic(graphic);
                        break;
                    case "Deleting":
                        graphic.setImage(images.get("deleteicon"));
                        sectionB.setGraphic(graphic);
                        break;
                    case "History":
                        graphic.setImage(images.get("historyicon"));
                        sectionB.setGraphic(graphic);
                        break;
                    case "How It Works":
                        graphic.setImage(images.get("machineicon"));
                        sectionB.setGraphic(graphic);
                        break;
                    case "Groups":
                        graphic.setImage(images.get("foldericon"));
                        sectionB.setGraphic(graphic);

                    case "Customisation":
                        graphic.setImage(images.get("customicon"));
                        sectionB.setGraphic(graphic);

                }
                contentSelection.getChildren().add(sectionB);
                sectionB.setWrapText(true);
                sectionB.setId("selector");
                sectionB.setOnMouseClicked(e->{
                    content.setContent(setContent(section));
                    content.setVvalue(0);
                });
            }
            Label version = new Label("©Vigilance LockBox V1.0");
            version.setId("version");
            version.setPadding(new Insets(0,0,10,10));
            //version.setAlignment(Pos.BASELINE_LEFT);
            contentSelection.getChildren().add(version);
            content.setContent(getworkings());
            window.show();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public VBox setContent(String section){
            if(section.equals("How It Works")){
                return getworkings();
            }
            else if(section.equals("Deleting")){
                return getdelete();
            }
            else if(section.equals("Groups")){
                return getGroupContent();
            }
            else if(section.equals("Security")){
                return getSecurityContent();
            }
            else if(section.equals("History")){
                return getHistoryContent();
            }
            else{
                return getCustomContent();
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private VBox getCustomContent(){
            VBox content = new VBox();
            content.setMaxHeight(550);
            content.setMinHeight(550);
            content.setSpacing(10);
            content.setAlignment(Pos.BASELINE_LEFT);
            content.setId("content");
            content.setPadding(new Insets(20));

            Label header = new Label("Customisation");
            content.getChildren().add(header);
            header.setId("header");

            Label sub0 = new Label("Background");
            sub0.setId("subheader");
            content.getChildren().add(sub0);

            HBox para0C = new HBox();
            Text para0 = new Text("LockBox allows you to select your own background image if you so wish. This feature can be found in the settings tab and will allow you to select png and jpg image types. It is best to select an image of 1280x720 or higher for better background quality. It is also important to select an image in a 16:9 aspect ratio. This just means for every 16 pixels in length, there should be 9 pixels in height.");
            para0.setId("paragraph");
            para0.setFill(Color.WHITE);
            para0.setWrappingWidth(560);
            para0C.getChildren().add(para0);
            content.getChildren().add(para0C);

            Label sub1 = new Label("Colour");
            sub1.setId("subheader");
            content.getChildren().add(sub1);

            HBox para1C = new HBox();
            Text para1 = new Text("You can also select the colour of certain screen elements. Colour selection can be found in the settings tab. You can select any colour you wish and this colour is applied to the header border and also button hover colours.");
            para1.setId("paragraph");
            para1.setFill(Color.WHITE);
            para1.setWrappingWidth(560);
            para1C.getChildren().add(para1);
            content.getChildren().add(para1C);

            return content;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private VBox getHistoryContent(){
            VBox content = new VBox();
            content.setMaxHeight(550);
            content.setMinHeight(550);
            content.setSpacing(10);
            content.setAlignment(Pos.BASELINE_LEFT);
            content.setId("content");
            content.setPadding(new Insets(20));

            Label header = new Label("Access History");
            content.getChildren().add(header);
            header.setId("header");

            Label sub0 = new Label("What is History");
            sub0.setId("subheader");
            content.getChildren().add(sub0);

            HBox para0C = new HBox();
            Text para0 = new Text("LockBox comes with a history feature, which records whenever data is viewed. The group, name and time of access are included in the log so you know whenever specific data has been accessed. What makes this a good feature is the fact that these history objects cannot be deleted, not even by you. So even if someone gains your password and looks at your credentials or files, all you need to  do is regularly look at your logs for any strange accesses like when you`ve not been home or when someone was using your device.");
            para0.setId("paragraph");
            para0.setFill(Color.WHITE);
            para0.setWrappingWidth(560);
            para0C.getChildren().add(para0);
            content.getChildren().add(para0C);

            Label sub1 = new Label("Configuring your History");
            sub1.setId("subheader");
            content.getChildren().add(sub1);

            HBox para1C = new HBox();
            Text para1 = new Text("Although you cannot individually delete these logs, there is a wiping feature where you select a time interval when LockBox will wipe the history. You can select intervals from 2 weeks to 3 months. The date of the next wipe is included in the history window as well as your selected interval. The default interval is 2 weeks.");
            para1.setId("paragraph");
            para1.setFill(Color.WHITE);
            para1.setWrappingWidth(560);
            para1C.getChildren().add(para1);
            content.getChildren().add(para1C);

            return content;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public VBox getSecurityContent(){
            VBox content = new VBox();
            content.setMaxHeight(500);
            content.setMinHeight(500);
            content.setSpacing(10);
            content.setAlignment(Pos.BASELINE_LEFT);
            content.setId("content");
            content.setPadding(new Insets(20));

            Label header = new Label("Security");
            content.getChildren().add(header);
            header.setId("header");

            Label sub0 = new Label("How to encrypt/decrypt");
            sub0.setId("subheader");
            content.getChildren().add(sub0);

            HBox para0C = new HBox();
            Text para0 = new Text("There are two ways to utilise LockBox`s security feature. One of them is using the security button in the bottom left of the screen which will decrypt/encrypt whats on the screen. The button will provide the opposite state of whats on the screen, so if whats on the screen is encrypted then the button will decrypt everything. The second way is by using the drag feature. Holding down the right mouse button on a piece of data and dragging it into the security pop-up will provide the opposite affect to the data`s security state.");
            para0.setId("paragraph");
            para0.setFill(Color.WHITE);
            para0.setWrappingWidth(560);
            para0C.getChildren().add(para0);
            content.getChildren().add(para0C);

            return content;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public VBox getGroupContent(){
            VBox content = new VBox();
            content.setMaxHeight(550);
            content.setMinHeight(550);
            content.setSpacing(10);
            content.setAlignment(Pos.BASELINE_LEFT);
            content.setId("content");
            content.setPadding(new Insets(20));

            Label header = new Label("Data Groups");
            content.getChildren().add(header);
            header.setId("header");

            Label sub0 = new Label("What are groups");
            sub0.setId("subheader");
            content.getChildren().add(sub0);

            HBox para0C = new HBox();
            Text para0 = new Text("Groups enable you to better organise your accounts, codes and files for easier viewing. With groupings, you know what accounts and files are for what purpose");
            para0.setId("paragraph");
            para0.setFill(Color.WHITE);
            para0.setWrappingWidth(560);
            para0C.getChildren().add(para0);
            content.getChildren().add(para0C);

            Label sub1 = new Label("Creating a Group");
            sub1.setId("subheader");
            content.getChildren().add(sub1);

            HBox para1C = new HBox();
            Text para1 = new Text("A group can be created by clicking on the plus icon in the bottom right corner of the app screen. A pop-up will appear where you can enter the group name and for which type of data its for.");
            para1.setId("paragraph");
            para1.setFill(Color.WHITE);
            para1.setWrappingWidth(560);
            para1C.getChildren().add(para1);
            content.getChildren().add(para1C);

            Label sub2 = new Label("Adding data to a group");
            sub2.setId("subheader");
            content.getChildren().add(sub2);

            HBox para2C=new HBox();
            Text para2 = new Text("When you add new pieces of data you have the option of selecting which group to add it too. When you view your data, you can also update any part of it, including which group it is in. Clicking the update button in this preview button will change the values to the ones you`ve selected, and move the data into another group if you specify.");
            para2.setId("paragraph");
            para2.setFill(Color.WHITE);
            para2.setWrappingWidth(560);
            para2C.getChildren().add(para2);
            content.getChildren().add(para2C);

            return content;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public VBox getdelete(){
            VBox content = new VBox();
            content.setMaxHeight(500);
            content.setMinHeight(500);
            content.setSpacing(10);
            content.setAlignment(Pos.BASELINE_LEFT);
            content.setId("content");
            content.setPadding(new Insets(20));

            Label header1 = new Label("Deleting Data");
            content.getChildren().add(header1);
            header1.setId("header");

            Label sub1 = new Label("Groups");
            sub1.setId("subheader");
            content.getChildren().add(sub1);

            HBox para1C = new HBox();
            Text para1 = new Text("You can delete a group along with any data held within by holding down the right mouse button on a group and dragging it to the delete box that appears. A small window will then appear asking you if you want to keep whats contained or delete it along with the group. If you keep the data, its moved into the background default group and will appear in the all section.");
            para1.setId("paragraph");
            para1.setFill(Color.WHITE);
            para1.setWrappingWidth(560);
            para1C.getChildren().add(para1);
            content.getChildren().add(para1C);

            Label sub2 = new Label("Data");
            sub2.setId("subheader");
            content.getChildren().add(sub2);

            HBox para2C = new HBox();
            Text para2 = new Text("Deleting your data such as cards, passcodes or images works the same as with groups. Holding down the right mouse button will reveal a deletion box. Dragging the data to this box will delete it. When files/images are involved, you also have the option of exporting the file (i.e decrypting it and selecting a destination) or deleting it entirely.");
            para2.setId("paragraph");
            para2.setFill(Color.WHITE);
            para2.setWrappingWidth(560);
            para2C.getChildren().add(para2);
            content.getChildren().add(para2C);

            return content;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public VBox getworkings(){
            VBox contents = new VBox();
            contents.setMaxHeight(900);
            contents.setMinHeight(900);
            contents.setSpacing(10);
            contents.setAlignment(Pos.BASELINE_LEFT);
            contents.setId("content");
            contents.setPadding(new Insets(20));

            Label header1 = new Label("How It Works");
            contents.getChildren().add(header1);
            header1.setId("header");

            Label sub1 = new Label("Encryption");
            sub1.setId("subheader");
            contents.getChildren().add(sub1);

            HBox para1C = new HBox();
            Text para1 = new Text("Encrypting and decrypting is essentially a transformation of information into a form unrecognisable without the associated key. The password you set forms this key, and is used everytime a piece of your data undergoes encryption or decryption. Any, even tiny deviation from your original password would result in the information being completely unreadable.");
            para1.setId("paragraph");
            para1.setFill(Color.WHITE);
            para1.setWrappingWidth(560);
            para1C.getChildren().add(para1);
            contents.getChildren().add(para1C);

            Label sub2 = new Label("File and Image Encryption");
            sub2.setId("subheader");
            contents.getChildren().add(sub2);

            HBox para2C=new HBox();
            Text para2 = new Text("The image and file encryption works by reading all the information from a selected file/image and encrypting it. The original file is then deleted and a new file created. The name of the new file is created by 'hashing' the name of the selected file. Hashing is very much alike to encryption, apart from no key is required. It is what is known as 'deterministic', meaning the same information hashes to the same value. When you select an image to view, a hash is created to locate the appropriate file and read its contents.");
            para2.setId("paragraph");
            para2.setFill(Color.WHITE);
            para2.setWrappingWidth(560);
            para2C.getChildren().add(para2);
            contents.getChildren().add(para2C);

            Label sub3 = new Label("What types of files can I store?");
            sub3.setId("subheader");
            contents.getChildren().add(sub3);

            HBox para3C = new HBox();
            Text para3 = new Text("We haven`t had a chance to test all file formats. So to stop the chance of any errors we have restricted the file formats you can import to your Encryption Management System:" +"\n -Files: .docx, .pdf, .txt"+"\n -Images: .jpg, .png"+"\n We are working on incorporating additional formats and regular updates will follow to include more and more");
            para3.setId("paragraph");
            para3.setFill(Color.WHITE);
            para3.setWrappingWidth(560);
            para3C.getChildren().add(para3);
            contents.getChildren().add(para3C);

            return contents;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private void close(){
            window.close();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class DragClearer extends Thread{
        public void main(String[] args) {

        }

        @Override
        public void run() {
            while(dragWait){

            }
            dragData = null;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    root.getChildren().remove(panelContainer);
                }
            });


        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
    public class timeCounter extends Thread{
        public static void main(String[] args) {

        }

        @Override
        public void run() {
            while(true){
                LocalDateTime nowtime = LocalDateTime.now();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
                time = format.format(nowtime);
                Platform.runLater(()->{
                    timeLabel.setText(time);
                });
            }
        }
    }
    */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class ImageWindow{
        private Stage iwStage;

        private Group<UserFile> currentGroup;

        private FileStore imageBank;

        private TextField nameF=new TextField();
        private ComboBox<String> groupF;

        private Label nameLabel;

        private int index;

        private UserFile data;

        private double rotation=0;

        private BorderPane rootPane = new BorderPane();

        private ImageView viewPortal=new ImageView();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public ImageWindow(Group<UserFile> group, Object data){
            currentGroup = group;
            index=currentGroup.indexOf(data);
            this.data = (UserFile) data;
            windowSetup();
            run();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public void resetPortal(){
            viewPortal.setPreserveRatio(true);
            viewPortal.maxHeight(540);
            viewPortal.minHeight(540);
            viewPortal.minWidth(960);
            viewPortal.maxWidth(960);
            viewPortal.setSmooth(true);
            viewPortal.setRotate(0);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private void windowSetup(){

            iwStage = new Stage();
            iwStage.setResizable(true);
            iwStage.initOwner(locker);
            iwStage.initModality(Modality.WINDOW_MODAL);
            iwStage.setOnCloseRequest(e->{
                populateDataSelect(currentGroup.name,IMAGES);
            });
            /////
            viewPortal.setPreserveRatio(true);
            viewPortal.maxHeight(540);
            viewPortal.minHeight(540);
            viewPortal.minWidth(960);
            viewPortal.maxWidth(960);
            viewPortal.setSmooth(true);

            lockbox.lockSingle(this.data,true);
            String internalPath = (this.data).generateInternalPath();
            iwStage.setTitle("Images - "+currentGroup.name);
            iwStage.getIcons().add(images.get("Logo"));
            nameLabel = new Label();
            nameLabel.setText(data.domain);
            nameF.setText(data.domain);
            lockbox.lockSingle(this.data,false);
            imageBank = new FileStore(internalPath, SaveConfigurations.BYTE);
            byte[] imageBytes = imageBank.loadBytes();
            imageBytes = lockbox.security.decryptBytes(imageBytes);
            imageBytes = Base64.getDecoder().decode(imageBytes);
            Image current = new Image(new ByteArrayInputStream(imageBytes));
            viewPortal.setImage(current);
            if(current.getWidth()>960){
                viewPortal.setFitWidth(960);
            }
            if(current.getHeight()>540){
                viewPortal.setFitHeight(540);
            }

            rootPane.setId("root");
            Scene iwScene = new Scene(rootPane,1280,700);
            iwScene.getStylesheets().add(this.getClass().getResource("/imageportal.css").toExternalForm());
            iwStage.setScene(iwScene);
            BorderPane viewC = new BorderPane();
            viewC.setCenter(viewPortal);
            rootPane.setCenter(viewC);
            ///////////////////////////////////////////////////
            lockbox.lockSingle((Credential)data,true);
            HBox labelHolder = new HBox();
            labelHolder.getChildren().add(nameLabel);
            labelHolder.setPadding(new Insets(20));
            rootPane.setTop(labelHolder);
            nameLabel.setText(((UserFile)data).domain+"."+((UserFile)data).getFileType());
            nameLabel.setId("label");
            lockbox.lockSingle((Credential)data,false);
            ///////////////////////////////////////////////////
            HBox headerContainer = new HBox();
            HBox controllers = new HBox();
            rootPane.setBottom(headerContainer);
            headerContainer.getChildren().add(controllers);
            headerContainer.setPadding(new Insets(20,0,20,250));
            controllers.setMaxHeight(65);
            controllers.setMinHeight(65);
            controllers.setSpacing(15);
            controllers.setId("controlpanel");
            controllers.setPadding(new Insets(4,20,0,20));
            ///////////////////////////////////////////////////
            Button delete = new Button("x");
            delete.setId("controls");
            controllers.getChildren().add(delete);
            delete.setOnAction(e ->{
                deleteImage();
            });

            Button rotate = new Button("↻");
            rotate.setId("controls");
            rotate.setOnAction(e->{
                viewPortal.setRotate(rotation+=90);
                if(rotation==360){
                    rotation=0;
                }
            });
            controllers.getChildren().add(rotate);

            Button back = new Button("<");
            back.setOnAction(e->{
                sequence(false);
            });
            back.setId("controls");
            controllers.getChildren().add(back);

            Button forward = new Button(">");
            forward.setOnAction(e->{
                sequence(true);
            });
            forward.setId("controls");
            controllers.getChildren().add(forward);
            //////////////////////////////////////////////////
            //////////////////////////////////////////////////
            VBox editHolder=new VBox();
            editHolder.setSpacing(5);
            String[] names = lockbox.images.getGroupNames().toArray(new String[0]);
            groupF=new ComboBox<>(FXCollections.observableArrayList(names));
            groupF.setValue(currentGroup.name);
            groupF.setPadding(new Insets(0,0,0,5));
            nameF.setId("editField");
            groupF.setId("editField");
            editHolder.getChildren().add(nameF);
            editHolder.getChildren().add(groupF);
            controllers.getChildren().add(editHolder);
            HBox updateHolder = new HBox();
            updateHolder.setPadding(new Insets(9,0,0,0));
            Button update = new Button("Update");
            update.setOnAction(e->{
                edit();
                lockbox.save();
            });
            update.setId("update");
            updateHolder.getChildren().add(update);
            controllers.getChildren().add(updateHolder);
            //////////////////////////////////////////////////


        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private void sequence(boolean forward){
            if(forward){
                index+=1;
            }else{
                index-=1;
            }
            if(index>=currentGroup.size() || index<0){
                if(index<0){
                    index=0;
                }else{
                    index-=1;
                }
                return;
            }
            data=currentGroup.get(index);
            lockbox.lockSingle(this.data,true);
            String internalPath = data.generateInternalPath();
            nameLabel.setText(data.domain);
            nameF.setText(data.domain);
            lockbox.lockSingle(data,false);
            imageBank = new FileStore(internalPath, SaveConfigurations.BYTE);
            byte[] imageBytes = imageBank.loadBytes();
            imageBytes = lockbox.security.decryptBytes(imageBytes);
            imageBytes = Base64.getDecoder().decode(imageBytes);
            Image current = new Image(new ByteArrayInputStream(imageBytes));
            resetPortal();
            viewPortal.setImage(current);
            if(current.getWidth()>960){
                viewPortal.setFitWidth(960);
            }
            if(current.getHeight()>540){
                viewPortal.setFitHeight(540);
            }


        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public void edit(){
            lockbox.lockSingle(data,true);
            if(!(this.data.domain.equals(nameF.getText()))){
                nameLabel.setText(nameF.getText());
                File oldName = new File(data.generateInternalPath());
                this.data.domain=nameF.getText();
                File newName = new File(data.generateInternalPath());
                oldName.renameTo(newName);
            }
            if(!(this.currentGroup.name.equals(groupF.getValue()))){
                lockbox.migrateData(groupF.getValue(),this.data,IMAGES);
                sequence(true);
            }
            lockbox.lockSingle(data,false);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public void deleteImage(){
            tempOpen=true;
            Stage temp = new Stage();
            temp.initOwner(locker);
            temp.initModality(Modality.WINDOW_MODAL);
            temp.setTitle("Delete File");
            temp.setResizable(false);
            temp.getIcons().add(images.get("Logo"));

            AnchorPane tempRoot = new AnchorPane();
            tempRoot.setStyle("-fx-background-color: #514b5e");

            Scene tempScene = new Scene(tempRoot, 150,75);
            temp.setScene(tempScene);

            HBox outerContainer = new HBox();
            outerContainer.setSpacing(10);
            tempRoot.getChildren().add(outerContainer);
            outerContainer.setPadding(new Insets(10,10,10,10));

            VBox container = new VBox();
            container.setSpacing(10);
            container.setPadding(new Insets(5,5,5,5));
            outerContainer.getChildren().add(container);

            RadioButton keep = new RadioButton("Export");
            keep.setTextFill(Color.WHITE);
            RadioButton destroy = new RadioButton("Discard");
            destroy.setTextFill(Color.WHITE);
            keep.setSelected(true);

            ToggleGroup toggler = new ToggleGroup();

            keep.setToggleGroup(toggler);
            destroy.setToggleGroup(toggler);

            container.getChildren().add(keep);
            container.getChildren().add(destroy);

            HBox buttonContainer = new HBox();
            buttonContainer.setPadding(new Insets(15,10,10,5));

            Button submit = new Button("✓");
            submit.styleProperty().bind(Bindings.when(submit.hoverProperty()).then(new SimpleStringProperty("-fx-background-color: grey")).otherwise(new SimpleStringProperty("-fx-background-color: "+lockbox.config.colourHex)));
            submit.setOnAction(e->{
                DataType fileT = data.dataType;
                if(new Utils().getToggleResult(toggler)){
                    DirectoryChooser dirChoose = new DirectoryChooser();
                    File destination = dirChoose.showDialog(locker);
                    if(destination==null){
                        return;
                    }else{
                        data.destination=destination.getAbsolutePath();
                        lockbox.deleteData(fileT,data);
                        populateDataSelect("default",fileT);
                        temp.close();
                        tempOpen=false;
                        lockbox.save();
                        if(currentGroup.size()==0){
                            iwStage.close();
                        }
                        else if(index+1>=currentGroup.size()){
                            sequence(false);
                        }else{
                            sequence(true);
                        }
                    }
                }else{
                    lockbox.lockSingle(data,true);
                    File removing = new File(data.generateInternalPath());
                    removing.delete();
                    lockbox.deleteData(fileT,data);
                    populateDataSelect("default",fileT);
                    temp.close();
                    tempOpen=false;
                    lockbox.save();
                    if(currentGroup.size()==0){
                        iwStage.close();
                    }
                    else if(index+1>=currentGroup.size()){
                        sequence(false);
                    }else{
                        sequence(true);
                    }
                }
            });

            buttonContainer.getChildren().add(submit);
            outerContainer.getChildren().add(buttonContainer);

            temp.show();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private void run(){
            iwStage.show();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class InputOutputWindow{

        private final DataType type;
        private Object data = null;
        private boolean output;
        private Stage ioStage;

        private  final TextField passwordField = new TextField();
        private final TextField nameField = new TextField();
        private final TextField domainField = new TextField();
        private final TextField emailField = new TextField();
        private final TextField codeField = new TextField();
        private final TextField accNumField = new TextField();
        private final TextField cusNumField = new TextField();
        private final TextField sortCodeField = new TextField();

        private final TextField carNum = new TextField();
        private final TextField expirDate = new TextField();
        private final TextField secNum = new TextField();

        public Dictionary<String, TextField> fields = new Hashtable<>();

        private ComboBox<String> groupSelect;
        private DatePicker dateSelection;

        private boolean groupInput;
        private boolean colourInput;

        private boolean multipleFiles=false;

        private List<File> selections=null;

        private Label errorLabel = new Label();

        private PasswordField currentPassField = new PasswordField();
        private PasswordField newPassField = new PasswordField();
        private PasswordField confirmNewField = new PasswordField();

        DropShadow dataShadow = new DropShadow();


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public InputOutputWindow(){
            this.type = ACCOUNTS;//does not matter, will not be used in this instance
            setup();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public InputOutputWindow(DataType type, boolean gIn, boolean cIn){

            this.type = type;
            this.output = false;
            this.groupInput = gIn;
            this.colourInput = cIn;
            setup();
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public InputOutputWindow(Object data){
            this.type = new Utils().classAssignment(data);
            this.data = data;
            this.output = true;
            setup();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private void newPassSetup(){
            currentPassField.setMinWidth(300);
            currentPassField.setMaxWidth(300);

            newPassField.setMinWidth(300);
            newPassField.setMinWidth(300);

            confirmNewField.setMinWidth(300);
            confirmNewField.setMaxWidth(300);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public void setup(){
            fields.put("domain", domainField);
            fields.put("email", emailField);
            fields.put("username",nameField);
            fields.put("password",passwordField);
            fields.put("code",codeField);
            fields.put("accNum", accNumField);
            fields.put("cusNum", cusNumField);
            fields.put("sortCode",sortCodeField);

            errorLabel.setOpacity(0);
            errorLabel.setMinWidth(300);
            errorLabel.setMaxWidth(300);
            errorLabel.setId("errorlabel");
            errorLabel.setWrapText(true);
            errorLabel.setTextFill(Color.RED);

            ioStage = new Stage();
            ioStage.initOwner(locker);
            ioStage.initModality(Modality.WINDOW_MODAL);

        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public void runNewKeyIO(boolean initialised){
            newPassSetup();
            DropShadow dataShadow = new DropShadow();
            dataShadow.setOffsetX(5);
            dataShadow.setOffsetY(5);
            dataShadow.setColor(Color.GRAY);

            if(lockbox.config.colourHex==null){
                lockbox.config.colourHex="orange";
            }

            ioStage.setTitle("New Password");
            ioStage.setResizable(false);
            ioStage.getIcons().add(images.get("Logo"));

            AnchorPane rootPane = new AnchorPane();
            Scene newKey = new Scene(rootPane, 640, 480);
            newKey.getStylesheets().add(this.getClass().getResource("/newkey.css").toExternalForm());

            VBox container = new VBox();
            container.setId("container");
            container.setSpacing(10);
            container.setLayoutX(165);
            container.setLayoutY(50);
            container.setEffect(dataShadow);
            container.setPadding(new Insets(10));
            rootPane.getChildren().add(container);

            if(initialised){
                Label currentPass = new Label("Current");
                currentPass.setId("label");
                currentPass.setTextFill(Color.BLACK);
                container.getChildren().add(currentPass);
                container.getChildren().add(currentPassField);
                currentPassField.setEffect(dataShadow);
                currentPassField.setStyle("-fx-border-color: "+lockbox.config.colourHex);
            }else{
                ioStage.initStyle(StageStyle.UNDECORATED);
                lockbox.config.updateConfig("2 Weeks");
            }

            Label newPass = new Label("New");
            newPass.setId("label");
            newPass.setTextFill(Color.BLACK);
            container.getChildren().add(newPass);
            container.getChildren().add(newPassField);
            newPassField.setEffect(dataShadow);
            newPassField.setStyle("-fx-border-color: "+lockbox.config.colourHex);

            Label confirm = new Label("Confirm");
            confirm.setId("label");
            confirm.setTextFill(Color.BLACK);
            container.getChildren().add(confirm);
            container.getChildren().add(confirmNewField);
            confirmNewField.setEffect(dataShadow);
            confirmNewField.setStyle("-fx-border-color: "+lockbox.config.colourHex);

            HBox buttonContainer = new HBox();
            buttonContainer.setPadding(new Insets(0, 135, 0, 135));
            container.getChildren().add(buttonContainer);

            Button enter = new Button();
            enter.setId("enter");
            enter.setText("✓");
            enter.styleProperty().bind(Bindings.when(enter.hoverProperty()).then(new SimpleStringProperty("-fx-background-color: "+lockbox.config.secondaryHex)).otherwise(new SimpleStringProperty("-fx-background-color: "+lockbox.config.colourHex)));
            enter.setOnAction(e -> {
                genNewKey(initialised);
            });
            buttonContainer.getChildren().add(enter);

            container.getChildren().add(errorLabel);

            VBox guideLabels = new VBox();
            guideLabels.setSpacing(2.5);
            Label desc1 = new Label("- Password should be atleast 10 characters long");
            desc1.setId("descriptiveLabel");
            desc1.setWrapText(true);
            desc1.setMinWidth(300);
            desc1.setMaxWidth(300);
            guideLabels.getChildren().add(desc1);
            Label desc2 = new Label("- Password should contain atleast one number and one capital letter");
            desc2.setId("descriptiveLabel");
            desc2.setWrapText(true);
            desc2.setMinWidth(300);
            desc2.setMaxWidth(300);
            guideLabels.getChildren().add(desc2);

            container.getChildren().add(guideLabels);

            if(lockbox.config.backgroundPath==null){
                lockbox.config.backgroundPath = "/default.png";
            }
            BackgroundImage backImg = new BackgroundImage(new Image(lockbox.config.backgroundPath), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
            rootPane.setBackground(new Background(backImg));

            ioStage.setScene(newKey);
            ioStage.show();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private void genErrorMsg(String msg){
            errorLabel.setOpacity(1);
            errorLabel.setText(msg);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private void genNewKey(boolean initialised){
            if(initialised){
                if(!(new Utils().checkForContents(currentPassField))){
                    genErrorMsg("Current password not presented");
                    return;
                }
                if(!(lockbox.verify(currentPassField.getText()))){
                    genErrorMsg("Incorrect Master Password");
                    return;
                }

            }
            if(!(new Utils().checkForContents(newPassField))){
                genErrorMsg("New password not presented");
                return;
            }
            if(!(new Utils().checkForContents(confirmNewField))){
                genErrorMsg("Confirmation not presented");
                return;
            }

            if(!(newPassField.getText().equals(confirmNewField.getText()))){
                genErrorMsg("New passwords do not match");
                return;
            }
            if(!(SecurityChecks.checkNewPass(newPassField.getText()))){
                genErrorMsg(SecurityChecks.errorMsg);
                return;
            }
            lockbox.newPassword(newPassField.getText(),initialised);
            stop();



        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public void runConfigIO(){
            ioStage.setTitle("Logs Config");
            ioStage.setResizable(false);
            ioStage.getIcons().add(images.get("Logo"));
            AnchorPane rootPane = new AnchorPane();
            Scene logOutput = new Scene(rootPane, 221, 100);
            ioStage.setScene(logOutput);
            logOutput.getStylesheets().add(this.getClass().getResource("/acclogcnf.css").toExternalForm());
            VBox parent = new VBox();
            parent.setSpacing(5);
            parent.setLayoutX(10);
            parent.setLayoutY(10);
            HBox layout = new HBox();
            Label label = new Label("Log Wipe Interval");
            label.setId("label");
            parent.getChildren().add(label);
            parent.getChildren().add(layout);
            rootPane.getChildren().add(parent);
            ArrayList<String> intervals = new ArrayList<>();
            intervals.add("2 Weeks");
            intervals.add("1 Month");
            intervals.add("2 Months");
            intervals.add("3 Months");
            ComboBox intervalSelect = new ComboBox<>(FXCollections.observableArrayList(intervals));
            layout.getChildren().add(intervalSelect);
            Button submission = new Button("Enter");
            submission.setOnAction(e -> {
                String selection = (String) intervalSelect.getValue();
                lockbox.config.interval=selection;
                if(selection==null){
                    return;
                }
                updateConfig(selection);
                ioStage.close();
            });
            layout.getChildren().add(submission);
            layout.setSpacing(5);
            submission.setId("node");
            submission.setStyle("-fx-border-color: "+lockbox.config.colourHex);
            intervalSelect.setId("node");
            intervalSelect.setStyle("-fx-border-color: "+lockbox.config.colourHex);
            ioStage.show();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private void updateConfig(String selection){
            lockbox.config.updateConfig(selection);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public void run(){
            ioStage.setResizable(false);
            ioStage.getIcons().add(images.get("Logo"));
            if(this.groupInput){
                ioStage.setScene(getGroupIO());
                ioStage.setTitle("New Group");
            }else if(this.colourInput){
                ioStage.setScene(genColourIO());
                ioStage.setTitle("Colour");
            }else{
                ioStage.setScene(getDataIO( this.output, this.type));
                ioStage.setTitle("View");
                ioStage.setOnCloseRequest(e->{
                    populateGroupSelect(this.type);
                });
            }
            dataShadow.setOffsetX(-5);
            dataShadow.setOffsetY(5);
            dataShadow.setColor(Color.valueOf("#514b5e"));
            ioStage.show();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public void stop(){
            ioStage.close();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public Scene getDataIO( boolean output, DataType type){

            AnchorPane root = new AnchorPane();
            root.setId("root");
            Scene accInput = new Scene(root,640,480);
            accInput.getStylesheets().add(this.getClass().getResource("/DataInput.css").toExternalForm());

            VBox main = new VBox();
            main.setMaxWidth(350);
            main.setMinWidth(350);
            main.setLayoutX(155);
            main.setLayoutY(10);
            main.setMaxHeight(480);

            DropShadow dataShadow = new DropShadow();
            dataShadow.setOffsetX(-2);
            dataShadow.setOffsetY(2);
            dataShadow.setColor(Color.BLACK);
            main.setEffect(dataShadow);

            main.setStyle("-fx-border-color: "+lockbox.config.colourHex);
            main.setSpacing(15);
            main.setId("container");
            main.setPadding(new Insets(20,45,20,45));
            main.setAlignment(Pos.BASELINE_LEFT);

            root.getChildren().add(main);

            switch(type){
                case ACCOUNTS:
                    main = genAccountIO(main);
                    break;
                case CODES:
                    main = genCodeIO(main);
                    break;
                case BANKS:
                    main = getCardIO(main);
                    break;
                case FILES:
                    main = getFileIO(main);
                    break;
                case IMAGES:
                    main = getFileIO(main);
                    break;
            }

            Label groupLabel = new Label("Group");
            groupLabel.setId("label");
            main.getChildren().add(groupLabel);
            ArrayList<String> groups = lockbox.getGroups(type);
            if(groups.size()==0){
                lockbox.addGroup("default", type);
                groups = lockbox.getGroups(type);
            }
            groupSelect = new ComboBox(FXCollections.observableArrayList(groups));
            groupSelect.setId("input");
            main.getChildren().add(groupSelect);

            if(output){
                HBox buttons = new HBox();
                buttons.setSpacing(10);
                buttons.setPadding(new Insets(5,0,0,0));
                Button update = new Button("Update");
                update.setId("submit");
                update.setOnAction(updateData());
                buttons.getChildren().add(update);

                Button back = new Button("Back");
                back.setId("submit");
                back.setOnAction(closeWindow());
                buttons.getChildren().add(back);
                main.getChildren().add(buttons);


                Button delete = new Button("Delete");
                delete.setId("submit");
                delete.setOnAction(deleteData());
                buttons.getChildren().add(delete);
            }else{
                Button submit = new Button("Submit");
                submit.setId("submit");
                submit.setOnAction(genObject());
                main.getChildren().add(submit);
            }
            main.getChildren().add(errorLabel);




            return accInput;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private EventHandler<ActionEvent> closeWindow(){
            EventHandler<ActionEvent> handler = event -> {
                ioStage.close();
                if(type!=null){
                    populateGroupSelect(type);
                }
            };
            return handler;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private EventHandler<ActionEvent> deleteData(){
            EventHandler<ActionEvent> handler = event ->{
                if(this.data instanceof UserFile){
                    new Utils().deleteFile((UserFile) this.data, ioStage);
                    populateGroupSelect(this.type);

                }else{
                    lockbox.deleteData(this.type, this.data);
                    lockbox.save();
                    ioStage.close();
                    populateGroupSelect(this.type);
                }
            };
            return handler;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private Scene getGroupIO(){
            AnchorPane root = new AnchorPane();
            root.setId("root");
            Scene accInput = new Scene(root,300,550);
            accInput.getStylesheets().add(this.getClass().getResource("/DataInput.css").toExternalForm());

            VBox main = new VBox();
            main.setSpacing(10);
            main.setAlignment(Pos.BASELINE_LEFT);
            root.getChildren().add(main);

            Label groupLabel = new Label("Group");
            groupLabel.setId("label");
            main.getChildren().add(groupLabel);

            String[] enuemrate = {"ACCOUNTS","CODES","BANKS","FILES","IMAGES"};
            groupSelect = new ComboBox(FXCollections.observableArrayList(enuemrate));
            groupSelect.setId("input");
            main.getChildren().add(groupSelect);

            fields.get("domain").setId("input");
            main.getChildren().add(fields.get("domain"));

            Button submit = new Button("Submit");
            submit.setId("submit");
            submit.setOnAction(genGroup());
            main.getChildren().add(submit);

            return accInput;

        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private EventHandler<ActionEvent> genGroup(){
            EventHandler<ActionEvent> generate= event ->{
                DataType localType;
                String newGroup = fields.get("domain").getText();
                localType = DataType.valueOf(groupSelect.getValue());
                lockbox.newGroup(localType, newGroup);
                populateGroupSelect(this.type);
                lockbox.save();
                stop(); 
            };
            return generate;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private Scene genColourIO(){
            AnchorPane root = new AnchorPane();
            root.setId("root");
            Scene accInput = new Scene(root,150,100);
            accInput.getStylesheets().add(this.getClass().getResource("/DataInput.css").toExternalForm());

            VBox main = new VBox();
            main.setSpacing(10);
            main.setAlignment(Pos.BASELINE_LEFT);
            root.getChildren().add(main);

            colourSelect.setId("colourinput");
            colourSelect.setLayoutX(100);
            colourSelect.setLayoutY(20);
            main.getChildren().add(colourSelect);

            Button submit = new Button("Submit");
            submit.setId("submit");
            submit.setOnAction(genColour());
            main.getChildren().add(submit);

            return accInput;

        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private EventHandler<ActionEvent> genColour(){
            EventHandler<ActionEvent> handler = event ->{
                Color selected = colourSelect.getValue();
                lockbox.config.colourHex = toHexString(selected);
                setPrimaryColours();
                setPrimaryColours();
                stop();
                lockbox.config.saveLogs();
            };
            return handler;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private String format(double val) {
            String in = Integer.toHexString((int) Math.round(val * 255));
            return in.length() == 1 ? "0" + in : in;
        }

        private String toHexString(Color value) {
            return "#" + (format(value.getRed()) + format(value.getGreen()) + format(value.getBlue()) + format(value.getOpacity()))
                    .toUpperCase();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private EventHandler<ActionEvent> genObject(){
            EventHandler<ActionEvent> generate=null;
            String destination = groupSelected;
            InputCheck.fieldBorderColour = lockbox.config.colourHex;
            if(groupSelected==null){
                destination = "default";
            }
            final String groupDest = destination;
            switch(type){
                case ACCOUNTS:
                    generate = event ->{
                        InputCheck.fields = new ArrayList<>();
                        InputCheck.fields.add(fields.get("domain"));
                        InputCheck.fields.add(fields.get("email"));
                        InputCheck.fields.add(fields.get("username"));
                        InputCheck.fields.add(fields.get("password"));
                        if(!(InputCheck.checkContents())){
                            genErrorMsg(InputCheck.errorMsg);
                            return;
                        }
                        Account account = new Account(fields.get("domain").getText(),fields.get("email").getText(),fields.get("username").getText(),fields.get("password").getText(), groupSelect.getValue());
                        account = (Account) lockbox.lockSingle(account,false);
                        lockbox.accounts.newCredential(account, lockbox);
                        lockbox.accounts.save();
                        populateDataSelect(groupDest,ACCOUNTS);
                        stop();
                        lockbox.save();
                    };
                    break;

                case CODES:
                    generate = event -> {
                        InputCheck.fields = new ArrayList<>();
                        InputCheck.fields.add(fields.get("password"));
                        InputCheck.fields.add(fields.get("username"));
                        if(!(InputCheck.checkContents())){
                            genErrorMsg(InputCheck.errorMsg);
                            return;
                        }
                        Code code = new Code(fields.get("password").getText(), fields.get("username").getText(), groupSelect.getValue());
                        code = (Code) lockbox.lockSingle(code,false);
                        lockbox.codes.newCredential(code,lockbox);
                        lockbox.codes.save();
                        populateDataSelect(groupDest,CODES);
                        stop();
                        lockbox.save();
                    };
                    break;

                case BANKS:
                    generate = event -> {
                        InputCheck.fields = new ArrayList<>();
                        InputCheck.fields.add(carNum);
                        InputCheck.fields.add(expirDate);
                        InputCheck.fields.add(secNum);
                        InputCheck.fields.add(domainField);
                        if(!(InputCheck.checkContents())){
                            genErrorMsg(InputCheck.errorMsg);
                            return;
                        }
                        BankCard code = new BankCard(domainField.getText(),carNum.getText(),secNum.getText(),expirDate.getText(),groupSelect.getValue());
                        code = (BankCard) lockbox.lockSingle(code,false);
                        lockbox.banks.newCredential(code,lockbox);
                        lockbox.banks.save();
                        populateDataSelect(groupDest,BANKS);
                        stop();
                        lockbox.save();
                    };
                    break;

                case IMAGES:
                    generate = event -> {
                        InputCheck.fields = new ArrayList<>();
                        InputCheck.fields.add(fields.get("username"));
                        InputCheck.fields.add(fields.get("domain"));
                        if(!(InputCheck.checkContents())){
                            genErrorMsg(InputCheck.errorMsg);
                            return;
                        }
                        if(multipleFiles){
                            String grouping = groupSelect.getValue();
                            if(!(InputCheck.checkFilePath(selections))){
                                genErrorMsg(InputCheck.errorMsg);
                                return;
                            }
                            for(File selection: selections){
                                String path = selection.getAbsolutePath();
                                genFile(path,IMAGES,grouping);
                            }
                        }else{
                            if(!(InputCheck.checkFilePath(fields.get("username")))){
                                genErrorMsg(InputCheck.errorMsg);
                                return;
                            }
                            UserFile code = new UserFile(fields.get("domain").getText(),groupSelect.getValue(),fields.get("username").getText(),IMAGES);
                            code = (UserFile) lockbox.lockSingle(code,false);
                            lockbox.images.newCredential(code,lockbox);
                        }
                        lockbox.save();
                        populateDataSelect(groupDest,IMAGES);
                        stop();
                    };
                    break;

                case FILES:
                    generate = event -> {
                        InputCheck.fields = new ArrayList<>();
                        InputCheck.fields.add(fields.get("username"));
                        InputCheck.fields.add(fields.get("domain"));
                        if(!(InputCheck.checkContents())){
                            genErrorMsg(InputCheck.errorMsg);
                            return;
                        }
                        if(!(InputCheck.checkFilePath(fields.get("username")))){
                            genErrorMsg(InputCheck.errorMsg);
                            return;
                        }
                        if(multipleFiles){
                            for(File selection: selections){
                                String path = selection.getAbsolutePath();
                                genFile(path,FILES,groupSelect.getValue());
                            }
                        }else{
                            UserFile code = new UserFile(fields.get("domain").getText(),groupSelect.getValue(),fields.get("username").getText(),FILES);
                            code = (UserFile) lockbox.lockSingle(code,false);
                            lockbox.files.newCredential(code,lockbox);
                        }
                        lockbox.save();
                        populateDataSelect(groupDest,FILES);
                        stop();
                    };
                    break;
            }
            return generate;

        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private VBox genCodeIO(VBox target){
            Code localData = (Code) this.data;
            Label nameLabel = new Label("Name");
            nameLabel.setId("label");
            nameLabel.setContentDisplay(ContentDisplay.LEFT);
            target.getChildren().add(nameLabel);

            fields.get("username").setId("input");
            if(output){
                fields.get("username").setText(localData.domain);
            }
            target.getChildren().add(fields.get("username"));

            Label codeLabel = new Label("Code");
            codeLabel.setId("label");
            codeLabel.setContentDisplay(ContentDisplay.LEFT);
            target.getChildren().add(codeLabel);

            fields.get("password").setId("input");
            if(output){
                fields.get("password").setText(localData.getCode());
            }
            target.getChildren().add(fields.get("password"));

            return target;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private VBox genAccountIO(VBox target){
            Account localData = (Account) this.data;

            Label domainLabel = new Label("Name");
            domainLabel.setId("label");
            domainLabel.setContentDisplay(ContentDisplay.LEFT);
            target.getChildren().add(domainLabel);
            fields.get("domain").setId("input");
            domainField.setEffect(dataShadow);
            if(output){
                fields.get("domain").setText(localData.domain);
            }
            target.getChildren().add(fields.get("domain"));

            Label emailLabel = new Label("Email");
            emailLabel.setContentDisplay(ContentDisplay.LEFT);
            emailLabel.setId("label");
            target.getChildren().add(emailLabel);
            fields.get("email").setId("input");
            if(output){
                fields.get("email").setText(localData.email);
            }
            target.getChildren().add(fields.get("email"));

            Label nameLabel = new Label("Your Username");
            nameLabel.setContentDisplay(ContentDisplay.LEFT);
            nameLabel.setId("label");
            target.getChildren().add(nameLabel);
            fields.get("username").setId("input");
            nameField.setEffect(dataShadow);
            if(output){
                fields.get("username").setText(localData.username);
            }
            target.getChildren().add(fields.get("username"));

            Label passwordLabel = new Label("Password");
            nameLabel.setContentDisplay(ContentDisplay.LEFT);
            passwordLabel.setId("label");
            target.getChildren().add(passwordLabel);
            fields.get("password").setId("input");
            passwordField.setEffect(dataShadow);
            if(output){
                fields.get("password").setText(localData.password);
            }
            emailField.setEffect(dataShadow);
            target.getChildren().add(fields.get("password"));

            return target;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private VBox getCardIO(VBox target){
            domainField.setId("input");
            carNum.setId("input");
            expirDate.setId("input");
            secNum.setId("input");
            if(output){
                domainField.setText(((BankCard)this.data).domain);
                carNum.setText(((BankCard)this.data).getCardnum());
                secNum.setText(((BankCard)this.data).getSecnum());
                expirDate.setText(((BankCard)this.data).getExpirdate());
            }
            domainField.setEffect(dataShadow);
            carNum.setEffect(dataShadow);
            secNum.setEffect(dataShadow);
            expirDate.setEffect(dataShadow);
            Label cardName = new Label("Card/Bank Name");
            cardName.setContentDisplay(ContentDisplay.LEFT);
            cardName.setId("label");
            target.getChildren().add(cardName);
            target.getChildren().add(domainField);
            Label cardNum = new Label("Card Number");
            cardNum.setContentDisplay(ContentDisplay.LEFT);
            cardNum.setId("label");
            target.getChildren().add(cardNum);
            target.getChildren().add(carNum);
            Label expir = new Label("Expiration Data");
            expir.setContentDisplay(ContentDisplay.LEFT);
            expir.setId("label");
            target.getChildren().add(expir);
            target.getChildren().add(expirDate);
            Label sec = new Label("Security Number");
            sec.setContentDisplay(ContentDisplay.LEFT);
            sec.setId("label");
            target.getChildren().add(sec);
            target.getChildren().add(secNum);
            return target;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private VBox getFileIO(VBox target){
            UserFile localData = (UserFile) this.data;
            fields.get("domain").setId("input");
            fields.get("domain").setText("Name");
            if(output){
                fields.get("domain").setText(localData.domain);
            }else{
                fields.get("domain").setText("Name");
            }
            target.getChildren().add(fields.get("domain"));

            if(!output){

                fields.get("username").setId("input");
                fields.get("username").setText("FilePath");
                if(output){
                    fields.get("username").setText(localData.getFilePath());
                }
                nameField.setEffect(dataShadow);
                domainField.setEffect(dataShadow);
                String types = " .pdf, .docx, .txt";
                if(this.type==IMAGES){
                    types = " .png, .jpg";
                }
                Button pathSelection = new Button("select Path(s)"+types);
                pathSelection.setId("submit");
                pathSelection.setOnAction(fileSelecting());

                target.getChildren().add(fields.get("username"));
                target.getChildren().add(pathSelection);
                ///////////////////////////////////////////////////////////
                ToggleGroup fileMultiplicity = new ToggleGroup();
                RadioButton single = new RadioButton("Single File");
                RadioButton multiple = new RadioButton("Multiple Files");
                single.setToggleGroup(fileMultiplicity);
                multiple.setToggleGroup(fileMultiplicity);
                fileMultiplicity.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
                    @Override
                    public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                        RadioButton reference = (RadioButton)fileMultiplicity.getSelectedToggle();
                        if(reference.getText().equals("Single File")){
                            multipleFiles=false;
                        }else{
                            multipleFiles=true;
                        }
                    }
                });
                fileMultiplicity.selectToggle(single);
                HBox selectionBox=new HBox();
                selectionBox.setSpacing(10);
                selectionBox.getChildren().add(single);
                selectionBox.getChildren().add(multiple);
                target.getChildren().add(selectionBox);
                ////////////////////////////////////////////////////////////
            }

            if(output){
               Label fileTypeLabel = new Label("File Type: "+localData.getFileType());
               fileTypeLabel.setId("label");
               target.getChildren().add(fileTypeLabel);
            }

            return target;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private EventHandler<ActionEvent> fileSelecting(){
            EventHandler<ActionEvent> handler = event -> {
                FileChooser.ExtensionFilter filter;
                if(this.type==FILES){
                    filter = new FileChooser.ExtensionFilter("Files",new String[]{"*.pdf","*.txt","*.docx"});
                }else if(this.type==IMAGES){
                    filter = new FileChooser.ExtensionFilter("Images",new String[]{"*.png","*.jpg"});
                }else{
                    filter = new FileChooser.ExtensionFilter("Videos",new String[]{"*.mp4"});
                }
                FileChooser chooser = new FileChooser();
                chooser.getExtensionFilters().add(filter);
                if(multipleFiles){
                    selections = chooser.showOpenMultipleDialog(ioStage);
                    fields.get("username").setText(selections.size()+" Files Selected");
                }else{
                    File selected = chooser.showOpenDialog(ioStage);
                    if(selected==null){
                        return;
                    }
                    fields.get("username").setText(selected.getAbsolutePath());
                }
            };
            return handler;

        };
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private EventHandler<ActionEvent> updateData(){
            switch(type){
                case ACCOUNTS:
                     return updateAccount(data);

                case CODES:
                    return updateCode(data);

                case BANKS:
                    return updateCard(data);

                case FILES:
                    return updateFile(data);

                case IMAGES:
                    return updateFile(data);

            }
            return null;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private EventHandler<ActionEvent> updateFile(Object data){
            UserFile localData = (UserFile) data;
            EventHandler<ActionEvent> handler = e ->{
                if(!(localData.domain.equals(domainField.getText()))){
                    lockbox.lockSingle((Credential) data,true);
                    File oldName = new File(localData.generateInternalPath());
                    localData.domain=domainField.getText();
                    File newName = new File(localData.generateInternalPath());
                    oldName.renameTo(newName);
                }
                stop();
            };
            return handler;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private EventHandler<ActionEvent> updateCard(Object card){
            BankCard localData = (BankCard) card;
            EventHandler<ActionEvent> handler = e ->{
                if(!(localData.domain.equals(domainField.getText()))){
                    ((BankCard)card).domain = carNum.getText();
                }
                if(!(localData.getCardnum().equals(carNum.getText()))){
                    ((BankCard)card).setCardnum(carNum.getText());
                }
                if(!(localData.getExpirdate().equals(expirDate.getText()))){
                    ((BankCard)card).setExpirdate(expirDate.getText());
                }
                if(!(localData.getSecnum().equals(secNum.getText()))){
                    ((BankCard)card).setSecnum(secNum.getText());
                }
                if(!(groupSelect.getValue()==null)){
                    if(!(localData.group.equals(groupSelect.getValue()))){
                        lockbox.migrateData(groupSelect.getValue(),(BankCard)card,BANKS);
                    }
                }
                stop();
            };
            return handler;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private EventHandler<ActionEvent> updateAccount(Object account){
            Account localData = (Account) account;
            EventHandler<ActionEvent> handler = event -> {
                if(!(localData.domain.equals(domainField.getText()))){
                    localData.domain = domainField.getText();
                }
                if(!(localData.email.equals(emailField.getText()))){
                    localData.email = emailField.getText();
                }
                if(!(localData.username.equals(nameField.getText()))){
                    localData.username = nameField.getText();
                }
                if(!(localData.password.equals(passwordField.getText()))){
                    localData.password = passwordField.getText();
                }
                if(!(groupSelect.getValue()==null)){
                    if(!(localData.group.equals(groupSelect.getValue()))){
                        lockbox.migrateData(groupSelect.getValue(),(Account)account,ACCOUNTS);
                    }
                }
                stop();
            };
            return handler;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private EventHandler<ActionEvent> updateCode(Object account){
            EventHandler<ActionEvent> handler = event ->{
                Code localData  = (Code) account;
                if(!(localData.domain.equals(domainField.getText()))){
                    localData.domain = domainField.getText();
                }
                if(!(localData.getCode().equals(domainField.getText()))){
                    localData.setCode(domainField.getText());
                }
                if(!(groupSelect.getValue()==null)){
                    if(!(localData.group.equals(groupSelect.getValue()))){
                        lockbox.migrateData(groupSelect.getValue(),(Code)account,CODES);
                    }
                }
                populateDataSelect(groupSelected,CODES);
                stop();

            };
            return handler;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private void genFile(String path, DataType type, String group){
            String[] partition = path.split("\\\\");
            String name = partition[partition.length-1];
            UserFile file = new UserFile(name,groupSelect.getValue(),path,type);
            lockbox.lockSingle(file,false);
            if(type==FILES){
                lockbox.files.newCredential(file,lockbox);
            }else{
                lockbox.images.newCredential(file,lockbox);
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    }




}
