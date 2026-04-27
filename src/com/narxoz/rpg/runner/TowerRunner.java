package com.narxoz.rpg.runner;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.FloorResult;
import com.narxoz.rpg.floor.TowerFloor;
import com.narxoz.rpg.tower.TowerRunResult;
import java.util.ArrayList;
import java.util.List;

public class TowerRunner {
    private final List<TowerFloor> floors;
    private final List<Hero> party;

    public TowerRunner(List<TowerFloor> floors, List<Hero> party) {
        this.floors = new ArrayList<>(floors);
        this.party = new ArrayList<>(party);
    }

    public TowerRunResult run() {
        int cleared = 0;
        for (TowerFloor floor : floors) {
            System.out.println("\n*** Starting floor " + (cleared+1) + " ***");
            // Check if any heroes alive before floor
            if (party.stream().noneMatch(Hero::isAlive)) {
                System.out.println("No heroes alive, stopping climb.");
                break;
            }
            FloorResult result = floor.explore(party);
            if (result.isCleared()) {
                cleared++;
                // Continue to next floor
            } else {
                System.out.println("Floor not cleared, climb ends.");
                break;
            }
        }
        boolean towerDefeated = cleared == floors.size() && party.stream().anyMatch(Hero::isAlive);
        List<Hero> survivors = new ArrayList<>();
        for (Hero h : party) {
            if (h.isAlive()) survivors.add(h);
        }
        return new TowerRunResult(cleared, survivors, towerDefeated);
    }
}