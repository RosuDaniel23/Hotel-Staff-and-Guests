import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonObject;

public class ChatbotPanel extends JPanel {
    private final ClentService service;
    private final JTextArea chatArea;
    private final JTextField inputField;
    private final JButton sendButton;
    private final List<String> chatHistory;

    public ChatbotPanel(ClentService service) {
        this.service = service;
        this.chatHistory = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("🤖 Hotel Concierge Chatbot");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        chatArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setPreferredSize(new Dimension(400, 400));
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));

        inputField = new JTextField();
        inputField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        bottomPanel.add(inputField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.setBackground(new Color(59, 130, 246));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        addWelcomeMessage();

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        JPanel quickQuestionsPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        quickQuestionsPanel.setBorder(BorderFactory.createTitledBorder("Quick Questions"));

        String[] quickQuestions = {
            "What are the advantages of the suite room?",
            "Tell me about room upgrades",
            "What amenities do you offer?",
            "What time is check-in?",
            "How much does a double room cost?"
        };

        for (String question : quickQuestions) {
            JButton qButton = new JButton(question);
            qButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
            qButton.addActionListener(e -> {
                inputField.setText(question);
                sendMessage();
            });
            quickQuestionsPanel.add(qButton);
        }

        add(quickQuestionsPanel, BorderLayout.EAST);
    }

    private void addWelcomeMessage() {
        String welcome = "👋 Hello! Welcome to our hotel chatbot!\n\n" +
                        "I can help you with:\n" +
                        "• Room information and upgrades\n" +
                        "• Hotel services and amenities\n" +
                        "• Check-in/out information\n" +
                        "• And much more!\n\n" +
                        "How can I assist you today?\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n";
        chatArea.append(welcome);
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (message.isEmpty()) return;

        appendMessage("You", message);
        inputField.setText("");
        sendButton.setEnabled(false);
        inputField.setEnabled(false);

        new Thread(() -> {
            try {
                JsonObject response = service.sendChatMessage(message);
                String botResponse = response.get("response").getAsString();
                SwingUtilities.invokeLater(() -> {
                    appendMessage("Bot", botResponse);
                    sendButton.setEnabled(true);
                    inputField.setEnabled(true);
                    inputField.requestFocus();
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    appendMessage("Bot", "❌ Sorry, I encountered an error. Please try again.");
                    sendButton.setEnabled(true);
                    inputField.setEnabled(true);
                });
                System.err.println("Chatbot error: " + ex.getMessage());
            }
        }).start();
    }

    private void appendMessage(String sender, String message) {
        chatHistory.add(sender + ": " + message);
        chatArea.append(sender + ":\n" + message + "\n");
        chatArea.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
}

