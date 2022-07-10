package org.mgg.langByFolder;

import java.util.ArrayList;

public class DirectoryContext extends FileContext {
    private int numSubFolders;
    private int numFiles ;
    private int numTestFiles ;
    private int numServices;
    public final ArrayList<LanguageStats> langStats ;

    public DirectoryContext(String itemPath, ReportOptions reportOptions) {
        super(itemPath, reportOptions) ;
        numSubFolders = 0 ;
        numFiles = 0;
        numTestFiles = 0 ;
        numServices = 0 ;
        langStats = new ArrayList<>() ;
    }

    @Override
    public int getNumTestFiles() {
        return numTestFiles;
    }

    @Override
    public int getNumSubFolders() {
        return numSubFolders;
    }

    @Override
    public int getNumFiles() {
        return numFiles;
    }

    public int getNumServices() {
        return numServices ;
    }
    public void mergeFile(FileContext file) {
        this.numFiles++;
        this.numTestFiles += getNumTestFiles() ;
        this.numServices += getNumServices() ;

        boolean found = false ;

        for(LanguageStats dls : langStats) {
            found = dls.languageName.equals(file.langName) ;

            if (found) {
                dls.increaseNumFiles(1);
                break ;
            }
        }

        if(!found) {
            LanguageStats ls =new LanguageStats(file.langName) ;
            ls.increaseNumFiles(1);
            langStats.add(ls) ;
        }
    }

    public String getLangStats() {
        //@todo use StrinJoin or convert to actual TableView columns
        StringBuilder langStatsStr = new StringBuilder();
        int max ;

        max = langStats.size() ;
        langStats.sort(reportOptions.langStatComparator);

        for(int i = 0; i < max; i++) {
            LanguageStats dls = langStats.get(i) ;

            langStatsStr.append(dls.languageName);
            langStatsStr.append(reportOptions.columnSeparator).append(dls.getNumFiles());

            if(i + 1 != max) {
                langStatsStr.append(reportOptions.columnSeparator);
            }
        }

        return langStatsStr.toString();
    }
    public void mergeDirectory(DirectoryContext subItem) {
        int pos ;

        numFiles += subItem.numFiles;
        numSubFolders += subItem.numSubFolders +1 ;
        numTestFiles += subItem.numTestFiles ;
        numServices += subItem.numServices ;

        for(LanguageStats dls : subItem.langStats) {
            pos = langStats.indexOf(dls) ;

            if(pos >= 0)
            {
                LanguageStats current = langStats.get(pos);
                current.increaseNumFiles(dls.getNumFiles()) ;
            }
            else {
                langStats.add(dls) ;
            }
        }
    }

    public String getItemType() {
        return "Dir";
    }
}
