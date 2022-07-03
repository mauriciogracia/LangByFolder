package org.mgg;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;

import static org.mgg.ReportOptions.usageOptions;

public class LangByFolder {
    private static ArrayList<LanguageExtensions> languages  ;
    private final static ArrayList<FileContext> items = new ArrayList<>();
    private final static ArrayList<String> excludeFolders = new ArrayList<>();
    private final static ArrayList<String> artifacts = new ArrayList<>();

    public static void main(String[] args) {
        ReportOptions reportOptions ;

        reportOptions = ReportOptions.processArguments(args);

        if(!reportOptions.validArguments) {
            System.out.println(reportOptions.errorMessage) ;
            System.out.println(usageOptions);
        }
        else {
            processRootFolder(reportOptions) ;
        }
    }
    public static void processRootFolder(ReportOptions reportOptions) {
        if(Files.exists(Paths.get(reportOptions.rootFolder))) {
            initLanguageExtensions();
            initFoldersToExclude();

            //Iterate the specified folder
            ReportOptions.rootFolderPathLength = reportOptions.rootFolder.length();
            DirectoryContext dc = new DirectoryContext(reportOptions.rootFolder);
            iterateFolder(dc, reportOptions);

            Collections.sort(items);

            showResults(reportOptions);
        }
        else {
            System.err.println("Path does not exist:" + reportOptions.rootFolder);
        }
    }

    private static void showResults(ReportOptions reportOptions) {
        reportOptions.output.println(reportOptions.getHeader());

        for (FileContext item : items) {
            reportOptions.output.println(item.toString(reportOptions));
        }
    }
    private static void initLanguageExtensions() {
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

    private  static String determineFileLanguage(String fileName, ReportOptions reportOptions) {
        String langName = "unknown" ;
        int i = 0 ;
        boolean match ;

        do {
            LanguageExtensions lang =languages.get(i) ;
            match = lang.isExtensionMatchedBy(fileName) ;

            if(match)
            {
                langName = lang.languageName ;
            }
            i++  ;
        } while (!match && (i < languages.size())) ;

        if(reportOptions.showUnknownExtensions && langName.equals("unknown")) {
            String ext ;
            int pos = fileName.lastIndexOf('.') ;

            ext = (pos >= 0) ? fileName.substring(pos) : fileName ;
            langName += "(" + ext + ")" ;
        }

        return langName ;
    }

    static int depthLevel = 0 ;

    private static void iterateFolder(DirectoryContext dirContext, ReportOptions reportOptions)  {
        try {
            File folder = new File(dirContext.itemPath);
            File[] folderItems = folder.listFiles();
            String childName ;
            String childPathStr ;
            BasicFileAttributes itemAttributes ;

            depthLevel++;
            if(folderItems != null) {
                for (File folderItem : folderItems) {
                    childName = folderItem.getName();
                    childPathStr = dirContext.itemPath + "/" + childName ;

                    if (reportOptions.showHiddenItems || (!folderItem.isHidden() && !childName.startsWith("/."))) {
                        itemAttributes = Files.readAttributes(folderItem.toPath(), BasicFileAttributes.class);

                        if (itemAttributes.isDirectory()) {
                            processFolder(dirContext, childName, reportOptions);
                        } else if (itemAttributes.isRegularFile()) {
                            //@todo; this should be part of the FileContext contructor
                            String whichLanguage = determineFileLanguage(childName, reportOptions);

                            FileContext childIL = new FileContext(childPathStr);
                            childIL.setLanguageName(whichLanguage);

                            if (reportOptions.reportDetailLevel == ReportDetailLevel.ALL_ITEMS) {
                                items.add(childIL);
                            }

                            dirContext.mergeFile(childIL);
                        }
                    }
                }
            }
            depthLevel--;
            //add the root folder stats
            if(depthLevel == 0) {
                items.add(dirContext) ;
            }
        }
        catch (Exception ex) {
            //@todo: unix links are being shown here, needs to be fixed
            System.err.println("Error:" + ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }

    private static void processFolder(DirectoryContext dirContext, String itemName, ReportOptions reportOptions) {
        String subDirPath ;

        if(!excludeFolders.contains(itemName)) {
            subDirPath = dirContext.itemPath + "/" + itemName ;
            DirectoryContext dlSub = new DirectoryContext(subDirPath);

            if((reportOptions.reportDetailLevel == ReportDetailLevel.CUSTOM) && !artifacts.contains(dlSub.artifactName)) {
                artifacts.add(dlSub.artifactName);
                items.add(dlSub);
            }
            else if((reportOptions.reportDetailLevel != ReportDetailLevel.CUSTOM)) {
                items.add(dlSub);
            }
            iterateFolder(dlSub, reportOptions);
            dirContext.mergeDirectory(dlSub);
        }
    }

    private static void initFoldersToExclude()  {
        excludeFolders.add("node_modules") ;
        excludeFolders.add("bazel-bin") ;
        excludeFolders.add("bazel-main") ;
        excludeFolders.add("bazel-out") ;
        excludeFolders.add("bazel-testlogs") ;
        excludeFolders.add("output") ;
    }






}
