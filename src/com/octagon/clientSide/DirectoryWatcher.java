package com.octagon.clientSide;

import com.octagon.Controller;
import com.octagon.ftp.DeleteData;
import com.octagon.ftp.SendData;

import java.io.Console;
import java.io.File;
import java.nio.file.*;
import java.util.List;

import static com.sun.nio.file.ExtendedWatchEventModifier.FILE_TREE;
import static java.nio.file.StandardWatchEventKinds.*;


public class DirectoryWatcher {
    private FileSystem fs;
    private WatchService ws;
    private Path pTemp;

    private String userHome;
    private String pathHome;

    private static DeleteData deleteData = new DeleteData();
    private static Controller controller = new Controller();
    private static SendData sendData = new SendData();

    public static Thread threadWatch = null;

    public DirectoryWatcher() {
        userHome = System.getProperty("user.home");
        pathHome = userHome + File.separator + "OctagonCloud" + File.separator;
        try{
            fs = FileSystems.getDefault();
            ws = fs.newWatchService();
            pTemp = Paths.get(pathHome);
            pTemp.register(ws, new WatchEvent.Kind[] {ENTRY_DELETE, ENTRY_CREATE, ENTRY_MODIFY}, FILE_TREE);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void watcher(){
        threadWatch = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(controller.statusAutoUpdate){
                        while (true){
                            WatchKey k = ws.take();
                            for (WatchEvent<?> e : k.pollEvents())
                            {
                                Object c = e.context();
                                if(e.kind() == ENTRY_CREATE){
                                    sendData.sendFile(new File(pathHome + c.toString()));
                                    System.out.println("Storing: " + c.toString());
                                }else if(e.kind() == ENTRY_MODIFY){
                                    sendData.sendFile(new File(pathHome + c.toString()));
                                    System.out.println("Updating: " + c.toString());
                                }else if(e.kind() == ENTRY_DELETE){

                                    deleteData.deleteFolder(c.toString(), "");
                                    deleteData.deleteFile(c.toString());
                                    System.out.println("Deleting: " + c.toString());
                                }
                            }
                            k.reset();
                        }
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        threadWatch.start();
    }
}
