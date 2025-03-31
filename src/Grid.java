import java.util.ArrayList;
import java.util.*;

public class Grid extends ArrayList<ArrayList<Cell> >{
    public int width;
    public int height;
    public Character character;
    public Cell cell_pos;

    public Grid(Character main, Cell cell) {
        this.width = 5;
        this.height = 5;
        this.character = main;
        this.cell_pos = cell;
    }

    public Grid(int wid, int high, Character main, Cell cell) {
        this.width = wid;
        this.height = high;
        this.character = main;
        this.cell_pos = cell;
        initGrid();
    }
    public static Grid generateMap(int height, int width, Character character) {
        if (height > 10 || width > 10) {
            throw new IllegalArgumentException("Maximum dimension is 10x10!");
        }

        // Inițializăm o grilă temporară pentru construcția celulelor
        Grid tempGrid = new Grid(width, height, character, null);
        Random random = new Random();

        // Plasăm cele două sanctuare
        for (int i = 0; i < 2; i++) {
            int x = random.nextInt(height);
            int y = random.nextInt(width);
            while (tempGrid.get(x).get(y).getType() != CellEntityType.VOID) {
                x = random.nextInt(height);
                y = random.nextInt(width);
            }
            tempGrid.get(x).get(y).setType(CellEntityType.SANCTUARY);
        }

        // Plasăm cei patru inamici
        for (int i = 0; i < 4; i++) {
            int x = random.nextInt(height);
            int y = random.nextInt(width);
            while (tempGrid.get(x).get(y).getType() != CellEntityType.VOID) {
                x = random.nextInt(height);
                y = random.nextInt(width);
            }
            tempGrid.get(x).get(y).setType(CellEntityType.ENEMY);
        }

        int x = random.nextInt(height);
        int y = random.nextInt(width);
        // Plasăm portalul
        while (tempGrid.get(x).get(y).getType() != CellEntityType.VOID) {
            x = random.nextInt(height);
            y = random.nextInt(width);
        }
        tempGrid.get(x).get(y).setType(CellEntityType.PORTAL);

        // Plasăm jucătorul pe o celulă de tip VOID
        while (true) {
            x = random.nextInt(height);
            y = random.nextInt(width);
            if (tempGrid.get(x).get(y).getType() == CellEntityType.VOID) {
                tempGrid.cell_pos = new Cell(x, y, CellEntityType.PLAYER);
                tempGrid.setPlayer(x, y);
                break;
            }
        }

        return tempGrid;
    }

