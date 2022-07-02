package org.mgg;

public class DirLanguageStats implements Comparable<DirLanguageStats>{
    public String languageName;
    private int numFiles;

    DirLanguageStats(String langName) {
        languageName = langName ;
        numFiles = 0;
    }

    public void increaseNumFiles(int delta) {
        this.numFiles += delta ;
    }

    @Override
    public boolean equals(Object other) {
        boolean isEqual = false ;

        if (other instanceof  DirLanguageStats) {
            isEqual = this.languageName.equals(((DirLanguageStats)other).languageName) ;
        }

        return isEqual ;
    }

    @Override
    public int compareTo(DirLanguageStats other) {
        return languageName.compareTo(other.languageName) ;
    }

    public int getNumFiles() {
        return numFiles;
    }
}
