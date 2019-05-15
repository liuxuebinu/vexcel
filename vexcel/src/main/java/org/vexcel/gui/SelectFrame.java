package org.vexcel.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.vexcel.pojo.ExcelConfig;
import org.vexcel.pojo.VSheet;
import org.vexcel.tools.XmlUtils;

public class SelectFrame extends JFrame implements Runnable {

    JPanel topPanel;
    JPanel middlePanel;
    JPanel bottomPanel;

    JLabel tipLabel;
    int mainWidth = 350;
    int height = 200;
    JFrame a = this;
    HashMap<String, ExcelConfig> valictors;
    JComboBox jcb;

    public boolean isChecking = false;

    public void init() {
        JButton closeButton = new JButton("X");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(Color.RED);
        closeButton.setBorder(null);
        closeButton.setBounds(mainWidth - 30, 0, 30, 30);
        closeButton.setName("quit");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                String name = ((JButton) e.getSource()).getName();
                if ("quit".equals(name)) {
                    System.exit(0);

                }

            }

        });

        JLabel jl = new JLabel("选择校验器： ");
        jl.setBounds(0, 30, 100, 30);
        jl.setBackground(Color.DARK_GRAY);
        jl.setForeground(Color.WHITE);

        Set<String> valictorNames = valictors.keySet();
        String[] valictorNames_ = valictorNames.toArray(new String[valictorNames.size()]);

        this.jcb = new JComboBox(valictorNames_);
        jcb.setBounds(100, 30, mainWidth - 100, 30);
        jcb.setBackground(Color.DARK_GRAY);
        jcb.setForeground(Color.WHITE);

        topPanel = new JPanel();
        topPanel.setBackground(Color.RED);
        topPanel.setBounds(0, 0, mainWidth, 30);// (mainWidth, 30);
        topPanel.setLayout(null);
        topPanel.add(closeButton);

        middlePanel = new JPanel();
        middlePanel.setBackground(Color.DARK_GRAY);
        middlePanel.setBounds(0, 30, mainWidth, 140);
        middlePanel.add(jcb);
        middlePanel.add(jl);
        middlePanel.setLayout(null);

        bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.DARK_GRAY);
        bottomPanel.setBounds(0, 170, mainWidth, 30);
        bottomPanel.setLayout(null);

        JButton ConfirmButton = new JButton("确认");
        ConfirmButton.setForeground(Color.WHITE);
        ConfirmButton.setBackground(Color.DARK_GRAY);
        ConfirmButton.setBorder(null);
        ConfirmButton.setBounds(mainWidth - 50, 0, 50, 30);
        ConfirmButton.setName("Confirm");
        ConfirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                String name = ((JButton) e.getSource()).getName();
                if ("Confirm".equals(name)) {
                    System.out.println(valictors.get((String) jcb.getSelectedItem()).getXmlPath());
                    System.out.println((String) jcb.getSelectedItem());
                    List<VSheet> rules = new XmlUtils().getRuleByName(
                            valictors.get((String) jcb.getSelectedItem()).getXmlPath(), (String) jcb.getSelectedItem());
                    System.out.println(rules.size());
                    new Thread(new MainFrame(rules, valictors.get((String) jcb.getSelectedItem()))).start();
                    ;
                    close();
                }

            }

        });

        bottomPanel.add(ConfirmButton);

        getContentPane().add(topPanel);
        getContentPane().add(middlePanel);
        getContentPane().add(bottomPanel);
        setSize(mainWidth, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.setLayout(null);

        this.setLocation(400, 200);

        setDragable();
        this.setVisible(true);

    }

    public void close() {
        this.dispose();
    }

    public SelectFrame(HashMap<String, ExcelConfig> paths) {
        this.valictors = paths;
    }

    Point loc = null;
    Point tmp = null;

    boolean isDragged = false;

    private void setDragable() {

        this.addMouseListener(

                new java.awt.event.MouseAdapter() {

                    public void mouseReleased(java.awt.event.MouseEvent e) {
                        isDragged = false;
                        a.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                    }

                    public void mousePressed(java.awt.event.MouseEvent e) {
                        tmp = new Point(e.getX(), e.getY());
                        isDragged = true;
                        a.setCursor(new Cursor(Cursor.MOVE_CURSOR));

                    }

                });

        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            public void mouseDragged(java.awt.event.MouseEvent e) {

                if (isDragged) {

                    loc = new Point(a.getLocation().x + e.getX() - tmp.x,

                            a.getLocation().y + e.getY() - tmp.y);

                    a.setLocation(loc);

                }

            }

        });
    }

    public void run() {

        // TODO Auto-generated method stub
        init();

    }

}
