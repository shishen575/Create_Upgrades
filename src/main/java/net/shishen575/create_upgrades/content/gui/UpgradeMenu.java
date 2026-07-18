package net.shishen575.create_upgrades.content.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.shishen575.create_upgrades.api.IUpgradeable;
import net.shishen575.create_upgrades.content.upgrade.UpgradeItem;
import net.shishen575.create_upgrades.content.upgrade.UpgradeType;
import net.shishen575.create_upgrades.registry.CUMenuTypes;

/**
 * アップグレード管理GUI のメニュー。
 * アップグレードスロット (UpgradeType ごとに1スロット) + プレイヤーインベントリ。
 */
public class UpgradeMenu extends AbstractContainerMenu {

    private final BlockPos targetPos;
    private final IUpgradeable upgradeable;

    public UpgradeMenu(int id, Inventory playerInv, BlockPos pos, IUpgradeable upgradeable) {
        super(CUMenuTypes.UPGRADE_MENU.get(), id);
        this.targetPos = pos;
        this.upgradeable = upgradeable;

        UpgradeType[] types = UpgradeType.values();
        for (int i = 0; i < types.length; i++) {
            final UpgradeType type = types[i];
            addSlot(new Slot(new UpgradeInventory(upgradeable, type), i, 44 + i * 22, 35) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() instanceof UpgradeItem ui && ui.getUpgradeType() == type;
                }
            });
        }

        // プレイヤーインベントリ
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
        for (int col = 0; col < 9; col++)
            addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
    }

    /** FriendlyByteBuf から復元するコンストラクタ (Forge IForgeMenuType / クライアント側) */
    public UpgradeMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, buf.readBlockPos(), findUpgradeable(playerInv, buf.readBlockPos()));
    }

    private static IUpgradeable findUpgradeable(Inventory inv, BlockPos pos) {
        BlockEntity be = inv.player.level().getBlockEntity(pos);
        return be instanceof IUpgradeable u ? u : IUpgradeable.EMPTY;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.distanceToSqr(
            targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5) < 64.0;
    }
}
