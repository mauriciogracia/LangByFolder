package org.mauriciogracia;

import java.util.ArrayList;

public class DirLanguage {
    public String name ;
    public int numSubfolders;
    public int numFiles ;
    private ArrayList<DirLanguageStats> langStats ;

    public DirLanguage(String dirName) {
        numSubfolders = 0 ;
        numFiles = 0 ;
        name = dirName ;
        langStats = new ArrayList<DirLanguageStats>() ;
    }

    public void addLanguageFileCount(String langName) {
        boolean found = false;
        DirLanguageStats dls = null;
        int i ;

        for(i = 0; !found && (i < langStats.size()); i++) {
            dls = langStats.get(i) ;
            found = dls.languageName.equals(langName) ;
        }

        if(!found) {
            dls = new DirLanguageStats(langName) ;
        }

        dls.numFiles++ ;

        if(!found) {
            langStats.add(dls) ;
        }
    }
    public boolean hastStats() {
        return ! langStats.isEmpty() ;
    }

    public String getStats(boolean withNumFiles) {
        String langStatsStr = "" ;
        DirLanguageStats dls ;

        for(int i = 0; i < langStats.size(); i++) {
            dls = langStats.get(i) ;
            langStatsStr += dls.languageName ;

            if(withNumFiles) {
                langStatsStr += ":" + dls.numFiles ;
            }
            langStatsStr += "|" ;
        }

        return langStatsStr ;
    }
}
