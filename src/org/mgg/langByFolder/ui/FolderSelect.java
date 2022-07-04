package org.mgg.langByFolder.ui;

import javax.swing.*;
import java.awt.*;

public class FolderSelect extends MarginPaddingPanel {

    public FolderSelect() {
        super(Color.gray, 7,10) ;
        add(new JLabel("Folder to check..."), BorderLayout.PAGE_START) ;
        add(new JTextField("path/to/folder/to/scan"), BorderLayout.CENTER) ;
        add(new JButton("..."), BorderLayout.LINE_END) ;
    }
}
