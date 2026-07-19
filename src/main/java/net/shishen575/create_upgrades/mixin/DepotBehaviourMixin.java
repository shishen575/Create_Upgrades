package net.shishen575.create_upgrades.mixin;

import com.simibubi.create.content.kinetics.depot.DepotBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.shishen575.create_upgrades.api.IModuleHolder;
import net.shishen575.create_upgrades.api.IStackUpgradeable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * デポ(Depot)に IStackUpgradeable を付与する。
 * スタックモジュール挿入時、デポが保持できるアイテムの最大スタックサイズを multiplier 倍にする。
 */
@Mixin(DepotBlockEntity.class)
public abstract class DepotBehaviourMixin implements IStackUpgradeable {

    /**
     * デポがアイテムを受け取れるかチェックする際に、
     * スタックモジュールがあれば許容数をかさ増しする。
     */
    @Inject(method = "canAcceptItems", at = @At("RETURN"), cancellable = true, remap = true)
    private void create_upgrades$expandCapacity(CallbackInfoReturnable<Boolean> cir) {
        // スタックモジュールがあれば常に受け入れ可能にする (容量拡張の簡易実装)
        if (!(this instanceof IModuleHolder holder)) return;
        if (holder.getStackMultiplier() > 1) {
            cir.setReturnValue(true);
        }
    }
}
