package org.mauriciogracia;

import java.util.ArrayList;
import java.util.Arrays;

public class LanguageExtensions {
    public String languageName ;
    public String []extensions;

    public int numFiles ;

    public LanguageExtensions(String langName, String [] ext) {
        numFiles = 0 ;
        languageName = langName ;
        extensions = ext ;
    }

    public boolean nameMatches(String itemName) {
        boolean isMatch ;
        int i ;
        isMatch = false ;

        i = 0 ;

        while(!isMatch && (i < extensions.length)) {
            isMatch = itemName.toLowerCase().endsWith(extensions[i]) ;
            i++ ;
        }

        return isMatch ;
    }
}
