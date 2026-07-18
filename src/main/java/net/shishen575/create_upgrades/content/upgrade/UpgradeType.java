package net.shishen575.create_upgrades.content.upgrade;

import net.minecraft.network.chat.Component;

public enum UpgradeType {
    SPEED("speed", 4, "upgrade.create_upgrades.speed"),
    EFFICIENCY("efficiency", 4, "upgrade.create_upgrades.efficiency"),
    STRESS("stress", 4, "upgrade.create_upgrades.stress"),
    RANGE("range", 4, "upgrade.create_upgrades.range");

    private final String id;
    private final int maxLevel;
    private final String translationKey;

    UpgradeType(String id, int maxLevel, String translationKey) {
        this.id = id;
        this.maxLevel = maxLevel;
        this.translationKey = translationKey;
    }

    public String getId() { return id; }
    public int getMaxLevel() { return maxLevel; }
    public Component getDisplayName() { return Component.translatable(translationKey); }

    /** 速度倍率 (1.0 = 変化なし) */
    public float getSpeedMultiplier(int level) {
        return switch (this) {
            case SPEED -> 1.0f + level * 0.5f;
            default -> 1.0f;
        };
    }

    /** ストレス消費倍率 (1.0 = 変化なし) */
    public float getStressMultiplier(int level) {
        return switch (this) {
            case EFFICIENCY -> Math.max(0.1f, 1.0f - level * 0.2f);
            case SPEED -> 1.0f + level * 0.3f;
            case STRESS -> 1.0f - level * 0.15f;
            default -> 1.0f;
        };
    }

    /** 処理速度倍率 (作業時間短縮など) */
    public float getProcessingSpeedMultiplier(int level) {
        return switch (this) {
            case SPEED -> 1.0f + level * 0.25f;
            case EFFICIENCY -> 1.0f + level * 0.15f;
            default -> 1.0f;
        };
    }

    /** 範囲加算 (ブロック単位) */
    public int getRangeBonus(int level) {
        return switch (this) {
            case RANGE -> level * 2;
            default -> 0;
        };
    }
}
