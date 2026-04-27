package com.narxoz.rpg;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.floor.*;
import com.narxoz.rpg.runner.TowerRunner;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.tower.TowerRunResult;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== THE HAUNTED TOWER: ASCENDING THE FLOORS ===\n");

        // Create heroes with different starting states (both normal)
        Hero warrior = new Hero("Sir Lancelot", 90, 22, 12);
        Hero mage = new Hero("Merlin", 55, 28, 6);
        System.out.println("Initial heroes: " + warrior.getName() + " (HP:90) and " + mage.getName() + " (HP:55)");
        System.out.println("Both start in Normal state.\n");

        // Define floors (≥4 floors, ≥3 distinct types)
        // Floor 1: Combat with goblins (poison chance)
        List<Monster> goblins = Arrays.asList(
                new Monster("Goblin Archer", 30, 12),
                new Monster("Goblin Brute", 40, 14)
        );
        CombatFloor floor1 = new CombatFloor("Goblin Den", goblins);

        // Floor 2: Trap floor (overrides announce)
        TrapFloor floor2 = new TrapFloor("Spike Corridor", 18);

        // Floor 3: Rest floor (overrides shouldAwardLoot)
        RestFloor floor3 = new RestFloor("Healing Spring", 25);

        // Floor 4: Boss floor (applies stun)
        Monster boss = new Monster("Orc Warlord", 80, 20);
        BossFloor floor4 = new BossFloor("Warlord's Chamber", boss);

        List<TowerFloor> towerFloors = Arrays.asList(floor1, floor2, floor3, floor4);

        // Run the tower
        TowerRunner runner = new TowerRunner(towerFloors, Arrays.asList(warrior, mage));
        TowerRunResult result = runner.run();
        result.printSummary();
    }
}