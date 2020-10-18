package com.octagon.clientSide;

import com.octagon.Controller;
import com.octagon.ftp.ReceiveData;
import com.octagon.ftp.ReceiveDataTable;
import com.octagon.ftp.SendData;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;

public class CompareDataTables {
    private static ReceiveDataTable receiveDataTable = new ReceiveDataTable();
    private static ReceiveLocalDataTable receiveLocalDataTable = new ReceiveLocalDataTable();
    private static SendData sendData = new SendData();
    private static ReceiveData receiveData = new ReceiveData();
    private static Controller controller = new Controller();
    private static DeleteDuplicatesArray deleteDuplicatesArray;

    public File[] remoteData;
    public File[] localData;
    public String[] isFileArray;
    public String[] timeStempsLocal;
    public String[] timeStempsremote;

    String userHome = System.getProperty("user.home") + File.separator + "OctagonCloud";

    public CompareDataTables() {
        remoteData = receiveDataTable.listTable();
        System.out.println();
        localData = receiveLocalDataTable.getData();

        deleteDuplicatesArray = new DeleteDuplicatesArray(remoteData);
        remoteData = deleteDuplicatesArray.deleteDuplicates();
        deleteDuplicatesArray = new DeleteDuplicatesArray(localData);
        localData = deleteDuplicatesArray.deleteDuplicates();


        if(controller.statusTimeStampsSync){
            timeStempsremote = receiveDataTable.getTimestamps();
            timeStempsLocal = receiveLocalDataTable.getLocalTimeStamps();
        }
        isFileArray = receiveDataTable.listIsFile();
        for (int i = 0; i < remoteData.length; i++) {
            String tmp = remoteData[i].getAbsolutePath();
            remoteData[i] = new File(tmp.replace("\\home\\pi\\Cloud", "").replace("C:", "").replace("/home/pi/Cloud", ""));
        }
        for (int i = 0; i < localData.length; i++) {
            String tmp = localData[i].getAbsolutePath();
            localData[i] = new File(tmp.replace(userHome, ""));
        }
    }
    public void returnTime(){
        try{
            System.out.println("--- Timestamps data ---");
            for(String item : timeStempsremote){
                System.out.println(item);
            }
            System.out.println("---");
            for(String item : timeStempsLocal){
                System.out.println(item);
            }
            System.out.println();
        }catch (NullPointerException e){

        }
    }
    public void returnData(){
        if(controller.statusTimeStampsSync)return;
        try{
            System.out.println("--- Data ---");
            for(File item : remoteData){
                System.out.println(item.getAbsolutePath());
            }
            System.out.println("---");
            for(File item : localData){
                System.out.println(item.getAbsolutePath());
            }
            System.out.println();
        }catch (NullPointerException e){

        }
    }

