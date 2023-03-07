package com.shawn.leadnews.audit.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shawn
 * @date 2023年 01月 11日 10:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditResult {
    private Integer conclusionType;
    private String conclusion;
    private String msg;
}
