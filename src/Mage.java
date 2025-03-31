public class Mage extends Character {
    private boolean Ice_immunity;

    // Constructor
    public Mage(String name, int maxHealth, int maxMana, int strength, int charisma, int dexterity, int level, int experience) {
        super(name, maxHealth, maxMana, strength, charisma, dexterity, level, experience, false, true, false);
    }

    public boolean isIce_immunity() {
        return Ice_immunity;
    }
    @Override
    public String getType() {
        return "Mage";
    }
}
