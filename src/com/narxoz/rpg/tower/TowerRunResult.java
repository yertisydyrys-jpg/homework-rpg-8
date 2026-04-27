package com.narxoz.rpg.tower;

import com.narxoz.rpg.combatant.Hero;
import java.util.List;

public class TowerRunResult {
    private final int floorsCleared;
    private final List<Hero> survivingHeroes;
    private final boolean towerDefeated;

    public TowerRunResult(int floorsCleared, List<Hero> survivingHeroes, boolean towerDefeated) {
        this.floorsCleared = floorsCleared;
        this.survivingHeroes = survivingHeroes;
        this.towerDefeated = towerDefeated;
    }

    public int getFloorsCleared() { return floorsCleared; }
    public List<Hero> getSurvivingHeroes() { return survivingHeroes; }
    public boolean isTowerDefeated() { return towerDefeated; }

    public void printSummary() {
        System.out.println("\n========== TOWER RUN RESULT ==========");
        System.out.println("Floors cleared: " + floorsCleared);
        System.out.println("Tower defeated: " + (towerDefeated ? "YES" : "NO"));
        System.out.println("Surviving heroes:");
        for (Hero h : survivingHeroes) {
            System.out.println("  - " + h.getName() + " (HP: " + h.getHp() + "/" + h.getMaxHp() + ")");
        }
        System.out.println("======================================\n");
    }
}