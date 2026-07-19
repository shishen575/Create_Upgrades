package net.shishen575.create_upgrades.mixin;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.shishen575.create_upgrades.api.IModuleHolder;
import net.shishen575.create_upgrades.api.IStackUpgradeable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 釜・デポなど IStackUpgradeable な機械にモジュールスロットを追加する。
 * SmartBlockEntity を継承している Create の非 Kinetic 機械に適用される。
 *
 * ターゲットを限定するため @Mixin は SmartBlockEntity にかけつつ、
 * 実際のスロット利用は IStackUpgradeable チェックで制限する。
 */
@Mixin(SmartBlockEntity.class)
public abstract class StorageBlockEntityMixin implements IModuleHolder {

    @Unique
    private final NonNullList<ItemStack> create_upgrades$moduleSlots =
        NonNullList.withSize(MODULE_SLOT_COUNT, ItemStack.EMPTY);

    @Override
    public NonNullList<ItemStack> getModuleSlots() {
        return create_upgrades$moduleSlots;
    }

    @Inject(method = "write", at = @At("TAIL"))
    private void create_upgrades$onWrite(CompoundTag tag, boolean clientPacket, CallbackInfo ci) {
        if ((Object) this instanceof IStackUpgradeable) saveModules(tag);
    }

    @Inject(method = "read", at = @At("TAIL"))
    private void create_upgrades$onRead(CompoundTag tag, boolean clientPacket, CallbackInfo ci) {
        if ((Object) this instanceof IStackUpgradeable) loadModules(tag);
    }
}
