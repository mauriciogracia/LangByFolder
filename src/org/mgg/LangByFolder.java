package org.mgg;

import org.mgg.langByFolder.DirectoryContext;
import org.mgg.langByFolder.FileContext;
import org.mgg.langByFolder.ReportDetailLevel;
import org.mgg.langByFolder.ReportOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;

import static org.mgg.langByFolder.ReportOptions.usageOptions;

public class LangByFolder {
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
            Collections.sort(reportOptions.items);
            showResults(reportOptions);
        }
    }
    public static void processRootFolder(ReportOptions reportOptions) {
        DirectoryContext dc = new DirectoryContext(reportOptions.getRootFolder(), reportOptions);
        iterateFolder(dc, reportOptions);
    }

    private static void showResults(ReportOptions reportOptions) {
        reportOptions.output.println(reportOptions.getHeader());

        for (FileContext item : reportOptions.items) {
            reportOptions.output.println(item.toString(reportOptions));
        }
    }


    static int depthLevel = 0 ;

    private static void iterateFolder(DirectoryContext dirContext, ReportOptions reportOptions)  {
        try {
            File folder = new File(dirContext.getItemPath());
            File[] folderItems = folder.listFiles();
            String childName ;
            String childPathStr ;
            BasicFileAttributes itemAttributes ;

            depthLevel++;
            if(folderItems != null) {
                for (File folderItem : folderItems) {
                    childName = folderItem.getName();
                    childPathStr = dirContext.getItemPath() + "/" + childName ;

                    if (reportOptions.showHiddenItems || (!folderItem.isHidden() && !childName.startsWith("."))) {
                        itemAttributes = Files.readAttributes(folderItem.toPath(), BasicFileAttributes.class);

                        if (itemAttributes.isDirectory()) {
                            processFolder(dirContext, childName, reportOptions);
                        } else if (itemAttributes.isRegularFile()) {
                            FileContext childIL = new FileContext(childPathStr, reportOptions);

                            if (reportOptions.reportDetailLevel == ReportDetailLevel.ALL_ITEMS) {
                                reportOptions.items.add(childIL);
                            }
                            dirContext.mergeFile(childIL);
                        }
                    }
                }
            }
            depthLevel--;
            //add the root folder stats
            if(depthLevel == 0) {
               reportOptions.items.add(dirContext) ;
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
            subDirPath = dirContext.getItemPath() + "/" + itemName ;
            DirectoryContext dlSub = new DirectoryContext(subDirPath, reportOptions);

            if((reportOptions.reportDetailLevel == ReportDetailLevel.CUSTOM) && !artifacts.contains(dlSub.artifactName)) {
                artifacts.add(dlSub.artifactName);
                reportOptions.items.add(dlSub);
            }
            else if((reportOptions.reportDetailLevel != ReportDetailLevel.CUSTOM)) {
                reportOptions.items.add(dlSub);
            }
            iterateFolder(dlSub, reportOptions);
            dirContext.mergeDirectory(dlSub);
        }
    }


}
