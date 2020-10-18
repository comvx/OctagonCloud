package com.octagon;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.event.MouseEvent;
import java.io.File;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Octagon Cloud");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 366.0, 322.0));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.requestFocus();
        primaryStage.show();
    }
    public static void main(String[] args) {
        File userHome = new File(System.getProperty("user.home") + File.separator + "OctagonCloud");
        if(userHome.isDirectory()){
            if(!userHome.exists()){
                userHome.mkdirs();
            }
        }
        launch(args);
    }
}
