import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

class DataBase {
    DataBase() {
    }

    //数据库的信息
    private static String url = "jdbc:opengauss://127.0.0.1:55433/forproject";
    private static String user = "gaussdb";
    private static String password = "Ww@12345678";

    private static Connection conn;

    private static final Dimension BUTTON_SIZE = new Dimension(380, 40);
    private static final Dimension SCREEN_SIZE = new Dimension(400, 300);

    //链接数据库
    public static Connection getConnect() {
        try {
            Class.forName("org.opengauss.Driver");
            conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //在大比分面板上的按钮
    public static ArrayList<JButton> getBigScoreButton(JFrame frame) {
        ArrayList<JButton> buttons = new ArrayList<>();

        String sql = "SELECT BSno,Team1,Team2,Cfotmat,FinalScore FROM BigScoreData";//Cformat打错Cfotmat
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String str = rs.getString("Team1") + "  VS  " + rs.getString("Team2") + "  " + rs.getString("Cfotmat") + "  " + rs.getString("FinalScore");

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
                String Team1 = rs.getString("Team1");
                String Team2 = rs.getString("Team2");
                String strGameTitle = Team1 + " VS " + Team2;
                String BSno = rs.getString("BSno");

                button.addActionListener(e -> {
                    //点击按钮后弹出的界面设计
                    JDialog dialog = new JDialog(frame,strGameTitle,true);
                    dialog.setPreferredSize(SCREEN_SIZE);
                    dialog.setMaximumSize(SCREEN_SIZE);
                    dialog.setMinimumSize(SCREEN_SIZE);

                    //界面Panel
                    JPanel panel = new JPanel();
                    panel.setLayout(new BorderLayout());

                    JPanel innerPanel = new JPanel();
                    innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

                    for (JButton Sbutton : getSmallScoreButton(dialog,BSno))
                    {
                        innerPanel.add(Sbutton);
                    }

                    JScrollPane scrollPane = new JScrollPane(innerPanel);
                    panel.add(scrollPane, BorderLayout.CENTER);

                    JButton buttonInsert = Controller.getButtonInsertSSD(dialog,Team1,Team2,BSno);

                    panel.add(buttonInsert, BorderLayout.SOUTH);

                    dialog.add(panel);
                    dialog.setVisible(true);
                    dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                });

                buttons.add(button);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return buttons;
    }

    //在小比分面板上的按钮
    public static ArrayList<JButton> getSmallScoreButton(JDialog dialog, String BSno) {
        ArrayList<JButton> buttons = new ArrayList<>();

        String sql = "SELECT SSno, Team1, Team2, FinalScore FROM SmallScoreData WHERE BSno = ?";

        ResultSet rs = null;

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, BSno);
            rs = ps.executeQuery();

            while (rs.next()) {
                String str = rs.getString("Team1") + "  VS  " + rs.getString("Team2") + rs.getString("FinalScore");

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
                String SSno = rs.getString("SSno");

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

                    for (JButton Pbutton : getPlayerButton(SSno))
                    {
                        innerPanel.add(Pbutton);
                    }

                    JScrollPane scrollPane = new JScrollPane(innerPanel);
                    panel.add(scrollPane, BorderLayout.CENTER);

                    JButton buttonInsert = Controller.getButtonInsertPD(Playerdialog,SSno);

                    panel.add(buttonInsert, BorderLayout.SOUTH);

                    Playerdialog.add(panel);
                    Playerdialog.setVisible(true);
                    Playerdialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                });

                buttons.add(button);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
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

        String sql = "SELECT PName, Rating, ADR, KD FROM PlayerData WHERE SSno = ?";

        ResultSet rs = null;

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, SSno);
            rs = ps.executeQuery();

            while (rs.next()) {
                String str = rs.getString("PName") + " " + rs.getString("Rating") + " " +rs.getString("ADR") + " " + rs.getString("KD");

                JButton button = new JButton(str);

                //按钮外观设计
                button.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
                button.setBackground(new Color(70, 130, 180));
                button.setForeground(Color.WHITE);
                button.setPreferredSize(BUTTON_SIZE);
                button.setMaximumSize(BUTTON_SIZE);
                button.setMinimumSize(BUTTON_SIZE);
                button.setOpaque(true);

                buttons.add(button);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return buttons;
    }

    //插入大比分数据
    public static boolean InsertBigScoreData(String Team1, String Team2, String Format,String FinalScore) {
        String sql = "INSERT INTO BigScoreData(Team1,Team2,Cfotmat,FinalScore) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, Team1);
            ps.setString(2, Team2);
            ps.setString(3, Format);
            ps.setString(4, FinalScore);
            ps.executeUpdate(); // 执行插入
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //插入小比分数据
    public static boolean InsertSmallScoreData(String Team1, String Team2, String BSno, String FinalScore) {
        String sql = "INSERT INTO SmallScoreData(BSno,Team1,Team2,FinalScore) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, BSno);
            ps.setString(2, Team1);
            ps.setString(3, Team2);
            ps.setString(4, FinalScore);
            ps.executeUpdate(); // 执行插入
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //插入选手表现数据
    public static boolean InsertPlayerData(String SSno, String Pname, String Rating, String ADR, String KD) {
        String sql = "INSERT INTO PlayerData(SSno,Pname,Rating,ADR,KD) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, SSno);
            ps.setString(2, Pname);
            ps.setString(3, Rating);
            ps.setString(4, ADR);
            ps.setString(5, KD);
            ps.executeUpdate(); // 执行插入
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

//用于返回控件的类
class Controller{
    private static final Dimension BUTTON_SIZE = new Dimension(380, 40);
    private static final Dimension SCREEN_SIZE = new Dimension(400, 300);

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
}
