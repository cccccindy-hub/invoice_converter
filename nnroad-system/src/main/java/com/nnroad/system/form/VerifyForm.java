package com.nnroad.system.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author raven
 * @since 2023/12/8 14:59
 */
@Data
public class VerifyForm {

    @NotBlank(message = "app_id不能为空")
    private String appId;

    @NotBlank(message = "app_key不能为空")
    private String appKey;

    @NotBlank(message = "app_token不能为空")
    private String appToken;
}