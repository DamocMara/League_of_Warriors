import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CharacterSelection extends JFrame {
    private Game game;
    private Account authenticatedAccount;

    public CharacterSelection(Game game, Account authenticatedAccount) {
        this.game = game;
        this.authenticatedAccount = authenticatedAccount;

        setTitle("Select a Character");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color gradientStart = new Color(70, 130, 180);
                Color gradientEnd = new Color(240, 248, 255);
                GradientPaint gradient = new GradientPaint(0, 0, gradientStart, 0, getHeight(), gradientEnd);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout(20, 20));
        add(backgroundPanel);

        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Select a Character to Start", SwingConstants.CENTER);
        title.setFont(new Font("Times New Roman", Font.BOLD, 15));
        title.setForeground(Color.MAGENTA);
        backgroundPanel.add(title, BorderLayout.NORTH);

        ArrayList<Character> characters = authenticatedAccount.getCharacters();

        // Lista cu personaje
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> characterList = new JList<>(listModel);
        characterList.setFont(new Font("Arial", Font.PLAIN, 12));
        characterList.setBackground(new Color(255, 255, 255, 200));
        characterList.setSelectionBackground(new Color(100, 149, 237));
        characterList.setSelectionForeground(Color.WHITE);

        if (characters != null) {
            for (Character character : characters) {
                String prof;
                if (character instanceof Warrior) {
                    prof = "Warrior";
                } else if (character instanceof Mage) {
                    prof = "Mage";
                } else {
                    prof = "Rouge";
                }
                listModel.addElement(character.getName() + ": " + prof);
            }
        } else {
            listModel.addElement("No characters available.");
        }

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(characterList);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,
                2), "Characters", 0, 0, new Font("Arial", Font.BOLD,
                14), Color.DARK_GRAY));
        centerPanel.add(scrollPane);

        // Zona cu detaliile pentru fiecare personaj inainte de selectie
        JLabel descriptionLabel = new JLabel();
        descriptionLabel.setVerticalAlignment(SwingConstants.TOP);
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descriptionLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        descriptionLabel.setOpaque(false);
        descriptionLabel.setForeground(Color.DARK_GRAY);
        panel.add(descriptionLabel, BorderLayout.EAST);

        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,
                2), "Details", 0, 0, new Font("Arial", Font.BOLD,
                14), Color.DARK_GRAY));
        centerPanel.add(descriptionPanel);
        backgroundPanel.add(centerPanel, BorderLayout.CENTER);

        // Afisez detaliile atunci cand se apasa pe un personaj
        characterList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && characters != null) {
                int selectedIndex = characterList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    Character selectedCharacter = characters.get(selectedIndex);
                    descriptionLabel.setText("<html><strong>" + selectedCharacter.getName() + "</strong><br>"
                            + "Level: " + selectedCharacter.getLevel() + "<br>"
                            + "Strength: " + selectedCharacter.getStrength() + "<br>"
                            + "Charisma: " + selectedCharacter.getCharisma() + "<br>"
                            + "Dexterity: " + selectedCharacter.getDexterity() + "</html>");
                }
            }
        });

        panel.add(scrollPane, BorderLayout.CENTER);

        // Butonul de selectie
        JButton selectButton = new JButton("Select");
        selectButton.setFont(new Font("Arial", Font.BOLD, 14));
        selectButton.setBackground(new Color(50, 205, 50));
        selectButton.setForeground(Color.WHITE);
        selectButton.setFocusPainted(false);
        selectButton.setBorder(BorderFactory.createRaisedBevelBorder());
        selectButton.setPreferredSize(new Dimension(120, 40));
        panel.add(selectButton, BorderLayout.SOUTH);

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = characterList.getSelectedIndex();
                if (selectedIndex >= 0 && characters != null && !characters.isEmpty()) {
                    Character selectedCharacter = characters.get(selectedIndex);
                    game.generate(authenticatedAccount, selectedCharacter);
                    game.setCurrentCharacter(selectedCharacter); // Setăm personajul curent
                    dispose(); // Închidem fereastra curentă
                    //System.out.println(authenticatedAccount.toString());
                    new GamePage(game, authenticatedAccount); // Deschidem pagina de joc
                } else {
                    JOptionPane.showMessageDialog(
                            CharacterSelection.this,
                            "Please select a character.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        add(panel);
        setVisible(true);
    }
}
