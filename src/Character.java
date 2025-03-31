
import java.util.Random;

public abstract class Character extends Entity{
    private String name;
    private int experience;
    private int level;
    private int strength;
    private int charisma;
    private int dexterity;
    public String Immunity;

    public Character(String name, int maxHealth, int maxMana, int strength, int charisma, int dexterity, int level, int experience,boolean immuneToFire, boolean immuneToIce, boolean immuneToEarth) {
        super(maxHealth, maxMana, immuneToFire, immuneToIce, immuneToEarth);
        this.name = name;
        this.experience = experience;
        this.level = level;
        this.strength = strength;
        this.charisma = charisma;
        this.dexterity = dexterity;
    }

    public void levelUp() {
        this.level++;
        if (this instanceof Warrior) {

            this.strength += 2 * level;
            this.charisma += level;
            this.dexterity += level;

        }else if (this instanceof Mage) {

            this.strength += level;
            this.charisma += 2 * level;
            this.dexterity += level;

        }else if (this instanceof Rogue) {

            this.strength += level;
            this.charisma += level;
            this.dexterity += 2 * level;
        }
    }


    public void gainExperience (int experience) {
        this.experience += experience;
        System.out.println(name + " gained " + experience + " experience!\n");
        if (this.experience >= 100) {
            levelUp();
        }
    }

    @Override
    public String receiveDamage(int damage) {
        Random random = new Random();
        if ((this.dexterity + this.charisma + this.strength) > 150) { // Daca suma atributelor este mai mare decat jumatate din total, se evita damage
            return "You avoided the attack!";
        } else {
            int newHealth = getCurrentHealth() - damage;
            if (newHealth < 0) {
                newHealth = 0;
            }
            this.setHealth(newHealth);
            return "You received " + damage + " damage. Current health: " + getCurrentHealth();
        }
    }



    @Override
    public int getDamage() {
        int baseDamage = (int)(strength * 0.2);
        if (Math.random() < dexterity / 3.0) {
            baseDamage *= 2;
            //System.out.println(name + " dealt a critical hit!");
        }
        return baseDamage;
    }

    public String getName() {
        return this.name;
    }

    public int getExperience() {
        return this.experience;
    }

    public int getLevel() {
        return this.level;
    }

    public int getStrength() {
        return this.strength;
    }

    public int getCharisma() {
        return this.charisma;
    }

    public int getDexterity() {
        return this.dexterity;
    }

    public void setHealth(int health) {
        this.currentHealth = health;
    }

    public void setMana(int mana) {
        this.currentMana = mana;
    }

    public void setExperience(int exp) {
        this.experience = exp;
    }

    public void doubleHealth() {
        this.currentHealth = this.currentHealth * 2;
        if(this.currentHealth > this.getMaxHealth())
            this.currentHealth = this.getMaxHealth();
    }

    @Override
    public String toString() {
        return "Name: " + name + "\n" +
                "Level: " + level + "\n" +
                "Experience: " + experience + "\n" +
                "Health: " + getCurrentHealth() + "/" + getMaxHealth() + "\n" +
                "Mana: " + getCurrentMana() + "/" + getMaxMana() + "\n" +
                "Strength: " + strength + "\n" +
                "Charisma: " + charisma + "\n" +
                "Dexterity: " + dexterity + "\n" +
                "Immunities: " + getImmunity();
    }

    public void setLevel(int level) {
        this.level = level;
    }
    public abstract String getType();

}
