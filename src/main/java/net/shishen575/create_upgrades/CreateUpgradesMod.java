package net.shishen575.create_upgrades;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.shishen575.create_upgrades.registry.CUItems;
import net.shishen575.create_upgrades.registry.CUMenuTypes;

@Mod("create_upgrades")
public class CreateUpgradesMod {

    public static final String MOD_ID = "create_upgrades";

    public CreateUpgradesMod(IEventBus modEventBus) {
        CUItems.register(modEventBus);
        CUMenuTypes.register(modEventBus);
    }
}
