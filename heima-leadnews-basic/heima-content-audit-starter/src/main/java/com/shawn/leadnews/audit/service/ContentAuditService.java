package com.shawn.leadnews.audit.service;

import com.shawn.leadnews.audit.result.AuditResult;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author shawn
 * @date 2023年 01月 10日 19:59
 */
public interface ContentAuditService {
    public AuditResult auditText(String content);

    public AuditResult auditImage(String imageUrl);
    public AuditResult auditImage(byte[] imageByte);

    public AuditResult auditImage(List<byte[]> images);

    public String OCRScan(BufferedImage bufferedImage);
}
