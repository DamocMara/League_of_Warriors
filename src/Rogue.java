public class Rogue extends Character {
    private boolean Earth_immunity;

    public Rogue(String name, int maxHealth, int maxMana, int strength, int charisma, int dexterity, int level, int experience) {
        super(name, maxHealth, maxMana, strength, charisma, dexterity, level, experience, false, false, true);
    }

    public boolean isEarth_immunity() {
        return Earth_immunity;
    }

    @Override
    public String getType() {
        return "Rouge";
    }
}
