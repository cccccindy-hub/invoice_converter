package com.nnroad.system.constants;

import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;

public enum MailEnum {

    HTML_TEMPLATE_COMPLETE("Complete Your Information","templates/mail/complete-html-template.html", "IT");

    private String title;

    private String template;

    private String support;

    private MailEnum(String title, String template, String support) {
        this.title = title;
        this.template = template;
        this.support = support;
    }

    public String getTitle() throws UnsupportedEncodingException {
        return MimeUtility.encodeWord(title,"UTF-8", "Q");
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }


    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }
}
