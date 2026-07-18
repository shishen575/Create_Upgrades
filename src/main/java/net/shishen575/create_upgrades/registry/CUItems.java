package net.shishen575.create_upgrades.registry;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.shishen575.create_upgrades.content.module.EfficiencyModuleItem;
import net.shishen575.create_upgrades.content.module.FortuneModuleItem;
import net.shishen575.create_upgrades.content.module.SpeedModuleItem;
import net.shishen575.create_upgrades.content.upgrade.UpgradeApplicatorItem;

public class CUItems {

    public static final DeferredRegister.Items ITEMS =
        DeferredRegister.createItems("create_upgrades");

    // ---- Module Applicator ----
    public static final DeferredItem<UpgradeApplicatorItem> MODULE_APPLICATOR = ITEMS.register(
        "module_applicator",
        () -> new UpgradeApplicatorItem(new Item.Properties().stacksTo(1)));

    // ---- Speed Module ----
    public static final DeferredItem<SpeedModuleItem> SPEED_MODULE = ITEMS.register(
        "speed_module",
        () -> new SpeedModuleItem(new Item.Properties().stacksTo(1)));

    // ---- Efficiency Module ----
    public static final DeferredItem<EfficiencyModuleItem> EFFICIENCY_MODULE = ITEMS.register(
        "efficiency_module",
        () -> new EfficiencyModuleItem(new Item.Properties().stacksTo(1)));

    // ---- Fortune Module ----
    public static final DeferredItem<FortuneModuleItem> FORTUNE_MODULE = ITEMS.register(
        "fortune_module",
        () -> new FortuneModuleItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
