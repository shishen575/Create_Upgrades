package net.shishen575.create_upgrades.content.module;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * 幸運モジュール
 * 利点: 副産物率 +40%
 * 欠点: 耐久度が処理ごとに 1 ずつ減少し、0 になるとスロットから消える
 *
 * 耐久度は Item.Properties#durability() で管理し、Minecraft 標準の
 * ダメージバーとして GUI 上に表示される。
 */
public class FortuneModuleItem extends MachineModuleItem {

    /** 副産物が追加でドロップする確率 (0.0〜1.0) */
    public static final float BONUS_CHANCE = 0.4f;

    /** 最大耐久度。処理 500 回で壊れる */
    public static final int MAX_DURABILITY = 500;

    public FortuneModuleItem(Properties properties) {
        super(properties.durability(MAX_DURABILITY));
    }

    @Override
    protected List<Component> getBenefits(ItemStack stack) {
        return List.of(
            Component.translatable("tooltip.create_upgrades.fortune_module.benefit",
                String.format("%.0f%%", BONUS_CHANCE * 100))
        );
    }

    @Override
    protected List<Component> getDrawbacks(ItemStack stack) {
        int remaining = stack.getMaxDamage() - stack.getDamageValue();
        return List.of(
            Component.translatable("tooltip.create_upgrades.fortune_module.drawback"),
            Component.translatable("tooltip.create_upgrades.fortune_module.durability", remaining, MAX_DURABILITY)
                .withStyle(remaining < 100 ? ChatFormatting.RED : ChatFormatting.GRAY)
        );
    }

    /**
     * 処理完了時に呼び出す。耐久を 1 消費し、0 になれば true (破壊) を返す。
     * 呼び出し元が破壊判定を行い、スロットをクリアする。
     */
    public static boolean consumeDurability(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof FortuneModuleItem)) return false;
        stack.setDamageValue(stack.getDamageValue() + 1);
        return stack.getDamageValue() >= stack.getMaxDamage();
    }
}
