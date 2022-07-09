package org.mgg.langByFolder;

import java.io.File;

public class FileContext implements IReportableItem, Comparable<FileContext>{
    public String itemPath ;
    public String artifactName;
    public boolean isService;
    public boolean isTestFile;
    public String langName ;

    String[] parts = new String[8] ;

    public FileContext(String itemPath, ReportOptions reportOptions) {
        this.itemPath = itemPath ;
        determineArtifact(itemPath, reportOptions) ;
        isService = itemPath.toLowerCase().contains("service") ;
        isTestFile = (reportOptions.testLang.isExtensionMatchedBy(itemPath) || reportOptions.testLang.isContainedBy(itemPath)) ;
        determineFileLanguage(itemPath, reportOptions);
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

    public String getLangStats(ReportOptions reportOptions) {

        return langName +
                reportOptions.columnSeparator +
                1;
    }

    private static String relativePath(String folder, String rootFolder) {
        return folder.substring(rootFolder.length()) ;
    }

    public String getPart(int index) {
        return this.parts[index] ;
    }


    private String rootArtifact(ReportOptions reportOptions) {
        String rf = reportOptions.getRootFolder() ;

        //Get the last folder name as the artifact of the root folder
        return rf.substring(rf.lastIndexOf(File.separatorChar)+1) ;
    }
    public final int preparePartsPathArtifact(ReportOptions reportOptions) {
        int i = 0;

        parts[i++] = itemType() ;
        String relPath = relativePath(itemPath, reportOptions.getRootFolder()) ;

        if(relPath.length()== 0) {
            parts[i++] = reportOptions.getRootFolder() ;
            parts[i++] = rootArtifact(reportOptions) ;
        }
        else {
            parts[i++] = relPath;
            parts[i++] = artifactName;
        }

        return i ;
    }

    public void prepareParts(ReportOptions reportOptions) {
        int i = preparePartsPathArtifact(reportOptions) ;

        parts[i++] = String.valueOf(getNumServices());
        parts[i++] = String.valueOf(getNumTestFiles());
        parts[i++] = "-"; //subfolders
        parts[i++] = "1"; //numFiles
        parts[i] = getLangStats(reportOptions);
    }
    public String toString(ReportOptions reportOptions) {
        prepareParts(reportOptions);
        return String.join(reportOptions.columnSeparator, parts) ;
    }

    public String itemType() {
        return "File";
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
