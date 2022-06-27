package org.mauriciogracia;

import java.util.ArrayList;
import java.util.Collections;

public class ItemLanguage implements Comparable<ItemLanguage>{
    public String itemPath ;
    public String artifactName;
    public int numSubfolders;
    public int numFiles ;
    public int numTestFiles ;
    public boolean isApiService ;
    private ArrayList<DirLanguageStats> langStats ;
    static int prefixLength ;

    public ItemLanguage(String itemPath) {
        this.itemPath = itemPath ;
        artifactName = determineArtifact(itemPath) ;
        isApiService = itemPath.contains("-service") ;
        numTestFiles = 0 ;
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
        if (pos >= 0) {
            aux = aux.substring(1,pos) ;
        }

        if(aux.startsWith("/")) {
            aux = aux.substring(1) ;
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

    public String getStats(boolean withNumFiles) {
        StringBuilder langStatsStr = new StringBuilder();
        DirLanguageStats dls ;
        int i ;
        int max ;

        max = langStats.size() ;
        Collections.sort(langStats);

        for(i = 0; i < max; i++) {
            dls = langStats.get(i) ;

            langStatsStr.append(dls.languageName);

            if(withNumFiles) {
                langStatsStr.append("|").append(dls.numFiles);
            }

            if(i +1 != max) {
                langStatsStr.append("|");
            }
        }

        return langStatsStr.toString();
    }

    public void mergeStats(ItemLanguage subItem) {
        int pos ;
        this.numFiles += subItem.numFiles ;
        this.numSubfolders += subItem.numSubfolders ;
        this.numTestFiles += subItem.numTestFiles ;

        for(DirLanguageStats dls : subItem.langStats) {
            pos = langStats.indexOf(dls) ;

            if(pos >= 0)
            {
                langStats.get(pos).numFiles += dls.numFiles ;
            }
            else {
                langStats.add(dls) ;
            }
        }
    }

    private static String relativePath(String folder, String rootFolder) {
        return folder.substring(rootFolder.length()) ;
    }

    public String toString(ReportOptions reportOptions) {
        String resp ;

        if(reportOptions.reportDetailLevel != ReportDetailLevel.CUSTOM) {
            resp = relativePath(itemPath, reportOptions.rootFolder);
            resp += "|" + artifactName;
        }
        else {
            resp = artifactName ;
        }

        resp += "|" + isApiService;
        resp += "|numTestFiles:" + numTestFiles;

        if (reportOptions.showFolderStats) {
            resp += "|" + numSubfolders;
            resp += "|" + numFiles;
        }

        resp += "|" + getStats(reportOptions.showFolderStats);

        return resp ;
    }

    public static String getHeader(ReportOptions reportOptions) {
        String header = "";

        if(reportOptions.reportDetailLevel != ReportDetailLevel.CUSTOM) {
            header += "Folder|";
        }
        header += "Artifact|isApiService|numTestFiles" ;

        if(!reportOptions.showFolderStats) {
            header += "|# Subfolders|# Total Files" ;
        }

        header += "|Languages" ;

        return header ;
    }

    @Override
    public int compareTo(ItemLanguage other) {
        return this.itemPath.compareToIgnoreCase(other.itemPath);
    }
}
