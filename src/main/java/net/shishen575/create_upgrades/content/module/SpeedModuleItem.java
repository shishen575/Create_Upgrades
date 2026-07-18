package net.shishen575.create_upgrades.content.module;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * 速度モジュール
 * 利点: 処理速度 ×1.5
 * 欠点: ストレス消費 ×1.6
 */
public class SpeedModuleItem extends MachineModuleItem {

    public static final float SPEED_MULT  = 1.5f;
    public static final float STRESS_MULT = 1.6f;

    public SpeedModuleItem(Properties properties) {
        super(properties);
    }

    @Override
    protected List<Component> getBenefits(ItemStack stack) {
        return List.of(
            Component.translatable("tooltip.create_upgrades.speed_module.benefit",
                String.format("×%.1f", SPEED_MULT))
        );
    }

    @Override
    protected List<Component> getDrawbacks(ItemStack stack) {
        return List.of(
            Component.translatable("tooltip.create_upgrades.speed_module.drawback",
                String.format("×%.1f", STRESS_MULT))
        );
    }
}
