package net.shishen575.create_upgrades.api;

import net.minecraft.nbt.CompoundTag;
import net.shishen575.create_upgrades.content.upgrade.UpgradeType;

import java.util.Map;

/**
 * 他のアドオンの機械がアップグレードに対応する場合、
 * BlockEntity にこのインターフェースを実装する。
 * Create の標準機械は Mixin で自動対応するため実装不要。
 */
public interface IUpgradeable {

    /** アップグレード未対応の機械用ダミー実装 */
    IUpgradeable EMPTY = new IUpgradeable() {
        private final Map<UpgradeType, Integer> empty = Map.of();

        @Override public Map<UpgradeType, Integer> getUpgrades() { return empty; }
        @Override public void setUpgrade(UpgradeType type, int level) {}
        @Override public void removeUpgrade(UpgradeType type) {}
    };

    Map<UpgradeType, Integer> getUpgrades();

    void setUpgrade(UpgradeType type, int level);

    void removeUpgrade(UpgradeType type);

    default int getUpgradeLevel(UpgradeType type) {
        return getUpgrades().getOrDefault(type, 0);
    }

    default boolean hasUpgrade(UpgradeType type) {
        return getUpgradeLevel(type) > 0;
    }

    default void saveUpgrades(CompoundTag tag) {
        CompoundTag upgradesTag = new CompoundTag();
        getUpgrades().forEach((type, level) -> upgradesTag.putInt(type.getId(), level));
        tag.put("CreateUpgrades", upgradesTag);
    }

    default void loadUpgrades(CompoundTag tag) {
        if (!tag.contains("CreateUpgrades")) return;
        CompoundTag upgradesTag = tag.getCompound("CreateUpgrades");
        for (UpgradeType type : UpgradeType.values()) {
            if (upgradesTag.contains(type.getId())) {
                setUpgrade(type, upgradesTag.getInt(type.getId()));
            }
        }
    }
}
