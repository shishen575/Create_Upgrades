package net.shishen575.create_upgrades;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.shishen575.create_upgrades.registry.CUItems;
import net.shishen575.create_upgrades.registry.CUMenuTypes;

@Mod("create_upgrades")
public class CreateUpgradesMod {

    public static final String MOD_ID = "create_upgrades";

    public CreateUpgradesMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        CUItems.register(modEventBus);
        CUMenuTypes.register(modEventBus);
    }
}
