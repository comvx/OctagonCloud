package com.octagon;

import com.jfoenix.controls.JFXToggleButton;
import com.octagon.clientSide.AutoUpdaterIO;
import com.octagon.clientSide.DirectoryWatcher;
import com.octagon.clientSide.MessageBox;
import com.octagon.ftp.FTPconnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;

import javax.swing.text.html.ImageView;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    AnchorPane frame;
    @FXML
    private ImageView logo;
    @FXML private JFXToggleButton tB;
    @FXML private JFXToggleButton aB;

    double x,y;
    public static boolean statusTimeStampsSync = false;
    public static boolean statusAutoUpdate = false;
    public String userHome = System.getProperty("user.home")+File.separator+"OctagonCloud";
    private FTPconnection ftPconnection;

    private static DirectoryWatcher directoryWatcher;
    private static MessageBox  messageBox = new MessageBox();
    private static AutoUpdaterIO autoUpdaterIO = new AutoUpdaterIO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        frame.requestFocus();
        tB.setSelected(false);
        aB.setSelected(false);
    }
    @FXML
    void close(MouseEvent event) {
        System.exit(0);
    }
    @FXML
    void hide(MouseEvent event){
        Stage stage = (Stage)frame.getScene().getWindow();
        stage.setOpacity(0.0);
    }
    @FXML
    void mini(MouseEvent event) {
        Stage stage = (Stage)frame.getScene().getWindow();
        stage.setIconified(true);
        tB.setSelected(true);
        aB.setSelected(true);

        statusTimeStampsSync = true;
        statusAutoUpdate = true;
    }
    @FXML
    void MOVEpressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }
    @FXML
    void MOVEdragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @FXML
    void SYNX(MouseEvent event) {
        launchSync();
    }
    public void launchSync(){
        ftPconnection = new FTPconnection();
        try{
            Thread startThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    CloudHandler cloudHandler = new CloudHandler();
                    cloudHandler.synx();
                }
            });
            startThread.start();
            messageBox.infoBox("Starting syncing");
            startThread.join();
            messageBox.infoBox("Finished syncing");
            Desktop.getDesktop().open(new File(userHome));
        }catch (Exception e){
            System.out.println("FATAL ERROR");
            messageBox.infoBox("Fatal Error");
        }
    }
    @FXML
    void sele(MouseEvent event) {
        if(tB.isSelected()){
            statusTimeStampsSync = false;
        }else if(!tB.isSelected() && statusTimeStampsSync == false){
            statusTimeStampsSync = true;
        }
    }
    @FXML
    void seleA(MouseEvent event) {
        directoryWatcher = new DirectoryWatcher();
        if(aB.isSelected()){
            statusAutoUpdate = false;
            directoryWatcher.threadWatch.stop();
        }else if(!aB.isSelected() && statusAutoUpdate == false){
            statusAutoUpdate = true;
            directoryWatcher.watcher();
            autoUpdaterIO.runChecker();
        }
    }
}
