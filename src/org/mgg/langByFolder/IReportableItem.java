package org.mgg.langByFolder;

public interface IReportableItem {
    void prepareParts(ReportOptions reportOptions) ;
    String toString(ReportOptions reportOptions);
    String getItemType() ;
    /*@todo include these methods and implement them both for file and directory
    getNumServices
    getNumTestFiles
    getNumSubfolders
    getNumFiles
    getLangStats(ReportOptions reportOptions)

    In this way the prepareParts could be unified single method
    */
}
