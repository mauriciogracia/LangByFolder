package org.mauriciogracia;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Main {
    private static String rootFolder = "/home/c-mgracia/press/main";
    private static ArrayList<LanguageExtensions> languages  ;
    private static ArrayList<DirLanguage> dirList = new ArrayList<DirLanguage>();
    private static ArrayList<String> excludeFolders = new ArrayList<String>();
    private static ArrayList<String> output = new ArrayList<String>();
    private static ArrayList<String> artifacts = new ArrayList<String>();

    private static boolean showFullPath = false ;
    private static boolean showFiles = false ;
    private static boolean compactMode = true ;
    private static boolean withNumFiles = false ;
    private static boolean uniqueArtifacts = false ;

    private static void InitLanguageExtensions() {
        languages = new ArrayList<LanguageExtensions>() ;

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
            String ext = fileName.substring(pos, fileName.length()) ;
            int i = 0 ;
            boolean match = false ;

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

    private static String prefix(String currentFolder) {
        return (showFullPath) ? currentFolder : currentFolder.substring(rootFolder.length()) ;
    }

    private static void IterateFolder(DirLanguage dl)  {
        try {
            String currentFolder = dl.name;
            String dirPrefix = prefix(currentFolder) ;
            File folder = new File(currentFolder);
            File[] listOfFiles = folder.listFiles();
            String itemName ;
            String itemPath ;


            for (int i = 0; i < listOfFiles.length; i++) {
                itemName = listOfFiles[i].getName() ;

                itemPath = dirPrefix + "/" + itemName;
                if (listOfFiles[i].isDirectory()) {
                    processFolder(itemName, itemPath, currentFolder) ;
                } else if (listOfFiles[i].isFile()) {
                    String whichLanguage = determineFileLanguage(itemName) ;
                    dl.numFiles++ ;

                    if(!whichLanguage.equals("unknown")) {
                        dl.addLanguageFileCount(whichLanguage);
                    }

                    if(showFiles) {
                        itemPath += "|" + whichLanguage;
                        output.add(itemPath);
                    }
                } else {
                    itemPath += "|" + "NOT_FILE_NOR_DIR" ;
                    output.add(itemPath);
                }
            }
        }
        catch (Exception ex) {
            System.err.println("Error:" + ex.getMessage());
        }
    }

    private static void processFolder(String itemName, String itemPath, String currentFolder) {
        String artifactName ;

        if(!excludeFolders.contains(itemName)) {
            artifactName = determineArtifact(itemPath) ;

            if(!uniqueArtifacts || (uniqueArtifacts && !artifacts.contains(artifactName))) {
                artifacts.add(artifactName);

                DirLanguage dlSub = new DirLanguage(currentFolder + "/" + itemName);
                dirList.add(dlSub);
                dlSub.numSubfolders++;
                IterateFolder(dlSub);

                if (dlSub.hastStats()) {
                    if (compactMode) {
                        itemPath += "|" + artifactName;
                        itemPath += "|" + dlSub.isApiService;
                        itemPath += "|" + dlSub.isTest;
                        itemPath += "|" + dlSub.getStats(withNumFiles);
                    } else {
                        itemPath += "|" + artifactName;
                        itemPath += "| sf:" + dlSub.numSubfolders;
                        itemPath += "| fi:" + dlSub.numFiles;
                        itemPath += "| stats:" + dlSub.getStats(withNumFiles);
                    }
                }
                output.add(itemPath);
            }
        }
    }

    private static String determineArtifact(String path) {
        int pos ;

        pos = path.indexOf('/', 1);

        //The name of the first level folder is used as the base/component/artifact name
        if (pos < 0) {
            pos = path.length() ;
        }
        return path.substring(1, pos);
    }

    public static void main(String[] args) {
        InitLanguageExtensions() ;
        excludeFolders.add("node_modules") ;
        excludeFolders.add("bazel-bin") ;
        excludeFolders.add("bazel-main") ;
        excludeFolders.add("bazel-out") ;
        excludeFolders.add("bazel-testlogs") ;
        excludeFolders.add("output") ;
        excludeFolders.add(".git") ;
        excludeFolders.add(".idea") ;
        excludeFolders.add(".ijwb") ;
        excludeFolders.add(".metals") ;
        excludeFolders.add(".vscode") ;

        DirLanguage dl = new DirLanguage(rootFolder);
        IterateFolder(dl) ;

        Collections.sort(output);

        System.out.println("Folder|Artifact|isApiService|isTest|Languages") ;
        Iterator<String> it = output.iterator();
        String line ;

        //@todo Propagate/merge the languages from child nodes to parent
        while(it.hasNext()) {
            line = it.next();
            System.out.println(line);
        }
    }
}
