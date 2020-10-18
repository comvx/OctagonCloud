package com.octagon.clientSide;

import javax.swing.*;

public class MessageBox {
    public void infoBox(String pText){
        JOptionPane.showMessageDialog(null, pText, "OctagonCloud", JOptionPane.INFORMATION_MESSAGE);
    }
}
