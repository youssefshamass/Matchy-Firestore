package com.se.matchy.utils;

import androidx.annotation.NonNull;

import java.util.List;

public class GeneralUtils {
    public static boolean isStringInList(final List<String> myList, @NonNull final String needle) {
        if (myList == null || myList.size() == 0)
            return false;

        for (String item : myList) {
            if (item.equalsIgnoreCase(needle))
                return true;
        }

        return false;
    }
}
