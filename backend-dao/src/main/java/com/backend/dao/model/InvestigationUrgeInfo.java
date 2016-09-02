package com.backend.dao.model;

import java.math.BigDecimal;
import java.util.Date;

public class InvestigationUrgeInfo {
    private Integer id;

    private String investigationUserId;

    private BigDecimal score;

    private String remark;

    private Byte delFlag;

    private Integer createManager;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvestigationUserId() {
        return investigationUserId;
    }

    public void setInvestigationUserId(String investigationUserId) {
        this.investigationUserId = investigationUserId;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Byte getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Byte delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getCreateManager() {
        return createManager;
    }

    public void setCreateManager(Integer createManager) {
        this.createManager = createManager;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}