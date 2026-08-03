package com.kano.project.provider.document;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * 支持解析的文件类型白名单（底层由 Apache Tika 统一解析）
 */
public final class SupportedFileTypes {

    private static final Set<String> SUPPORTED =
            new HashSet<>(Arrays.asList("txt", "pdf", "doc", "docx", "md", "html", "xml", "rtf", "csv"));

    private SupportedFileTypes() {
    }

    public static boolean isSupported(String fileName) {
        return fileName != null && SUPPORTED.contains(extensionOf(fileName));
    }

    public static String extensionOf(String fileName) {
        int idx = fileName.lastIndexOf('.');
        return idx < 0 ? "" : fileName.substring(idx + 1).toLowerCase(Locale.ROOT);
    }
}
