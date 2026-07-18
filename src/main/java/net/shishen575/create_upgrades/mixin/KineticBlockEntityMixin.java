package net.shishen575.create_upgrades.mixin;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.shishen575.create_upgrades.api.IModuleHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KineticBlockEntity.class)
public abstract class KineticBlockEntityMixin implements IModuleHolder {

    @Unique
    private final NonNullList<ItemStack> create_upgrades$moduleSlots =
        NonNullList.withSize(MODULE_SLOT_COUNT, ItemStack.EMPTY);

    @Override
    public NonNullList<ItemStack> getModuleSlots() {
        return create_upgrades$moduleSlots;
    }

    @Inject(method = "write", at = @At("TAIL"))
    private void create_upgrades$onWrite(CompoundTag tag, boolean clientPacket, CallbackInfo ci) {
        saveModules(tag);
    }

    @Inject(method = "read", at = @At("TAIL"))
    private void create_upgrades$onRead(CompoundTag tag, boolean clientPacket, CallbackInfo ci) {
        loadModules(tag);
    }

    /**
     * 速度・効率モジュールの倍率を回転速度に適用する。
     * getTheoreticalSpeed は「現在の設計速度」を返す。
     */
    @Inject(method = "getTheoreticalSpeed", at = @At("RETURN"), cancellable = true)
    private void create_upgrades$modifySpeed(CallbackInfoReturnable<Float> cir) {
        float mult = getCombinedSpeedMultiplier();
        if (mult != 1.0f) {
            cir.setReturnValue(cir.getReturnValue() * mult);
        }
    }
}
