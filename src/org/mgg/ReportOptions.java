package org.mgg;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ReportOptions {
    private final static String[] testExt = {"spec.ts","spec.py","spec.scala","test"} ;
    final static LanguageExtensions testLang = new LanguageExtensions("TestFiles",testExt) ;
    public static String columnSeparator = ";";
    static int rootFolderPathLength;
    public boolean validArguments ;
    public String errorMessage ;
    public String rootFolder ;
    public boolean showHiddenItems  ;
    public boolean showUnknownExtensions  ;
    public ReportDetailLevel reportDetailLevel = ReportDetailLevel.ALL_ITEMS ;

    public PrintStream output ;

    public Comparator<LanguageStats>  langStatComparator ;
    private final static List<String> validOptions = Arrays.asList("a","f","c","h","u","o","n");

    public static String usageOptions =
        "\nLangByFolder <path> <options> <outFile.csv>" +
                "\n\t<options>:" +
                "\n\t\t-a: show all files" +
                "\n\t\t-f: show only folders (default)" +
                "\n\t\t-c: show custom artifact" +
                "\n" +
                "\n\t\t-h: show hidden files/folders" +
                "\n" +
                "\n\t\t-u: show unknown extensions" +
                "\n" +
                "\n\t\t-o: order stats by language occurrence (default)" +
                "\n\t\t-n: order stats by language name" +
                "\n\n\tExample: $java org.mgg.LangByFolder /your/path -fhu output.txt" ;

    public String getHeader() {
        StringBuilder header ;

        header = new StringBuilder() ;

        if(reportDetailLevel != ReportDetailLevel.CUSTOM) {
            header.append("Folder").append(columnSeparator);
        }
        header.append("Artifact").append(columnSeparator);
        header.append("isService").append(columnSeparator);
        header.append("numTestFiles").append(columnSeparator);
        header.append("# Subfolders").append(columnSeparator);
        header.append("# Total Files").append(columnSeparator);
        header.append("Languages").append(columnSeparator);

        return header.toString() ;
    }

    private void setDefaultReportOptions() {
        reportDetailLevel = ReportDetailLevel.FOLDER ;
        showHiddenItems = false ;
        showUnknownExtensions = false ;
        langStatComparator = new CompareByLanguageOccurrence();
    }
    private void parseArguments(String[] args) {
        int numArgs ;

        validArguments = false ;
        numArgs = args.length ;

        if((numArgs >= 1) && (numArgs <=  3))
        {
            rootFolder = args[0] ;
            output = System.out ;

            validArguments = parseOptions(args);

        }
        else {
            errorMessage = "Wrong number of arguments, path is required, other arguments are optional" ;
        }
    }

    private boolean parseOptions(String[] args) {
        boolean optionsParsedOk ;
        String currentChar ;
        int numArgs ;

        numArgs = args.length ;
        optionsParsedOk = false ;

        setDefaultReportOptions();

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
                        case "h":
                            showHiddenItems = true ;
                            break ;
                        case "u":
                            showUnknownExtensions = true ;
                            break ;
                        case "o":
                            langStatComparator = new CompareByLanguageOccurrence() ;
                            break ;
                        case "n":
                            langStatComparator = new CompareByLanguageName() ;
                            break ;
                    }
                }
                else {
                    errorMessage = String.format("'%s' must be a valid option: %s", currentChar, validOptions) ;
                    break ;
                }
            }

            if(numArgs == 3){
                setOutputStream(args[2]);
            }

            optionsParsedOk = (errorMessage == null) ;
        }
        else if(numArgs == 3){
             errorMessage = "The options must start with a single '-'" ;
        }
        else {
            if(numArgs == 2){
                setOutputStream(args[1]);
            }
            optionsParsedOk = true ;
        }

        return optionsParsedOk ;
    }

    private void setOutputStream(String reportDestination) {
        try {
            this.output = new PrintStream(reportDestination) ;
        } catch (FileNotFoundException e) {
            errorMessage = e.getMessage();
        }
    }
    public static ReportOptions processArguments(String[] args) {
        ReportOptions options = new ReportOptions();

        options.parseArguments(args) ;

        return options ;
    }
}
