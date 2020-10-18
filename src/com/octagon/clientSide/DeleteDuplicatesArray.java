package com.octagon.clientSide;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class DeleteDuplicatesArray {
    private File[] array;
    public DeleteDuplicatesArray(File[] pInput){
        array = pInput;
    }
    public File[] deleteDuplicates(){
        LinkedHashSet<File> buffer = new LinkedHashSet<>(Arrays.asList(array));
        File[] output = buffer.toArray(new File[buffer.size()]);
        return output;
    }
}
