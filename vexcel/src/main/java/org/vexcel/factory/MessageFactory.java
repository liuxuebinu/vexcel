package org.vexcel.factory;

import org.vexcel.pojo.Message;

public class MessageFactory {

    public static Message getMessage() {

        return new Message();
    }

    public static Message getDefaultFalseMessage() {
        Message msg = new Message();
        msg.setSuccess(false);
        return msg;
    }

}
