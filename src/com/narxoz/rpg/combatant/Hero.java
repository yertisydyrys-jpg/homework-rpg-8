package com.narxoz.rpg.combatant;

import com.narxoz.rpg.state.HeroState;
import com.narxoz.rpg.state.NormalState;

public class Hero {
    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private HeroState state;

    public Hero(String name, int hp, int attackPower, int defense) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.state = new NormalState(); // default state
    }

    public String getName()        { return name; }
    public int getHp()             { return hp; }
    public int getMaxHp()          { return maxHp; }
    public int getAttackPower()    { return attackPower; }
    public int getDefense()        { return defense; }
    public boolean isAlive()       { return hp > 0; }

    public void takeDamage(int amount) {
        int modified = state.modifyIncomingDamage(amount);
        hp = Math.max(0, hp - modified);
        System.out.println("  " + name + " takes " + modified + " damage (HP: " + hp + "/" + maxHp + ")");
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
        System.out.println("  " + name + " heals " + amount + " HP (HP: " + hp + "/" + maxHp + ")");
    }

    public void attack(Monster monster) {
        int baseDamage = attackPower;
        int finalDamage = state.modifyOutgoingDamage(baseDamage);
        monster.takeDamage(finalDamage);
        System.out.println("  " + name + " attacks " + monster.getName() + " for " + finalDamage + " damage!");
    }

    // State management
    public HeroState getState() { return state; }
    public void setState(HeroState newState) {
        System.out.println("  " + name + " transitions: " + this.state.getName() + " → " + newState.getName());
        this.state = newState;
    }

    public void onTurnStart() { state.onTurnStart(this); }
    public void onTurnEnd()   { state.onTurnEnd(this); }
    public boolean canAct()   { return state.canAct(); }
}