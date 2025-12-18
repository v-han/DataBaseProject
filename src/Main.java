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
        JButton buttonInsert = Controller.getButtonInsertBSD(frame);

        panel.add(buttonInsert, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

