import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowSpells extends JFrame {

    public ShowSpells(Character player, ActionListener spellActionListener) {
        setTitle("Select a Spell");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel spellsPanel = new JPanel();
        spellsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        // Iteram prin spells
        for (Spell spell : player.spells) {
            JPanel spellPanel = new JPanel();
            spellPanel.setLayout(new BorderLayout(5, 5));
            spellPanel.setPreferredSize(new Dimension(180, 260));
            spellPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            // Adaug imagini pentru fiecare tip de spell
            JLabel spellImage = new JLabel();
            ImageIcon icon;
            if (spell instanceof Ice) {
                icon = new ImageIcon("./src/ice.png");
            } else if (spell instanceof Fire) {
                icon = new ImageIcon("./src/fire.png");
            } else {
                icon = new ImageIcon("./src/earth.png");
            }
            Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            spellImage.setIcon(new ImageIcon(scaledImage));
            spellPanel.add(spellImage, BorderLayout.CENTER);

            // Adaug detalii despre fiecare spell
            JLabel spellDetails = new JLabel("<html>" +
                    "Name: " + spell.getSpell() + "<br>" +
                    "Damage: " + spell.getDamage() + "<br>" +
                    "Mana: " + spell.mana +
                    "</html>", SwingConstants.CENTER);
            spellDetails.setFont(new Font("Arial", Font.PLAIN, 12));
            spellPanel.add(spellDetails, BorderLayout.SOUTH);

            // Adaug buton de selectie
            JButton selectButton = new JButton("Select");
            selectButton.addActionListener(e -> {
                spellActionListener.actionPerformed(new ActionEvent(spell, ActionEvent.ACTION_PERFORMED, "SelectSpell"));
                dispose(); // Close the spell selection window
            });
            spellPanel.add(selectButton, BorderLayout.NORTH);

            spellsPanel.add(spellPanel);
        }

        JScrollPane scrollPane = new JScrollPane(spellsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane);
        setVisible(true);
    }
}
