package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class StunnedState implements HeroState {
    private int turnsRemaining;

    public StunnedState(int duration) {
        this.turnsRemaining = duration;
    }

    @Override
    public String getName() {
        return "Stunned";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return 0; // cannot deal damage
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return (int) (rawDamage * 1.1); // 10% more damage while stunned
    }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.println("  " + hero.getName() + " is stunned and cannot act!");
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsRemaining--;
        if (turnsRemaining <= 0) {
            System.out.println("  " + hero.getName() + " recovers from stun.");
            hero.setState(new NormalState());
        }
    }

    @Override
    public boolean canAct() {
        return false;
    }
}