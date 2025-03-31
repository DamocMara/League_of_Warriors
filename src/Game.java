import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;

public class Game {
    private static Game instance;
    ArrayList<Account> accounts;
    private Grid map;
    private boolean running, fighting;

    private Game() {
        this.accounts = new ArrayList<>();
        this.running = true;
        this.fighting = false;
        loadGameData("./src/accounts.json");
    }

    // Metodă statică pentru a obține instanța unică a clasei
    public static Game getInstance() {
        if (instance == null) { // Inițializare întârziată
            instance = new Game();
        }
        return instance;
    }

    public int getX() {
        return this.map.cell_pos.getX();
    }

    public int getY() {
        return this.map.cell_pos.getY();
    }

    public void addAccount(Account account) {
        if (account != null) {
            accounts.add(account);
            System.out.println("Account added successfully: " + account.getInformation().getName());
        } else {
            System.out.println("Invalid account. Could not add to the list.");
        }
    }

    public void loadGameData(String jsonFilePath) {
        this.accounts = JsonInput.deserializeAccounts(jsonFilePath);
    }

    private void loadSingleAccount(JSONObject accountJson) {
        Account account = JsonInput.deserializeAccount(accountJson);
        if (account != null) {
            this.accounts.add(account);
        }
    }

    public void generate(Account authenticatedAccount) {
        Scanner scanner = new Scanner(System.in);

        ArrayList<Character> characters = authenticatedAccount.getCharacters();
        if (characters == null || characters.isEmpty()) {
            System.out.println("No characters found for the account.");
            return;
        }
        System.out.println("Choose a character to start game:");
        for (int i = 0; i < characters.size(); i++) {
            System.out.println((i + 1) + ". " + characters.get(i).getName());
        }
        int characterChoice = -1;
        while (characterChoice < 1 || characterChoice > characters.size()) {
            System.out.print("Character number: ");
            characterChoice = scanner.nextInt();
            if (characterChoice < 1 || characterChoice > characters.size()) {
                System.out.println("Invalid choice! Please choose a valid number");
            }
        }

        Character selectedCharacter = characters.get(characterChoice - 1);

        System.out.println("You have chosen the character: " + selectedCharacter.getName());

        try {
            Random random = new Random();
            int x = random.nextInt(6) + 5;
            int y = random.nextInt(6) + 5;
            this.map = Grid.generateMap(x, y, selectedCharacter);
            this.map.cell_pos.setType(CellEntityType.PLAYER);
            System.out.println("Start Game!\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare la generarea hărții: " + e.getMessage());
        }
        ;
        startGame(map.cell_pos.getType(), authenticatedAccount);
    }

    public void generate(Account authenticatedAccount, Character selectedCharacter) {
        try {
            Random random = new Random();
            int x = random.nextInt(6) + 5;
            int y = random.nextInt(6) + 5;
            // Pentru testarea folosind scenariul prezentat in tema1, se apelează functia generateHardcoded. Pentru a
            // genera aleator prima harta din joc, putem comenta linia cu generateHardcoded() si decomenta linia cu
            // generateMap()
            this.map = Grid.generateHardcoded(selectedCharacter);
            //this.map = Grid.generateMap(x, y, selectedCharacter); //Pentru a genera aleator prima harta
            this.map.cell_pos.setType(CellEntityType.PLAYER);
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare la generarea hărții: " + e.getMessage());
        }
        ;
    }


    public void startGame(CellEntityType type, Account authenticatedAccount) {
        while (running) {
            try {

            switch (type) {
                case ENEMY:
                    startBattle(new Enemy(100,100), authenticatedAccount);
                    break;
                case SANCTUARY:
                    healAtSanctuary();
                    break;
                case PORTAL:
                    activatePortal(authenticatedAccount);
                    break;
                case VOID:
                    chooseDirection(authenticatedAccount);
                    break;
                default:
                    break;
            }
            type = chooseDirection(authenticatedAccount);
        }catch (QuitGameException e) {
                System.out.println(e.getMessage());
                running = false;
            } catch (Exception e) {
                System.out.println("");
            }
        }

    }

