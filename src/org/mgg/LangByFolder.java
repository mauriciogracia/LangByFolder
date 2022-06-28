package org.mgg;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import static org.mgg.ReportOptions.usageOptions;

public class LangByFolder {
    private static ArrayList<LanguageExtensions> languages  ;
    private final static ArrayList<ItemLanguage> items = new ArrayList<>();
    private final static ArrayList<String> excludeFolders = new ArrayList<>();
    private final static ArrayList<String> artifacts = new ArrayList<>();
    private final static String[] testExt = {"spec.ts","spec.py","spec.scala","test"} ;
    private final static LanguageExtensions testLang = new LanguageExtensions("TestFiles",testExt) ;
    public static void main(String[] args) {
        ReportOptions reportOptions ;

        reportOptions = ReportOptions.processArguments(args);

        if(!reportOptions.validArguments) {
            System.out.println(usageOptions);
        }
        else {
            processRootFolder(reportOptions) ;
        }
    }
    public static void processRootFolder(ReportOptions reportOptions) {
        initLanguageExtensions();
        initFoldersToExclude();

        //Iterate the specified folder
        ItemLanguage.prefixLength = reportOptions.rootFolder.length();
        ItemLanguage dl = new ItemLanguage(reportOptions.rootFolder);
        iterateFolder(dl, reportOptions);

        Collections.sort(items);

        showResults(reportOptions);
    }

    private static void showResults(ReportOptions reportOptions) {
        reportOptions.output.println(ItemLanguage.getHeader(reportOptions));

        for (ItemLanguage item : items) {
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

    private static void iterateFolder(ItemLanguage currentFolder, ReportOptions reportOptions)  {
        try {
            File folder = new File(currentFolder.itemPath);
            File[] folderItems = folder.listFiles();
            String itemName ;
            String childPathStr ;
            ItemLanguage childIL ;

            if(folderItems != null) {
                for (File folderItem : folderItems) {
                    itemName = folderItem.getName();
                    childPathStr = currentFolder.itemPath + "/" + itemName ;

                    childIL = null ;

                    if ((reportOptions.showHiddenItems || !folderItem.isHidden()) && !itemName.startsWith("/.")) {
                        if (folderItem.isDirectory()) {
                            childIL = processFolder(currentFolder, itemName, reportOptions);
                        } else if (folderItem.isFile()) {
                            String whichLanguage = determineFileLanguage(itemName, reportOptions);
                            currentFolder.numFiles++;

                            if (!whichLanguage.equals("unknown")) {
                                currentFolder.addLanguageFileCount(whichLanguage);
                            }

                            childIL = new ItemLanguage(childPathStr);
                            childIL.addLanguageFileCount(whichLanguage);

                            if(testLang.isExtensionMatchedBy(itemName) || testLang.isContainedBy(itemName))
                            {
                                childIL.numTestFiles += 1 ;
                            }

                            if (reportOptions.reportDetailLevel == ReportDetailLevel.ALL_ITEMS) {
                                items.add(childIL);
                            }

                        }

                        //merge stats from child items (both folders/files) with current parent element
                        if(childIL != null)
                        {
                            currentFolder.mergeStats(childIL);
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
            System.err.println("Error:" + ex.getMessage());
        }
    }

    private static ItemLanguage processFolder(ItemLanguage itemLanguage,String itemName, ReportOptions reportOptions) {
        String artifactName ;
        String subDirPath ;
        ItemLanguage dlSub = null ;

        if(!excludeFolders.contains(itemName)) {
            subDirPath = itemLanguage.itemPath + "/" + itemName ;
            artifactName = ItemLanguage.determineArtifact(subDirPath) ;

            dlSub = new ItemLanguage(subDirPath);

            if((reportOptions.reportDetailLevel == ReportDetailLevel.CUSTOM) && !artifacts.contains(artifactName)) {
                artifacts.add(artifactName);
            }

            items.add(dlSub);
            itemLanguage.numSubfolders++;
            iterateFolder(dlSub, reportOptions);
        }

        return dlSub ;
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
