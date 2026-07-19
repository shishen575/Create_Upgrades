package net.shishen575.create_upgrades.mixin;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.shishen575.create_upgrades.api.IModuleHolder;
import net.shishen575.create_upgrades.api.IStackUpgradeable;
import net.shishen575.create_upgrades.content.module.StackModuleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * 釜(Basin)にスタックアップグレード対応を追加する。
 * IStackUpgradeable を実装してモジュールスロットに StackModuleItem を受け付ける。
 * スタックモジュールが挿入されると、レシピ出力を multiplier 倍にして返す。
 */
@Mixin(BasinBlockEntity.class)
public abstract class BasinBlockEntityMixin implements IStackUpgradeable {

    /**
     * レシピ出力アイテムリストを取得した直後にスタック倍率を適用する。
     * BasinBlockEntity#getResultItems() が呼ばれるたびに倍率をかける。
     */
    @Inject(method = "getResultItems", at = @At("RETURN"), cancellable = true, remap = true)
    private void create_upgrades$multiplyResults(CallbackInfoReturnable<List<ItemStack>> cir) {
        if (!(this instanceof IModuleHolder holder)) return;
        int mult = holder.getStackMultiplier();
        if (mult <= 1) return;

        List<ItemStack> results = cir.getReturnValue();
        List<ItemStack> amplified = results.stream()
            .map(stack -> {
                ItemStack copy = stack.copy();
                copy.setCount(Math.min(copy.getCount() * mult, copy.getMaxStackSize()));
                return copy;
            })
            .toList();
        cir.setReturnValue(amplified);
    }
}
