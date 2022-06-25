package org.mauriciogracia;

public class DirLanguageStats {
    public String languageName;
    public int numFiles;

    DirLanguageStats(String langName) {
        languageName = langName ;
        numFiles = 0;
    }

    /*
    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false ;

        if(obj != null) {
            isEqual = ((DirLanguageStats)obj).languageName.equals(this.languageName) ;
        }

        return isEqual ;
    }
    */
}
