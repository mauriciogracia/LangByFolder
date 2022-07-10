package org.mgg.langByFolder.stats;

import org.mgg.langByFolder.stats.LanguageStats;

import java.util.Comparator;

public class CompareByLanguageName extends LanguageStatsComparator {
    public CompareByLanguageName() {
        comparatorType = LanguageStatsComparatorType.BY_LANGUAGE_NAME;
    }
    @Override
    public int compare(LanguageStats o1, LanguageStats o2) {
        return o1.languageName.compareToIgnoreCase(o2.languageName);
    }
}
