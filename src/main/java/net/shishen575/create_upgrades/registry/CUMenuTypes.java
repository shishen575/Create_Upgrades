package net.shishen575.create_upgrades.registry;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shishen575.create_upgrades.content.gui.ModuleMenu;

public class CUMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
        DeferredRegister.create(ForgeRegistries.MENU_TYPES, "create_upgrades");

    public static final RegistryObject<MenuType<ModuleMenu>> MODULE_MENU =
        MENU_TYPES.register("module_menu",
            () -> IForgeMenuType.create(ModuleMenu::new));

    public static void register(IEventBus bus) {
        MENU_TYPES.register(bus);
    }
}
