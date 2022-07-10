package org.mgg.langByFolder;

public interface IReportableItem {
    void prepareParts(ReportOptions reportOptions) ;
    String toString(ReportOptions reportOptions);
    String getItemType() ;
    int getNumServices() ;
    int getNumTestFiles() ;
    int getNumSubFolders() ;
    int getNumFiles() ;
}
