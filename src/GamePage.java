import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePage extends JFrame {
    private Game game;
    private JPanel mapPanel;
    private JLabel characterInfoLabel;
    private JLabel actionMessageLabel;
    private Account authenticatedAccount;
    private int enemiesDefeated;

    public GamePage(Game game, Account authenticatedAccount) {
        this.game = game;
        this.enemiesDefeated = 0;
        this.authenticatedAccount = authenticatedAccount;

        if (game.getCurrentCharacter() == null) {
            throw new IllegalStateException("No character selected for the game!");
        }

        setTitle("Game Page");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel leftPanel = new JPanel(new BorderLayout());

        JPanel movementPanel = new JPanel();
        movementPanel.setLayout(new BoxLayout(movementPanel, BoxLayout.Y_AXIS));

        Dimension buttonSize = new Dimension(120, 40);

        // Creez butoanele de direcție
        JButton upButton = new JButton("Go North");
        upButton.setMaximumSize(buttonSize);
        upButton.setPreferredSize(buttonSize);
        upButton.setBackground(Color.ORANGE);
        upButton.setForeground(Color.BLACK);

        JButton leftButton = new JButton("Go West");
        leftButton.setMaximumSize(buttonSize);
        leftButton.setPreferredSize(buttonSize);
        leftButton.setBackground(Color.ORANGE);
        leftButton.setForeground(Color.BLACK);

        JButton rightButton = new JButton("Go East");
        rightButton.setMaximumSize(buttonSize);
        rightButton.setPreferredSize(buttonSize);
        rightButton.setBackground(Color.ORANGE);
        rightButton.setForeground(Color.BLACK);

        JButton downButton = new JButton("Go South");
        downButton.setMaximumSize(buttonSize);
        downButton.setPreferredSize(buttonSize);
        downButton.setBackground(Color.ORANGE);
        downButton.setForeground(Color.BLACK);
        // Quit Button
        JButton quitButton = new JButton("Quit Game");
        quitButton.setMaximumSize(buttonSize);
        quitButton.setPreferredSize(buttonSize);
        quitButton.setBackground(Color.RED);
        quitButton.setForeground(Color.WHITE);
        quitButton.setFont(new Font("Arial", Font.BOLD, 14));

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleQuit();
            }
        });

        movementPanel.add(Box.createVerticalGlue());
        movementPanel.add(upButton);
        movementPanel.add(Box.createVerticalStrut(10));
        movementPanel.add(leftButton);
        movementPanel.add(Box.createVerticalStrut(10));
        movementPanel.add(rightButton);
        movementPanel.add(Box.createVerticalStrut(10));
        movementPanel.add(downButton);
        movementPanel.add(Box.createVerticalStrut(30));
        movementPanel.add(quitButton);
        movementPanel.add(Box.createVerticalGlue());

        upButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        downButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);


        leftPanel.add(movementPanel, BorderLayout.NORTH);

        actionMessageLabel = new JLabel("Welcome to the game!");
        actionMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        actionMessageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(actionMessageLabel, BorderLayout.SOUTH); // Adaugă în partea de jos

        // Adaug detaliile personajului
        characterInfoLabel = new JLabel();
        characterInfoLabel.setVerticalAlignment(SwingConstants.TOP);
        characterInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        characterInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        updateCharacterInfo();
        leftPanel.add(characterInfoLabel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);
        leftPanel.add(Box.createVerticalStrut(20));

        // Adaug harta
        mapPanel = new JPanel();
        mapPanel.setLayout(new GridLayout(game.getMapHeight(), game.getMapWidth()));
        add(mapPanel, BorderLayout.CENTER);

        // Adaug pe harta butoane pentru celule
        for (int row = 0; row < game.getMapHeight(); row++) {
            for (int col = 0; col < game.getMapWidth(); col++) {
                JButton button = new JButton();
                mapPanel.add(button);
            }
        }


        upButton.addActionListener(e -> movePlayer("up", authenticatedAccount));
        downButton.addActionListener(e -> movePlayer("down", authenticatedAccount));
        leftButton.addActionListener(e -> movePlayer("left", authenticatedAccount));
        rightButton.addActionListener(e -> movePlayer("right", authenticatedAccount));

        pack();
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        updateMap();

    }

    private void updateMap() {
        mapPanel.removeAll();
        for (int row = 0; row < game.getMapHeight(); row++) {
            for (int col = 0; col < game.getMapWidth(); col++) {
                Cell cell = game.getCellAt(row, col);
                JButton cellButton = new JButton();

                // Gestionez fiecare tip de celula
                if (cell != null) {
                    if (cell.getType() == CellEntityType.PLAYER) {
                        cellButton.setText("P");
                        game.setPlayerVisited();
                        cellButton.setBackground(Color.BLUE);
                    } else if (cell.isVisited() == true) {
                        cellButton.setText("V");
                    }else {
                        cellButton.setText("N");
                    }
                }

                cellButton.setEnabled(false);
                mapPanel.add(cellButton);
            }
        }
        mapPanel.revalidate();
        mapPanel.repaint();
    }


    private void updateCharacterInfo() {
        Character character = game.getCurrentCharacter();
        if (character == null) {
            throw new IllegalStateException("No character is set in the game!");
        }

        String characterDetails = "<html>" + character.toString().replace("\n", "<br>") + "</html>";
        characterInfoLabel.setText(characterDetails);
    }

    private void movePlayer(String direction, Account authenticatedAccount) {
        JButton cellButton = new JButton();
        try {
            game.setPlayerVisited();
            cellButton.setText("V");
            switch (direction) {
                case "up" -> {
                    CellEntityType action = game.movePlayerNorth();
                    updateMap();
                    startAction(action, authenticatedAccount);
                }
                case "down" -> {
                    CellEntityType action = game.movePlayerSouth();
                    updateMap();
                    startAction(action, authenticatedAccount);
                }
                case "left" -> {
                    CellEntityType action = game.movePlayerWest();
                    updateMap();
                    startAction(action, authenticatedAccount);
                }
                case "right" -> {
                    CellEntityType action = game.movePlayerEast();
                    updateMap();
                    startAction(action, authenticatedAccount);
                }
                default -> System.out.println("Invalid direction!");
            }
            updateCharacterInfo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot move in that direction: " +
                    e.getMessage());
        }
    }

    public void startAction(CellEntityType action, Account authenticatedAccount) {

        switch (action) {
            case ENEMY:
                actionMessageLabel.setText("An enemy appears! Prepare for battle.");
                new BattlePage(game, game.getCurrentCharacter(), new Enemy(100, 100),
                        authenticatedAccount, this);
                break;
            case SANCTUARY:
                String message = game.healAtSanctuary();
                actionMessageLabel.setText(message);
                break;
            case PORTAL:
                int exp = game.activatePortal(authenticatedAccount);
                new FinalPage(game.getCurrentCharacter(), enemiesDefeated, game.getCurrentCharacter().getLevel(),
                        authenticatedAccount);
                actionMessageLabel.setText("You found a portal! Gained " + exp + " experience. Teleporting...");
                updateMap();
                // Astept 2 secunde inainte de a afisa noul mesaj
                Timer timer = new Timer(2000, e -> actionMessageLabel.setText("New Level!"));
                timer.setRepeats(false);
                timer.start();
                break;
            default:
                actionMessageLabel.setText("You are moving through the void.");
                break;
        }
    }

    public void startBattle() {
        Character player = game.getCurrentCharacter();
        Enemy enemy = new Enemy(100, 100);
        new BattlePage(game, player, enemy, authenticatedAccount, this);
    }

    private void handleQuit() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to quit the game?",
                "Confirm Quit",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> new CharacterSelection(game, authenticatedAccount));
            dispose();
        }
    }

    public int getEnemiesDefeated() {
        return this.enemiesDefeated;
    }

    public void setEnemiesDefeated(int enemies) {
        this.enemiesDefeated = enemies;
    }

}
