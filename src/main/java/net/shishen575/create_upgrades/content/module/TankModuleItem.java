package net.shishen575.create_upgrades.content.module;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.shishen575.create_upgrades.api.IFluidCapacityUpgradeable;

import java.util.List;

public class TankModuleItem extends MachineModuleItem {

    public static final int CAPACITY_MULTIPLIER = 2;
    // 流体転送速度への影響は FluidPump mixin で実装 (TODO)
    public static final float TRANSFER_SPEED_MULT = 0.5f;

    public TankModuleItem(Properties properties) {
        super(properties);
    }

    @Override
    protected List<Component> getBenefits(ItemStack stack) {
        return List.of(
            Component.translatable("tooltip.create_upgrades.tank_module.benefit")
        );
    }

    @Override
    protected List<Component> getDrawbacks(ItemStack stack) {
        return List.of(
            Component.translatable("tooltip.create_upgrades.tank_module.drawback")
        );
    }

    /** タンクモジュールは IFluidCapacityUpgradeable な機械にのみ使用可能 */
    public static boolean supportsTarget(Object blockEntity) {
        return blockEntity instanceof IFluidCapacityUpgradeable;
    }
}
