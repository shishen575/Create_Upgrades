package net.shishen575.create_upgrades.content.gui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.shishen575.create_upgrades.api.IModuleHolder;

/**
 * IModuleHolder の NonNullList を Minecraft の Container として橋渡しするラッパー。
 * スロット変更時に自動的に BE の markUpdated() が呼ばれるよう setChanged() をオーバーライド。
 */
public class ModuleContainer implements Container {

    private final IModuleHolder holder;

    public ModuleContainer(IModuleHolder holder) {
        this.holder = holder;
    }

    @Override
    public int getContainerSize() {
        return IModuleHolder.MODULE_SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        return holder.getModuleSlots().stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return holder.getModuleSlots().get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = holder.getModuleSlots().get(slot);
        if (stack.isEmpty()) return ItemStack.EMPTY;
        ItemStack split = stack.split(amount);
        setChanged();
        return split;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack old = holder.getModuleSlots().get(slot);
        holder.getModuleSlots().set(slot, ItemStack.EMPTY);
        return old;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        holder.getModuleSlots().set(slot, stack);
        setChanged();
    }

    @Override
    public void setChanged() {
        // IModuleHolder が BlockEntity を実装している場合に通知
        if (holder instanceof net.minecraft.world.level.block.entity.BlockEntity be) {
            be.setChanged();
            if (be.getLevel() != null && !be.getLevel().isClientSide) {
                be.getLevel().sendBlockUpdated(be.getBlockPos(), be.getBlockState(), be.getBlockState(), 3);
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < getContainerSize(); i++) {
            holder.getModuleSlots().set(i, ItemStack.EMPTY);
        }
        setChanged();
    }
}
