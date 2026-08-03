package com.kano.project.provider.document;

/**
 * 文档文本抽取接口。
 * 面向实现层抽象，替换实现（Tika / 自研解析 / 云 OCR 等）不影响上层。
 */
public interface DocumentParser {

    /**
     * 从文件二进制中抽取纯文本
     *
     * @param fileBytes 文件二进制内容
     * @param fileName  文件名（带扩展名）
     * @return 抽取出的纯文本
     */
    String parse(byte[] fileBytes, String fileName);
}
