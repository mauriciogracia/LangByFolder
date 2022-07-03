package org.mgg;

import java.util.Comparator;

public class CompareByLanguageOccurrence implements Comparator<LanguageStats> {
    @Override
    public int compare(LanguageStats o1, LanguageStats o2) {
        return o2.getNumFiles() - o1.getNumFiles();
    }
}
