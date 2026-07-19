package net.shishen575.create_upgrades.content.module;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.shishen575.create_upgrades.api.IStackUpgradeable;

import java.util.List;

/**
 * スタックアップグレードモジュール。
 * 釜・デポなど IStackUpgradeable を実装した機械専用。
 *
 * 利点: 処理量(スタックサイズ) ×2
 * 欠点: 処理時間 ×1.8
 */
public class StackModuleItem extends MachineModuleItem {

    /** 処理量(スタック数)の倍率 */
    public static final int STACK_MULTIPLIER = 2;

    /** 処理時間の倍率 */
    public static final float PROCESSING_TIME_MULT = 1.8f;

    public StackModuleItem(Properties properties) {
        super(properties);
    }

    @Override
    protected List<Component> getBenefits(ItemStack stack) {
        return List.of(
            Component.translatable("tooltip.create_upgrades.stack_module.benefit", STACK_MULTIPLIER)
        );
    }

    @Override
    protected List<Component> getDrawbacks(ItemStack stack) {
        return List.of(
            Component.translatable("tooltip.create_upgrades.stack_module.drawback",
                String.format("×%.1f", PROCESSING_TIME_MULT))
        );
    }

    /** スタックアップグレードは IStackUpgradeable の機械にしか挿入できない */
    @Override
    public boolean canInsertIntoMachine() {
        return true; // 判定は ModuleMenu のスロット側で行う
    }

    /** 対象機械かどうかを判定する */
    public static boolean supportsTarget(Object blockEntity) {
        return blockEntity instanceof IStackUpgradeable;
    }
}
