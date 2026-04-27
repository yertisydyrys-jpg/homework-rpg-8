package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import java.util.List;

public class TrapFloor extends TowerFloor {
    private final String floorName;
    private final int trapDamage;

    public TrapFloor(String name, int damage) {
        this.floorName = name;
        this.trapDamage = damage;
    }

    @Override
    protected void announce() {
        System.out.println("\n--- ⚠️  DANGER! " + getFloorName() + " ⚠️ ---");
        System.out.println("  You hear clicking sounds... The floor is booby-trapped!");
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("  The trap is triggered!");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        int initialHp = party.stream().mapToInt(Hero::getHp).sum();
        System.out.println("  A spiked trap springs! All heroes take " + trapDamage + " damage.");
        for (Hero hero : party) {
            hero.takeDamage(trapDamage);
        }
        boolean allAlive = party.stream().allMatch(Hero::isAlive);
        int finalHp = party.stream().mapToInt(Hero::getHp).sum();
        String summary = allAlive ? "Survived the trap" : "Some heroes fell to the trap";
        return new FloorResult(allAlive, initialHp - finalHp, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        if (result.isCleared()) {
            System.out.println("  Found a hidden treasure! Each hero receives a healing potion (+15 HP).");
            for (Hero hero : party) {
                if (hero.isAlive()) hero.heal(15);
            }
        }
    }
}