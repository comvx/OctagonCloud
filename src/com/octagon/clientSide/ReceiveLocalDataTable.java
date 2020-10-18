package com.octagon.clientSide;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReceiveLocalDataTable {
    private ArrayList<File> outputArray;
    private ArrayList<String> outputTimeStamps;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

    private TimeZoneConverter timeZoneConverter = new TimeZoneConverter();

    public ReceiveLocalDataTable() {
        outputArray = new ArrayList<>();
        outputTimeStamps = new ArrayList<>();
    }

    private void createDataTable(File pPath) {
        try {
            File[] inputFiles = pPath.listFiles();
            for (File file : inputFiles) {
                if (file.isFile()) {
                    outputArray.add(file);

                    FileTime fileTime = Files.getLastModifiedTime(Paths.get(file.getAbsolutePath()));
                    String timeStamp = new String(dateFormat.format(fileTime.toMillis()));
                    outputTimeStamps.add(timeStamp + ";" + file.getAbsolutePath());

                } else if (file.isDirectory()) {
                    outputArray.add(file);
                    createDataTable(file);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public File[] getData() {
        String userHome = System.getProperty("user.home");
        File pathHome = new File(userHome + File.separator + "OctagonCloud" + File.separator);
        createDataTable(pathHome);
        File[] output = new File[outputArray.size()];
        for (int i = 0; i < outputArray.size(); i++) {
            output[i] = outputArray.get(i);
        }
        outputArray.clear();
        return output;
    }
    public String[] getLocalTimeStamps(){
        String userHome = System.getProperty("user.home");
        File pathHome = new File(userHome + File.separator + "OctagonCloud" + File.separator);
        createDataTable(pathHome);
        String[] output = new String[outputTimeStamps.size()];
        for (int i = 0; i < outputTimeStamps.size(); i++) {
            output[i] = outputTimeStamps.get(i);
        }
        outputTimeStamps.clear();
        return output;
    }
}
