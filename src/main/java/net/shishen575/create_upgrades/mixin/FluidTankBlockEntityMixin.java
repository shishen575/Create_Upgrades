package net.shishen575.create_upgrades.mixin;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.shishen575.create_upgrades.api.IFluidCapacityUpgradeable;
import net.shishen575.create_upgrades.api.IModuleHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * FluidTankBlockEntity を IFluidCapacityUpgradeable としてマーク。
 * モジュールスロット自体は StorageBlockEntityMixin (SmartBlockEntity) が提供する。
 *
 * 容量注入: Create の FluidTankBlockEntity が公開する容量ゲッターに @Inject する。
 * require=0 にしているので、メソッドが見つからなくてもクラッシュしない。
 * Create のバージョンによってメソッド名が異なる場合は調整すること。
 *
 * 候補メソッド (どれか一つが存在する):
 *   - getTankCapacity(int)  → IFluidHandler 実装
 *   - getCapacity()         → 内部ヘルパー
 */
@Mixin(FluidTankBlockEntity.class)
public abstract class FluidTankBlockEntityMixin implements IFluidCapacityUpgradeable {

    // --- 容量 × モジュール倍率 ---

    @Inject(method = "getTankCapacity", at = @At("RETURN"), cancellable = true, require = 0)
    private void create_upgrades$multiplyTankCapacity(int tank, CallbackInfoReturnable<Long> cir) {
        if ((Object) this instanceof IModuleHolder holder) {
            int mult = holder.getFluidCapacityMultiplier();
            if (mult > 1) cir.setReturnValue(cir.getReturnValue() * mult);
        }
    }

    // Forge 1.20.1 compat: getTankCapacity returns int
    @Inject(method = "getTankCapacity(I)I", at = @At("RETURN"), cancellable = true, require = 0)
    private void create_upgrades$multiplyTankCapacityInt(int tank, CallbackInfoReturnable<Integer> cir) {
        if ((Object) this instanceof IModuleHolder holder) {
            int mult = holder.getFluidCapacityMultiplier();
            if (mult > 1) cir.setReturnValue(cir.getReturnValue() * mult);
        }
    }
}
