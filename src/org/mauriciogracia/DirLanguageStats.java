package org.mauriciogracia;

public class DirLanguageStats {
    public String languageName;
    public int numFiles;

    DirLanguageStats(String langName) {
        languageName = langName ;
        numFiles = 0;
    }

    @Override
    public boolean equals(Object other) {
        boolean isEqual = false ;

        if (other instanceof  DirLanguageStats) {
            isEqual = this.languageName.equals(((DirLanguageStats)other).languageName) ;
        }

        return isEqual ;
    }
}
