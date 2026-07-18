package net.shishen575.create_upgrades.content.module;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * 効率モジュール
 * 利点: ストレス消費 ×0.5
 * 欠点: 処理速度 ×0.7
 */
public class EfficiencyModuleItem extends MachineModuleItem {

    public static final float STRESS_MULT = 0.5f;
    public static final float SPEED_MULT  = 0.7f;

    public EfficiencyModuleItem(Properties properties) {
        super(properties);
    }

    @Override
    protected List<Component> getBenefits(ItemStack stack) {
        return List.of(
            Component.translatable("tooltip.create_upgrades.efficiency_module.benefit",
                String.format("×%.1f", STRESS_MULT))
        );
    }

    @Override
    protected List<Component> getDrawbacks(ItemStack stack) {
        return List.of(
            Component.translatable("tooltip.create_upgrades.efficiency_module.drawback",
                String.format("×%.1f", SPEED_MULT))
        );
    }
}
