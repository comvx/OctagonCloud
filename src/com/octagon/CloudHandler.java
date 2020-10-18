package com.octagon;

import com.octagon.clientSide.CompareDataTables;
import com.octagon.clientSide.DirectoryWatcher;
import com.octagon.ftp.ReceiveData;
import com.octagon.ftp.SendData;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;

public class CloudHandler {
    //private static Run run = new Run();
    private static CompareDataTables compareDataTables;
    private static SendData sendData = new SendData();
    private static ReceiveData receiveData = new ReceiveData();

    public static Thread threadDownload, threadUpload;

    public void synx() {
        try {
            compareDataTables = new CompareDataTables();
            threadUpload = new Thread(new Runnable() {
                @Override
                public void run() {
                    File[] uploadedFiles = compareDataTables.getUploadFile();
                    for(File file : uploadedFiles){
                        System.out.println("Uploaded file[t1]: " + file.getName());
                        sendData.sendFile(file);
                    }
                }
            });
            threadDownload = new Thread(new Runnable() {
                @Override
                public void run() {
                    File[] downloadedFiles = compareDataTables.getDownloadFile();
                    String[] isFileInfo = compareDataTables.isFileArray;

                    for (File file : downloadedFiles) {
                        boolean isFile = false;
                        for (String itemInfo : isFileInfo) {
                            String tmpInfo = itemInfo.split(";")[0];
                            String name = itemInfo.split(";")[1];
                            name = name.replace("/", "").replaceAll("homepiCloud", "");

                            String tmp = file.getAbsolutePath();
                            tmp = tmp.replace(File.separator, "").replace("C:", "").replaceAll("homepiCloud", "");

                            if (name.equals(tmp)) {
                                if (tmpInfo.equals("true")) {
                                    isFile = true;
                                }
                            }
                        }
                        System.out.println("Downloaded file[t1]: " + file.getName());
                        String tmp = file.getAbsolutePath();
                        tmp = tmp.replace("\\home\\pi\\Cloud\\", "").replace("C:", "").replaceAll("home/pi/Cloud/", "");
                        receiveData.downloadFile(tmp, isFile);
                    }
                }
            });//        directoryWatcher.watcher();
            threadDownload.start();
            threadUpload.start();
            try {
                threadDownload.join();
                threadUpload.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}