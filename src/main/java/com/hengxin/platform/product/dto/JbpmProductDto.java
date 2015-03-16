package com.hengxin.platform.product.dto;

import java.io.Serializable;

import org.jbpm.workflow.instance.WorkflowProcessInstance;

public class JbpmProductDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String productId;

    private boolean approve;

    private String user;

    private long taskId;

    private String type;

    private boolean isViolate;

    private boolean qualityPass;

    private String investor;

    private String financier;

    public JbpmProductDto() {

    }

    public JbpmProductDto(String user, long taskId, WorkflowProcessInstance process) {
        this.user = user;
        this.taskId = taskId;
        type = process.getVariable("type").toString();
        isViolate = Boolean.valueOf(process.getVariable("isViolate").toString());
        qualityPass = Boolean.valueOf(process.getVariable("qualityPass").toString());
        investor = process.getVariable("investor").toString();
        financier = process.getVariable("financier").toString();
    }

    /**
     * @return return the value of the var approve
     */

    public boolean isApprove() {
        return approve;
    }

    /**
     * @param approve
     *            Set approve value
     */

    public void setApprove(boolean approve) {
        this.approve = approve;
    }

    /**
     * @return return the value of the var productId
     */

    public String getProductId() {
        return productId;
    }

    /**
     * @param productId
     *            Set productId value
     */

    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the taskId
     */
    public long getTaskId() {
        return taskId;
    }

    /**
     * @param taskId
     *            the taskId to set
     */
    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the isViolate
     */
    public boolean isViolate() {
        return isViolate;
    }

    /**
     * @param isViolate
     *            the isViolate to set
     */
    public void setViolate(boolean isViolate) {
        this.isViolate = isViolate;
    }

    /**
     * @return the qualityPass
     */
    public boolean isQualityPass() {
        return qualityPass;
    }

    /**
     * @param qualityPass
     *            the qualityPass to set
     */
    public void setQualityPass(boolean qualityPass) {
        this.qualityPass = qualityPass;
    }

    /**
     * @return the investor
     */
    public String getInvestor() {
        return investor;
    }

    /**
     * @param investor
     *            the investor to set
     */
    public void setInvestor(String investor) {
        this.investor = investor;
    }

    /**
     * @return the financier
     */
    public String getFinancier() {
        return financier;
    }

    /**
     * @param financier
     *            the financier to set
     */
    public void setFinancier(String financier) {
        this.financier = financier;
    }
}
