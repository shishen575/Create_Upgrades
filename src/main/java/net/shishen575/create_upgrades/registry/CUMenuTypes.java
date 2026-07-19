package net.shishen575.create_upgrades.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.shishen575.create_upgrades.content.gui.ModuleMenu;

public class CUMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
        DeferredRegister.create(Registries.MENU, "create_upgrades");

    public static final DeferredHolder<MenuType<?>, MenuType<ModuleMenu>> MODULE_MENU =
        MENU_TYPES.register("module_menu",
            () -> IMenuTypeExtension.create((id, inv, buf) -> new ModuleMenu(id, inv, buf)));

    public static void register(IEventBus bus) {
        MENU_TYPES.register(bus);
    }
}
