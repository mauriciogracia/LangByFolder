package org.mauriciogracia;

public class ReportOptions {
    public String rootFolder ;
    public boolean showFolderStats;
    public boolean showHiddenItems  ;
    public boolean showUnknownExtensions  ;
    public ReportDetailLevel reportDetailLevel = ReportDetailLevel.ALL_ITEMS ;

    public static ReportOptions processArguments(String[] args) {
        ReportOptions options = new ReportOptions();

        /*
        LangByFolder <path> <options> <outFile.csv>
            <options>:
                -a: show all files
                -f: show only folders
                -c: custom artifact
                -h: show hidden files/folders
                -u: show unknown extensions
         */
        options.rootFolder = "C:\\DATOS\\repos\\ApParquearApi" ;
        options.showFolderStats = true ;
        options.showHiddenItems = false ;
        options.showUnknownExtensions = false ;
        options.reportDetailLevel = ReportDetailLevel.CUSTOM ;

        return options ;
    }
}
