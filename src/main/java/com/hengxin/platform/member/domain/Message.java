package com.hengxin.platform.member.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hengxin.platform.common.entity.BasePo;
import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;
import com.hengxin.platform.member.domain.converter.MessageEnumConverter;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.product.domain.converter.BooleanStringConverter;

@Entity
@Table(name = "UM_USER_MSG")
@EntityListeners(IdInjectionEntityListener.class)
public class Message extends BasePo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "MSG_ID")
    private String messageId;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "MSG_TYPE")
    @Convert(converter = MessageEnumConverter.class)
    private EMessageType type;

    @Column(name = "READ_FLG")
    @Convert(converter = BooleanStringConverter.class)
    private Boolean read;

    @Column(name = "MSG_TITLE")
    private String title;

    @Column(name = "MSG_TX")
    private String message;

    /**
     * @return the messageId
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId
     *            the messageId to set
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the type
     */
    public EMessageType getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(EMessageType type) {
        this.type = type;
    }

    /**
     * @return the read
     */
    public Boolean getRead() {
        return read;
    }

    /**
     * @param read
     *            the read to set
     */
    public void setRead(Boolean read) {
        this.read = read;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }


}
