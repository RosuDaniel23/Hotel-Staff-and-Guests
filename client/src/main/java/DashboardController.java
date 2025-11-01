import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DashboardController {
    private final ClentService service;
    private final JsonObject guestLoginResp;
    private JFrame frame;

    public DashboardController(ClentService service, JsonObject guestLoginResp) {
        this.service = service;
        this.guestLoginResp = guestLoginResp;
    }

    public void show() {
        System.out.println("Opening guest dashboard for: " + guestLoginResp.get("name").getAsString());
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Guest Portal - " + guestLoginResp.get("name").getAsString());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);

            JTabbedPane tabbedPane = new JTabbedPane();

            JPanel roomPanel = createRoomPanel();
            tabbedPane.addTab("My Room", roomPanel);

            ChatbotPanel chatbotPanel = new ChatbotPanel(service);
            tabbedPane.addTab("💬 Chat Assistant", chatbotPanel);

            frame.setContentPane(tabbedPane);
            frame.setVisible(true);

            loadRoomData(roomPanel);
        });
    }

    private JPanel createRoomPanel() {
        JPanel main = new JPanel(new BorderLayout(12, 12));
        main.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JPanel top = new JPanel(new GridLayout(3, 1));
        top.add(new JLabel("<html><h2>Welcome, " + guestLoginResp.get("name").getAsString() + "</h2></html>"));
        top.add(new JLabel("Email: " + guestLoginResp.get("email").getAsString()));
        top.add(new JLabel("Guest ID: " + guestLoginResp.get("id").getAsString()));
        main.add(top, BorderLayout.NORTH);

        return main;
    }

    private void loadRoomData(JPanel roomPanel) {
        JPanel center = new JPanel(new GridLayout(1,2,12,12));

        JTextArea roomArea = new JTextArea();
        roomArea.setEditable(false);
        roomArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane roomScroll = new JScrollPane(roomArea);
        roomScroll.setBorder(BorderFactory.createTitledBorder("Your Room Details"));

        DefaultListModel<String> upgradesModel = new DefaultListModel<>();
        JList<String> upgradesList = new JList<>(upgradesModel);
        upgradesList.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        JScrollPane upgradesScroll = new JScrollPane(upgradesList);
        upgradesScroll.setBorder(BorderFactory.createTitledBorder("Available Room Upgrades"));

        center.add(roomScroll);
        center.add(upgradesScroll);
        roomPanel.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshBtn = new JButton("Refresh Data");
        JButton requestBtn = new JButton("Request Upgrade");
        bottom.add(refreshBtn);
        bottom.add(requestBtn);
        roomPanel.add(bottom, BorderLayout.SOUTH);

        Runnable load = () -> {
            try {
                long guestId = guestLoginResp.get("id").getAsLong();
                System.out.println("Fetching room data for guest ID: " + guestId);

                JsonObject roomResp = service.getGuestRoom(guestId);
                SwingUtilities.invokeLater(() -> {
                    if (roomResp == null || !roomResp.has("room")) {
                        roomArea.setText("No room assigned.\n\nPlease contact the front desk.");
                        System.out.println("No room assigned to guest");
                    } else {
                        JsonObject room = roomResp.getAsJsonObject("room");
                        StringBuilder sb = new StringBuilder();
                        sb.append("Room ID: ").append(room.get("id")).append('\n');
                        sb.append("Room Number: ").append(room.get("number").getAsString()).append('\n');
                        sb.append("Room Type: ").append(room.get("type").getAsString()).append('\n');
                        sb.append("Price per Night: $").append(room.get("price").getAsString()).append('\n');
                        sb.append("Status: ").append(room.get("status").getAsString()).append('\n');
                        roomArea.setText(sb.toString());
                        System.out.println("Room data loaded: " + room.get("number").getAsString());
                    }
                });

                System.out.println("Fetching available upgrades...");
                JsonArray upgrades = service.getAvailableUpgrades(guestId);
                SwingUtilities.invokeLater(() -> {
                    upgradesModel.clear();
                    if (upgrades.size() == 0) {
                        upgradesModel.addElement("No upgrades available");
                        System.out.println("No upgrades available");
                    } else {
                        for (JsonElement el : upgrades) {
                            JsonObject r = el.getAsJsonObject();
                            String v = String.format("Room %s - %s ($%s/night)",
                                r.get("number").getAsString(),
                                r.get("type").getAsString(),
                                r.get("price").getAsString());
                            upgradesModel.addElement(v);
                        }
                        System.out.println("Loaded " + upgrades.size() + " available upgrades");
                    }
                });

            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    roomArea.setText("Error loading data:\n" + ex.getMessage() + "\n\nPlease check:\n- Backend is running\n- Network connection");
                });
                System.err.println("Error loading data: " + ex.getMessage());
            }
        };

        new Thread(load).start();

        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshBtn.setEnabled(false);
                System.out.println("Refreshing data...");
                new Thread(() -> {
                    try {
                        load.run();
                    } finally {
                        SwingUtilities.invokeLater(() -> {
                            refreshBtn.setEnabled(true);
                            System.out.println("Data refresh complete");
                        });
                    }
                }).start();
            }
        });

        requestBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = upgradesList.getSelectedIndex();
                if (idx < 0) {
                    JOptionPane.showMessageDialog(frame, "Please select a room upgrade from the list first", "No Selection", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                String sel = upgradesModel.get(idx);
                if (sel.equals("No upgrades available")) {
                    return;
                }

                try {
                    long guestId = guestLoginResp.get("id").getAsLong();
                    JsonArray upgrades = service.getAvailableUpgrades(guestId);
                    if (idx >= upgrades.size()) return;

                    JsonObject selectedRoom = upgrades.get(idx).getAsJsonObject();
                    long requestedRoomId = selectedRoom.get("id").getAsLong();

                    int confirm = JOptionPane.showConfirmDialog(frame,
                        "Request upgrade to:\n" + sel + "\n\nContinue?",
                        "Confirm Upgrade Request",
                        JOptionPane.YES_NO_OPTION);

                    if (confirm != JOptionPane.YES_OPTION) return;

                    System.out.println("Requesting upgrade to room ID: " + requestedRoomId);
                    requestBtn.setEnabled(false);

                    new Thread(() -> {
                        try {
                            JsonObject resp = service.requestUpgrade(guestId, requestedRoomId);
                            SwingUtilities.invokeLater(() -> {
                                if (resp != null && resp.has("message")) {
                                    JOptionPane.showMessageDialog(frame,
                                        resp.get("message").getAsString(),
                                        "Success",
                                        JOptionPane.INFORMATION_MESSAGE);
                                    System.out.println("Upgrade request submitted successfully");
                                    refreshBtn.doClick();
                                } else {
                                    JOptionPane.showMessageDialog(frame,
                                        "Upgrade request failed. Please try again.",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                                    System.err.println("Upgrade request failed");
                                }
                                requestBtn.setEnabled(true);
                            });
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(frame,
                                    "Error: " + ex.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                                requestBtn.setEnabled(true);
                            });
                            System.err.println("Upgrade request error: " + ex.getMessage());
                        }
                    }).start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame,
                        "Error processing request: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
