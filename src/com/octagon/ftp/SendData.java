package com.octagon.ftp;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class SendData {
    public static FTPconnection ftPconnection = new FTPconnection();
    public static ReceiveDataTable receiveDataTable = new ReceiveDataTable();

    private FTPClient client;
    private File[] ftpFiles;

    public SendData() {
        client = ftPconnection.getClient();
        ftpFiles = receiveDataTable.listTable();
    }

    public void sendFile(File pInputFile) {
        try {
            if (!pInputFile.exists()) {
                return;
            }
            String userHome = System.getProperty("user.home");
            String pathHome = userHome + File.separator + "OctagonCloud" + File.separator;
            String subDirs = pInputFile.getAbsolutePath().replace(pathHome, "");//REPLACE FEHLER ER REPLACED ZUM BEISPIEL ALLE D ODER ALLE R etc //////fine
            subDirs = subDirs.replace(File.separator, "/");

            int count = StringUtils.countMatches(subDirs, "/") - 1;
            if (!(count == -1)) {
                String[] splitter = subDirs.split("/");
                String output = "";
                for (int i = 0; i <= count; i++) {
                    output += splitter[i] + "/";
                }
                subDirs = output;
            }

            if (subDirs.equals(pInputFile.getName())) {
                client.changeWorkingDirectory("/home/pi/Cloud/");
            } else {
                client.changeWorkingDirectory("/home/pi/Cloud/" + subDirs + "/");
            }
            if (pInputFile.isDirectory()) {
                boolean status = client.makeDirectory(pInputFile.getName());
                System.out.println("Uploaded: " + status);
            } else if (pInputFile.isFile()) {
                InputStream inputStream = new FileInputStream(pInputFile);//get data from file
                boolean status = client.storeFile(pInputFile.getName(), inputStream);
                inputStream.close();
                System.out.println("Uploaded: " + status);
            }
            System.out.println();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}