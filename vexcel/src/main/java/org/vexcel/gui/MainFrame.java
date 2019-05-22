package org.vexcel.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;
import org.vexcel.factory.MessageFactory;
import org.vexcel.pojo.ExcelConfig;
import org.vexcel.pojo.Message;
import org.vexcel.pojo.VSheet;
import org.vexcel.tools.CommonUtil;
import org.vexcel.tools.ExcelUtils;
import org.vexcel.tools.MD5Utils;

public class MainFrame implements Runnable {

    private static Logger log = Logger.getLogger(MainFrame.class);
    public JPanel dragPanel;// 要接受拖拽的面板
    public JPanel panel2;// 要接受拖拽的面板
    public JTextArea fileLabel;
    public JLabel tipLabel;
    public JButton jb;
    int mainWidth = 300;
    int height = 100;
    public JFrame mainFrame = new JFrame();
    public boolean isChecking = false;
    Point loc = null;
    Point tmp = null;
    boolean isDragged = false;
    List<VSheet> rules;
    ExcelConfig config;
    String excelLocalPath = null;
    private volatile Message message = MessageFactory.getDefaultFalseMessage();

    public void drawIsChecking() throws Exception {

        fileLabel.setBackground(Color.black);
        panel2.setBackground(Color.black);
        dragPanel.setBackground(Color.black);
        jb.setBackground(Color.black);
        tipLabel.setText("");
        // this.
        try {
            tipLabel.setIcon(new ImageIcon(this.getClass().getResource("/resources/timg.gif")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread.sleep(2000);
        decodeExcel();
    }

    private void decodeExcel() {
        new Thread() {
            public void run() {
                ExcelUtils.readExcel(excelLocalPath, rules, fileLabel, message, config.getExcelType());
            }
        }.start();// 开启线程

    }

    private void setDragable() {

        mainFrame.addMouseListener(

                new java.awt.event.MouseAdapter() {

                    public void mouseReleased(java.awt.event.MouseEvent e) {
                        isDragged = false;
                        mainFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                    }

                    public void mousePressed(java.awt.event.MouseEvent e) {
                        tmp = new Point(e.getX(), e.getY());
                        isDragged = true;
                        mainFrame.setCursor(new Cursor(Cursor.MOVE_CURSOR));

                    }

                });

        mainFrame.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            public void mouseDragged(java.awt.event.MouseEvent e) {

                if (isDragged) {

                    loc = new Point(mainFrame.getLocation().x + e.getX() - tmp.x,

                            mainFrame.getLocation().y + e.getY() - tmp.y);

                    mainFrame.setLocation(loc);

                }

            }

        });
    }

    public MainFrame(List<VSheet> rules, ExcelConfig config) {
        this.rules = rules;
        this.config = config;
        dragPanel = new JPanel();
        panel2 = new JPanel();
        tipLabel = new JLabel("<html><br/><br/><br/>拖拽文件到此区域</html>", SwingConstants.LEFT);
        fileLabel = new JTextArea();
        fileLabel.setForeground(Color.white);
        fileLabel.setSize(mainWidth, 1 * height);
        fileLabel.setBackground(Color.RED);
        fileLabel.setEditable(false);
        fileLabel.setBorder(null);

        panel2.setLayout(new BorderLayout());
        panel2.add(fileLabel, BorderLayout.WEST);
        jb = new JButton("X");
        jb.setForeground(Color.white);
        jb.setBackground(Color.RED);
        jb.setName("quit");
        jb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                String name = ((JButton) e.getSource()).getName();
                if ("quit".equals(name)) {
                    System.exit(0);
                }

            }

        });
        panel2.add(jb, BorderLayout.EAST);
        dragPanel.setBackground(Color.DARK_GRAY);
        dragPanel.setToolTipText("drag file on this area");
        panel2.setBackground(Color.RED);
        fileLabel.setSize(mainWidth, height);
        panel2.setSize(mainWidth, 1 * height);
        dragPanel.setSize(mainWidth, 160);

        Font f = new Font("宋体", Font.PLAIN, 24);
        tipLabel.setFont(f);
        tipLabel.setForeground(Color.WHITE);
        dragPanel.add(tipLabel);

        mainFrame.getContentPane().add(dragPanel, BorderLayout.CENTER);
        mainFrame.getContentPane().add(panel2, BorderLayout.NORTH);
        mainFrame.setSize(mainWidth, 260);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setUndecorated(true); // 去掉窗口的装饰

        mainFrame.setLocation(400, 200);
        mainFrame.setTitle("excel校验器");
        drag();// 启用拖拽
        setDragable();
        mainFrame.setVisible(true);
    }

    public void drag()// 定义的拖拽方法
    {
        // panel表示要接受拖拽的控件
        new DropTarget(dragPanel, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
            public void drop(DropTargetDropEvent dtde)// 重写适配器的drop方法
            {
                try {
                    if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))// 如果拖入的文件格式受支持
                    {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);// 接收拖拽来的数据
                        List<File> list =
                                (List<File>) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                        String temp = "";

                        temp = list.get(0).getAbsolutePath();
                        String fileName = list.get(0).getName();
                        if (!checkType(fileName, config.getExcelType())) {

                            JOptionPane.showMessageDialog(null, "只支持" + config.getExcelType() + "格式");

                        } else if (isChecking) {
                            JOptionPane.showMessageDialog(null, "文件正在检测，请稍后");
                        } else {
                            fileLabel.setText("读取excel文件：" + fileName + "中...");
                            excelLocalPath = temp;
                            isChecking = true;
                            dtde.dropComplete(true);// 指示拖拽操作已完成

                            drawIsChecking();

                        }

                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean checkType(String filePath, String fileType) {
        if (fileType.equals("xls")) {
            return new String(filePath.substring(filePath.length() - 3, filePath.length())).equals(fileType);
        } else if (fileType.equals("xlsx")) {
            return new String(filePath.substring(filePath.length() - 4, filePath.length())).equals(fileType);
        } else {
            return false;
        }

    };

    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            if (message.isSuccess()) {
                String md5 = MD5Utils.MD5(new File(excelLocalPath));
                int i = JOptionPane.showConfirmDialog(null, md5, "文件检测成功,点击是，自动复制剪贴板", JOptionPane.YES_OPTION);
                if (JOptionPane.YES_OPTION == i) {
                    CommonUtil.setSysClipboardText(md5);
                    System.exit(0);

                }

            } else {
                if (message.getMsg() != null) {
                    int i = JOptionPane.showConfirmDialog(null, message.getMsg(), "文件检测失败:", JOptionPane.YES_OPTION);
                    if (JOptionPane.YES_OPTION == i)
                        System.exit(0);

                }
            }
        }

    }

}
