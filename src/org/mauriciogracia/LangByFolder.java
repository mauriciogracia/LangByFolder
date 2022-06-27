package org.mauriciogracia;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import static org.mauriciogracia.ReportOptions.usageOptions;

public class LangByFolder {
    private static ArrayList<LanguageExtensions> languages  ;
    private final static ArrayList<ItemLanguage> items = new ArrayList<>();
    private final static ArrayList<String> excludeFolders = new ArrayList<>();
    private final static ArrayList<String> artifacts = new ArrayList<>();
    private final static String[] testExt = {"spec.ts","spec.py","spec.scala"} ;
    private final static LanguageExtensions testLangExt = new LanguageExtensions("TestFiles",testExt) ;

    private static ReportOptions reportOptions ;

    public static void main(String[] args) {
        reportOptions = ReportOptions.processArguments(args);

        if(!reportOptions.validArguments) {
            System.out.println(usageOptions);
        }
        else {
            InitLanguageExtensions();
            InitFoldersToExclude();

            //Iterate the specified folder
            ItemLanguage.prefixLength = reportOptions.rootFolder.length();
            ItemLanguage dl = new ItemLanguage(reportOptions.rootFolder);
            IterateFolder(dl);

            Collections.sort(items);

            showResults();
        }
    }
    private static void InitLanguageExtensions() {
        languages = new ArrayList<>() ;

        String []tsExt = {".ts"} ;
        languages.add(new LanguageExtensions("Typescript",tsExt)) ;

        String []scalaExt = {".scala"} ;
        languages.add(new LanguageExtensions("Scala",scalaExt)) ;

        String []javaExt = {".java",".jar","war"} ;
        languages.add(new LanguageExtensions("Java",javaExt)) ;

        String []jsExt = {".js"} ;
        languages.add(new LanguageExtensions("Javascript",jsExt)) ;

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

    private  static String determineFileLanguage(String fileName) {
        String langName = "unknown" ;
        int i = 0 ;
        boolean match ;

        do {
            LanguageExtensions lang =languages.get(i) ;
            match = lang.nameMatches(fileName) ;

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

    private static void IterateFolder(ItemLanguage itemLanguage)  {
        try {
            File folder = new File(itemLanguage.itemPath);
            File[] folderItems = folder.listFiles();
            String itemName ;
            String itemPathStr ;
            ItemLanguage newIL ;

            if(folderItems != null) {
                for (File folderItem : folderItems) {
                    itemName = folderItem.getName();
                    itemPathStr = itemLanguage.itemPath + "/" + itemName ;

                    if ((reportOptions.showHiddenItems || !folderItem.isHidden()) && !itemName.startsWith("/.")) {
                        if (folderItem.isDirectory()) {
                            processFolder(itemLanguage, itemName);
                        } else if (folderItem.isFile()) {
                            String whichLanguage = determineFileLanguage(itemName);
                            itemLanguage.numFiles++;

                            if (!whichLanguage.equals("unknown")) {
                                itemLanguage.addLanguageFileCount(whichLanguage);
                            }

                            newIL = new ItemLanguage(itemPathStr);
                            newIL.addLanguageFileCount(whichLanguage);

                            if(testLangExt.nameMatches(itemName))
                            {
                                newIL.numTestFiles += 1 ;
                                itemLanguage.numTestFiles += 1 ;
                            }

                            if (reportOptions.reportDetailLevel == ReportDetailLevel.ALL_ITEMS) {
                                items.add(newIL);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
            System.err.println("Error:" + ex.getMessage());
        }
    }

    private static void processFolder(ItemLanguage itemLanguage,String itemName) {
        String artifactName ;
        String subDirPath ;

        if(!excludeFolders.contains(itemName)) {
            subDirPath = itemLanguage.itemPath + "/" + itemName ;
            artifactName = ItemLanguage.determineArtifact(subDirPath) ;

            ItemLanguage dlSub = new ItemLanguage(subDirPath);

            if((reportOptions.reportDetailLevel != ReportDetailLevel.CUSTOM) || (!artifacts.contains(artifactName))) {
                artifacts.add(artifactName);
                items.add(dlSub);
            }

            itemLanguage.numSubfolders++;
            IterateFolder(dlSub);
            itemLanguage.mergeStats(dlSub);
        }
    }

    private static void InitFoldersToExclude()  {
        excludeFolders.add("node_modules") ;
        excludeFolders.add("bazel-bin") ;
        excludeFolders.add("bazel-main") ;
        excludeFolders.add("bazel-out") ;
        excludeFolders.add("bazel-testlogs") ;
        excludeFolders.add("output") ;
    }

    private static void showResults() {
        System.out.println(ItemLanguage.getHeader(reportOptions)) ;

        for (ItemLanguage item : items) {
            System.out.println(item.toString(reportOptions));
        }
    }




}
