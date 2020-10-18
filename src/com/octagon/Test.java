package com.octagon;

import com.octagon.clientSide.DirectoryWatcher;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.*;

import static com.sun.nio.file.ExtendedWatchEventModifier.FILE_TREE;
import static java.nio.file.StandardWatchEventKinds.*;

public class Test {
    public static String lastEvent = "";
    public static void main(String[] args) throws Exception
    {
            DirectoryWatcher directoryWatcher = new DirectoryWatcher();
            directoryWatcher.watcher();
    }
}
