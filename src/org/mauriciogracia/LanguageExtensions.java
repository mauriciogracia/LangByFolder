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

    public boolean fileMatches(String fileExtension) {
        boolean isMatch ;

        isMatch = Arrays.asList(extensions).contains(fileExtension.toLowerCase()) ;
        if(isMatch) {
            numFiles++ ;
        }
        return isMatch ;
    }
}
