package org.mgg.langByFolder;

public class FileContext implements IReportableItem, Comparable<FileContext>{
    public String itemPath ;
    public String artifactName;
    public boolean isService;
    public boolean isTestFile;
    public String langName ;

    public FileContext(String itemPath, ReportOptions reportOptions) {
        this.itemPath = itemPath ;
        determineArtifact(itemPath, reportOptions) ;
        isService = itemPath.toLowerCase().contains("service") ;
        isTestFile = (reportOptions.testLang.isExtensionMatchedBy(itemPath) || reportOptions.testLang.isContainedBy(itemPath)) ;
        determineFileLanguage(itemPath, reportOptions);
    }

    public String getItemPath() {
        return itemPath ;
    }

    private void determineFileLanguage(String fileName, ReportOptions reportOptions) {
        boolean match ;
        int i = 0 ;
        int max ;

        langName = "unknown" ;
        max = reportOptions.languages.size() ;

        do {
            LanguageExtensions lang = reportOptions.languages.get(i) ;
            match = lang.isExtensionMatchedBy(fileName) ;

            if(match)
            {
                langName = lang.languageName ;
            }
            i++  ;
        } while (!match && (i < max)) ;

        if(reportOptions.showUnknownExtensions && langName.equals("unknown")) {
            String ext ;
            int pos = fileName.lastIndexOf('.') ;

            ext = (pos >= 0) ? fileName.substring(pos) : fileName ;
            langName += "(" + ext + ")" ;
        }
    }
    private void determineArtifact(String path, ReportOptions reportOptions) {
        int pos ;
        String aux ;

        if(path.equals(reportOptions.getRootFolder())) {
            aux = reportOptions.getRootFolder();
        }
        else {
            aux = path.substring(reportOptions.getRootFolderPathLength());
            pos = aux.indexOf('/', 1);

            //The name of the first level folder is used as the base/component/artifact name
            if (pos >= 0) {
                aux = aux.substring(1, pos);
            }

            if (aux.startsWith("/")) {
                aux = aux.substring(1);
            }
        }

        artifactName = aux ;
    }

    public final void setLanguageName(String langName) {
        this.langName = langName ;
    }
    public String getLangStats(ReportOptions reportOptions) {

        return langName +
                reportOptions.columnSeparator +
                1;
    }

    private static String relativePath(String folder, String rootFolder) {
        return folder.substring(rootFolder.length()) ;
    }

    public String toString(ReportOptions reportOptions) {
        StringBuilder resp ;

        resp = new StringBuilder() ;

        //@todo: using StringJoiner here
        if(reportOptions.reportDetailLevel != ReportDetailLevel.CUSTOM) {
            String relPath = relativePath(itemPath, reportOptions.getRootFolder()) ;

            if(relPath.length()== 0) {
                relPath = "ROOT" + reportOptions.columnSeparator + reportOptions.getRootFolder();
                resp.append(relPath);
            }
            else {
                resp.append(relPath);
                resp.append(reportOptions.columnSeparator).append(artifactName);
            }
        }
        else {
            resp.append(artifactName) ;
        }

        resp.append(reportOptions.columnSeparator).append(getNumServices());
        resp.append(reportOptions.columnSeparator).append(getNumTestFiles());
        resp.append(reportOptions.columnSeparator).append('$'); //subfolders
        resp.append(reportOptions.columnSeparator).append(1); //numGiles
        resp.append(reportOptions.columnSeparator).append(getLangStats(reportOptions));

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
