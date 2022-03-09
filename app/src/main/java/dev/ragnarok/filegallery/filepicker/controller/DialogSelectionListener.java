package dev.ragnarok.filegallery.filepicker.controller;

import androidx.annotation.NonNull;

/**
 * @author akshay sunil masram
 */
public interface DialogSelectionListener {
    void onSelectedFilePaths(@NonNull String[] files);
}
