package net.shishen575.create_upgrades.registry;

import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.shishen575.create_upgrades.content.gui.UpgradeMenu;

public class CUMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
        DeferredRegister.create(Registries.MENU, "create_upgrades");

    public static final DeferredHolder<MenuType<?>, MenuType<UpgradeMenu>> UPGRADE_MENU =
        MENU_TYPES.register("upgrade_menu",
            () -> new MenuType<>(UpgradeMenu::new, net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS));

    public static void register(IEventBus bus) {
        MENU_TYPES.register(bus);
    }
}
