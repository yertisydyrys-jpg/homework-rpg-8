package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CombatFloor extends TowerFloor {
    private final String floorName;
    private List<Monster> monsters;
    private int totalDamageTaken;
    private final Random random = new Random();

    public CombatFloor(String name, List<Monster> monsters) {
        this.floorName = name;
        this.monsters = new ArrayList<>(monsters);
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("  Preparing for combat... " + monsters.size() + " enemies appear!");
        totalDamageTaken = 0;
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("  Battle begins!");
        List<Hero> heroes = new ArrayList<>(party);
        List<Monster> activeMonsters = new ArrayList<>(monsters);
        int initialHpSum = heroes.stream().mapToInt(Hero::getHp).sum();

        while (true) {
            // Heroes' turn
            for (Hero hero : heroes) {
                if (!hero.isAlive()) continue;
                hero.onTurnStart();
                if (hero.canAct() && !activeMonsters.isEmpty()) {
                    Monster target = activeMonsters.get(0); // simple: attack first monster
                    hero.attack(target);
                    if (!target.isAlive()) {
                        System.out.println("    " + target.getName() + " is defeated!");
                        activeMonsters.remove(target);
                    }
                }
                hero.onTurnEnd();
                if (activeMonsters.isEmpty()) break;
            }

            if (activeMonsters.isEmpty()) break;

            // Monsters' turn
            for (Monster monster : activeMonsters) {
                if (!monster.isAlive()) continue;
                Hero target = heroes.stream().filter(Hero::isAlive).findFirst().orElse(null);
                if (target == null) break;
                System.out.println("  " + monster.getName() + " attacks " + target.getName() + "!");
                int oldHp = target.getHp();
                monster.attack(target);
                int damageDealt = oldHp - target.getHp();
                totalDamageTaken += damageDealt;
                // 30% chance to poison the target
                if (random.nextDouble() < 0.3 && target.isAlive()) {
                    System.out.println("    " + target.getName() + " is poisoned!");
                    target.setState(new com.narxoz.rpg.state.PoisonedState(3));
                }
                if (!target.isAlive()) {
                    System.out.println("    " + target.getName() + " has fallen!");
                }
            }

            // Check if any heroes alive
            boolean anyAlive = heroes.stream().anyMatch(Hero::isAlive);
            if (!anyAlive) break;
        }

        boolean cleared = activeMonsters.isEmpty() && heroes.stream().anyMatch(Hero::isAlive);
        int finalHpSum = heroes.stream().mapToInt(Hero::getHp).sum();
        int damageTaken = initialHpSum - finalHpSum;
        String summary = cleared ? "Victory!" : "Defeat...";
        return new FloorResult(cleared, damageTaken, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        if (result.isCleared()) {
            System.out.println("  Loot: each hero gains 10 gold and restores 10 HP.");
            for (Hero hero : party) {
                if (hero.isAlive()) {
                    hero.heal(10);
                }
            }
        } else {
            System.out.println("  No loot for defeat.");
        }
    }
}