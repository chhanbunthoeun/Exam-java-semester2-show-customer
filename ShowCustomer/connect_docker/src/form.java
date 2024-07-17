import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
public class form {
    private JPanel table;
    private JButton previous;
    private JButton next;
    private JTextField fn;
    private JTextField ln;
    private JTextField phone;
    private ResultSet rs;
    private int currentRow = -1; // Initialize to -1 to start before the first row
    public static void main(String[] args) {
        JFrame frame = new JFrame("form");
        frame.setContentPane(new form().table);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(400,300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public void connection() {
        String url = "jdbc:mysql://localhost:3333/crudapp";
        String username = "root";
        String password = "root";
        Connection con = null;
        Statement stmt = null;
        try {
            con = DriverManager.getConnection(url, username, password);
            System.out.println("Successfully Connected to database");
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String query = "SELECT ID, first_name, last_name, phone FROM customer";
            rs = stmt.executeQuery(query);
            // Move to the first row
            if (rs.next()) {
                currentRow = 0; // Start with the first row
                updateFormFields();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void updateFormFields() throws SQLException {
        if (currentRow >= 0 && rs != null) {
            fn.setText(rs.getString("first_name"));
            ln.setText(rs.getString("last_name"));
            phone.setText(rs.getString("phone"));
        }
    }
    public form() {
        connection();
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (rs.next()) {
                        currentRow++;
                        updateFormFields();
                    }
                } catch (SQLException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        });
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (rs != null && rs.previous()) {
                        currentRow--;
                        updateFormFields();
                    }
                } catch (SQLException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        });
    }
    public JPanel getTablePanel() {
        return table;
    }
}