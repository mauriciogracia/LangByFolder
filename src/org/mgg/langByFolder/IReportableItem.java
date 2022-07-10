package org.mgg.langByFolder;

public interface IReportableItem {
    void prepareParts() ;
    String toString();
    String getItemType() ;
    int getNumServices() ;
    int getNumTestFiles() ;
    int getNumSubFolders() ;
    int getNumFiles() ;
}