    public static Grid generateHardcoded(Character character) {

        // Inițializăm o grilă temporară pentru construcția celulelor
        Grid tempGrid = new Grid(5, 5, character, null);
        Random random = new Random();

        // Populează grila cu entități specifice
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i == 0 && j == 0) {
                    tempGrid.get(i).get(j).setType(CellEntityType.PLAYER);
                    tempGrid.cell_pos = new Cell(i, j, CellEntityType.PLAYER);
                    tempGrid.setPlayer(i, j);
                } else if (i == 3 && j == 4) {
                    tempGrid.get(i).get(j).setType(CellEntityType.ENEMY);
                } else if ((i == 0 && j == 3) || (i == 1 && j == 3) || (i == 4 && j == 3) || (i == 2 && j == 0)) {
                    tempGrid.get(i).get(j).setType(CellEntityType.SANCTUARY);
                } else if (i == 4 && j == 4) {
                    tempGrid.get(i).get(j).setType(CellEntityType.PORTAL);
                } else {
                    tempGrid.get(i).get(j).setType(CellEntityType.VOID);
                }
            }
        }

        return tempGrid;
    }

    public void setPlayer(int x, int y) {
        cell_pos.setType(CellEntityType.VOID);
        cell_pos.setX(x);
        cell_pos.setY(y);
        cell_pos = get(x).get(y);
        cell_pos.setType(CellEntityType.PLAYER);
        cell_pos.setVisited(true);

    }

    private void initGrid() {
        for (int i = 0; i < this.height; i++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int j = 0; j < this.width; j++) {
                row.add(new Cell(i, j, CellEntityType.VOID));
            }
            this.add(row);
        }
    }

    public void placeCharacterOnEmptyCell(Character character) {
        Random random = new Random();
        while (true) {
            int row = random.nextInt(height);
            int col = random.nextInt(width);

            if (this.get(row).get(col).getType() == CellEntityType.VOID) {
                Cell targetCell = this.get(row).get(col);
                targetCell.setType(CellEntityType.PLAYER);
                this.character = character;
                this.cell_pos = targetCell;
                System.out.println("Personajul a fost poziționat pe celula (" + row + ", " + col + ").");
                break;
            }
        }
    }


    public CellEntityType goNorth() throws Exception {
        int newX = cell_pos.getX() - 1;
        int newY = cell_pos.getY();

        if (newX < 0) {
            throw new ImpossibleMoveException("Cannot move north!");
        }

        // Celula curentă devine vizitată și `VOID`
        cell_pos.setVisited(true);
        cell_pos.setType(CellEntityType.VOID);


        // Mută jucătorul în celula nouă
        Cell newCell = getCell(newX, newY);
        CellEntityType prev = newCell.getType();
        cell_pos = newCell;
        cell_pos.setType(CellEntityType.PLAYER);

        return prev;
    }


    public CellEntityType goSouth() throws Exception {
        int newX = cell_pos.getX() + 1;
        int newY = cell_pos.getY();

        if (newX > this.height) {
            throw new ImpossibleMoveException("Cannot move south!");
        }

        // Celula curentă devine vizitată și `VOID`
        cell_pos.setVisited(true);
        cell_pos.setType(CellEntityType.VOID);

        // Mută jucătorul în celula nouă
        Cell newCell = getCell(newX, newY);
        CellEntityType prev = newCell.getType();
        cell_pos = newCell;
        cell_pos.setType(CellEntityType.PLAYER);

        return prev;
    }


    public CellEntityType goEast() throws Exception {
        int newX = cell_pos.getX();
        int newY = cell_pos.getY() + 1;

        if (newY > this.width) {
            throw new ImpossibleMoveException("Cannot move East!");
        }

        // Celula curentă devine vizitată și `VOID`
        cell_pos.setVisited(true);
        cell_pos.setType(CellEntityType.VOID);

        // Mută jucătorul în celula nouă
        Cell newCell = getCell(newX, newY);
        CellEntityType prev = newCell.getType();
        cell_pos = newCell;
        cell_pos.setType(CellEntityType.PLAYER);

        return prev;
    }


    public CellEntityType goWest() throws Exception {
        int newX = cell_pos.getX();
        int newY = cell_pos.getY() - 1;

        if (newY < 0) {
            throw new ImpossibleMoveException("Cannot move West!");
        }

        // Celula curentă devine vizitată și `VOID`
        cell_pos.setVisited(true);
        cell_pos.setType(CellEntityType.VOID);

        // Mută jucătorul în celula nouă
        Cell newCell = getCell(newX, newY);
        CellEntityType prev = newCell.getType();
        cell_pos = newCell;
        cell_pos.setType(CellEntityType.PLAYER);

        return prev;
    }


    public int getheight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public Character getCharacter() {
        return this.character;
    }

    public Cell getCell_pos() {
        return this.cell_pos;
    }

    public void setCharacter(Character character) {
        this.character = character;
        this.cell_pos.setType(CellEntityType.PLAYER);
    }

    @Override
    public String toString() {
        StringBuilder mapRepresentation = new StringBuilder();

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                Cell cell = this.get(i).get(j);
                if(cell.getType() == CellEntityType.PLAYER) {
                    mapRepresentation.append(" P ");
                } else{
                    if(cell.getType() == CellEntityType.ENEMY){
                        mapRepresentation.append(" E ");
                    }
                    if(cell.getType() == CellEntityType.PORTAL){
                        mapRepresentation.append(" F ");
                    }
                    if(cell.getType() == CellEntityType.SANCTUARY){
                        mapRepresentation.append(" S ");
                    }
                }
            }
            mapRepresentation.append("\n");
        }
        return mapRepresentation.toString();
    }

    public Cell getCell(int row, int col) {
        if (row >= 0 && row < height && col >= 0 && col < width) {
            return this.get(row).get(col); // Accesăm rândul și coloana specificată
        }
        throw new IndexOutOfBoundsException("Invalid cell coordinates");
    }

}
