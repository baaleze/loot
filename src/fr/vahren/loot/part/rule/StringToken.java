package fr.vahren.loot.part.rule;

import fr.vahren.loot.Item;
import fr.vahren.loot.LootGen;
import fr.vahren.loot.part.Price;

public class StringToken implements Token {

    public String feminineSingular;
    public String femininePlural;
    public String masculineSingular;
    public String masculinePlural;
    public String singularVowel;

    public StringToken(String feminineSingular, String femininePlural, String masculineSingular, String masculinePlural,
        String singularVowel) {
        this.feminineSingular = feminineSingular;
        this.femininePlural = femininePlural;
        this.masculineSingular = masculineSingular;
        this.masculinePlural = masculinePlural;
        this.singularVowel = singularVowel;
    }

    @Override
    public String gen(Item item, boolean masculine, boolean plural, boolean vowel, LootGen lootGen, Price p) {
        return masculine
            ? (plural ? this.masculinePlural : (vowel ? this.singularVowel : this.masculineSingular))
            : (plural ? this.femininePlural : (vowel ? this.singularVowel : this.feminineSingular));
    }

}
