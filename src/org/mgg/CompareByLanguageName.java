package org.mgg;

import java.util.Comparator;

public class CompareByLanguageName implements Comparator<DirLanguageStats> {
    @Override
    public int compare(DirLanguageStats o1, DirLanguageStats o2) {
        return o1.languageName.compareToIgnoreCase(o2.languageName);
    }
}
