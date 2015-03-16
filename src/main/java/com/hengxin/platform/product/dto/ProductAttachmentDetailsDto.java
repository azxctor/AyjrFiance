/**
 * 
 */
package com.hengxin.platform.product.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Class Name: ProductAttachmentDetailsDto Description: 资料库
 * 
 * @author Ryan
 * 
 */
public class ProductAttachmentDetailsDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String file;// 附件
    
    private String createOpid;

    private String lastMntOpid;

    private Date createTs;

    private Date lastMntTs;
    
    private String orgFileName;
    
    private String path;


    /**
     * @return return the value of the var createOpid
     */
    
    public String getCreateOpid() {
        return createOpid;
    }

    /**
     * @param createOpid Set createOpid value
     */
    
    public void setCreateOpid(String createOpid) {
        this.createOpid = createOpid;
    }

    /**
     * @return return the value of the var lastMntOpid
     */
    
    public String getLastMntOpid() {
        return lastMntOpid;
    }

    /**
     * @param lastMntOpid Set lastMntOpid value
     */
    
    public void setLastMntOpid(String lastMntOpid) {
        this.lastMntOpid = lastMntOpid;
    }

    /**
     * @return return the value of the var createTs
     */
    
    public Date getCreateTs() {
        return createTs;
    }

    /**
     * @param createTs Set createTs value
     */
    
    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    /**
     * @return return the value of the var lastMntTs
     */
    
    public Date getLastMntTs() {
        return lastMntTs;
    }

    /**
     * @param lastMntTs Set lastMntTs value
     */
    
    public void setLastMntTs(Date lastMntTs) {
        this.lastMntTs = lastMntTs;
    }

    /**
     * @return return the value of the var file
     */

    public String getFile() {
        return file;
    }

    /**
     * @param file
     *            Set file value
     */

    public void setFile(String file) {
        this.file = file;
    }

    /**
     * @return return the value of the var orgFileName
     */
    
    public String getOrgFileName() {
        return orgFileName;
    }

    /**
     * @param orgFileName Set orgFileName value
     */
    
    public void setOrgFileName(String orgFileName) {
        this.orgFileName = orgFileName;
    }

    /**
     * @return return the value of the var path
     */
    
    public String getPath() {
        return path;
    }

    /**
     * @param path Set path value
     */
    
    public void setPath(String path) {
        this.path = path;
    }

}
