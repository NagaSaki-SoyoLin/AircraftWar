package edu.hitsz.application;

import edu.hitsz.dao.ScoreDao;
import edu.hitsz.dao.ScoreDaoByFile;
import edu.hitsz.dao.ScoreRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// 得分排行榜对话框
public class ScoreBoardDialog extends JDialog {

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final ScoreDao scoreDao;

    public ScoreBoardDialog(JFrame parent, String daoFilePath, String difficultyLabel) {
        super(parent, "得分排行榜 - " + difficultyLabel, true);
        this.scoreDao = new ScoreDaoByFile();
        ((ScoreDaoByFile) scoreDao).setFilePath(daoFilePath);

        setSize(500, 420);
        setLocationRelativeTo(parent);
        setResizable(false);

        // 表格模型（不可编辑单元格）
        String[] columnNames = {"排名", "玩家名", "得分", "时间"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // 所有列均不可编辑
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 设置列宽
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(220);   // 时间列变宽

        // 添加鼠标点击监听器，点击行即触发删除确认
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0 && row < tableModel.getRowCount()) {
                    String playerName = (String) tableModel.getValueAt(row, 1);
                    int score = (Integer) tableModel.getValueAt(row, 2);
                    int confirm = JOptionPane.showConfirmDialog(
                            ScoreBoardDialog.this,
                            "确定要删除第 " + (row + 1) + " 名的记录吗？\n"
                                    + playerName + " - " + score + " 分",
                            "确认删除",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        scoreDao.doDelete(row);
                        refreshTableData();
                    }
                }
            }
        });

        // 加载并显示数据
        refreshTableData();

        // 底部按钮面板
        JPanel buttonPanel = createButtonPanel();

        // 整体布局
        JLabel titleLabel = new JLabel("🏆 " + difficultyLabel + " 得分排行榜", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JScrollPane scrollPane = new JScrollPane(table);

        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 窗口关闭时直接退出整个应用
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
    }

    // 创建底部按钮面板
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton exitBtn = new JButton("退出游戏");

        Dimension btnSize = new Dimension(120, 35);
        exitBtn.setPreferredSize(btnSize);
        exitBtn.setFont(new Font("SansSerif", Font.BOLD, 14));

        exitBtn.addActionListener(e -> System.exit(0));

        buttonPanel.add(exitBtn);
        return buttonPanel;
    }

    // 从文件读取数据并刷新表格
    public void refreshTableData() {
        tableModel.setRowCount(0);  // 清空现有数据
        ArrayList<ScoreRecord> records = scoreDao.getAllScoreRecords();
        for (int i = 0; i < records.size(); i++) {
            ScoreRecord r = records.get(i);
            Object[] row = {i + 1, r.getPlayerName(), r.getScore(), r.getTime()};
            tableModel.addRow(row);
        }
    }

    // 游戏结束后显示排行榜对话框
    public static void showGameOver(JFrame parent, int score, String daoFilePath,
                                    String difficultyLabel, MusicController musicController) {
        // 播放结束音效和停止BGM
        musicController.playSoundEffect(MusicController.GAME_OVER);
        musicController.stopAllBGM();

        // 弹出输入框获取玩家姓名
        String playerName = JOptionPane.showInputDialog(
                parent,
                "🎮 游戏结束！\n\n您的得分: " + score + "\n\n请输入您的姓名：",
                "记录得分",
                JOptionPane.PLAIN_MESSAGE
        );
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Anonymous";
        } else {
            playerName = playerName.trim();
        }

        // 记录分数到文件
        ScoreDaoByFile scoreDao = new ScoreDaoByFile();
        scoreDao.setFilePath(daoFilePath);
        ScoreRecord record = new ScoreRecord(
                playerName, score,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
        );
        scoreDao.doAdd(record);

        // 控制台输出排行榜
        ArrayList<ScoreRecord> allRecords = scoreDao.getAllScoreRecords();
        System.out.println("**********************************************");
        System.out.println("                   得分排行榜                   ");
        System.out.println("**********************************************");
        for (int i = 0; i < allRecords.size(); i++) {
            System.out.println("第" + (i + 1) + "名: " +
                    allRecords.get(i).getPlayerName() + "," +
                    allRecords.get(i).getScore() + "," +
                    allRecords.get(i).getTime());
        }
        System.out.println("Game Over!");

        // 打开排行榜对话框
        SwingUtilities.invokeLater(() -> new ScoreBoardDialog(
                parent, daoFilePath, difficultyLabel
        ).setVisible(true));
    }

}