package org.vexcel.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.vexcel.exception.ValidateRuntimeException;

public class MD5Utils {

    public static String MD5(File file) {
        String Md5Str = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] b = new byte[(int) file.length()];
            in.read(b);
            Md5Str = org.apache.commons.codec.digest.DigestUtils.md5Hex(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new ValidateRuntimeException("加签文件未找到");

        } catch (IOException e) {
            e.printStackTrace();
            throw new ValidateRuntimeException("文件加签失败");
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        return Md5Str;
    }

}
