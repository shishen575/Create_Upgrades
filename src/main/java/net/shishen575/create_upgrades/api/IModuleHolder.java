package net.shishen575.create_upgrades.api;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.shishen575.create_upgrades.content.module.EfficiencyModuleItem;
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
