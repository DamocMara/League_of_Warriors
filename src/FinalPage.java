import javax.swing.*;
import java.awt.*;

public class FinalPage extends JFrame {

    public FinalPage(Character character, int enemiesDefeated, int levelsCompleted, Account authenticatedAccount) {
        setTitle("Level Completed!");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Progress Details Panel
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        progressPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Adaug poza si detaliile playerului
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String characterImagePath = "./src/rouge.png";
        if (character instanceof Warrior) {
            characterImagePath = "./src/warrior.png";
        } else if (character instanceof Mage) {
            characterImagePath = "./src/mage.png";
        }

        ImageIcon characterIcon = new ImageIcon(characterImagePath);
        JLabel characterImageLabel = new JLabel(characterIcon);
        characterImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(characterImageLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        JLabel characterNameLabel = new JLabel("Character Name: " + character.getName());
        JLabel levelLabel = new JLabel("Level Reached: " + character.getLevel());
        JLabel experienceLabel = new JLabel("Total Experience: " + character.getExperience());
        JLabel enemiesDefeatedLabel = new JLabel("Enemies Defeated: " + enemiesDefeated);

        Font detailsFont = new Font("Arial", Font.PLAIN, 18);

        characterNameLabel.setFont(detailsFont);
        levelLabel.setFont(detailsFont);
        experienceLabel.setFont(detailsFont);
        enemiesDefeatedLabel.setFont(detailsFont);

        progressPanel.add(characterNameLabel);
        progressPanel.add(Box.createVerticalStrut(10)); // Spacer
        progressPanel.add(levelLabel);
        progressPanel.add(Box.createVerticalStrut(10));
        progressPanel.add(experienceLabel);
        progressPanel.add(Box.createVerticalStrut(10));
        progressPanel.add(enemiesDefeatedLabel);
        add(progressPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        //Buton de exit
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 16));

        exitButton.addActionListener(e -> {
            dispose();
        });
        buttonPanel.add(exitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

}
