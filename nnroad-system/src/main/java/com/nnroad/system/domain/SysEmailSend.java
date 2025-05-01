package com.nnroad.system.domain;

import com.nnroad.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Map;

/**
 * 通知公告表 sys_notice
 *
 * @author Hrone
 */
public class SysEmailSend extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 发件人名 */
    private String sendFromName;
    /** 发件人邮箱地址 */
    private String sendAddress;
    /** 主题 */
    private String subject;
    /** 内容 */
    private String content;
    /** 收件人邮箱地址 */
    private String toAddress;
    /** 收件人组邮箱地址 */
    private String[] toAddressList;
    /** 附件名称 */
    private String fileName;
    /** 附件 */
    private MultipartFile file;

    /**
     * attachments {filename: file}
     */
    private Map<String, MultipartFile> files;


    public SysEmailSend() {
    }

    public SysEmailSend(String sendFromName, String subject, String content, String[] toAddressList) {
        this.sendFromName = sendFromName;
        this.subject = subject;
        this.content = content;
        this.toAddressList = toAddressList;
    }

    public String getSendFromName() {
        return sendFromName;
    }

    public void setSendFromName(String sendFromName) {
        this.sendFromName = sendFromName;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String[] getToAddressList() {
        return toAddressList;
    }

    public void setToAddressList(String[] toAddressList) {
        this.toAddressList = toAddressList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Map<String, MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(Map<String, MultipartFile> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "SysEmailSend{" +
                "sendFromName='" + sendFromName + '\'' +
                ", sendAddress='" + sendAddress + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", toAddress='" + toAddress + '\'' +
                ", toAddressList=" + Arrays.toString(toAddressList) +
                ", fileName='" + fileName + '\'' +
                ", file=" + file +
                '}';
    }
}
