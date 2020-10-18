package com.octagon.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReceiveDataTable {
    public static FTPconnection ftPconnection = new FTPconnection();

    private FTPClient client;
    private ArrayList<File> outputDataTable = new ArrayList<>();
    private ArrayList<String> outputTimeStamps = new ArrayList<>();
    public ArrayList<String> outputDataTableIsFile = new ArrayList<String>();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");

    public ReceiveDataTable() {
        client = ftPconnection.getClient();
    }

    private void listDirectory(String parentDir, String currentDir) throws IOException {
        String dirToList = parentDir;
        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        }
        FTPFile[] subFiles = client.listFiles(dirToList);
        if (subFiles != null && subFiles.length > 0) {
            for (FTPFile aFile : subFiles) {
                String currentFileName = aFile.getName();
                if (currentFileName.equals(".")
                        || currentFileName.equals("..")) {
                    continue;
                }
                try{
                    if (aFile.isDirectory()) {
                        outputDataTable.add(new File(dirToList + "/" + currentFileName));
                        outputDataTableIsFile.add("false;"+dirToList + "/" + currentFileName);
                        listDirectory(dirToList, currentFileName);

                       //String time = new String(client.getModificationTime(dirToList + "/" + currentFileName));
                    } else {
                        outputDataTable.add(new File(dirToList + "/" + currentFileName));
                        outputDataTableIsFile.add("true;"+dirToList + "/" + currentFileName);

                        String time = new String(client.getModificationTime(dirToList + "/" + currentFileName));
                        String year = time.substring(0,4);
                        String month = time.substring(4,6);
                        String day = time.substring(6,8);
                        String hour = time.substring(8,10);
                        String minute = time.substring(10,12);
                        String second = time.substring(12,14);
                        outputTimeStamps.add((day+"/"+month+"/"+year+" - ") + (hour+":"+minute+":"+second) + ";" + dirToList + "/" + currentFileName);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    public File[] listTable() {
        try {
            listDirectory("/home/pi/Cloud/", "");
            if (outputDataTable.size() > -1) {
                File[] output = new File[outputDataTable.size()];
                for (int i = 0; i < outputDataTable.size(); i++) {
                    output[i] = outputDataTable.get(i);
                }
                outputDataTable.clear();
                return output;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public String[] listIsFile(){
        String[] output = new String[outputDataTableIsFile.size()];
        for(int i=0;i<outputDataTableIsFile.size();i++){
            output[i] = outputDataTableIsFile.get(i);
        }
        outputDataTableIsFile.clear();
        return output;
    }
    public String[] getTimestamps(){
        try{
            listDirectory("/home/pi/Cloud/", "");
            if (outputDataTable.size() > -1) {
                String[] output = new String[outputTimeStamps.size()];
                for (int i = 0; i < outputTimeStamps.size(); i++) {
                    output[i] = outputTimeStamps.get(i);
                }
                outputTimeStamps.clear();
                return output;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
