package com.nnroad.system.service;
import com.nnroad.system.domain.SysEmailSend;
import org.springframework.web.multipart.MultipartFile;


/**
 * 参数配置 服务层
 *
 * @author Hrone
 */
public interface ISysMailService
{
    /**
     * 查询参数配置信息
     *
     * @param emailSend SysEmailSend
     *
     */
    public void mailSend(SysEmailSend emailSend) throws Exception;

    /**
     * 接收邮件
     */
    public void reciveMail() throws Exception ;

}
