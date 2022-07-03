package org.mgg;

public class FileContext implements ReportableItem, Comparable<FileContext>{
    public String itemPath ;
    public String artifactName;
    public boolean isService;
    public boolean isTestFile;
    public String langName ;

    public FileContext(String itemPath) {
        this.itemPath = itemPath ;
        artifactName = determineArtifact(itemPath) ;
        isService = itemPath.toLowerCase().contains("service") ;
        isTestFile = (ReportOptions.testLang.isExtensionMatchedBy(itemPath) || ReportOptions.testLang.isContainedBy(itemPath)) ;
    }

    public final String determineArtifact(String path) {
        int pos ;
        String aux ;

        aux = path.substring(ReportOptions.rootFolderPathLength) ;
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

    public final void setLanguageName(String langName) {
        this.langName = langName ;
    }
    public String getLangStats(ReportOptions reportOptions) {

        return langName +
                ReportOptions.columnSeparator +
                1;
    }

    private static String relativePath(String folder, String rootFolder) {
        return folder.substring(rootFolder.length()) ;
    }

    public String toString(ReportOptions reportOptions) {
        StringBuilder resp ;

        resp = new StringBuilder() ;

        if(reportOptions.reportDetailLevel != ReportDetailLevel.CUSTOM) {
            String relPath = relativePath(itemPath, reportOptions.rootFolder) ;

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

        resp.append(ReportOptions.columnSeparator).append(getNumServices());
        resp.append(ReportOptions.columnSeparator).append(getNumTestFiles());
        resp.append(ReportOptions.columnSeparator).append('$'); //subfolders
        resp.append(ReportOptions.columnSeparator).append(1); //numGiles
        resp.append(ReportOptions.columnSeparator).append(getLangStats(reportOptions));

        return resp.toString() ;
    }

    public int getNumTestFiles() {
        return isTestFile? 1 : 0 ;
    }

    public int getNumServices() {
        return isService? 1 : 0 ;
    }
    @Override
    public int compareTo(FileContext other) {
        return this.itemPath.compareToIgnoreCase(other.itemPath);
    }
}
