package org.mauriciogracia;

import java.util.ArrayList;

public class ItemLanguage implements Comparable<ItemLanguage>{
    public String itemPath ;
    public String artifactName;
    public int numSubfolders;
    public int numFiles ;
    public boolean isApiService ;
    public boolean isTest ;
    private ArrayList<DirLanguageStats> langStats ;
    static int prefixLength ;

    public ItemLanguage(String itemPath) {
        this.itemPath = itemPath ;
        artifactName = determineArtifact(itemPath) ;
        isApiService = itemPath.contains("-service") ;
        isTest = itemPath.contains("test") ;
        numSubfolders = 0 ;
        numFiles = 0 ;
        langStats = new ArrayList<>() ;
    }

    public static String determineArtifact(String path) {
        int pos ;
        String aux ;

        aux = path.substring(prefixLength) ;
        pos = aux.indexOf('/', 1);

        //The name of the first level folder is used as the base/component/artifact name
        if (pos > 0) {
            aux = aux.substring(1,pos) ;
        }
        return aux ;
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
        StringBuilder langStatsStr = new StringBuilder();
        DirLanguageStats dls ;
        int i ;
        int max ;

        max = langStats.size() ;

        for(i = 0; i < max; i++) {
            dls = langStats.get(i) ;
            langStatsStr.append(dls.languageName);

            if(withNumFiles) {
                langStatsStr.append(":").append(dls.numFiles);
            }
            if(i +1 != max) {
                langStatsStr.append("|");
            }
        }

        return langStatsStr.toString();
    }
    private static String relativePath(String folder, String rootFolder) {
        return folder.substring(rootFolder.length()) ;
    }

    public String toString(boolean compactMode, String rootFolder) {
        String resp ;

        resp = relativePath(itemPath, rootFolder) ;

        if (hastStats()) {
            resp += "|" + artifactName;
            resp += "|" + isApiService;
            resp += "|" + isTest;
            resp += "|" + getStats(!compactMode);

            if (!compactMode) {
                resp += "|" + numSubfolders;
                resp += "|" + numFiles;
            }
        }

        return resp ;
    }

    @Override
    public int compareTo(ItemLanguage other) {
        return this.itemPath.compareToIgnoreCase(other.itemPath);
    }
}
