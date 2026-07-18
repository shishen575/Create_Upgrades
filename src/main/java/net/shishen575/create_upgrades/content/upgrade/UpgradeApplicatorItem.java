package net.shishen575.create_upgrades.content.upgrade;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.shishen575.create_upgrades.api.IUpgradeable;

import java.util.List;

/**
 * アップグレード設定ツール。
 * Create の機械 (IUpgradeable) を右クリックすると
 * アップグレード管理GUIを開く。
 */
public class UpgradeApplicatorItem extends Item {

    public UpgradeApplicatorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof IUpgradeable upgradeable)) {
            player.displayClientMessage(
                Component.translatable("item.create_upgrades.applicator.not_supported"), true);
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            net.shishen575.create_upgrades.content.gui.UpgradeMenu.open(serverPlayer, pos);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
            List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.create_upgrades.applicator"));
    }
}
