package org.vexcel.exception;

public class ValidateXmlException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 12437321214827134L;
    String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ValidateXmlException(String msg) {
        this.msg = msg;
    }

}
