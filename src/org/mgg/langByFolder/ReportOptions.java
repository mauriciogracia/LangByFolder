package org.mgg.langByFolder;

import org.mgg.langByFolder.stats.CompareByLanguageName;
import org.mgg.langByFolder.stats.CompareByLanguageOccurrence;
import org.mgg.langByFolder.stats.LanguageStats;
import org.mgg.langByFolder.stats.LanguageStatsComparator;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ReportOptions {
    private final static String[] testExt = {"spec.ts","spec.py","spec.scala","test"} ;
    public final ArrayList<String> artifacts = new ArrayList<>();
    public ArrayList<FileContext> items = new ArrayList<>();
    final LanguageExtensions testLang = new LanguageExtensions("TestFiles",testExt) ;
    final ArrayList<String> excludeFolders = new ArrayList<>();
    public String columnSeparator = ";";
    private int rootFolderPathLength;
    public static ArrayList<LanguageExtensions> supportedLanguageExt;
    public boolean validArguments ;
    public String errorMessage ;
    private String rootFolder ;
    public boolean showHiddenItems  ;
    public boolean showUnknownExtensions  ;
    public ReportDetailLevel reportDetailLevel = ReportDetailLevel.ALL_ITEMS ;
    public PrintStream output ;
    public LanguageStatsComparator langStatComparator ;
    private final static List<String> validOptions = Arrays.asList("a","f","c","h","u","o","n");

    private static final String[] headers = {"Type","Path","Artifact","numServices","numTestFiles","# SubFolders","# Files","Languages"} ;
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

    public ReportOptions(String[] args) {
        parseArguments(args);

        if(validArguments) {
            initLanguageExtensions();
            initFoldersToExclude();
        }
    }

    public ReportOptions() {
        setDefaultReportOptions() ;
        initLanguageExtensions();
        initFoldersToExclude();
    }

    public static String getHeader(int i) {
        return headers[i] ;
    }
    public boolean isExcludedFolder(String folderName) {
        return excludeFolders.contains(folderName) ;
    }
    void initLanguageExtensions() {
        supportedLanguageExt = new ArrayList<>() ;

        String []tsExt = {".ts"} ;
        supportedLanguageExt.add(new LanguageExtensions("Typescript",tsExt)) ;

        String []scalaExt = {".scala"} ;
        supportedLanguageExt.add(new LanguageExtensions("Scala",scalaExt)) ;

        String []javaExt = {".java",".jar","war"} ;
        supportedLanguageExt.add(new LanguageExtensions("Java",javaExt)) ;

        String []jsExt = {".js"} ;
        supportedLanguageExt.add(new LanguageExtensions("Javascript",jsExt)) ;

        String []cSharpExt = {".cs"} ;
        supportedLanguageExt.add(new LanguageExtensions("C#",cSharpExt)) ;

        String []cPlusPlusExt = {".cpp"} ;
        supportedLanguageExt.add(new LanguageExtensions("C++",cPlusPlusExt)) ;

        String []vsProjectSolution = {".csproj", ".sln"} ;
        supportedLanguageExt.add(new LanguageExtensions("vsProjectSolution(sln,csproj)",vsProjectSolution)) ;

        String []winArtifact = {".dll", ".exe", ".pdb"} ;
        supportedLanguageExt.add(new LanguageExtensions("Windows(exe,dll,pdb)",winArtifact)) ;

        String []rubyExt = {".rb"} ;
        supportedLanguageExt.add(new LanguageExtensions("Ruby",rubyExt)) ;

        String []databaseExt = {".sql"} ;
        supportedLanguageExt.add(new LanguageExtensions("Database(sql)",databaseExt)) ;

        String []pythonExt = {".py"} ;
        supportedLanguageExt.add(new LanguageExtensions("Python",pythonExt)) ;

        String []htmlExt = {".html",".htm"} ;
        supportedLanguageExt.add(new LanguageExtensions("HTML",htmlExt)) ;

        String []docExt = {".md",".txt"} ;
        supportedLanguageExt.add(new LanguageExtensions("Doc(md,txt)",docExt)) ;

        String []dataExt = {".json",".xml"} ;
        supportedLanguageExt.add(new LanguageExtensions("Data(json,xml)",dataExt)) ;

        String []bazelExt = {".bazel",".bzl"} ;
        supportedLanguageExt.add(new LanguageExtensions("Bazel",bazelExt)) ;

        String []shellExt = {".bash",".sh"} ;
        supportedLanguageExt.add(new LanguageExtensions("Shell",shellExt)) ;

        String []imageExt = {".png",".svg",".jpg",".jpeg"} ;
        supportedLanguageExt.add(new LanguageExtensions("Image",imageExt)) ;
    }

    void initFoldersToExclude()  {
        excludeFolders.add("node_modules") ;
        excludeFolders.add("bazel-bin") ;
        excludeFolders.add("bazel-main") ;
        excludeFolders.add("bazel-out") ;
        excludeFolders.add("bazel-testlogs") ;
        excludeFolders.add("output") ;
    }

    public String getHeader() {
        return String.join(columnSeparator, headers) ;
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
            String path ;

            path = args[0] ;

            if(Files.exists(Paths.get(path))) {
                setRootFolder(path) ;
                output = System.out ;
                validArguments = parseOptions(args);
            }
            else {
                errorMessage = "Path does not exist:" + path;
                validArguments = false ;
            }

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

    public String getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
        this.rootFolderPathLength = rootFolder.length();
    }

    public int getRootFolderPathLength() {
        return rootFolderPathLength;
    }

    public void clear() {
        items.clear();
        artifacts.clear();
    }
}