    public File[] getUploadFile() { //Files that must be uploaded to the server
        returnTime();
        returnData();
        ArrayList<File> outputFiles = new ArrayList<>();
        boolean statusFound;
        for (File fileLocal : localData) {
            statusFound = false;
            for (File fileRemote : remoteData) {
                if (fileLocal.equals(fileRemote)) {
                    //File that will be uploaded, hence it is not on the server
                    if(new File(userHome+fileLocal).isFile() && controller.statusTimeStampsSync){
                        String metaLocal = "", metaRemote = "";
                        for(int i=0;i<timeStempsLocal.length;i++){
                            String timeName = FilenameUtils.getName(timeStempsLocal[i].split(";")[1]);
                            if(FilenameUtils.getName(fileLocal.toString()).equals(timeName)){
                                metaLocal = timeStempsLocal[i].split(";")[0];
                            }
                        }
                        for(int i=0;i<timeStempsremote.length;i++){
                            String timeName = FilenameUtils.getName(timeStempsremote[i].split(";")[1]);
                            if(FilenameUtils.getName(fileLocal.toString()).equals(timeName)){
                                metaRemote = timeStempsremote[i].split(";")[0];
                            }
                        }
                        String dateL = "", dateR = "", timeL = "", timeR = "";
                            dateL = metaLocal.split("- ")[0];
                            dateR = metaRemote.split("- ")[0];
                            timeL = metaLocal.split("- ")[1];
                            timeR = metaRemote.split("- ")[1];

                        if(!(dateL.equals(dateR))){
                            if(Integer.parseInt(dateL.substring(0,2)) > Integer.parseInt(dateR.substring(0,2))){
                                if(Integer.parseInt(dateL.substring(3,5)) >= Integer.parseInt(dateR.substring(3,5))){
                                    if(Integer.parseInt(dateL.substring(6,10)) >= Integer.parseInt(dateR.substring(6,10))){
                                        statusFound=false;
                                    }else if(Integer.parseInt(dateL.substring(6,10)) != Integer.parseInt(dateR.substring(6,10))){
                                        statusFound=true;
                                    }
                                }else if(Integer.parseInt(dateL.substring(3,5)) != Integer.parseInt(dateR.substring(3,5))){
                                    statusFound=true;
                                }
                            }else if(Integer.parseInt(dateL.substring(0,2)) != Integer.parseInt(dateR.substring(0,2))){
                                statusFound=true;
                            }
                            if(Integer.parseInt(dateL.substring(3,5)) > Integer.parseInt(dateR.substring(3,5))){
                                if(Integer.parseInt(dateL.substring(6,10)) >= Integer.parseInt(dateR.substring(6,10))){
                                    statusFound=false;
                                }else if(Integer.parseInt(dateL.substring(6,10)) != Integer.parseInt(dateR.substring(6,10))){
                                    statusFound=true;
                                }
                            }else if(Integer.parseInt(dateL.substring(3,5)) != Integer.parseInt(dateR.substring(3,5))){
                                statusFound=true;
                            }
                            if(Integer.parseInt(dateL.substring(6,10)) > Integer.parseInt(dateR.substring(6,10))){
                                statusFound=false;
                            }else if(Integer.parseInt(dateL.substring(6,10)) != Integer.parseInt(dateR.substring(6,10))){
                                statusFound=true;
                            }
                        }else{
                            statusFound=true;
                        }

                        //*
                        if(!(timeL.equals(timeR))){
                            if(Integer.parseInt(timeL.substring(6,8)) > Integer.parseInt(timeR.substring(6,8))){
                                if(Integer.parseInt(timeL.substring(3,5)) >= Integer.parseInt(timeR.substring(3,5))){
                                    if(Integer.parseInt(timeL.substring(0,2)) >= Integer.parseInt(timeR.substring(0,2))){
                                        statusFound=false;
                                    }else if(Integer.parseInt(timeL.substring(0,2)) != Integer.parseInt(timeR.substring(0,2))){
                                        statusFound=true;
                                    }
                                }else if(Integer.parseInt(timeL.substring(3,5)) != Integer.parseInt(timeR.substring(3,5))){
                                    statusFound=true;
                                }
                            }else if(Integer.parseInt(timeL.substring(6,8)) != Integer.parseInt(timeR.substring(6,8))){
                                statusFound=true;
                            }
                            if(Integer.parseInt(timeL.substring(3,5)) > Integer.parseInt(timeR.substring(3,5))){
                                if(Integer.parseInt(timeL.substring(0,2)) >= Integer.parseInt(timeR.substring(0,2))){
                                    statusFound=false;
                                }else if(Integer.parseInt(timeL.substring(0,2)) != Integer.parseInt(timeR.substring(0,2))){
                                    statusFound=true;
                                }
                            }else if(Integer.parseInt(timeL.substring(3,5)) != Integer.parseInt(timeR.substring(3,5))){
                                statusFound=true;
                            }
                            if(Integer.parseInt(timeL.substring(0,2)) > Integer.parseInt(timeR.substring(0,2))){
                                statusFound=false;
                            }else if(Integer.parseInt(timeL.substring(0,2)) != Integer.parseInt(timeR.substring(0,2))){
                                statusFound=true;
                            }
                        }else{
                            statusFound=true;
                        }
                    }else{
                        statusFound=true;
                    }
                }
            }
            if (!statusFound) {
                outputFiles.add(new File(userHome + fileLocal));
            }
        }
        File[] OUTPUT = new File[outputFiles.size()];
        for (int i = 0; i < outputFiles.size(); i++) {
            OUTPUT[i] = outputFiles.get(i);
        }
        return OUTPUT;
    }

