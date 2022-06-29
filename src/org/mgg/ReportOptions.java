package org.mgg;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

public class ReportOptions {
    public boolean validArguments ;
    public String errorMessage ;

    public String rootFolder ;
    public boolean showHiddenItems  ;
    public boolean showUnknownExtensions  ;
    public ReportDetailLevel reportDetailLevel = ReportDetailLevel.ALL_ITEMS ;

    public PrintStream output ;

    private final static List<String> validOptions = Arrays.asList("a","f","c","h","u");

    public static String usageOptions =
        "\nLangByFolder <path> <options> <outFile.csv>" +
                "\n\t<options>:" +
                "\n\t\t-a: show all files" +
                "\n\t\t-f: show only folders (default)" +
                "\n\t\t-c: show custom artifact" +
                "\n\t\t-h: show hidden files/folders" +
                "\n\t\t-u: show unknown extensions" +
                "\n\n\tExample: $java org.mgg.LangByFolder /your/path -fhu output.txt" ;

    private void setDefaultReportOptions() {
        reportDetailLevel = ReportDetailLevel.FOLDER ;
        showHiddenItems = false ;
        showUnknownExtensions = false ;
    }
    private void parseArguments(String[] args) {
        int numArgs ;
        String outputPath ;
        String options ;
        validArguments = false ;
        numArgs = args.length ;

        if((numArgs >= 1) && (numArgs <=  3))
        {
            rootFolder = args[0] ;
            output = System.out ;

            validArguments = parseOptions(args);
            /*
            if output file is pass as parameter
                String destination = "file1.txt";

            try(PrintStream ps = new PrintStream(destination)){
            https://riptutorial.com/java/example/12976/writing-a-file-using-printstream

            */
        }
    }

    private boolean parseOptions(String[] args) {
        boolean optionsParsedOk ;
        String currentChar ;
        int numArgs ;

        numArgs = args.length ;
        optionsParsedOk = false ;

        if((numArgs > 1) && (args[1].startsWith("-"))) {
            String opts = args[1] ;

            for(int i = 1; i < opts.length(); i++)
            {
                currentChar = "" + opts.charAt(i) ;

                if(validOptions.contains(currentChar)){
                    switch (currentChar) {
                        case "a":
                            reportDetailLevel = ReportDetailLevel.ALL_ITEMS ;
                            break ;
                        case "c":
                            reportDetailLevel = ReportDetailLevel.CUSTOM ;
                            break ;
                        case "f":
                            reportDetailLevel = ReportDetailLevel.FOLDER ;
                            break ;
                    }
                }
                else {
                    errorMessage = String.format("'%s' must be a valid option: %s", currentChar, validOptions) ;
                    break ;
                }
            }

            optionsParsedOk = (errorMessage == null) ;
        }
        else if(numArgs == 3){
             errorMessage = "option must start with '-'" ;
        }
        else {
            setDefaultReportOptions();
            optionsParsedOk = true ;
        }

        return optionsParsedOk ;
    }


    public static ReportOptions processArguments(String[] args) {
        ReportOptions options = new ReportOptions();

        options.parseArguments(args) ;

        return options ;
    }
}
