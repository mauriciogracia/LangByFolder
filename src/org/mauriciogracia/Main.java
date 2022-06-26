package org.mauriciogracia;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class Main {
    private static String rootFolder = "/home/c-mgracia/press/main";
    private static ArrayList<LanguageExtensions> languages  ;
    private static ArrayList<ItemLanguage> items = new ArrayList<>();
    private static ArrayList<String> excludeFolders = new ArrayList<>();
    private static ArrayList<String> artifacts = new ArrayList<>();

    private static boolean showFiles = true ;
    private static boolean compactMode = false ;
    private static boolean uniqueArtifacts = false ;
    private static boolean showHiddenItems = false ;

    private static void InitLanguageExtensions() {
        languages = new ArrayList<>() ;

        String []tsExt = {".ts"} ;
        languages.add(new LanguageExtensions("Typescript",tsExt)) ;

        String []scalaExt = {".scala"} ;
        languages.add(new LanguageExtensions("Scala",scalaExt)) ;

        String []javaExt = {".java"} ;
        languages.add(new LanguageExtensions("Java",javaExt)) ;

        String []jsExt = {".js"} ;
        languages.add(new LanguageExtensions("Javascript",jsExt)) ;

        String []pythonExt = {".py"} ;
        languages.add(new LanguageExtensions("Python",pythonExt)) ;

        String []bazelExt = {".bazel"} ;
        languages.add(new LanguageExtensions("Bazel",bazelExt)) ;
    }

    private  static String determineFileLanguage(String fileName) {
        String langName = "unknown" ;

        int pos = fileName.lastIndexOf('.') ;

        if(pos >= 0) {
            String ext = fileName.substring(pos) ;
            int i = 0 ;
            boolean match ;

            do {
                LanguageExtensions lang =languages.get(i) ;
                match = lang.fileMatches(ext) ;

                if(match)
                {
                    langName = lang.languageName ;
                }
                i++  ;
            } while (!match && (i < languages.size())) ;
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

                    if ((showHiddenItems || !folderItem.isHidden()) && !itemName.startsWith("/.")) {
                        if (folderItem.isDirectory()) {
                            processFolder(itemLanguage, itemName);
                        } else if (folderItem.isFile()) {
                            String whichLanguage = determineFileLanguage(itemName);
                            itemLanguage.numFiles++;

                            if (!whichLanguage.equals("unknown")) {
                                itemLanguage.addLanguageFileCount(whichLanguage);
                            }

                            if (showFiles) {
                                newIL = new ItemLanguage(itemPathStr);
                                newIL.addLanguageFileCount(whichLanguage);
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

            if(!uniqueArtifacts || (!artifacts.contains(artifactName))) {
                artifacts.add(artifactName);

                ItemLanguage dlSub = new ItemLanguage(subDirPath);
                items.add(dlSub);
                itemLanguage.numSubfolders++;
                IterateFolder(dlSub);
                itemLanguage.mergeStats(dlSub);
            }
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
        String header = "Folder|Artifact|isApiService|isTest|Languages" ;

        if(!compactMode) {
            header += "|# Subfolders|# Total Files" ;
        }

        System.out.println(header) ;

        for (ItemLanguage item : items) {
            System.out.println(item.toString(compactMode, rootFolder));
        }
    }

    public static void main(String[] args) {
        InitLanguageExtensions();
        InitFoldersToExclude();

        //Iterate the specified folder
        ItemLanguage.prefixLength = rootFolder.length();
        ItemLanguage dl = new ItemLanguage(rootFolder);
        IterateFolder(dl);

        Collections.sort(items);

        showResults() ;
    }
}
