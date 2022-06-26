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
        boolean isEqueal = false ;

        if(other != null) {
            isEqueal = this.languageName.equals(((DirLanguageStats)other).languageName) ;
        }

        return isEqueal ;
    }
}
