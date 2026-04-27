package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.StunnedState;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BossFloor extends TowerFloor {
    private final String floorName;
    private Monster boss;
    private int totalDamageTaken;
    private final Random random = new Random();

    public BossFloor(String name, Monster boss) {
        this.floorName = name;
        this.boss = boss;
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("  A mighty boss blocks your path! " + boss.getName() + " appears!");
        totalDamageTaken = 0;
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("  Boss fight begins!");
        List<Hero> heroes = new ArrayList<>(party);
        Monster currentBoss = boss;
        int initialHpSum = heroes.stream().mapToInt(Hero::getHp).sum();

        while (currentBoss.isAlive() && heroes.stream().anyMatch(Hero::isAlive)) {
            // Heroes' turn
            for (Hero hero : heroes) {
                if (!hero.isAlive()) continue;
                hero.onTurnStart();
                if (hero.canAct()) {
                    hero.attack(currentBoss);
                }
                hero.onTurnEnd();
                if (!currentBoss.isAlive()) break;
            }
            if (!currentBoss.isAlive()) break;

            // Boss attack
            Hero target = heroes.stream().filter(Hero::isAlive).findFirst().orElse(null);
            if (target == null) break;
            System.out.println("  " + currentBoss.getName() + " attacks " + target.getName() + "!");
            int oldHp = target.getHp();
            currentBoss.attack(target);
            int damageDealt = oldHp - target.getHp();
            totalDamageTaken += damageDealt;
            // Boss stuns with 40% chance
            if (random.nextDouble() < 0.4 && target.isAlive()) {
                System.out.println("    " + target.getName() + " is STUNNED!");
                target.setState(new StunnedState(1));
            }
            if (!target.isAlive()) {
                System.out.println("    " + target.getName() + " has fallen!");
            }
        }

        boolean cleared = !currentBoss.isAlive() && heroes.stream().anyMatch(Hero::isAlive);
        int finalHpSum = heroes.stream().mapToInt(Hero::getHp).sum();
        int damageTaken = initialHpSum - finalHpSum;
        return new FloorResult(cleared, damageTaken, cleared ? "Boss defeated!" : "Party wiped by boss.");
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        if (result.isCleared()) {
            System.out.println("  Epic loot! Each hero gains +20 max HP (fully healed) and 50 gold.");
            for (Hero hero : party) {
                // In a real game you'd increase max HP, but for simplicity we fully heal
                hero.heal(hero.getMaxHp());
            }
        }
    }
}