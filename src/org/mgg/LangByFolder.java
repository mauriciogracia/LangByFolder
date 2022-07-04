package org.mgg;

import org.mgg.langByFolder.DirectoryContext;
import org.mgg.langByFolder.FileContext;
import org.mgg.langByFolder.ReportDetailLevel;
import org.mgg.langByFolder.ReportOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;

import static org.mgg.langByFolder.ReportOptions.usageOptions;

public class LangByFolder {
    private final static ArrayList<FileContext> items = new ArrayList<>();
    private final static ArrayList<String> artifacts = new ArrayList<>();

    public static void main(String[] args) {
        ReportOptions reportOptions ;

        reportOptions = new ReportOptions(args);

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
            DirectoryContext dc = new DirectoryContext(reportOptions.rootFolder, reportOptions);
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
                            FileContext childIL = new FileContext(childPathStr, reportOptions);

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
        catch (FileNotFoundException ex) {
            /* unix links will now be catch and ignored here since they are not processed
            System.err.println("File Not Found:" + ex.getMessage());
            ex.printStackTrace(System.err);
            */
        }
        catch (Exception ex) {
            System.err.println("Error:" + ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }

    private static void processFolder(DirectoryContext dirContext, String itemName, ReportOptions reportOptions) {
        String subDirPath ;

        if(!reportOptions.isExcludedFolder(itemName)) {
            subDirPath = dirContext.itemPath + "/" + itemName ;
            DirectoryContext dlSub = new DirectoryContext(subDirPath, reportOptions);

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


}
