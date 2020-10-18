package com.octagon.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.net.ConnectException;

public class FTPconnection {
    public static String host = "octagoncloudpi.ddns.net", username = "pi", password = "xizido77"; //octagoncloudpi.ddns.net
    private static FTPClient client = null;

    public FTPconnection() {
        try {
            client = new FTPClient();
            //client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            client.connect(host);
            client.login(username, password);
            client.setFileType(FTP.BINARY_FILE_TYPE);
            client.enterLocalPassiveMode();
            client.changeWorkingDirectory("/home/pi/Cloud/");
        } catch (ConnectException ce){
            System.out.println("Check your/the server internet connection!");
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public FTPClient getClient() {
        if (!client.equals(null)) {
            return client;
        }
        return null;
    }
}
