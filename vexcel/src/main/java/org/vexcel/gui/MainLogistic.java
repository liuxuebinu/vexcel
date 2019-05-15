package org.vexcel.gui;

import java.io.File;
import java.util.HashMap;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.PatternLayout;
import org.vexcel.pojo.ExcelConfig;
import org.vexcel.tools.ExcelUtils;
import org.vexcel.tools.XmlUtils;

public class MainLogistic {
    // private static Logger log = Logger.getLogger(MainLogistic.class).

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // new MainLogistic().setLog();
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // new MainLogistic().setLog();
        new MainLogistic().setLog2();

        HashMap<String, ExcelConfig> paths = new XmlUtils().getAllValidators();
        new Thread(new SelectFrame(paths)).start();

    }

    public void setLog2() {

        try {
            File file = new File("vexcel_error.log");
            if (file.exists())
                file.delete();
            ExcelUtils.log.addAppender(new DailyRollingFileAppender(new PatternLayout("%-d{yyyy-MM-dd HH:mm:ss} %m%n"),
                    "vexcel_error.log", "yyyy-MM-dd"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
