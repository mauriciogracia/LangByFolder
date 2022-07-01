package org.mgg;

import java.util.Comparator;

public class CompareByLanguageOccurrence implements Comparator<DirLanguageStats> {
    @Override
    public int compare(DirLanguageStats o1, DirLanguageStats o2) {
        return o2.numFiles - o1.numFiles;
    }
}
