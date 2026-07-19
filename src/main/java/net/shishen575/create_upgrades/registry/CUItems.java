package net.shishen575.create_upgrades.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shishen575.create_upgrades.content.module.EfficiencyModuleItem;
import net.shishen575.create_upgrades.content.module.SpeedModuleItem;
import net.shishen575.create_upgrades.content.module.StackModuleItem;
import net.shishen575.create_upgrades.content.upgrade.UpgradeApplicatorItem;

public class CUItems {

    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, "create_upgrades");

    public static final RegistryObject<UpgradeApplicatorItem> MODULE_APPLICATOR = ITEMS.register(
        "module_applicator",
        () -> new UpgradeApplicatorItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<SpeedModuleItem> SPEED_MODULE = ITEMS.register(
        "speed_module",
        () -> new SpeedModuleItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<EfficiencyModuleItem> EFFICIENCY_MODULE = ITEMS.register(
        "efficiency_module",
        () -> new EfficiencyModuleItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<StackModuleItem> STACK_MODULE = ITEMS.register(
        "stack_module",
        () -> new StackModuleItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
