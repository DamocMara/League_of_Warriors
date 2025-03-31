public class DamageVisitor implements Visitor<Entity> {
    private int damage;
    private int mana;
    private String message;

    public DamageVisitor(int damage) {
        this.damage = damage;
        this.message = "";
    }

    @Override
    public void visit(Entity to) {
        if (to != null) {
            message = to.receiveDamage(damage);
        }
    }

    public String getMessage() {
        return message;
    }
}
