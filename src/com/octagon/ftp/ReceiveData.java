package com.octagon.ftp;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReceiveData {
    private static FTPconnection ftPconnection = new FTPconnection();

    private FTPClient client;

    public ReceiveData() {
        client = ftPconnection.getClient();
    }

    public void downloadFile(String pInputFile, boolean pIsFile) {
        String userHome = System.getProperty("user.home");
        pInputFile = pInputFile.replace(File.separator, "/");
        String outputAbsolutePath = userHome + File.separator + "OctagonCloud" + File.separator + pInputFile;
        if (!pIsFile) {
            String userHomeDir = userHome + File.separator + "OctagonCloud";
            userHomeDir += File.separator + pInputFile;

            new File(userHomeDir).mkdir();
        }
        if (pIsFile) {
            try (FileOutputStream fos = new FileOutputStream(outputAbsolutePath)) {
                System.out.println("/home/pi/Cloud/"+pInputFile);
                boolean status = client.retrieveFile("/home/pi/Cloud/"+pInputFile, fos);
                System.out.println("Downloaded: " + status+"[" + outputAbsolutePath + "]");
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}