    public CellEntityType chooseDirection(Account authenticatedAccount) throws QuitGameException{
        Scanner scanner = new Scanner(System.in);
        boolean validChoice = false;
        CellEntityType type = null;

        while (!validChoice) {
            System.out.println("Choose a direction of movement:");
            System.out.println("1. Go North");
            System.out.println("2. Go South");
            System.out.println("3. Go East");
            System.out.println("4. Go West");
            System.out.println("5. Quit Game");
            System.out.print("Enter the number of the desired option: ");

            int choice = scanner.nextInt();

            try {
                switch (choice) {
                    case 1:
                        type = map.goNorth();
                        validChoice = true;
                        break;
                    case 2:
                        type = map.goSouth();
                        validChoice = true;
                        break;
                    case 3:
                        type = map.goEast();
                        validChoice = true;
                        break;
                    case 4:
                        type = map.goWest();
                        validChoice = true;
                        break;
                    case 5:
                        System.out.println("Game Ended!");
                        generate(authenticatedAccount);
                        throw new QuitGameException("Game Ended");
                    default:
                        throw new InvalidCommandException("Invalid option! Please choose a valid address.");
                }
            } catch (ImpossibleMoveException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected Error: " + e.getMessage());
            }

            if(type != null)
                startGame(type, authenticatedAccount);
        }
        return type;
    }


    public void generateSpells(Entity character) {
        Random rand = new Random();
        int numAbilities = rand.nextInt(4) + 3;

        character.spells.clear();
        String[] types = {"FIRE", "ICE", "EARTH"};
        ArrayList<String> addedTypes = new ArrayList<>();

        for (int i = 0; i < numAbilities; i++) {
            String type = types[rand.nextInt(3)];
            addedTypes.add(type);

            int manaCost = rand.nextInt(5) + 5;
            int damage = rand.nextInt(15) + 20;

            Spell spell = null;
            int spellType = rand.nextInt(3);
            switch (spellType) {
                case 0:
                    spell = new Fire(damage, manaCost);
                    addedTypes.add("FIRE");
                    break;
                case 1:
                    spell = new Ice(damage, manaCost);
                    addedTypes.add("ICE");
                    break;
                case 2:
                    spell = new Earth(damage, manaCost);
                    addedTypes.add("EARTH");
                    break;
            }
        character.addSpell(spell);
        addedTypes.add(spell.getSpell());
        }

        if (!addedTypes.contains("FIRE")) {
            if(character.spells.size() == 6) {
                character.spells.remove(character.spells.get(character.spells.size() - 1));
            }
            character.addSpell(new Fire(rand.nextInt(15) + 5, rand.nextInt(25) + 10));
        }
        if (!addedTypes.contains("ICE")) {
            if(character.spells.size() == 6) {
                character.spells.remove(character.spells.get(character.spells.size() - 1));
            }
            character.addSpell(new Ice(rand.nextInt(15) + 5, rand.nextInt(25) + 10));
        }
        if (!addedTypes.contains("EARTH")) {
            if(character.spells.size() == 6) {
                character.spells.remove(character.spells.get(character.spells.size() - 1));
            }
            character.addSpell(new Earth(rand.nextInt(15) + 5, rand.nextInt(25) + 10));
        }
    }

    public void startBattle(Enemy enemy, Account account) {
        fighting = true;
        boolean playerTurn = true;
        //System.out.println("\n" + enemy.toString() + "\n\n");
        generateSpells(this.map.character);
        generateSpells(enemy);

        while (this.map.character.getCurrentHealth() > 0 && enemy.getCurrentHealth() > 0) {
            if (playerTurn) {
                chooseAction(enemy, account);
                playerTurn = false;
            } else {
                enemyAction(enemy, account);
                playerTurn = true;
            }
        }

        if (enemy.getCurrentHealth() <= 0) {
            map.getCharacter().levelUp();
            map.getCharacter().setMana(50);
            map.getCharacter().doubleHealth();
            Random random = new Random();
            map.getCharacter().setExperience(random.nextInt(10));
        }
        fighting = false;
    }

