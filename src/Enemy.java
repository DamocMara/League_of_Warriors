import java.util.*;

public class Enemy extends Entity {
    public int defaultAttackDamage;
    private boolean immuneToFire;
    private boolean immuneToIce;
    private boolean immuneToEarth;
    public ArrayList<Spell> spells;

    private Spell generateRandomSpell() {
        Random random = new Random();
        int damage = random.nextInt(15, 31);
        int manaCost = random.nextInt(10, 21);

        int spellType = random.nextInt(3);
        switch (spellType) {
            case 0: return new Ice(damage, manaCost);
            case 1: return new Fire(damage, manaCost);
            case 2: return new Earth(damage, manaCost);
            default: throw new IllegalStateException("Unexpected spell type");
        }
    }

    public void setMana(int mana) {
        this.currentMana = mana;
    }

    public void useAbility(Entity target) {
        Spell selectedSpell;
        if (spells.size() != 0) {
            Random random = new Random();
            int idx = random.nextInt(spells.size()) - 1;
            if (currentMana < spells.get(idx).mana) {
                performDefaultAttack(target);
                System.out.println("Enemy performed default attack!");
            } else {
                setMana(getCurrentMana() - spells.get(idx).mana);
                System.out.println("Enemy performed " + spells.get(idx).spell + " spell! Damage: " + spells.get(idx).damage);
                if(spells.get(idx).spell == "FIRE" && target.isImmuneToFire() ||
                    spells.get(idx).spell == "ICE" && target.isImmuneToIce() ||
                    spells.get(idx).spell == "EARTH" && target.isImmuneToEarth())  {
                    System.out.println("You are immune to the attack! No damage applyed");
                } else {
                    target.receiveDamage(spells.get(idx).damage + defaultAttackDamage);
                }
            }

        } else {
            performDefaultAttack(target);
            System.out.println("Enemy performed default attack!");
        }
    }

    public Enemy(int maxHealth, int maxMana) {
        super(maxHealth, maxMana, false, false, false);
        Random random = new Random();

        // Setari aleatoare pentru viața, mana si damage
        this.defaultAttackDamage = random.nextInt(10, 21);
        this.immuneToFire = random.nextBoolean();
        this.immuneToIce = random.nextBoolean();
        this.immuneToEarth = random.nextBoolean();


        // Generez lista de abilitati
        this.spells = new ArrayList<>();
        int numberOfSpells = random.nextInt(3, 7); // Număr între 3 și 6
        for (int i = 0; i < numberOfSpells; i++) {
            Spell spell = generateRandomSpell();
            spells.add(spell);
        }
    }

    @Override
    public String receiveDamage(int damage) {
        Random random = new Random();
        if (random.nextBoolean()) {
            return "Enemy avoided the attack!";
        } else {
            int newHealth = getCurrentHealth() - damage;
            if (newHealth < 0) {
                newHealth = 0;
            }
            int healthLoss = getCurrentHealth() - newHealth;
            regenerateLife(-healthLoss);
            return "Enemy received " + damage + " damage. Current health: " + getCurrentHealth();
        }
    }

    @Override
    public int getDamage() {
        Random random = new Random();
        if (random.nextBoolean()) {
            //System.out.println("Enemy dealt a critical hit!");
            return (int)(defaultAttackDamage * 3/2);
        }
        return defaultAttackDamage;
    }

    public void setHealth(int health) {
        this.currentHealth = health;
    }

    public String ImmunityList() {
        String immunity = "";
        if (immuneToFire) immunity += "Fire ";
        if (immuneToIce) immunity += "Ice ";
        if (immuneToEarth) immunity += "Earth ";
        if (immunity.isEmpty()) immunity = "None";
        return immunity;
    }


    @Override
    public String toString() {
        return "Enemy:\n" +
                "Health: " + getCurrentHealth() + "/" + getMaxHealth() + "\n" +
                "Mana: " + getCurrentMana() + "/" + getMaxMana() + "\n" +
                "Immunities: " + this.ImmunityList();
    }
}
