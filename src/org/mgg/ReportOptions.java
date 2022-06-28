package org.mgg;

import java.io.PrintStream;

public class ReportOptions {
    public boolean validArguments ;
    public String rootFolder ;
    public boolean showHiddenItems  ;
    public boolean showUnknownExtensions  ;
    public ReportDetailLevel reportDetailLevel = ReportDetailLevel.ALL_ITEMS ;

    public PrintStream output ;
    public static String usageOptions =
        "\nLangByFolder <path> <options> <outFile.csv>" +
                "\n\t<options>:" +
                "\n\t\t-a: show all files" +
                "\n\t\t-f: show only folders (default)" +
                "\n\t\t-c: show custom artifact" +
                "\n\t\t-h: show hidden files/folders" +
                "\n\t\t-u: show unknown extensions" +
                "\n\n\tExample $java org.mgg.LangByFolder /your/path -fhu output.txt" ;

    private void setDefaultReportOptions() {
        reportDetailLevel = ReportDetailLevel.FOLDER ;
        showHiddenItems = false ;
        showUnknownExtensions = false ;
    }
    private void parseArguments(String[] args) {
        int numArgs ;

        validArguments = false ;
        numArgs = args.length ;

        if((numArgs >= 1) && (numArgs <=  3))
        {
            rootFolder = args[0] ;

            if(numArgs == 1) {
                setDefaultReportOptions() ;
                output = System.out ;
            }

            /*
            if output file is pass as parameter
                String destination = "file1.txt";

            try(PrintStream ps = new PrintStream(destination)){
            https://riptutorial.com/java/example/12976/writing-a-file-using-printstream

            */
            validArguments = true ;
        }
    }


    public static ReportOptions processArguments(String[] args) {
        ReportOptions options = new ReportOptions();

        options.parseArguments(args) ;

        return options ;
    }
}