    public File[] getDownloadFile() { //Files that must be downloaded to the computer
        ArrayList<File> outputFiles = new ArrayList<>();
        boolean statusFound;
        for (int i=0;i<remoteData.length;i++) {
            statusFound = false;
            for (File fileLocal : localData) {
                if(new File(userHome+fileLocal).isFile() && controller.statusTimeStampsSync){
                    String metaLocal = "", metaRemote = "";
                    for(int i2=0;i2<timeStempsLocal.length;i2++){
                        String timeName = FilenameUtils.getName(timeStempsLocal[i2].split(";")[1]);
                        if(FilenameUtils.getName(fileLocal.toString()).equals(timeName)){
                            metaLocal = timeStempsLocal[i2].split(";")[0];
                        }
                    }
                    for(int i2=0;i2<timeStempsremote.length;i2++){
                        String timeName = FilenameUtils.getName(timeStempsremote[i2].split(";")[1]);
                        if(FilenameUtils.getName(fileLocal.toString()).equals(timeName)){
                            metaRemote = timeStempsremote[i2].split(";")[0];
                        }
                    }
                    String dateL = "", dateR = "", timeL = "", timeR = "";
                    dateL = metaLocal.split("- ")[0];
                    dateR = metaRemote.split("- ")[0];
                    timeL = metaLocal.split("- ")[1];
                    timeR = metaRemote.split("- ")[1];

                    if(!(dateR.equals(dateL))){
                        if(Integer.parseInt(dateR.substring(0,2)) > Integer.parseInt(dateL.substring(0,2))){
                            if(Integer.parseInt(dateR.substring(3,5)) >= Integer.parseInt(dateL.substring(3,5))){
                                if(Integer.parseInt(dateR.substring(6,10)) >= Integer.parseInt(dateL.substring(6,10))){
                                    statusFound=false;
                                }else if(Integer.parseInt(dateR.substring(6,10)) != Integer.parseInt(dateL.substring(6,10))){
                                    statusFound=true;
                                }
                            }else if(Integer.parseInt(dateR.substring(3,5)) != Integer.parseInt(dateL.substring(3,5))){
                                statusFound=true;
                            }
                        }else if(Integer.parseInt(dateR.substring(0,2)) != Integer.parseInt(dateL.substring(0,2))){
                            statusFound=true;
                        }
                        if(Integer.parseInt(dateR.substring(3,5)) > Integer.parseInt(dateL.substring(3,5))){
                            if(Integer.parseInt(dateR.substring(6,10)) >= Integer.parseInt(dateL.substring(6,10))){
                                statusFound=false;
                            }else if(Integer.parseInt(dateR.substring(6,10)) != Integer.parseInt(dateL.substring(6,10))){
                                statusFound=true;
                            }
                        }else if(Integer.parseInt(dateR.substring(3,5)) != Integer.parseInt(dateL.substring(3,5))){
                            statusFound=true;
                        }
                        if(Integer.parseInt(dateR.substring(6,10)) > Integer.parseInt(dateL.substring(6,10))){
                            statusFound=false;
                        }else if(Integer.parseInt(dateR.substring(6,10)) != Integer.parseInt(dateL.substring(6,10))){
                            statusFound=true;
                        }
                    }else{
                        statusFound=true;
                    }

                    //*
                    if(!(timeR.equals(timeL))){
                        if(Integer.parseInt(timeR.substring(6,8)) > Integer.parseInt(timeL.substring(6,8))){
                            if(Integer.parseInt(timeR.substring(3,5)) >= Integer.parseInt(timeL.substring(3,5))){
                                if(Integer.parseInt(timeR.substring(0,2)) >= Integer.parseInt(timeL.substring(0,2))){
                                    statusFound=false;
                                }else if(Integer.parseInt(timeR.substring(0,2)) != Integer.parseInt(timeL.substring(0,2))){
                                    statusFound=true;
                                }
                            }else if(Integer.parseInt(timeR.substring(3,5)) != Integer.parseInt(timeL.substring(3,5))){
                                statusFound=true;
                            }
                        }else if(Integer.parseInt(timeR.substring(6,8)) != Integer.parseInt(timeL.substring(6,8))){
                            statusFound=true;
                        }
                        if(Integer.parseInt(timeR.substring(3,5)) > Integer.parseInt(timeL.substring(3,5))){
                            if(Integer.parseInt(timeR.substring(0,2)) >= Integer.parseInt(timeL.substring(0,2))){
                                statusFound=false;
                            }else if(Integer.parseInt(timeR.substring(0,2)) != Integer.parseInt(timeL.substring(0,2))){
                                statusFound=true;
                            }
                        }else if(Integer.parseInt(timeR.substring(3,5)) != Integer.parseInt(timeL.substring(3,5))){
                            statusFound=true;
                        }
                        if(Integer.parseInt(timeR.substring(0,2)) > Integer.parseInt(timeL.substring(0,2))){
                            statusFound=false;
                        }else if(Integer.parseInt(timeR.substring(0,2)) != Integer.parseInt(timeL.substring(0,2))){
                            statusFound=true;
                        }
                    }else{
                        statusFound=true;
                    }
                }else{
                    statusFound=true;
                }
            }
            if (!statusFound) {
                outputFiles.add(new File("/home/pi/Cloud" + remoteData[i]));
            }
        }
        File[] OUTPUT = new File[outputFiles.size()];
        for (int i = 0; i < outputFiles.size(); i++) {
            OUTPUT[i] = outputFiles.get(i);
        }
        return OUTPUT;
    }
}
