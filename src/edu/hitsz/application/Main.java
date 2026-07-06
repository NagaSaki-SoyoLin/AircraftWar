package edu.hitsz.application;

import javax.swing.*;

/**
 * 程序入口
 *
 * @author hitsz
 */
public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    // 全局Frame引用，供面板切换时使用
    public static JFrame mainFrame;

    public static void main(String[] args) {
        System.out.println("✈欢迎来到飞机大战!");

        // 获得屏幕的分辨率，初始化Frame
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame = new JFrame("飞机大战");
        mainFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainFrame.setResizable(false);
        // 设置窗口的大小和位置，居中放置
        mainFrame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 初始化页面管理器
        PageManager pageManager = PageManager.getInstance();
        pageManager.initialize(mainFrame);

        // 创建并注册开始菜单页面
        StartMenu menu = new StartMenu();
        pageManager.registerPage(PageManager.PAGE_MENU, menu.getMainPanel());

        // 默认显示菜单页面
        pageManager.showPage(PageManager.PAGE_MENU);
        mainFrame.setVisible(true);
    }

}