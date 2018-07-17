package fr.vahren.loot.part.terminal;

import fr.vahren.loot.Item;
import fr.vahren.loot.LootGen;

public class Modifier extends Terminal {

    public String value;
    public double factor;

    @Override
    public String gen(Item item, boolean masculine, boolean plural, LootGen lootGen) {
        return this.value;
    }

    @Override
    public void parse(String s) {
        final String[] split = s.split("\\|", 2);
        if (split.length != 2) {
            throw new IllegalArgumentException(s + " is not a valid Modifier");
        }
        this.factor = Double.parseDouble(split[1]);
        this.value = split[0];
    }

}
