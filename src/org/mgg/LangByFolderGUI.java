package org.mgg;

import org.mgg.langByFolder.ui.FolderSelect;

import javax.swing.*;
import java.awt.*;

public class LangByFolderGUI {
    public static void main(String[] args) {
        JFrame frame ;
        JPanel panel ;

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new FolderSelect(),BorderLayout.PAGE_START) ;

        frame = new JFrame("LangByFolder") ;
        frame.add(panel) ;
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
