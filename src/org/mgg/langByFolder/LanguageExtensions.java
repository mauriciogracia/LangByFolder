package org.mgg.langByFolder;

public class LanguageExtensions {
    public String languageName ;
    public String []extensions;

    public LanguageExtensions(String langName, String [] ext) {
        languageName = langName ;
        extensions = ext ;
    }

    public boolean isExtensionMatchedBy(String itemName) {
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

    public boolean isContainedBy(String itemName) {
        boolean isMatch ;
        int i ;
        isMatch = false ;

        i = 0 ;

        while(!isMatch && (i < extensions.length)) {
            isMatch = itemName.toLowerCase().contains(extensions[i]) ;
            i++ ;
        }

        return isMatch ;
    }
}
