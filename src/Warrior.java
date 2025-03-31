public class Warrior extends Character{
    private boolean Fire_immunity;

    public Warrior(String name, int maxHealth, int maxMana, int strength, int charisma, int dexterity, int level, int experience) {
        super(name, maxHealth, maxMana, strength, charisma, dexterity, level, experience, true, false, false);
    }

    public boolean getImmuneToFire() {
        return Fire_immunity;
    }
    @Override
    public String getType() {
        return "Warrior";
    }

}
