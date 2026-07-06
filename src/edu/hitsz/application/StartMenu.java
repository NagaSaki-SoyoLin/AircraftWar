package edu.hitsz.application;

import edu.hitsz.game.AbstractGame;
import edu.hitsz.game.GameEasy;
import edu.hitsz.game.GameHard;
import edu.hitsz.game.GameNormal;

import javax.swing.*;
import java.awt.*;

// 游戏开始菜单界面，难度选择
public class StartMenu {

    private JPanel mainPanel;
    private JButton easyButton;
    private JButton normalButton;
    private JButton hardButton;

    public StartMenu() {
        initComponents();
        bindEvents();
    }

    private void initComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));

        // 标题区域
        JPanel titlePanel = getJPanel();

        // 按钮区域
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 0, 25));
        buttonPanel.setBackground(new Color(30, 40, 60));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 80, 80));

        easyButton = createStyledButton("简单模式", new Color(76, 175, 80));
        normalButton = createStyledButton("普通模式", new Color(255, 193, 7));
        hardButton = createStyledButton("困难模式", new Color(244, 67, 54));

        buttonPanel.add(easyButton);
        buttonPanel.add(normalButton);
        buttonPanel.add(hardButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
    }

    // 创建一个样式统一的标题面板
    private static JPanel getJPanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setPreferredSize(new Dimension(Main.WINDOW_WIDTH, 200));
        titlePanel.setBackground(new Color(30, 40, 60));

        JLabel titleLabel = new JLabel("✈ 飞机大战 ✈", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 42));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        JLabel subtitleLabel = new JLabel("—— 请选择难度 ——", SwingConstants.CENTER);
        subtitleLabel.setForeground(new Color(200, 200, 220));
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        return titlePanel;
    }

    // 创建一个样式统一的按钮
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text); // 设置按钮文本
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 22));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(300, 60));
        return btn;
    }

    private void bindEvents() {
        easyButton.addActionListener(e -> startGame(new GameEasy()));
        normalButton.addActionListener(e -> startGame(new GameNormal()));
        hardButton.addActionListener(e -> startGame(new GameHard()));
    }

    // 启动游戏
    private void startGame(AbstractGame game) {
        PageManager pageManager = PageManager.getInstance();
        // 注册游戏页面到CardLayout
        pageManager.registerPage(PageManager.PAGE_GAME, game);
        // 切换到游戏页面
        pageManager.showPage(PageManager.PAGE_GAME);
        // 启动游戏主循环
        game.action();
    }

    // 返回主面板
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
