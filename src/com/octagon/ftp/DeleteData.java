package com.octagon.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DeleteData {
    private static FTPconnection ftPconnection = new FTPconnection();

    private FTPClient client;

    public DeleteData() {
        client = ftPconnection.getClient();
    }

    public void deleteFile(String pInputFile) {
        try{
            pInputFile = pInputFile.replace(File.separator, "/");
            boolean status = client.deleteFile("/home/pi/Cloud/"+pInputFile);
            System.out.println("Deleted: " + status);
        }catch (Exception ex){

        }
    }
    public void deleteFolder(String parentDir, String currentDir) throws IOException {
        String dirToList = parentDir;
        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        }

        FTPFile[] subFiles = client.listFiles(dirToList);

        if (subFiles != null && subFiles.length > 0) {
            for (FTPFile aFile : subFiles) {
                String currentFileName = aFile.getName();
                if (currentFileName.equals(".") || currentFileName.equals("..")) {
                    // skip parent directory and the directory itself
                    continue;
                }
                String filePath = parentDir + "/" + currentDir + "/"
                        + currentFileName;
                if (currentDir.equals("")) {
                    filePath = parentDir + "/" + currentFileName;
                }

                if (aFile.isDirectory()) {
                    // remove the sub directory
                    deleteFolder(dirToList, currentFileName);
                } else {
                    // delete the file
                    boolean deleted = client.deleteFile(filePath);
                    System.out.println("Deleting[File]: " + filePath);
                }
            }

            // finally, remove the directory itself
            boolean removed = client.removeDirectory(dirToList);
            if (removed) {
                System.out.println("Deleting[Dir]: " + dirToList);
            }
        }
    }
}
