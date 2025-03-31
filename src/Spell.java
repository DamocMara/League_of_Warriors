import javax.swing.*;
import java.awt.*;

public abstract class Spell implements Visitor<Entity>{
    public String spell;
    public int damage;
    public int mana;
    public String message;

    public Spell(String spell, int damage, int manaCost) {
        this.spell = spell;
        this.damage = damage;
        this.mana = manaCost;
    }

    public String getSpell() {
        return this.spell;
    }

    public int getDamage() {
        return this.damage;
    }

    public int getManaCost() {
        return this.mana;
    }

    @Override
    public String toString() {
        return "Spell: " + this.spell + " Damage: " + this.damage + " Mana Cost: " + this.mana +"\n";
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public void visit(Entity entity) {
        if (entity.isImmuneToFire() && this instanceof Fire) {
            this.message = "Target is immune to Fire!";
        } else if (entity.isImmuneToIce() && this instanceof Ice) {
           this. message = "Target is immune to Ice!";
        } else if (entity.isImmuneToEarth() && this instanceof Earth) {
            this.message = "Target is immune to Earth!";
        } else {
            int damageDealt = this.damage;
            entity.receiveDamage(damageDealt);
            this.message = "Applied " + this.spell + " causing " + damageDealt + " damage.";
        }
    }

}
