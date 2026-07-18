package net.shishen575.create_upgrades.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.shishen575.create_upgrades.content.upgrade.UpgradeType;

import java.util.*;
import java.util.function.BiFunction;

/**
 * 他のアドオンが自分の機械をアップグレードシステムに対応させるためのAPI。
 *
 * 使用例 (他のアドオンの初期化コード):
 * <pre>
 * UpgradeRegistry.registerSupport(
 *     ResourceLocation.withDefaultNamespace("my_machine"),
 *     Set.of(UpgradeType.SPEED, UpgradeType.EFFICIENCY)
 * );
 * </pre>
 */
public class UpgradeRegistry {

    private static final Map<ResourceLocation, Set<UpgradeType>> SUPPORTED_UPGRADES = new HashMap<>();
    private static final Map<ResourceLocation, List<CustomUpgradeEffect>> CUSTOM_EFFECTS = new HashMap<>();

    /** 機械IDに対してサポートするアップグレード種別を登録する */
    public static void registerSupport(ResourceLocation blockId, Set<UpgradeType> supportedTypes) {
        SUPPORTED_UPGRADES.computeIfAbsent(blockId, k -> new HashSet<>()).addAll(supportedTypes);
    }

    /**
     * カスタムアップグレード効果を登録する。
     * handler は (blockEntity, upgradeLevel) -> 効果を適用するラムダ。
     */
    public static void registerCustomEffect(ResourceLocation blockId, UpgradeType type,
            BiFunction<BlockEntity, Integer, Void> handler) {
        CUSTOM_EFFECTS.computeIfAbsent(blockId, k -> new ArrayList<>())
            .add(new CustomUpgradeEffect(type, handler));
    }

    public static Set<UpgradeType> getSupportedUpgrades(ResourceLocation blockId) {
        return SUPPORTED_UPGRADES.getOrDefault(blockId, Set.of(UpgradeType.values()));
    }

    public static boolean isSupported(ResourceLocation blockId, UpgradeType type) {
        if (!SUPPORTED_UPGRADES.containsKey(blockId)) return true; // 未登録 = 全種対応
        return SUPPORTED_UPGRADES.get(blockId).contains(type);
    }

    public static List<CustomUpgradeEffect> getCustomEffects(ResourceLocation blockId) {
        return CUSTOM_EFFECTS.getOrDefault(blockId, List.of());
    }

    public record CustomUpgradeEffect(UpgradeType type, BiFunction<BlockEntity, Integer, Void> handler) {}
}
