package net.shishen575.create_upgrades.api;

import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.shishen575.create_upgrades.content.module.EfficiencyModuleItem;
import net.shishen575.create_upgrades.content.module.SpeedModuleItem;
import net.shishen575.create_upgrades.content.module.StackModuleItem;
import net.shishen575.create_upgrades.content.module.TankModuleItem;

public interface IModuleHolder {

    int MODULE_SLOT_COUNT = 3;

    IModuleHolder EMPTY = new IModuleHolder() {
        private final NonNullList<ItemStack> slots = NonNullList.withSize(MODULE_SLOT_COUNT, ItemStack.EMPTY);
        @Override public NonNullList<ItemStack> getModuleSlots() { return slots; }
    };

    NonNullList<ItemStack> getModuleSlots();

    default ItemStack getModule(int slot) { return getModuleSlots().get(slot); }
    default void setModule(int slot, ItemStack stack) { getModuleSlots().set(slot, stack); }

    default float getCombinedSpeedMultiplier() {
        float mult = 1.0f;
        for (ItemStack stack : getModuleSlots()) {
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof SpeedModuleItem)      mult *= SpeedModuleItem.SPEED_MULT;
            if (stack.getItem() instanceof EfficiencyModuleItem) mult *= EfficiencyModuleItem.SPEED_MULT;
        }
        return mult;
    }

    default float getCombinedStressMultiplier() {
        float mult = 1.0f;
        for (ItemStack stack : getModuleSlots()) {
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof SpeedModuleItem)      mult *= SpeedModuleItem.STRESS_MULT;
            if (stack.getItem() instanceof EfficiencyModuleItem) mult *= EfficiencyModuleItem.STRESS_MULT;
        }
        return mult;
    }

    default int getStackMultiplier() {
        int mult = 1;
        for (ItemStack stack : getModuleSlots()) {
            if (stack.getItem() instanceof StackModuleItem) mult *= StackModuleItem.STACK_MULTIPLIER;
        }
        return mult;
    }

    default float getStackProcessingTimeMult() {
        float mult = 1.0f;
        for (ItemStack stack : getModuleSlots()) {
            if (stack.getItem() instanceof StackModuleItem) mult *= StackModuleItem.PROCESSING_TIME_MULT;
        }
        return mult;
    }

    default int getFluidCapacityMultiplier() {
        int mult = 1;
        for (ItemStack stack : getModuleSlots()) {
            if (stack.getItem() instanceof TankModuleItem) mult *= TankModuleItem.CAPACITY_MULTIPLIER;
        }
        return mult;
    }

    // ---- NBT ----
    // アイテムIDのみ保存 (1.21.1 で ItemStack.save() が HolderLookup.Provider を要求するため)
    // モジュールは stacksTo(1) で追加NBTなしのためこれで十分

    default void saveModules(CompoundTag tag) {
        ListTag list = new ListTag();
        for (ItemStack stack : getModuleSlots()) {
            if (!stack.isEmpty()) {
                list.add(StringTag.valueOf(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString()));
            } else {
                list.add(StringTag.valueOf(""));
            }
        }
        tag.put("CreateModules", list);
    }

    default void loadModules(CompoundTag tag) {
        if (!tag.contains("CreateModules")) return;
        ListTag list = tag.getList("CreateModules", Tag.TAG_STRING);
        NonNullList<ItemStack> slots = getModuleSlots();
        for (int i = 0; i < Math.min(list.size(), slots.size()); i++) {
            String id = list.getString(i);
            if (id.isEmpty()) {
                slots.set(i, ItemStack.EMPTY);
            } else {
                Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
                slots.set(i, item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item));
            }
        }
    }
}
