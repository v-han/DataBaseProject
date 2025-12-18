import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class Controller{
    private static final Dimension BUTTON_SIZE = new Dimension(380, 40);
    private static final Dimension SCREEN_SIZE = new Dimension(400, 300);

    //主函数的界面
    public static void StartFrame() {
        JFrame frame = new JFrame();

        frame.setPreferredSize(SCREEN_SIZE);
        frame.setMaximumSize(SCREEN_SIZE);
        frame.setMinimumSize(SCREEN_SIZE);

        //最下面的Panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        //记录大比分的Panel
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

        //链接数据库
        DataBase.getConnect();

        for (JButton button : getBigScoreButton(frame))
        {
            innerPanel.add(button);
        }

        JScrollPane scrollPane = new JScrollPane(innerPanel);
        panel.add(scrollPane, BorderLayout.CENTER);

        //底部区域的设计
        JButton buttonInsert = getButtonInsertBSD(frame);

        panel.add(buttonInsert, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //获得插入大比分数据的按钮
    public static JButton getButtonInsertBSD(JFrame frame) {
        JButton button = new JButton("插入数据");

        //按钮外观设计
        button.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        button.setMinimumSize(BUTTON_SIZE);
        button.setOpaque(true);

        //按钮功能设计
        button.addActionListener(e -> {
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
        return button;
    }

    //获得插入小比分功能的按钮
    public static JButton getButtonInsertSSD(JDialog dialog, String Team1, String Team2,String BSno) {
        JButton button = new JButton("插入数据");

        //按钮外观设计
        button.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        button.setMinimumSize(BUTTON_SIZE);
        button.setOpaque(true);

        //按钮功能设计
        button.addActionListener(e -> {
            JDialog dialogInsert = new JDialog(dialog, "添加小比分数据", true);
            dialogInsert.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialogInsert.setLayout(new BorderLayout(10, 10));

            JLabel title = new JLabel("添加大比分数据", SwingConstants.CENTER);
            title.setFont(new Font("微软雅黑", Font.BOLD, 18));
            dialogInsert.add(title, BorderLayout.NORTH);

            JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

            JTextField fieldScore = new JTextField();

            formPanel.add(new JLabel("最终得分："));
            formPanel.add(fieldScore);

            dialogInsert.add(formPanel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnOk = new JButton("提交");

            buttonPanel.add(btnOk);

            btnOk.addActionListener(e2 -> {

                String score = fieldScore.getText().trim();

                if (score.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "输入不能有空", "输入错误", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                boolean ok = DataBase.InsertSmallScoreData(Team1, Team2, BSno, score);

                if (ok) {
                    JOptionPane.showMessageDialog(dialogInsert, "插入成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose(); // 关闭对话框
                } else {
                    JOptionPane.showMessageDialog(dialogInsert, "插入失败，请检查数据库", "错误", JOptionPane.ERROR_MESSAGE);
                }
            });

            dialogInsert.add(buttonPanel, BorderLayout.SOUTH);

            dialogInsert.pack();
            dialogInsert.setLocationRelativeTo(dialog);
            dialogInsert.setVisible(true);
        });
        return button;
    }

    //获得插入选手表现功能的按钮
    public static JButton getButtonInsertPD(JDialog dialog, String SSno) {
        JButton button = new JButton("插入数据");

        //按钮外观设计
        button.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        button.setMinimumSize(BUTTON_SIZE);
        button.setOpaque(true);

        //按钮功能设计
        button.addActionListener(e -> {
            JDialog dialogInsert = new JDialog(dialog, "添加小比分数据", true);
            dialogInsert.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialogInsert.setLayout(new BorderLayout(10, 10));

            JLabel title = new JLabel("添加大比分数据", SwingConstants.CENTER);
            title.setFont(new Font("微软雅黑", Font.BOLD, 18));
            dialogInsert.add(title, BorderLayout.NORTH);

            JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

            JTextField fieldPname = new JTextField();
            JTextField fieldRating = new JTextField();
            JTextField fieldADR = new JTextField();
            JTextField fieldKD = new JTextField();

            formPanel.add(new JLabel("选手姓名"));
            formPanel.add(fieldPname);
            formPanel.add(new JLabel("选手Reting"));
            formPanel.add(fieldRating);
            formPanel.add(new JLabel("选手ADR"));
            formPanel.add(fieldADR);
            formPanel.add(new JLabel("选手KD"));
            formPanel.add(fieldKD);

            dialogInsert.add(formPanel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnOk = new JButton("提交");

            buttonPanel.add(btnOk);

            btnOk.addActionListener(e2 -> {

                String Pname = fieldPname.getText().trim();
                String Rating = fieldRating.getText().trim();
                String ADR = fieldADR.getText().trim();
                String KD = fieldKD.getText().trim();

                if (Pname.isEmpty() || Rating.isEmpty() || ADR.isEmpty() || KD.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "输入不能有空", "输入错误", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                boolean ok = DataBase.InsertPlayerData(SSno, Pname, Rating, ADR, KD);

                if (ok) {
                    JOptionPane.showMessageDialog(dialogInsert, "插入成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose(); // 关闭对话框
                } else {
                    JOptionPane.showMessageDialog(dialogInsert, "插入失败，请检查数据库", "错误", JOptionPane.ERROR_MESSAGE);
                }
            });

            dialogInsert.add(buttonPanel, BorderLayout.SOUTH);

            dialogInsert.pack();
            dialogInsert.setLocationRelativeTo(dialog);
            dialogInsert.setVisible(true);
        });
        return button;
    }

    //在大比分面板上的按钮
    public static ArrayList<JButton> getBigScoreButton(JFrame frame) {
        ArrayList<JButton> buttons = new ArrayList<>();

        ResultSet rs = DataBase.getBigScore(frame);

        String str,Team1,Team2,BSno,score;

        while (true) {
            try {
                if (!rs.next()) break;
                str = rs.getString("Team1") + "  VS  " + rs.getString("Team2") + "  " + rs.getString("Cfotmat") + "  " + rs.getString("FinalScore");
                Team1 = rs.getString("Team1");
                Team2 = rs.getString("Team2");
                BSno = rs.getString("BSno");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            JButton button = new JButton(str);

            //按钮外观设计
            button.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
            button.setBackground(new Color(70, 130, 180));
            button.setForeground(Color.WHITE);
            button.setPreferredSize(BUTTON_SIZE);
            button.setMaximumSize(BUTTON_SIZE);
            button.setMinimumSize(BUTTON_SIZE);
            button.setOpaque(true);

            //按钮功能设计
            String strGameTitle = Team1 + " VS " + Team2;

            String FTeam1 = Team1;
            String FTeam2 = Team2;
            String FBSno = BSno;

            button.addActionListener(e -> {
                //点击按钮后弹出的界面设计
                JDialog dialog = new JDialog(frame, strGameTitle, true);
                dialog.setPreferredSize(SCREEN_SIZE);
                dialog.setMaximumSize(SCREEN_SIZE);
                dialog.setMinimumSize(SCREEN_SIZE);

                //界面Panel
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());

                JPanel innerPanel = new JPanel();
                innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

                for (JButton Sbutton : getSmallScoreButton(dialog, FBSno)) {
                    innerPanel.add(Sbutton);
                }

                JScrollPane scrollPane = new JScrollPane(innerPanel);
                panel.add(scrollPane, BorderLayout.CENTER);

                JButton buttonInsert = getButtonInsertSSD(dialog, FTeam1, FTeam2, FBSno);

                panel.add(buttonInsert, BorderLayout.SOUTH);

                dialog.add(panel);
                dialog.setVisible(true);
                dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            });
            buttons.add(button);
        }
        return buttons;
    }

    //在小比分面板上的按钮
    public static ArrayList<JButton> getSmallScoreButton(JDialog dialog, String BSno) {
        ArrayList<JButton> buttons = new ArrayList<>();

        ResultSet rs = DataBase.getSmallScore(dialog, BSno);

        String str,SSno;

        while (true) {
            try {
                if (!rs.next()) break;
                str = rs.getString("Team1") + "  VS  " + rs.getString("Team2") + rs.getString("FinalScore");
                SSno = rs.getString("SSno");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            JButton button = new JButton(str);

            //按钮外观设计
            button.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
            button.setBackground(new Color(70, 130, 180));
            button.setForeground(Color.WHITE);
            button.setPreferredSize(BUTTON_SIZE);
            button.setMaximumSize(BUTTON_SIZE);
            button.setMinimumSize(BUTTON_SIZE);
            button.setOpaque(true);

            //按钮功能设计
            String strTitle = "Player Performance";
            String FSSno = SSno;

            button.addActionListener(_ -> {
                //点击按钮后弹出的界面设计
                JDialog Playerdialog = new JDialog(dialog,strTitle,true);
                Playerdialog.setPreferredSize(SCREEN_SIZE);
                Playerdialog.setMaximumSize(SCREEN_SIZE);
                Playerdialog.setMinimumSize(SCREEN_SIZE);

                //界面Panel
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());

                JPanel innerPanel = new JPanel();
                innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

                for (JButton Pbutton : getPlayerButton(FSSno))
                {
                    innerPanel.add(Pbutton);
                }

                JScrollPane scrollPane = new JScrollPane(innerPanel);
                panel.add(scrollPane, BorderLayout.CENTER);

                JButton buttonInsert = getButtonInsertPD(Playerdialog,FSSno);

                panel.add(buttonInsert, BorderLayout.SOUTH);

                Playerdialog.add(panel);
                Playerdialog.setVisible(true);
                Playerdialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            });
            buttons.add(button);
        }
        return buttons;
    }

    //在选手表现面板的按钮
    public static ArrayList<JButton> getPlayerButton(String SSno) {
        ArrayList<JButton> buttons = new ArrayList<>();

        JButton Topbutton =  new JButton("Pname   Rating   ADR   KD");

        //顶部信息栏
        Topbutton.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        Topbutton.setBackground(new Color(70, 130, 180));
        Topbutton.setForeground(Color.WHITE);
        Topbutton.setPreferredSize(BUTTON_SIZE);
        Topbutton.setMaximumSize(BUTTON_SIZE);
        Topbutton.setMinimumSize(BUTTON_SIZE);
        Topbutton.setOpaque(true);

        buttons.add(Topbutton);

        ResultSet rs = DataBase.getPlayer(SSno);

        String str,strRating;
        double Rating;

        while (true) {
            try {
                if (!rs.next()) break;
                str = rs.getString("PName") + " " + rs.getString("Rating") + " " +rs.getString("ADR") + " " + rs.getString("KD");
                strRating = rs.getString("Rating");
                Rating = Double.parseDouble(strRating);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            JButton button = new JButton(str);

            //按钮外观设计
            button.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
            if (Rating < 1) {
                button.setBackground(new Color(255, 0, 0));
            } else if  (Rating < 1.1) {
                button.setBackground(new Color(197, 197, 197));
            } else {
                button.setBackground(new Color(85, 255, 0));
            }

            button.setForeground(Color.WHITE);
            button.setPreferredSize(BUTTON_SIZE);
            button.setMaximumSize(BUTTON_SIZE);
            button.setMinimumSize(BUTTON_SIZE);
            button.setOpaque(true);

            buttons.add(button);
        }
        return  buttons;
    }

}
