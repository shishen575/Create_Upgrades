package net.shishen575.create_upgrades.content.upgrade;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class UpgradeItem extends Item {

    private final UpgradeType upgradeType;
    private final int level;

    public UpgradeItem(UpgradeType upgradeType, int level, Properties properties) {
        super(properties);
        this.upgradeType = upgradeType;
        this.level = level;
    }

    public UpgradeType getUpgradeType() { return upgradeType; }
    public int getLevel() { return level; }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.create_upgrades.level", level));

        float speedMult = upgradeType.getSpeedMultiplier(level);
        float stressMult = upgradeType.getStressMultiplier(level);
        float processMult = upgradeType.getProcessingSpeedMultiplier(level);
        int rangeBonus = upgradeType.getRangeBonus(level);

        if (speedMult != 1.0f)
            tooltipComponents.add(Component.translatable("tooltip.create_upgrades.speed_mult",
                String.format("%.0f%%", speedMult * 100)));
        if (stressMult != 1.0f)
            tooltipComponents.add(Component.translatable("tooltip.create_upgrades.stress_mult",
                String.format("%.0f%%", stressMult * 100)));
        if (processMult != 1.0f)
            tooltipComponents.add(Component.translatable("tooltip.create_upgrades.process_mult",
                String.format("%.0f%%", processMult * 100)));
        if (rangeBonus != 0)
            tooltipComponents.add(Component.translatable("tooltip.create_upgrades.range_bonus", rangeBonus));
    }
}
