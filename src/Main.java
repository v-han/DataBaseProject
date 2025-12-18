import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();

        Dimension BUTTON_SIZE = new Dimension(380, 40);
        Dimension SCREEN_SIZE = new Dimension(400, 300);
        Dimension COMMIT_SCREEN_SIZE = new Dimension(400, 300);
        frame.setPreferredSize(SCREEN_SIZE);
        frame.setMaximumSize(SCREEN_SIZE);
        frame.setMinimumSize(SCREEN_SIZE);

        //最下面的Panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        //记录大比分的Panel
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

        try {
            DataBase.getConnect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        for (JButton button : DataBase.getBigScoreButton(frame))
        {
            innerPanel.add(button);
        }

        JScrollPane scrollPane = new JScrollPane(innerPanel);
        panel.add(scrollPane, BorderLayout.CENTER);

        //底部区域的设计
        JButton buttonInsert = new JButton("插入数据");

        //按钮外观设计

        buttonInsert.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        buttonInsert.setBackground(new Color(70, 130, 180));
        buttonInsert.setForeground(Color.WHITE);
        buttonInsert.setPreferredSize(BUTTON_SIZE);
        buttonInsert.setMaximumSize(BUTTON_SIZE);
        buttonInsert.setMinimumSize(BUTTON_SIZE);
        buttonInsert.setOpaque(true);

        //按钮功能设局
        buttonInsert.addActionListener(e -> {
            JDialog dialog = new JDialog(frame, "添加大比分数据", true);
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog.setLayout(new BorderLayout(10, 10));

            JLabel title = new JLabel("添加大比分数据", SwingConstants.CENTER);
            title.setFont(new Font("微软雅黑", Font.BOLD, 18));
            dialog.add(title, BorderLayout.NORTH);

            JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

            JTextField fieldTeam1 = new JTextField();
            JTextField fieldTeam2 = new JTextField();
            JTextField fieldFormat = new JTextField();
            JTextField fieldScore = new JTextField();

            formPanel.add(new JLabel("Team 1："));
            formPanel.add(fieldTeam1);

            formPanel.add(new JLabel("Team 2："));
            formPanel.add(fieldTeam2);

            formPanel.add(new JLabel("赛制："));
            formPanel.add(fieldFormat);

            formPanel.add(new JLabel("最终得分："));
            formPanel.add(fieldScore);

            dialog.add(formPanel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnOk = new JButton("提交");
            JButton btnCancel = new JButton("取消");

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnOk);

            btnOk.addActionListener(e2 -> {

                String team1 = fieldTeam1.getText().trim();
                String team2 = fieldTeam2.getText().trim();
                String format = fieldFormat.getText().trim();
                String score = fieldScore.getText().trim();

                if (team1.isEmpty() || team2.isEmpty() || format.isEmpty() || score.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "输入不能有空", "输入错误", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                boolean ok = DataBase.InsertBigScoreData(team1, team2, format, score);

                if (ok) {
                    JOptionPane.showMessageDialog(dialog, "插入成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose(); // 关闭对话框
                } else {
                    JOptionPane.showMessageDialog(dialog, "插入失败，请检查数据库", "错误", JOptionPane.ERROR_MESSAGE);
                }
            });

            dialog.add(buttonPanel, BorderLayout.SOUTH);

            dialog.pack();
            dialog.setLocationRelativeTo(frame);
            dialog.setVisible(true);

        });

        panel.add(buttonInsert, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

