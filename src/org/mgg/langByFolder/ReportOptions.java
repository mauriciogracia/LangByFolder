package org.mgg.langByFolder;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ReportOptions {
    private final static String[] testExt = {"spec.ts","spec.py","spec.scala","test"} ;
    public ArrayList<FileContext> items = new ArrayList<>();
    final LanguageExtensions testLang = new LanguageExtensions("TestFiles",testExt) ;
    final ArrayList<String> excludeFolders = new ArrayList<>();
    public String columnSeparator = ";";
    private int rootFolderPathLength;
    public ArrayList<LanguageExtensions> languages  ;
    public boolean validArguments ;
    public String errorMessage ;
    private String rootFolder ;
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

    public boolean isExcludedFolder(String folderName) {
        return excludeFolders.contains(folderName) ;
    }
    void initLanguageExtensions() {
        languages = new ArrayList<>() ;

        String []tsExt = {".ts"} ;
        languages.add(new LanguageExtensions("Typescript",tsExt)) ;

        String []scalaExt = {".scala"} ;
        languages.add(new LanguageExtensions("Scala",scalaExt)) ;

        String []javaExt = {".java",".jar","war"} ;
        languages.add(new LanguageExtensions("Java",javaExt)) ;

        String []jsExt = {".js"} ;
        languages.add(new LanguageExtensions("Javascript",jsExt)) ;

        String []cSharpExt = {".cs"} ;
        languages.add(new LanguageExtensions("C#",cSharpExt)) ;

        String []cPlusPlusExt = {".cpp"} ;
        languages.add(new LanguageExtensions("C++",cPlusPlusExt)) ;

        String []vsProjectSolution = {".csproj", ".sln"} ;
        languages.add(new LanguageExtensions("vsProjectSolution(sln,csproj)",vsProjectSolution)) ;

        String []winArtifact = {".dll", ".exe", ".pdb"} ;
        languages.add(new LanguageExtensions("Windows(exe,dll,pdb)",winArtifact)) ;

        String []rubyExt = {".rb"} ;
        languages.add(new LanguageExtensions("Ruby",rubyExt)) ;

        String []databaseExt = {".sql"} ;
        languages.add(new LanguageExtensions("Database(sql)",databaseExt)) ;

        String []pythonExt = {".py"} ;
        languages.add(new LanguageExtensions("Python",pythonExt)) ;

        String []htmlExt = {".html",".htm"} ;
        languages.add(new LanguageExtensions("HTML",htmlExt)) ;

        String []docExt = {".md",".txt"} ;
        languages.add(new LanguageExtensions("Doc(md,txt)",docExt)) ;

        String []dataExt = {".json",".xml"} ;
        languages.add(new LanguageExtensions("Data(json,xml)",dataExt)) ;

        String []bazelExt = {".bazel",".bzl"} ;
        languages.add(new LanguageExtensions("Bazel",bazelExt)) ;

        String []shellExt = {".bash",".sh"} ;
        languages.add(new LanguageExtensions("Shell",shellExt)) ;

        String []imageExt = {".png",".svg",".jpg",".jpeg"} ;
        languages.add(new LanguageExtensions("Image",imageExt)) ;
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
        StringJoiner joiner = new StringJoiner(columnSeparator) ;

        if(reportDetailLevel != ReportDetailLevel.CUSTOM) {
            joiner.add("Folder");
        }
        joiner.add("Artifact");
        joiner.add("isService");
        joiner.add("numTestFiles");
        joiner.add("# Subfolders");
        joiner.add("# Total Files");
        joiner.add("Languages");

        return joiner.toString() ;
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
}