    public void enemyAction(Enemy enemy, Account account) {
        Random rand = new Random();
        if (enemy.getCurrentMana() != 0) {
            enemy.useAbility(map.getCharacter());
        } else {
            enemy.performDefaultAttack(map.getCharacter());
            System.out.println("Enemy performed default attack");
        }
        System.out.println(map.getCharacter().toString());
        System.out.println(enemy.toString());
        if(map.getCharacter().getCurrentHealth() == 0){
            System.out.println("Enemy Wins! GAME OVER!");
            generate(account);
        }
    }

    public int activatePortal( Account authenticatedAccount) {
        int exp = this.map.getCharacter().getLevel() * 5;
        this.map.getCharacter().gainExperience(exp);
        this.map.character.setHealth(this.map.character.getMaxHealth());
        this.map.character.setMana((this.map.character.getMaxMana()));
        this.map.character.levelUp();
        Random random = new Random();
        int x = random.nextInt(6) + 5;
        int y = random.nextInt(6) + 5;
        this.map = Grid.generateMap(x, y, this.map.character);
        return exp;
    }


    public String healAtSanctuary() {
        Random rand = new Random();
        int healthBonus = rand.nextInt(20);
        int manaBonus = rand.nextInt(10);
        this.map.character.setHealth(this.map.character.getCurrentHealth() + healthBonus);
        if(this.map.character.currentHealth > this.map.character.getMaxHealth())
            this.map.character.currentHealth -= this.map.character.getCurrentHealth()  % this.map.character.getMaxHealth();
        this.map.character.setMana(this.map.character.getCurrentMana() + manaBonus);
        if(this.map.character.currentMana > this.map.character.getMaxMana())
            this.map.character.currentMana -= this.map.character.getCurrentMana()  % this.map.character.getMaxMana();
        return "You reached Sanctuary! You gained " + healthBonus + " healing points and received " + manaBonus + " mana.";
    }

