package net.shishen575.create_upgrades.content.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.IContainerFactory;
import net.shishen575.create_upgrades.api.IUpgradeable;
import net.shishen575.create_upgrades.content.upgrade.UpgradeItem;
import net.shishen575.create_upgrades.content.upgrade.UpgradeType;
import net.shishen575.create_upgrades.registry.CUMenuTypes;

/**
 * アップグレード管理GUI のメニュー。
 * アップグレードスロット (UpgradeType ごとに1スロット) + プレイヤーインベントリ。
 */
public class UpgradeMenu extends AbstractContainerMenu {

    public static final int UPGRADE_SLOTS = UpgradeType.values().length;

    private final BlockPos targetPos;
    private final IUpgradeable upgradeable;

    public static void open(ServerPlayer player, BlockPos pos) {
        BlockEntity be = player.level().getBlockEntity(pos);
        if (!(be instanceof IUpgradeable)) return;
        player.openMenu(new net.minecraft.world.MenuProvider() {
            @Override
            public net.minecraft.network.chat.Component getDisplayName() {
                return net.minecraft.network.chat.Component.translatable("gui.create_upgrades.title");
            }

            @Override
            public AbstractContainerMenu createMenu(int id, Inventory inv, Player p) {
                return new UpgradeMenu(id, inv, pos, (IUpgradeable) be);
            }
        }, buf -> buf.writeBlockPos(pos));
    }

    public UpgradeMenu(int id, Inventory playerInv, BlockPos pos, IUpgradeable upgradeable) {
        super(CUMenuTypes.UPGRADE_MENU.get(), id);
        this.targetPos = pos;
        this.upgradeable = upgradeable;

        // アップグレードスロット (1スロット per UpgradeType)
        UpgradeType[] types = UpgradeType.values();
        for (int i = 0; i < types.length; i++) {
            final UpgradeType type = types[i];
            final int slotIndex = i;
            addSlot(new Slot(new UpgradeInventory(upgradeable, type), slotIndex, 44 + i * 22, 35) {
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

    /** FriendlyByteBuf から復元するコンストラクタ (クライアント側) */
    public UpgradeMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, buf.readBlockPos(), findUpgradeable(playerInv, buf));
    }

    private static IUpgradeable findUpgradeable(Inventory inv, FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
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
