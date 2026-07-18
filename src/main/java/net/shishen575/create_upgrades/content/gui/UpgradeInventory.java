package net.shishen575.create_upgrades.content.gui;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.shishen575.create_upgrades.api.IUpgradeable;
import net.shishen575.create_upgrades.content.upgrade.UpgradeItem;
import net.shishen575.create_upgrades.content.upgrade.UpgradeType;

/**
 * アップグレードスロット用の仮想インベントリ。
 * IUpgradeable の Map<UpgradeType, Integer> とスロットを橋渡しする。
 */
public class UpgradeInventory extends SimpleContainer {

    private final IUpgradeable upgradeable;
    private final UpgradeType type;

    public UpgradeInventory(IUpgradeable upgradeable, UpgradeType type) {
        super(1);
        this.upgradeable = upgradeable;
        this.type = type;

        int level = upgradeable.getUpgradeLevel(type);
        if (level > 0) {
            // 既存アップグレードをスロットに反映
            // 実際にはアイテム登録から引く必要があるが、ここでは簡略化
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        ItemStack stack = getItem(0);
        if (stack.isEmpty()) {
            upgradeable.removeUpgrade(type);
        } else if (stack.getItem() instanceof UpgradeItem ui && ui.getUpgradeType() == type) {
            upgradeable.setUpgrade(type, ui.getLevel());
        }
    }
}
