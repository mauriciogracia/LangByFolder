package org.mauriciogracia;

public class ReportOptions {
    public boolean validArguments ;
    public String rootFolder ;
    public boolean showFolderStats;
    public boolean showHiddenItems  ;
    public boolean showUnknownExtensions  ;
    public ReportDetailLevel reportDetailLevel = ReportDetailLevel.ALL_ITEMS ;


    public static String usageOptions =
        "LangByFolder <path> <options> <outFile.csv>\n\t<options>:\n\t\t-a: show all files (default)\n\t\t-f: show only folders\n\t\t-c: custom artifact\n\t\t-h: show hidden files/folders\n\t\t-u: show unknown extensions (default)" ;
    private void checkArguments(String[] args) {
        int numArgs ;

        validArguments = false ;
        numArgs = args.length ;

        if((numArgs >= 2) && (numArgs <=  4))
        {
            rootFolder = args[1] ;

            if(numArgs == 2) {
                setDefaultReportOptions() ;
            }
        }
    }

    private void setDefaultReportOptions() {
        showFolderStats = true ;
        showHiddenItems = false ;
        showUnknownExtensions = true ;
        reportDetailLevel = ReportDetailLevel.ALL_ITEMS ;
    }
    public static ReportOptions processArguments(String[] args) {
        ReportOptions options = new ReportOptions();
        boolean validArgs ;

        options.checkArguments(args) ;



        return options ;
    }
}
