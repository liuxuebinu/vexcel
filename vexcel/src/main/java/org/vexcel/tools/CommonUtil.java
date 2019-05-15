package org.vexcel.tools;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class CommonUtil {
    public static <T> boolean isNull(T param) {
        if (param == null)

            return true;
        else if ("".equals(param))
            return true;
        else
            return false;
    }

    public static void setSysClipboardText(String writeMe) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(writeMe);
        clip.setContents(tText, null);
    }

}
