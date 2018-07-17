package fr.vahren.loot.part.rule;

import fr.vahren.loot.Item;
import fr.vahren.loot.LootGen;
import fr.vahren.loot.part.Price;

public interface Token {

    static final String vowels = "aeiouyéèàêôîâû";

    String gen(Item item, boolean masculine, boolean plural, boolean vowel, LootGen lootGen, Price p);

}