    public void chooseAction(Enemy enemy, Account account) {
        Scanner scanner = new Scanner(System.in);
        boolean validChoice = false;

        while (!validChoice) {
            System.out.println("Choose an action:");
            System.out.println("1. Default attack");
            System.out.println("2. Use ability");

            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> {
                        map.getCharacter().performDefaultAttack(enemy);
                        validChoice = true;
                        break;
                    }
                    case 2 -> {
                        if (map.getCharacter().spells.size() == 0) {
                            System.out.println("\nNo abilities left to use! Using default attack");
                            map.getCharacter().performDefaultAttack(enemy);
                            validChoice = true;
                            break;
                        }
                        else {
                            System.out.println("\nChoose an ability to use:");
                            for (int i = 0; i < map.getCharacter().spells.size(); i++) {
                                Spell ability = map.getCharacter().spells.get(i);
                                System.out.println((i + 1) + ". " + ability.toString());
                            }

                            int abilityChoice = scanner.nextInt() - 1;
                            if (abilityChoice < 0 || abilityChoice > map.getCharacter().spells.size()) {
                                System.out.println("Invalid choice! Choose a valid ability.");
                                break;
                            }

                            if(enemy.isImmuneToIce() && map.getCharacter().spells.get(abilityChoice).spell == "ICE"){
                                System.out.println("Enemy is immune to this spell!");
                                map.getCharacter().setMana(map.getCharacter().getCurrentMana() - map.getCharacter().spells.get(abilityChoice).mana);
                            } else if(enemy.isImmuneToFire() && map.getCharacter().spells.get(abilityChoice).spell == "FIRE") {
                                System.out.println("Enemy is immune to this spell!");
                                map.getCharacter().setMana(map.getCharacter().getCurrentMana() - map.getCharacter().spells.get(abilityChoice).mana);
                            } else if(enemy.isImmuneToEarth() && map.getCharacter().spells.get(abilityChoice).spell == "EARTH"){
                                System.out.println("Enemy is immune to this spell!");
                                map.getCharacter().setMana(map.getCharacter().getCurrentMana() - map.getCharacter().spells.get(abilityChoice).mana);
                            } else {
                                map.getCharacter().useSpell(abilityChoice, enemy);
                            }
                            validChoice = true;
                        }
                    }
                    default -> System.out.println("Invalid options! Choose a valid option");
                }
                System.out.println(map.getCharacter().toString());
                System.out.println(enemy.toString());

                if (enemy.currentHealth == 0) {
                    map.getCharacter().gainExperience(50);
                    int healAmount = map.getCharacter().getMaxHealth() / 5;
                    int manaRegen = map.getCharacter().getMaxMana() / 5;
                    map.getCharacter().setHealth(Math.min(map.getCharacter().getMaxHealth(), map.getCharacter().getCurrentHealth() + healAmount));
                    map.getCharacter().setMana(Math.min(map.getCharacter().getMaxMana(), map.getCharacter().getCurrentMana() + manaRegen));
                    System.out.println("You Won! You earned 50 experience and gained " + healAmount + " healing points and " + manaRegen + " mana points.");
                    System.out.println(this.map.toString());
                }

                if(map.getCharacter().currentHealth == 0) {
                    System.out.println("Enemy Wins! GAME OVER!");
                    generate(account);
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Enter a valid number.");
                scanner.next();
            }
        }
    }
    public int getMapHeight() {
        return map != null ? map.height : 0; // Returnează 0 dacă harta nu a fost inițializată
    }

    public int getMapWidth() {
        return map != null ? map.width : 0;
    }

    public CellEntityType movePlayerNorth() throws Exception {
        if (map != null) {
            map.cell_pos.setType(CellEntityType.VOID);
            map.cell_pos.setVisited(true);
            Cell previousCell = map.getCell(map.cell_pos.getX(), map.cell_pos.getY());
            CellEntityType prev = map.goNorth();
            previousCell.setType(CellEntityType.VOID);
            return prev;
        }
        return CellEntityType.VOID;
    }


    public CellEntityType movePlayerSouth() throws Exception {
        if (map != null) {
            map.cell_pos.setType(CellEntityType.VOID);
            map.cell_pos.setVisited(true);
            Cell previousCell = map.getCell(map.cell_pos.getX(), map.cell_pos.getY());
            CellEntityType prev = map.goSouth();
            previousCell.setType(CellEntityType.VOID);
            return prev;
        }
        return CellEntityType.VOID;
    }

    public CellEntityType movePlayerEast() throws Exception  {
        if (map != null) {
            map.cell_pos.setType(CellEntityType.VOID);
            map.cell_pos.setVisited(true);
            Cell previousCell = map.getCell(map.cell_pos.getX(), map.cell_pos.getY());
            CellEntityType prev = map.goEast();
            previousCell.setType(CellEntityType.VOID);
            return prev;
        }
        return CellEntityType.VOID;
    }

    public CellEntityType movePlayerWest() throws Exception  {
        if (map != null) {
            map.cell_pos.setType(CellEntityType.VOID);
            map.cell_pos.setVisited(true);
            Cell previousCell = map.getCell(map.cell_pos.getX(), map.cell_pos.getY());
            CellEntityType prev = map.goWest();
            previousCell.setType(CellEntityType.VOID);
            return prev;
        }
        return CellEntityType.VOID;
    }

    public Character getCurrentCharacter() {
        return map != null ? map.getCharacter() : null; // Returnează personajul activ
    }
    public Cell getCellAt(int row, int col) {
        if (map != null && row >= 0 && row < map.height && col >= 0 && col < map.width) {
            return map.getCell(row, col);
        }
        throw new IllegalStateException("Map is not properly initialized or indices are out of bounds");
    }

    public void setCurrentCharacter(Character character) {
        if (map == null || map.getCharacter() == null) {
            initializeMap(character); // Inițializăm harta doar dacă nu este setată
        }
        map.setCharacter(character); // Setăm personajul activ
    }

    private void initializeMap(Character character) {
        if (map == null) {
            map = new Grid(character, new Cell(0, 0, CellEntityType.PLAYER));
        }

        for (int i = 0; i < map.height; i++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int j = 0; j < map.width; j++) {
                row.add(new Cell(i, j, CellEntityType.VOID));
            }
            map.add(row);
        }

        // Setăm poziția inițială a jucătorului
        map.getCell(0, 0).setType(CellEntityType.PLAYER);
    }

    public void setPlayerVisited() {
        if (map != null && map.cell_pos != null) {
            map.cell_pos.setVisited(true); // Marchează celula curentă ca vizitată
            map.cell_pos.setType(CellEntityType.VOID); // Setează tipul celulei ca VOID
        }
    }


}
