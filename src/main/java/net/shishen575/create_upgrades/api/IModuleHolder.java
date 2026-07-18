package net.shishen575.create_upgrades.api;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.shishen575.create_upgrades.content.module.EfficiencyModuleItem;
import net.shishen575.create_upgrades.content.module.FortuneModuleItem;
import net.shishen575.create_upgrades.content.module.MachineModuleItem;
import net.shishen575.create_upgrades.content.module.SpeedModuleItem;

/**
 * モジュールスロットを持つ BlockEntity が実装するインターフェース。
 * Create の標準機械は KineticBlockEntityMixin で自動実装される。
 */
public interface IModuleHolder {

    int MODULE_SLOT_COUNT = 3;

    IModuleHolder EMPTY = new IModuleHolder() {
        private final NonNullList<ItemStack> slots = NonNullList.withSize(MODULE_SLOT_COUNT, ItemStack.EMPTY);
        @Override public NonNullList<ItemStack> getModuleSlots() { return slots; }
    };

    NonNullList<ItemStack> getModuleSlots();

    default ItemStack getModule(int slot) {
        return getModuleSlots().get(slot);
    }

    default void setModule(int slot, ItemStack stack) {
        getModuleSlots().set(slot, stack);
    }

    // ---- 効果の集約計算 ----

    /** 全スロットの速度倍率を掛け合わせて返す */
    default float getCombinedSpeedMultiplier() {
        float mult = 1.0f;
        for (ItemStack stack : getModuleSlots()) {
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof SpeedModuleItem)      mult *= SpeedModuleItem.SPEED_MULT;
            if (stack.getItem() instanceof EfficiencyModuleItem) mult *= EfficiencyModuleItem.SPEED_MULT;
        }
        return mult;
    }

    /** 全スロットのストレス倍率を掛け合わせて返す */
    default float getCombinedStressMultiplier() {
        float mult = 1.0f;
        for (ItemStack stack : getModuleSlots()) {
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof SpeedModuleItem)      mult *= SpeedModuleItem.STRESS_MULT;
            if (stack.getItem() instanceof EfficiencyModuleItem) mult *= EfficiencyModuleItem.STRESS_MULT;
        }
        return mult;
    }

    /** 幸運モジュールが少なくとも1枚挿入されているか */
    default boolean hasFortuneModule() {
        for (ItemStack stack : getModuleSlots()) {
            if (stack.getItem() instanceof FortuneModuleItem) return true;
        }
        return false;
    }

    /**
     * 処理完了時に呼ぶ。幸運モジュールの耐久を消費し、壊れたスロットをクリアする。
     * @return 副産物ボーナスを付与すべきかどうか
     */
    default boolean onProcessingComplete(net.minecraft.util.RandomSource random) {
        boolean grantBonus = false;
        NonNullList<ItemStack> slots = getModuleSlots();
        for (int i = 0; i < slots.size(); i++) {
            ItemStack stack = slots.get(i);
            if (!(stack.getItem() instanceof FortuneModuleItem)) continue;
            if (random.nextFloat() < FortuneModuleItem.BONUS_CHANCE) grantBonus = true;
            if (FortuneModuleItem.consumeDurability(stack)) {
                slots.set(i, ItemStack.EMPTY); // 耐久切れで消去
            }
        }
        return grantBonus;
    }

    // ---- NBT ----

    default void saveModules(CompoundTag tag) {
        ListTag list = new ListTag();
        for (ItemStack stack : getModuleSlots()) {
            list.add(stack.save(new CompoundTag()));
        }
        tag.put("CreateModules", list);
    }

    default void loadModules(CompoundTag tag) {
        if (!tag.contains("CreateModules")) return;
        ListTag list = tag.getList("CreateModules", Tag.TAG_COMPOUND);
        NonNullList<ItemStack> slots = getModuleSlots();
        for (int i = 0; i < Math.min(list.size(), slots.size()); i++) {
            CompoundTag itemTag = list.getCompound(i);
            slots.set(i, itemTag.isEmpty() ? ItemStack.EMPTY : ItemStack.of(itemTag));
        }
    }
}
