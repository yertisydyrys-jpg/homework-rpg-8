package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class PoisonedState implements HeroState {
    private int turnsRemaining;

    public PoisonedState(int duration) {
        this.turnsRemaining = duration;
    }

    @Override
    public String getName() {
        return "Poisoned (" + turnsRemaining + " turns left)";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return (int) (basePower * 0.7);
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return (int) (rawDamage * 1.2);
    }

    @Override
    public void onTurnStart(Hero hero) {
        int poisonDamage = 5;
        System.out.println("  " + hero.getName() + " suffers " + poisonDamage + " poison damage!");
        hero.takeDamage(poisonDamage); // damage goes through modifyIncomingDamage (intentional)
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsRemaining--;
        if (turnsRemaining <= 0) {
            System.out.println("  " + hero.getName() + " is no longer poisoned.");
            hero.setState(new NormalState());
        }
    }

    @Override
    public boolean canAct() {
        return true;
    }
}