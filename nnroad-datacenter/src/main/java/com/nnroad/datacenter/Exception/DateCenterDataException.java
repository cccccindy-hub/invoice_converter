package com.nnroad.datacenter.Exception;

import com.nnroad.datacenter.domain.DCTableImportLog;

public class DateCenterDataException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    protected final String message;
    protected  DCTableImportLog log;
    public DateCenterDataException(String message, DCTableImportLog log)
    {
        this.message = message;
        this.log =log;
    }
    public DateCenterDataException(String message, DCTableImportLog log, Throwable e)
    {
        super(message, e);
        this.log =log;
        this.message = message;
    }
    @Override
    public String getMessage()
    {
        return message;
    }

    public DCTableImportLog getOpId(){
        return log;
}
}
