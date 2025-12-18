import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

class DataBase {

    //数据库的信息
    private static String url = "jdbc:opengauss://127.0.0.1:55433/forproject";
    private static String user = "gaussdb";
    private static String password = "Ww@12345678";

    private static Connection conn;
    private static ResultSet rs = null;

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
    public static ResultSet getBigScore(JFrame frame) {
        ArrayList<JButton> buttons = new ArrayList<>();

        String sql = "SELECT BSno,Team1,Team2,Cfotmat,FinalScore FROM BigScoreData";//Cformat打错Cfotmat
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //在小比分面板上的按钮
    public static ResultSet getSmallScore(JDialog dialog, String BSno) {
        ArrayList<JButton> buttons = new ArrayList<>();

        String sql = "SELECT SSno, Team1, Team2, FinalScore FROM SmallScoreData WHERE BSno = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, BSno);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //在选手表现面板的按钮
    public static ResultSet getPlayer(String SSno) {
        ArrayList<JButton> buttons = new ArrayList<>();

        String sql = "SELECT PName, Rating, ADR, KD FROM PlayerData WHERE SSno = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, SSno);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
