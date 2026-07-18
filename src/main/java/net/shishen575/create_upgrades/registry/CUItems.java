package net.shishen575.create_upgrades.registry;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.shishen575.create_upgrades.content.upgrade.UpgradeApplicatorItem;
import net.shishen575.create_upgrades.content.upgrade.UpgradeItem;
import net.shishen575.create_upgrades.content.upgrade.UpgradeType;

public class CUItems {

    public static final DeferredRegister.Items ITEMS =
        DeferredRegister.createItems("create_upgrades");

    // ---- Upgrade Applicator ----
    public static final DeferredItem<UpgradeApplicatorItem> UPGRADE_APPLICATOR = ITEMS.register(
        "upgrade_applicator",
        () -> new UpgradeApplicatorItem(new Item.Properties().stacksTo(1)));

    // ---- Speed Upgrades ----
    public static final DeferredItem<UpgradeItem> SPEED_UPGRADE_1 = upgradeItem("speed_upgrade_1", UpgradeType.SPEED, 1);
    public static final DeferredItem<UpgradeItem> SPEED_UPGRADE_2 = upgradeItem("speed_upgrade_2", UpgradeType.SPEED, 2);
    public static final DeferredItem<UpgradeItem> SPEED_UPGRADE_3 = upgradeItem("speed_upgrade_3", UpgradeType.SPEED, 3);
    public static final DeferredItem<UpgradeItem> SPEED_UPGRADE_4 = upgradeItem("speed_upgrade_4", UpgradeType.SPEED, 4);

    // ---- Efficiency Upgrades ----
    public static final DeferredItem<UpgradeItem> EFFICIENCY_UPGRADE_1 = upgradeItem("efficiency_upgrade_1", UpgradeType.EFFICIENCY, 1);
    public static final DeferredItem<UpgradeItem> EFFICIENCY_UPGRADE_2 = upgradeItem("efficiency_upgrade_2", UpgradeType.EFFICIENCY, 2);
    public static final DeferredItem<UpgradeItem> EFFICIENCY_UPGRADE_3 = upgradeItem("efficiency_upgrade_3", UpgradeType.EFFICIENCY, 3);
    public static final DeferredItem<UpgradeItem> EFFICIENCY_UPGRADE_4 = upgradeItem("efficiency_upgrade_4", UpgradeType.EFFICIENCY, 4);

    // ---- Stress Upgrades ----
    public static final DeferredItem<UpgradeItem> STRESS_UPGRADE_1 = upgradeItem("stress_upgrade_1", UpgradeType.STRESS, 1);
    public static final DeferredItem<UpgradeItem> STRESS_UPGRADE_2 = upgradeItem("stress_upgrade_2", UpgradeType.STRESS, 2);
    public static final DeferredItem<UpgradeItem> STRESS_UPGRADE_3 = upgradeItem("stress_upgrade_3", UpgradeType.STRESS, 3);
    public static final DeferredItem<UpgradeItem> STRESS_UPGRADE_4 = upgradeItem("stress_upgrade_4", UpgradeType.STRESS, 4);

    // ---- Range Upgrades ----
    public static final DeferredItem<UpgradeItem> RANGE_UPGRADE_1 = upgradeItem("range_upgrade_1", UpgradeType.RANGE, 1);
    public static final DeferredItem<UpgradeItem> RANGE_UPGRADE_2 = upgradeItem("range_upgrade_2", UpgradeType.RANGE, 2);
    public static final DeferredItem<UpgradeItem> RANGE_UPGRADE_3 = upgradeItem("range_upgrade_3", UpgradeType.RANGE, 3);
    public static final DeferredItem<UpgradeItem> RANGE_UPGRADE_4 = upgradeItem("range_upgrade_4", UpgradeType.RANGE, 4);

    private static DeferredItem<UpgradeItem> upgradeItem(String name, UpgradeType type, int level) {
        return ITEMS.register(name, () -> new UpgradeItem(type, level, new Item.Properties().stacksTo(1)));
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
