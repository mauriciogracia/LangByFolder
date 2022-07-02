package org.mgg;

import java.util.ArrayList;

public class ItemLanguage implements Comparable<ItemLanguage>{
    public String itemPath ;
    public String artifactName;
    public int numSubfolders;
    public int numFiles ;
    public int numTestFiles ;
    public boolean isService;
    private final ArrayList<DirLanguageStats> langStats ;
    static int prefixLength ;
    public static String columnSeparator = ";";

    public ItemLanguage(String itemPath) {
        this.itemPath = itemPath ;
        artifactName = determineArtifact(itemPath) ;
        isService = itemPath.toLowerCase().contains("service") ;
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

        dls.increaseNumFiles(1);

        if(!found) {
            langStats.add(dls) ;
        }
    }

    public String getStats(ReportOptions reportOptions) {
        StringBuilder langStatsStr = new StringBuilder();
        int max ;

        max = langStats.size() ;
        langStats.sort(reportOptions.langStatComparator);

        for(int i = 0; i < max; i++) {
            DirLanguageStats dls = langStats.get(i) ;

            langStatsStr.append(dls.languageName);
            langStatsStr.append(columnSeparator).append(dls.getNumFiles());

            if(i +1 != max) {
                langStatsStr.append(columnSeparator);
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
                DirLanguageStats current = langStats.get(pos);
                current.increaseNumFiles(dls.getNumFiles()) ;
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
        StringBuilder resp ;
        String relPath ;

        resp = new StringBuilder() ;

        if(reportOptions.reportDetailLevel != ReportDetailLevel.CUSTOM) {
            relPath = relativePath(itemPath, reportOptions.rootFolder) ;

            if(relPath.length()== 0) {
                relPath = "ROOT" + columnSeparator + reportOptions.rootFolder;
                resp.append(relPath);
            }
            else {
                resp.append(relPath);
                resp.append(columnSeparator).append(artifactName);
            }


        }
        else {
            resp.append(artifactName) ;
        }

        resp.append(columnSeparator).append(isService);
        resp.append(columnSeparator).append(numTestFiles);
        resp.append(columnSeparator).append(numSubfolders);
        resp.append(columnSeparator).append(numFiles);
        resp.append(columnSeparator).append(getStats(reportOptions));

        return resp.toString() ;
    }

    public static String getHeader(ReportOptions reportOptions) {
        StringBuilder header ;

        header = new StringBuilder() ;

        if(reportOptions.reportDetailLevel != ReportDetailLevel.CUSTOM) {
            header.append("Folder").append(columnSeparator);
        }
        header.append("Artifact").append(columnSeparator);
        header.append("isService").append(columnSeparator);
        header.append("numTestFiles").append(columnSeparator);
        header.append("# Subfolders").append(columnSeparator);
        header.append("# Total Files").append(columnSeparator);
        header.append("Languages").append(columnSeparator);

        return header.toString() ;
    }

    @Override
    public int compareTo(ItemLanguage other) {
        return this.itemPath.compareToIgnoreCase(other.itemPath);
    }
}
