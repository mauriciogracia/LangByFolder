package org.mgg;

public class DirectoryContext extends FileContext {
    private int numSubfolders;
    private int numFiles ;
    private int numTestFiles ;
    private int numServices;

    public DirectoryContext(String itemPath) {
        super(itemPath) ;
        numSubfolders = 0 ;
        numFiles = 0;
        numTestFiles = 0 ;
        numServices = 0 ;
    }

    public void mergeFile(FileContext file) {
        int pos ;
        this.numFiles++;
        this.numTestFiles += getNumTestFiles() ;
        this.numServices += getNumServices() ;

        for(LanguageStats dls : file.langStats) {
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
    public void mergeDirectory(DirectoryContext subItem) {
        int pos ;

        numFiles += subItem.numFiles;
        numSubfolders += subItem.numSubfolders ;
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
        StringBuilder resp ;
        String relPath ;

        resp = new StringBuilder() ;

        if(reportOptions.reportDetailLevel != ReportDetailLevel.CUSTOM) {
            relPath = relativePath(itemPath, reportOptions.rootFolder) ;

            if(relPath.length()== 0) {
                relPath = "ROOT" + ReportOptions.columnSeparator + reportOptions.rootFolder;
                resp.append(relPath);
            }
            else {
                resp.append(relPath);
                resp.append(ReportOptions.columnSeparator).append(artifactName);
            }


        }
        else {
            resp.append(artifactName) ;
        }

        resp.append(ReportOptions.columnSeparator).append(numServices);
        resp.append(ReportOptions.columnSeparator).append(numTestFiles);
        resp.append(ReportOptions.columnSeparator).append(numSubfolders);
        resp.append(ReportOptions.columnSeparator).append(getNumFiles());
        resp.append(ReportOptions.columnSeparator).append(getLangStats(reportOptions));

        return resp.toString() ;
    }

    public int getNumFiles() {
        return numFiles;
    }

}
