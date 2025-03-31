public class Cell {
    private int x, y;
    private CellEntityType type;
    private boolean visited;

    public Cell(int x, int y, CellEntityType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.visited = false;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public CellEntityType getType() {
        return this.type;
    }

    public boolean isVisited() {
        return this.visited;
    }

    public void setVisited(boolean vis) {
        this.visited = vis;
    }

    public void setType(CellEntityType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Cell{ x=" + this.x + ", y=" + this.y + ", type=" + this.type + ", visited=" + this.visited + "}";
    }
}
