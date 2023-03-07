package com.heima.common.constants;

/**
 * @author shawn
 * @date 2023年 01月 09日 16:34
 */

public class MediaConstants {
    public final static Integer MATERIAL_TYPE_IMAGE = 0 ;
    public final static Integer MATERIAL_TYPE_VIDEO = 1 ;
    public final static Integer MATERIAL_NOT_COLLECTION = 0 ;
    public final static Integer MATERIAL_COLLECTION = 1 ;
    public final static Integer MATERIAL_GENERAL_TIME_FORMAT = 1 ;

    /**文章状态
     *             0 草稿
     *             1 提交（待审核）
     *             2 审核失败
     *             3 人工审核
     *             4 人工审核通过
     *             8 审核通过（待发布）
     *             9 已发布
     */
    public final static Short PUBLISH_DRAFT_STATUS = 0 ;
    public final static Short PUBLISH_SUBMIT_STATUS = 1 ;
    public final static Short PUBLISH_AUDIT_FAIL_STATUS = 2 ;
    public final static Short PUBLISH_MANUAL_AUDIT_STATUS = 3 ;
    public final static Short PUBLISH_MANUAL_AUDIT_PASS_STATUS = 4 ;
    public final static Short PUBLISH_AUDIT_PASS_STATUS = 8 ;
    public final static Short PUBLISH_ON_STATUS = 9 ;

    /**
     * 自动审核结果
     * 1 自动审核通过
     * 2 自动审核拒绝
     *
     * 3 自动审核无法确认 --> 转人工
     * 4 自动审核调用失败 --> 请检查
     */
    public final static Integer AUTO_AUDIT_PASS = 1 ;
    public final static Integer AUTO_AUDIT_REJECT= 2 ;
    public final static Integer AUTO_AUDIT_SUSPECTED= 3 ;
    public final static Integer AUTO_AUDIT_FAIL= 4 ;


    /**
     * Kafka消息topic分组
     *
     */
    public final static String KAFKA_TOPIC_UP_OR_DOWN = "KAFKA_TOPIC_UP_OR_DOWN" ;
}
