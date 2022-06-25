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

    private static boolean showFullPath = false ;
    private static boolean showFiles = false ;
    private static boolean compactMode = true ;
    private static boolean withNumFiles = false ;
    private static boolean layerMode = false ;

    private static void InitLanguageExtensions() {
        languages = new ArrayList<LanguageExtensions>() ;

        if(layerMode) {
            String[] frontEnd = {".ts", ".js", ".html"};
            languages.add(new LanguageExtensions("frontEnd", frontEnd));

            String[] backEnd = {".scala", ".java"};
            languages.add(new LanguageExtensions("backend", backEnd));

            String[] buildConf = {".bazel", ".conf"};
            languages.add(new LanguageExtensions("build-conf", buildConf));
        }
        else {
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
            } while (i < languages.size() && !match) ;
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
            String itemString ;

            for (int i = 0; i < listOfFiles.length; i++) {
                itemName = listOfFiles[i].getName() ;

                itemString = dirPrefix + "/" + itemName;
                if (listOfFiles[i].isDirectory()) {
                    if(!excludeFolders.contains(itemName)) {
                        DirLanguage dlSub = new DirLanguage(currentFolder + "/" + itemName);
                        dirList.add(dlSub);
                        dlSub.numSubfolders++;
                        IterateFolder(dlSub);

                        if (dlSub.hastStats()) {
                            if (compactMode) {
                                itemString += "|" + determineArtifact(itemString) ;
                                itemString += "|" + dlSub.isApiService ;
                                itemString += "|" + dlSub.isTest ;
                                itemString += "|" + dlSub.getStats(withNumFiles);
                            } else {
                                itemString += "|" + determineArtifact(itemString) ;
                                itemString += "| sf:" + dlSub.numSubfolders;
                                itemString += "| fi:" + dlSub.numFiles;
                                itemString += "| stats:" + dlSub.getStats(withNumFiles);
                            }
                            output.add(itemString);
                        }
                    }
                } else if (listOfFiles[i].isFile()) {
                    String whichLanguage = determineFileLanguage(itemName) ;
                    dl.numFiles++ ;

                    if(!whichLanguage.equals("unknown")) {
                        dl.addLanguageFileCount(whichLanguage);
                    }

                    if(showFiles) {
                        itemString += "|" + whichLanguage;
                        output.add(itemString);
                    }
                } else {
                    itemString += "|" + "NOT_FILE_NOR_DIR" ;
                    output.add(itemString);
                }
            }
        }
        catch (Exception ex) {
            System.err.println("Error:" + ex.getMessage());
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

        DirLanguage dl = new DirLanguage(rootFolder);
        IterateFolder(dl) ;

        Collections.sort(output);

        System.out.println("Folder|Artifact|isApiService|isTest|Languages") ;
        Iterator<String> it = output.iterator();
        String line ;


        while(it.hasNext()) {
            line = it.next();
            System.out.println(line);
        }
    }
}
