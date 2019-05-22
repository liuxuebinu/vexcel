package org.vexcel.exception;

public class ValidateRuntimeException extends RuntimeException {

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

    public ValidateRuntimeException(String msg) {
        this.msg = msg;
    }

}
