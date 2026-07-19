package net.shishen575.create_upgrades.content.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.shishen575.create_upgrades.api.IModuleHolder;
import net.shishen575.create_upgrades.api.IFluidCapacityUpgradeable;
import net.shishen575.create_upgrades.api.IStackUpgradeable;
import net.shishen575.create_upgrades.content.module.MachineModuleItem;
import net.shishen575.create_upgrades.content.module.StackModuleItem;
import net.shishen575.create_upgrades.content.module.TankModuleItem;
import net.shishen575.create_upgrades.registry.CUMenuTypes;

/**
 * モジュールスロット管理GUI。
 * 3 つのモジュールスロット + プレイヤーインベントリ。
 */
public class ModuleMenu extends AbstractContainerMenu {

    private final BlockPos targetPos;
    private final IModuleHolder holder;

    public static void open(ServerPlayer player, BlockPos pos) {
        BlockEntity be = player.level().getBlockEntity(pos);
        if (!(be instanceof IModuleHolder holder)) return;
        player.openMenu(new MenuProvider() {
            @Override
            public net.minecraft.network.chat.Component getDisplayName() {
                return net.minecraft.network.chat.Component.translatable("gui.create_upgrades.title");
            }

            @Override
            public AbstractContainerMenu createMenu(int id, Inventory inv, Player p) {
                return new ModuleMenu(id, inv, pos, holder);
            }
        }, buf -> buf.writeBlockPos(pos));
    }

    public ModuleMenu(int id, Inventory playerInv, BlockPos pos, IModuleHolder holder) {
        super(CUMenuTypes.MODULE_MENU.get(), id);
        this.targetPos = pos;
        this.holder = holder;

        // モジュールスロッ�� (3つ, 横並び, GUI 中央上部)
        NonNullList<ItemStack> slots = holder.getModuleSlots();
        boolean isStackCapable = holder instanceof IStackUpgradeable;
        boolean isTankCapable = holder instanceof IFluidCapacityUpgradeable;
        ModuleContainer container = new ModuleContainer(holder);
        for (int i = 0; i < IModuleHolder.MODULE_SLOT_COUNT; i++) {
            addSlot(new Slot(container, i, 53 + i * 22, 20) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    if (!(stack.getItem() instanceof MachineModuleItem)) return false;
                    // スタックモジュールは IStackUpgradeable な機械にのみ挿入可能
                    if (stack.getItem() instanceof StackModuleItem && !isStackCapable) return false;
                    if (stack.getItem() instanceof TankModuleItem && !isTankCapable) return false;
                    return true;
                }
            });
        }

        // プレイヤーインベントリ (3行)
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 56 + row * 18));
        // ホットバー
        for (int col = 0; col < 9; col++)
            addSlot(new Slot(playerInv, col, 8 + col * 18, 114));
    }

    /** FriendlyByteBuf から復元 (NeoForge クライアント側) */
    public ModuleMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, buf.readBlockPos(), resolveHolder(playerInv, buf.readBlockPos()));
    }

    private static IModuleHolder resolveHolder(Inventory inv, BlockPos pos) {
        BlockEntity be = inv.player.level().getBlockEntity(pos);
        return be instanceof IModuleHolder h ? h : IModuleHolder.EMPTY;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return result;

        ItemStack slotStack = slot.getItem();
        result = slotStack.copy();

        int moduleSlots = IModuleHolder.MODULE_SLOT_COUNT;
        if (index < moduleSlots) {
            // モジュールスロット → プレイヤーへ
            if (!moveItemStackTo(slotStack, moduleSlots, slots.size(), true)) return ItemStack.EMPTY;
        } else {
            // プレイヤー → モジュールスロットへ (MachineModuleItem のみ)
            if (slotStack.getItem() instanceof MachineModuleItem) {
                if (!moveItemStackTo(slotStack, 0, moduleSlots, false)) return ItemStack.EMPTY;
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (slotStack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();

        slot.onTake(player, slotStack);
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.distanceToSqr(
            targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5) < 64.0;
    }
}
