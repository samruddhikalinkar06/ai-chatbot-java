// ChatbotDashboard.java
// A GUI-based chatbot system with a dashboard using Java Swing.
// Save this in the same folder as responses.txt
// Compile: javac ChatbotDashboard.java
// Run: java ChatbotDashboard

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ChatbotDashboard {
    public static void main(String[] args) {
        new DashboardFrame();
    }
}

// Dashboard Screen
class DashboardFrame extends JFrame {
    public DashboardFrame() {
        setTitle("AI Chatbot Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton startBtn = new JButton("💬 Start Chat");
        JButton aboutBtn = new JButton("ℹ️ About");
        JButton exitBtn = new JButton("❌ Exit");

        startBtn.addActionListener(e -> {
            new ChatbotFrame();
            dispose();
        });

        aboutBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Project: A Research on Chatbots and Virtual Assistants using AI\n" +
                "Developed by: Samruddhi Sunil Kalinkar\n" +
                "Technology: Java Swing + File-based AI Logic",
                "About Project", JOptionPane.INFORMATION_MESSAGE));

        exitBtn.addActionListener(e -> System.exit(0));

        setLayout(new GridLayout(3, 1, 20, 20));
        add(startBtn);
        add(aboutBtn);
        add(exitBtn);

        setVisible(true);
    }
}

// Chat Window
class ChatbotFrame extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private Map<String, String> responses;

    public ChatbotFrame() {
        setTitle("AI Chatbot Assistant");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        sendButton = new JButton("Send");

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        responses = loadResponses();

        appendBot("Hello! I am your AI assistant. Type something below 👇");

        sendButton.addActionListener(e -> handleUserInput());
        inputField.addActionListener(e -> handleUserInput());

        setVisible(true);
    }

    private void handleUserInput() {
        String userText = inputField.getText().trim();
        if (userText.isEmpty()) return;

        appendUser(userText);
        inputField.setText("");

        String reply = generateResponse(userText.toLowerCase());
        appendBot(reply);
    }

    private Map<String, String> loadResponses() {
        Map<String, String> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("responses.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(":")) {
                    String[] parts = line.split(":", 2);
                    map.put(parts[0].trim().toLowerCase(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            appendBot("Error loading responses.txt");
        }
        return map;
    }

    private String generateResponse(String input) {
        if (input.equals("bye") || input.equals("exit")) {
            appendBot("Goodbye! Have a great day!");
           javax.swing.Timer t = new javax.swing.Timer(2000, e -> System.exit(0));

            t.setRepeats(false);
            t.start();
            return "";
        }

        if (input.contains("time"))
            return "Current time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
        if (input.contains("date"))
            return "Today's date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));

        for (String key : responses.keySet()) {
            if (input.contains(key))
                return responses.get(key);
        }
        return "Sorry, I didn’t understand that.";
    }

    private void appendUser(String text) {
        chatArea.append("You: " + text + "\n");
    }

    private void appendBot(String text) {
        chatArea.append("Bot: " + text + "\n\n");
    }
}
