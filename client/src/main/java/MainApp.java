import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.google.gson.JsonObject;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("Starting Hotel Management Client...");
        System.out.println("Backend URL: http://localhost:8080");

        SwingUtilities.invokeLater(() -> {
            ClentService service = new ClentService();
            JFrame frame = new JFrame("Hotel Client - Login");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(420, 300);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel typeLabel = new JLabel("Login as");
            JRadioButton guestRadio = new JRadioButton("Guest", true);
            JRadioButton staffRadio = new JRadioButton("Staff");
            ButtonGroup group = new ButtonGroup();
            group.add(guestRadio);
            group.add(staffRadio);

            JTextField emailField = new JTextField(20);
            JPasswordField passwordField = new JPasswordField(20);

            gbc.gridx = 0; gbc.gridy = 0; panel.add(typeLabel, gbc);
            gbc.gridx = 1; gbc.gridy = 0; panel.add(guestRadio, gbc);
            gbc.gridx = 2; gbc.gridy = 0; panel.add(staffRadio, gbc);

            gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Email"), gbc);
            gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; panel.add(emailField, gbc); gbc.gridwidth = 1;

            gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Password"), gbc);
            gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2; panel.add(passwordField, gbc); gbc.gridwidth = 1;

            JButton loginBtn = new JButton("Login");
            gbc.gridx = 1; gbc.gridy = 3; panel.add(loginBtn, gbc);

            JLabel message = new JLabel(" ");
            message.setForeground(Color.RED);
            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3; panel.add(message, gbc);

            loginBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    loginBtn.setEnabled(false);
                    message.setForeground(Color.BLUE);
                    message.setText("Authenticating...");
                    String email = emailField.getText().trim();
                    String password = new String(passwordField.getPassword());
                    boolean isGuest = guestRadio.isSelected();

                    System.out.println("Attempting login as " + (isGuest ? "Guest" : "Staff") + " with email: " + email);

                    new Thread(() -> {
                        try {
                            if (isGuest) {
                                JsonObject resp = service.guestLogin(email, password);
                                if (resp == null) {
                                    SwingUtilities.invokeLater(() -> {
                                        message.setForeground(Color.RED);
                                        message.setText("Invalid credentials");
                                        loginBtn.setEnabled(true);
                                    });
                                    System.err.println("Guest login failed: Invalid credentials");
                                } else {
                                    System.out.println("Guest login successful: " + resp.get("name").getAsString());
                                    SwingUtilities.invokeLater(() -> {
                                        frame.dispose();
                                        new DashboardController(service, resp).show();
                                    });
                                }
                            } else {
                                JsonObject resp = service.employeeLogin(email, password);
                                if (resp == null) {
                                    SwingUtilities.invokeLater(() -> {
                                        message.setForeground(Color.RED);
                                        message.setText("Invalid credentials");
                                        loginBtn.setEnabled(true);
                                    });
                                    System.err.println("Staff login failed: Invalid credentials");
                                } else {
                                    System.out.println("Staff login successful: " + resp.get("name").getAsString());
                                    SwingUtilities.invokeLater(() -> {
                                        frame.dispose();
                                        JFrame staffFrame = new JFrame("Staff Dashboard");
                                        staffFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                        staffFrame.setSize(600, 400);
                                        staffFrame.setLocationRelativeTo(null);

                                        JPanel staffPanel = new JPanel(new BorderLayout(20, 20));
                                        staffPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                                        JLabel welcomeLabel = new JLabel("<html><h1>Welcome, " + resp.get("name").getAsString() + "</h1></html>");
                                        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
                                        staffPanel.add(welcomeLabel, BorderLayout.NORTH);

                                        JTextArea infoArea = new JTextArea();
                                        infoArea.setEditable(false);
                                        infoArea.setText("Staff Dashboard\n\n" +
                                                "Name: " + resp.get("name").getAsString() + "\n" +
                                                "Email: " + resp.get("email").getAsString() + "\n" +
                                                "Role: " + resp.get("role").getAsString() + "\n\n" +
                                                "This is a placeholder staff interface.\n" +
                                                "Full staff management features can be accessed via the web interface.");
                                        staffPanel.add(new JScrollPane(infoArea), BorderLayout.CENTER);

                                        staffFrame.add(staffPanel);
                                        staffFrame.setVisible(true);
                                    });
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            SwingUtilities.invokeLater(() -> {
                                message.setForeground(Color.RED);
                                message.setText("Error: " + ex.getMessage());
                                loginBtn.setEnabled(true);
                            });
                            System.err.println("Login error: " + ex.getMessage());
                        }
                    }).start();
                }
            });

            frame.setContentPane(panel);
            frame.setVisible(true);
            System.out.println("Login window displayed");
        });
    }
}
