package org.mgg.langByFolder.stats;

import org.mgg.langByFolder.stats.LanguageStats;

import java.util.Comparator;

public class CompareByLanguageOccurrence extends LanguageStatsComparator {

    public CompareByLanguageOccurrence() {
        comparatorType = LanguageStatsComparatorType.BY_LANGUAGE_OCURRENCE;
    }
    @Override
    public int compare(LanguageStats o1, LanguageStats o2) {
        return o2.getNumFiles() - o1.getNumFiles();
    }
}
