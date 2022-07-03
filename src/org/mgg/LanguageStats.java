package org.mgg;

public class LanguageStats implements Comparable<LanguageStats>{
    public String languageName;
    private int numFiles;

    LanguageStats(String langName) {
        languageName = langName ;
        numFiles = 0;
    }

    public void increaseNumFiles(int delta) {
        this.numFiles += delta ;
    }

    @Override
    public boolean equals(Object other) {
        boolean isEqual = false ;

        if (other instanceof LanguageStats) {
            isEqual = this.languageName.equals(((LanguageStats)other).languageName) ;
        }

        return isEqual ;
    }

    @Override
    public int compareTo(LanguageStats other) {
        return languageName.compareTo(other.languageName) ;
    }

    public int getNumFiles() {
        return numFiles;
    }

}
