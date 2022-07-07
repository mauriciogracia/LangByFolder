package org.mgg.langByFolder;

import java.util.ArrayList;
import java.util.StringJoiner;

public class DirectoryContext extends FileContext {
    private int numSubfolders;
    private int numFiles ;
    private int numTestFiles ;
    private int numServices;
    public final ArrayList<LanguageStats> langStats ;

    public DirectoryContext(String itemPath, ReportOptions reportOptions) {
        super(itemPath, reportOptions) ;
        numSubfolders = 0 ;
        numFiles = 0;
        numTestFiles = 0 ;
        numServices = 0 ;
        langStats = new ArrayList<>() ;
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

    public String getLangStats(ReportOptions reportOptions) {
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
        numSubfolders += subItem.numSubfolders +1 ;
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

    private static String relativePath(String folder, String rootFolder) {
        return folder.substring(rootFolder.length()) ;
    }

    public String toString(ReportOptions reportOptions) {
        StringJoiner joiner ;
        String relPath ;

        joiner = new StringJoiner(reportOptions.columnSeparator);

        if(reportOptions.reportDetailLevel != ReportDetailLevel.CUSTOM) {
            relPath = relativePath(itemPath, reportOptions.getRootFolder()) ;

            if(relPath.length()== 0) {
                relPath = reportOptions.getRootFolder() + reportOptions.columnSeparator + "ROOT" ;
                joiner.add(relPath);
            }
            else {
                joiner.add(relPath);
                joiner.add(artifactName);
            }
        }
        else {
            joiner.add(artifactName) ;
        }

        joiner.add("" + numServices);
        joiner.add("" + numTestFiles);
        joiner.add("" + numSubfolders);
        joiner.add("" + getNumFiles());
        joiner.add(getLangStats(reportOptions));

        return joiner.toString() ;
    }

    public int getNumFiles() {
        return numFiles;
    }

}
