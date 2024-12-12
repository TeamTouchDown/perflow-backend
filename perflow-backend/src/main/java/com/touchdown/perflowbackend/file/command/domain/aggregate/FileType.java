package com.touchdown.perflowbackend.file.command.domain.aggregate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {

    PNG("image/png"),
    CSV("text/csv"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    HWP("application/x-hwp"),
    PDF("application/pdf"),
    JPEG("image/jpeg");

    private final String mimeType;

    public static boolean isValidExtension(String fileName) {
        String extension = getFileExtension(fileName).toUpperCase();
        for (FileType ext : values()) {
            if (ext.name().equals(extension)) {
                return true;
            }
        }
        return false;
    }

    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            throw new IllegalArgumentException("Invalid file name: " + fileName);
        }
        return fileName.substring(lastDotIndex + 1);
    }
}

