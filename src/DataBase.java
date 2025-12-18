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
                String strGameTitle = rs.getString("Team1") + " VS " + rs.getString("Team2");
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
                    panel.add(new JButton("底部按钮示例"), BorderLayout.SOUTH);

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

                button.addActionListener(e -> {
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
                    panel.add(new JButton("底部按钮示例"), BorderLayout.SOUTH);

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
}

//用于返回控件的类
class Controller{

}
