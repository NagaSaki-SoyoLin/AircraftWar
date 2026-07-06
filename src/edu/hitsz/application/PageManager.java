package edu.hitsz.application;

import javax.swing.*;
import java.awt.*;

// 页面管理器，用于管理页面切换
public class PageManager {

    // 单例模式
    private static PageManager instance;

    public static synchronized PageManager getInstance() {
        if (instance == null) {
            instance = new PageManager();
        }
        return instance;
    }

    private PageManager() {
    }

    // 卡片面板
    private JPanel cardPanel;

    // 卡片布局
    private CardLayout cardLayout;

    // 页面名称常量
    public static final String PAGE_MENU = "menu";
    public static final String PAGE_GAME = "game";

    // 初始化方法，在Main中调用
    public void initialize(JFrame frame) {
        // 创建 CardLayout 和承载它的面板
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setPreferredSize(new Dimension(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));

        // 将cardPanel设置为frame的内容面板
        frame.setContentPane(cardPanel);
    }

    // 注册页面
    public void registerPage(String name, JPanel panel) {
        if (cardPanel == null) {
            throw new IllegalStateException("请先调用 initialize() 方法！");
        }
        cardPanel.add(panel, name);
    }

    // 显示页面
    public void showPage(String name) {
        if (cardLayout != null && cardPanel != null) {
            cardLayout.show(cardPanel, name);
        }
    }

}