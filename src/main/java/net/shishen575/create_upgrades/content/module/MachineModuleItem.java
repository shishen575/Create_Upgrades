package net.shishen575.create_upgrades.content.module;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * すべてのモジュールアイテムの基底クラス。
 * モジュールにはトレードオフがある（利点と欠点が共存する）。
 */
public abstract class MachineModuleItem extends Item {

    public MachineModuleItem(Properties properties) {
        super(properties);
    }

    /**
     * モジュールが持つプラス効果のテキスト一覧。
     * サブクラスでオーバーライドして返す。
     */
    protected abstract List<Component> getBenefits(ItemStack stack);

    /**
     * モジュールが持つマイナス効果(トレードオフ)のテキスト一覧。
     * サブクラスでオーバーライドして返す。
     */
    protected abstract List<Component> getDrawbacks(ItemStack stack);

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
            List<Component> tooltipComponents, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipComponents, flag);

        for (Component benefit : getBenefits(stack)) {
            tooltipComponents.add(Component.literal(" + ").withStyle(ChatFormatting.GREEN)
                .append(benefit.copy().withStyle(ChatFormatting.GREEN)));
        }
        for (Component drawback : getDrawbacks(stack)) {
            tooltipComponents.add(Component.literal(" - ").withStyle(ChatFormatting.RED)
                .append(drawback.copy().withStyle(ChatFormatting.RED)));
        }

        tooltipComponents.add(Component.translatable("tooltip.create_upgrades.module_slot_hint")
            .withStyle(ChatFormatting.DARK_GRAY));
    }

    /** このモジュールが機械スロットに挿入可能かどうか */
    public boolean canInsertIntoMachine() {
        return true;
    }
}
