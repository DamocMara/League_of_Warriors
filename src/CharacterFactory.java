public class CharacterFactory {
    public static Character createCharacter(String type, String name, int maxHealth, int maxMana, int strength,
                                            int charisma, int dexterity, int level, int experience) {
        switch (type.toLowerCase()) {
            case "warrior":
                return new Warrior(name, maxHealth, maxMana, strength, charisma, dexterity, level, experience);
            case "mage":
                return new Mage(name, maxHealth, maxMana, strength, charisma, dexterity, level, experience);
            case "rogue":
                return new Rogue(name, maxHealth, maxMana, strength, charisma, dexterity, level, experience);
            default:
                throw new IllegalArgumentException("Unknown character type: " + type);
        }
    }
}