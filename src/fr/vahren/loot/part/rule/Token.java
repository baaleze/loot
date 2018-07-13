package fr.vahren.loot.part.rule;

import fr.vahren.loot.Item;

public interface Token {

    String gen(Item item, boolean masculine, boolean plural, boolean vowel);

}
