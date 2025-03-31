import java.util.ArrayList;
import java.util.List;

public abstract class Entity implements Element<Entity>{
    public List<Spell> spells;
    public int currentHealth;
    private int maxHealth;
    public int currentMana;
    private int maxMana;
    private boolean immuneToFire;
    private boolean immuneToIce;
    private boolean immuneToEarth;

    public Entity(int maxHealth, int maxMana, boolean immuneToFire, boolean immuneToIce, boolean immuneToEarth) {
        this.spells = new ArrayList<>();
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.maxMana = maxMana;
        this.currentMana = maxMana;
        this.immuneToFire = immuneToFire;
        this.immuneToIce = immuneToIce;
        this.immuneToEarth = immuneToEarth;
    }

    public void regenerateLife(int value) {
        if(this.currentHealth + value < this.maxHealth)
            this.currentHealth = this.currentHealth + value;
        else
            this.currentHealth = this.maxHealth;
    }

    public void regenerateMana(int value) {
        if(this.currentMana + value < this.maxMana)
            this.currentMana = this.currentMana + value;
        else
            this.currentMana = this.maxMana;
    }

    public void addSpell(Spell spell) {
        spells.add(spell);
    }

    public void useSpell(int idx, Entity target) {

        Spell chosenSpell = spells.get(idx);
        if (currentMana < chosenSpell.getManaCost()) {
            System.out.println("Not enough mana! Using default attack.");
            performDefaultAttack(target);
            return;
        }
        boolean isImmune = (chosenSpell instanceof Fire && target.isImmuneToFire()) ||
                (chosenSpell instanceof Ice && target.isImmuneToIce()) ||
                (chosenSpell instanceof Earth && target.isImmuneToEarth());

        if (isImmune) {
            System.out.println("The target is immune to this spell!");
            currentMana -= chosenSpell.getManaCost();
            spells.remove(idx);
            return;
        }
        target.receiveDamage(chosenSpell.getDamage());
        currentMana -= chosenSpell.mana;
        spells.remove(idx);
        System.out.println("Used ability: " + chosenSpell);
    }

    public void performDefaultAttack(Entity target) {
        int defaultDamage = getDamage();
        String ret = target.receiveDamage(defaultDamage);
    }

    public abstract String receiveDamage(int damage);
    public abstract int getDamage();

    public int getCurrentHealth() {
        return this.currentHealth;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    public int getCurrentMana() {
        return this.currentMana;
    }

    public int getMaxMana() {
        return this.maxMana;
    }

    public String getImmunity() {
        StringBuilder immunity = new StringBuilder();

        if (immuneToFire) {
            immunity.append("Fire ");
        }
        if (immuneToIce) {
            immunity.append("Ice ");
        }
        if (immuneToEarth) {
            immunity.append("Earth ");
        }

        if (immunity.length() == 0) {
            return "None";
        }

        return immunity.toString().trim();
    }

    public boolean isImmuneToFire() {
        return this.immuneToFire;
    }

    public boolean isImmuneToIce() {
        return this.immuneToIce;
    }

    public boolean isImmuneToEarth() {
        return this.immuneToEarth;
    }

    public void setMana(int mana) {
        this.currentMana = mana;
    }

    @Override
    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);
    }
}
