package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import java.util.List;

public class RestFloor extends TowerFloor {
    private final String floorName;
    private final int healAmount;

    public RestFloor(String name, int heal) {
        this.floorName = name;
        this.healAmount = heal;
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("  A peaceful spring. Heroes rest and recover.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        int initialHp = party.stream().mapToInt(Hero::getHp).sum();
        for (Hero hero : party) {
            if (hero.isAlive()) hero.heal(healAmount);
        }
        int finalHp = party.stream().mapToInt(Hero::getHp).sum();
        return new FloorResult(true, 0, "Restored " + (finalHp - initialHp) + " HP");
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        // Rest floor gives no loot, just healing
        System.out.println("  No loot on rest floor.");
        return false;
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        // Never called because shouldAwardLoot returns false
    }
}