import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class BattlePage extends JFrame {
    private JLabel playerImageLabel;
    private JLabel enemyImageLabel;
    private JTextArea battleLog;
    private JLabel playerInfoLabel;
    private JLabel enemyInfoLabel;
    private Game game;
    private boolean fighting;
    private boolean playerTurn;
    private Account authenticatedAccount;
    private GamePage gamepage;

    public BattlePage(Game game, Character player, Enemy enemy, Account authenticatedAccount, GamePage gamepage) {
        this.game = game;
        this.authenticatedAccount = authenticatedAccount;
        this.gamepage = gamepage;

        setTitle("Battle Page. Fight!");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel pentru imagini și detalii
        JPanel imagesPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        imagesPanel.setPreferredSize(new Dimension(800, 600)); // Dimensiunea panelului de imagini

        // Imaginea și informațiile pentru jucător
        ImageIcon playerIcon = new ImageIcon("./src/rouge.png");
        if(player instanceof Warrior) {
            playerIcon = new ImageIcon("./src/warrior.png");
        } else if(player instanceof Mage) {
            playerIcon = new ImageIcon("./src/mage.png");
        }

        playerImageLabel = new JLabel(playerIcon);

        playerInfoLabel = new JLabel(formatCharacterDetails(player));
        playerInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPanel.add(playerImageLabel, BorderLayout.CENTER);
        playerPanel.add(playerInfoLabel, BorderLayout.SOUTH);

        // Imaginea și informațiile pentru inamic
        ImageIcon enemyIcon = new ImageIcon("./src/enemy.png");
        enemyImageLabel = new JLabel(enemyIcon);


        enemyInfoLabel = new JLabel(formatEnemyDetails(enemy));
        enemyInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel enemyPanel = new JPanel(new BorderLayout());
        enemyPanel.add(enemyImageLabel, BorderLayout.NORTH);
        enemyPanel.add(enemyInfoLabel, BorderLayout.SOUTH);

        // Adaugă panelurile în `imagesPanel`
        imagesPanel.add(playerPanel);
        imagesPanel.add(enemyPanel);

        // Adaugă imagini și detalii în partea de sus
        add(imagesPanel, BorderLayout.NORTH);

        // Center Panel: Battle Log
        battleLog = new JTextArea();
        battleLog.setEditable(false);
        battleLog.setLineWrap(true);
        battleLog.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(battleLog);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel: Buttons for Actions
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton defaultAttackButton = new JButton("Default Attack");
        JButton abilityButton = new JButton("Ability");

        playerTurn = true;

        // Add actions for buttons
        defaultAttackButton.addActionListener(e -> performDefaultAttack(player, enemy));
        abilityButton.addActionListener(e -> useAbility(player, enemy));

        bottomPanel.add(defaultAttackButton);
        bottomPanel.add(abilityButton);

        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        // Setăm dimensiunea ferestrei
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setVisible(true);
        game.generateSpells(player);
        game.generateSpells(enemy);
    }


    private String formatCharacterDetails(Character character) {
        return "<html>" + character.toString().replace("\n", "<br>") + "</html>";
    }

    private String formatEnemyDetails(Enemy enemy) {
        return "<html>" + enemy.toString().replace("\n", "<br>") + "</html>";
    }

    private void handlePlayerAction(String actionType,Character player, Enemy enemy) {
        if (!playerTurn) return; // Ignoră acțiunile dacă nu este rândul jucătorului

        if (actionType.equals("ability") && player.spells.size() > 0 && player.getCurrentMana() > 0) {
            useAbility(player, enemy);
        } else if (actionType.equals("default")) {
            performDefaultAttack(player, enemy);
        }

        if (enemy.getCurrentHealth() > 0) {
            playerTurn = false;
            SwingUtilities.invokeLater(() -> enemyTurn(player, enemy));
        }
    }

    private void performDefaultAttack(Character player, Enemy enemy) {
        int baseDamage = (int)(player.getStrength() * 0.2);
        if (player.getDexterity() + player.getCharisma() + player.getStrength() < 150) {
            baseDamage *= 2;
        }
        DamageVisitor visitor = new DamageVisitor(baseDamage);
        // Enemy acceptă vizitatorul pentru a aplica damage
        enemy.accept(visitor);
        // Afișăm mesajul generat de vizitator
        String message = visitor.getMessage();
        battleLog.setText("Player attacks:\n" + message + "\n");
        player.setMana(player.getCurrentMana() - (int)(baseDamage / 3));
        updateDetails(player, enemy);
        checkBattleState(player, enemy);
        if (enemy.getCurrentHealth() > 0) {
            enemyTurn(player, enemy);
        }
    }

    private void useAbility(Character player, Enemy enemy) {
        // Open the ShowSpells page for spell selection
        new ShowSpells(player, e -> {
            if (e.getSource() instanceof Spell selectedSpell) {
                if(player.getCurrentMana() < selectedSpell.mana) {
                    performDefaultAttack(player, enemy);
                    battleLog.setText("Not enough mana! Using default attack...\n");
                } else {
                    // Use the selected spell in the battle
                    player.setMana(player.getCurrentMana() - selectedSpell.mana);

                    if (player.getDexterity() + player.getCharisma() + player.getStrength() < 150) {
                        selectedSpell.damage *= 2;
                    }
                    // Use the spell
                    selectedSpell.visit(enemy);
                    player.spells.remove(selectedSpell);
                    // Retrieve the message and append to battleLog
                    String spellMessage = selectedSpell.getMessage();
                    battleLog.setText(spellMessage + "\n");
                }


                updateDetails(player, enemy);
                checkBattleState(player, enemy);

                if (enemy.getCurrentHealth() > 0) {
                    playerTurn = false; // Set turn to enemy
                    SwingUtilities.invokeLater(() -> enemyTurn(player, enemy));
                }
            }
        });
    }


    private void enemyTurn(Character player, Enemy enemy) {

        Timer timer = new Timer(2000, e -> {
            int damage;
            battleLog.setText("Enemy's turn: ");
            if(enemy.getCurrentMana() <= 0 || enemy.spells.size() <= 0) {
                damage = enemy.getDamage();
                Random random = new Random();
                if(random.nextBoolean()) {  //Sansa de 50% sa dea damage dublu
                    damage = damage * 2;
                }
                DamageVisitor visitor = new DamageVisitor(damage);
                player.accept(visitor);
                enemy.setMana(enemy.getCurrentMana() - enemy.defaultAttackDamage);
            } else {
                Random random = new Random();
                int idx = random.nextInt(enemy.spells.size());
                enemy.setMana(enemy.getCurrentMana() - enemy.spells.get(idx).mana);
                enemy.spells.get(idx).visit(player);
                damage = enemy.spells.get(idx).damage;
                DamageVisitor visitor = new DamageVisitor(damage);
                player.accept(visitor);
                String spellMessage = enemy.spells.get(idx).getMessage();
                battleLog.append(spellMessage + "\n Your turn!\n");
                enemy.spells.remove(enemy.spells.get(idx));
            }
            updateDetails(player, enemy);
            checkBattleState(player, enemy);
        });
        timer.setRepeats(false);
        timer.start();
}

    private void updateDetails(Character player, Enemy enemy) {
        playerInfoLabel.setText(formatCharacterDetails(player));
        enemyInfoLabel.setText(formatEnemyDetails(enemy)); // Actualizează detaliile
    }

    private void checkBattleState(Character player, Enemy enemy) {
        if (player.getCurrentHealth() <= 0) {
            battleLog.setText("You were defeated!\n");
            JOptionPane.showMessageDialog(this, "GAME OVER! Returning to the main menu.");
            gamepage.dispose();
            SwingUtilities.invokeLater(() -> new CharacterSelection(game, authenticatedAccount));
            dispose();
        } else if (enemy.getCurrentHealth() <= 0) {
            battleLog.setText("Enemy defeated!\n");
            gamepage.setEnemiesDefeated(gamepage.getEnemiesDefeated() + 1);
            JOptionPane.showMessageDialog(this, "You won the battle!");
            player.setHealth(player.getCurrentHealth() * 2);
            if(player.getCurrentHealth() > player.getCurrentHealth()) {
                player.setHealth(player.getMaxHealth());
            }
            player.setMana(player.getMaxMana());
            Random random = new Random();
            int exp = random.nextInt(5) + 5;
            player.setExperience(player.getExperience() + exp);
            updateDetails(player, enemy);
            dispose();
        }
    }

    private void useSelectedSpell(Character player, Enemy enemy, Spell spell) {
        if (player.getCurrentMana() >= spell.getManaCost()) {
            DamageVisitor visitor = new DamageVisitor(spell.getDamage());
            enemy.accept(visitor);
            player.setMana(player.getCurrentMana() - spell.getManaCost());
            battleLog.append("Player used " + spell.getSpell() + " for " + spell.getDamage() + " damage!\n");
        } else {
            battleLog.append("Not enough mana to use " + spell.getSpell() + "!\n");
        }

        updateDetails(player, enemy);
        checkBattleState(player, enemy);
        if (enemy.getCurrentHealth() > 0) {
            playerTurn = false;
            SwingUtilities.invokeLater(() -> enemyTurn(player, enemy));
        }
    }

}
