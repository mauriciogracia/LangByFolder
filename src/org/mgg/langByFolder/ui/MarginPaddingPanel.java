package org.mgg.langByFolder.ui;

import javax.swing.*;
import java.awt.*;

public class MarginPaddingPanel extends JPanel{
    JPanel margin ;
    JPanel content;

    //@todo: switch to JavaFX - table view example https://jenkov.com/tutorials/javafx/tableview.html
    /**
     * This is a JPanel that has margin, padding and a border color
     * @param borderColor to be used
     * @param marg margin in px
     * @param pad padding in px
     */
    public MarginPaddingPanel(Color borderColor, int marg, int pad) {
        super() ;
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(marg,marg,marg,marg));

        content = new JPanel(new BorderLayout()) ;
        content.setBorder(BorderFactory.createEmptyBorder(pad,pad,pad,pad));

        margin = new JPanel(new BorderLayout()) ;
        margin.setBorder(BorderFactory.createLineBorder(borderColor));
        margin.add(content) ;

        this.add(margin) ;
    }

    @Override
    public void add(Component comp, Object constraints) {
        content.add(comp, constraints);
    }
}
