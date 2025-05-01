package com.nnroad.common.exception;

import org.springframework.http.HttpStatus;

import java.util.Collection;

/**
 * @author wangbi
 */
public class Asserts {

    public static void notNull(Object obj, Integer errorCode, String errorMsg) throws ServiceException {
        if (obj == null) {
            throw new ServiceException(errorMsg, errorCode);
        }
    }

    public static void notNull(Object obj, String errorMsg) throws ServiceException {
        notNull(obj, HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg);
    }

    public static void notBlank(String obj, Integer errorCode, String errorMsg) throws ServiceException {
        if (obj == null || "".equals(obj.trim())) {
            throw new ServiceException(errorMsg, errorCode);
        }
    }

    public static void notBlank(String obj, String errorMsg) throws ServiceException {
        notBlank(obj, HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg);
    }

    public static void notEmpty(Collection<?> obj, Integer errorCode, String errorMsg) throws ServiceException {
        if (obj == null || obj.size() == 0) {
            throw new ServiceException(errorMsg, errorCode);
        }
    }

    public static void notEmpty(Collection<?> obj, String errorMsg) throws ServiceException {
        notEmpty(obj, HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg);
    }

    public static void isTrue(boolean expression, String errorMsg) throws ServiceException {
        isTrue(expression, HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMsg);
    }

    public static void isTrue(boolean expression, Integer errorCode, String errorMsg) throws ServiceException {
        if (!expression) {
            throw new ServiceException(errorMsg, errorCode);
        }
    }
}