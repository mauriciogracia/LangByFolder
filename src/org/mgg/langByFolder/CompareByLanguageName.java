package org.mgg.langByFolder;

import java.util.Comparator;

public class CompareByLanguageName implements Comparator<LanguageStats> {
    @Override
    public int compare(LanguageStats o1, LanguageStats o2) {
        return o1.languageName.compareToIgnoreCase(o2.languageName);
    }
}
