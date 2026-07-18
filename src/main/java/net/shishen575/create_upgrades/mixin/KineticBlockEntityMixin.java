package net.shishen575.create_upgrades.mixin;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.shishen575.create_upgrades.api.IUpgradeable;
import net.shishen575.create_upgrades.content.upgrade.UpgradeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.EnumMap;
import java.util.Map;

@Mixin(KineticBlockEntity.class)
public abstract class KineticBlockEntityMixin implements IUpgradeable {

    private final Map<UpgradeType, Integer> create_upgrades$upgrades = new EnumMap<>(UpgradeType.class);

    @Override
    public Map<UpgradeType, Integer> getUpgrades() {
        return create_upgrades$upgrades;
    }

    @Override
    public void setUpgrade(UpgradeType type, int level) {
        if (level <= 0) create_upgrades$upgrades.remove(type);
        else create_upgrades$upgrades.put(type, Math.min(level, type.getMaxLevel()));
    }

    @Override
    public void removeUpgrade(UpgradeType type) {
        create_upgrades$upgrades.remove(type);
    }

    @Inject(method = "write", at = @At("TAIL"))
    private void create_upgrades$onWrite(CompoundTag tag, boolean clientPacket, CallbackInfo ci) {
        saveUpgrades(tag);
    }

    @Inject(method = "read", at = @At("TAIL"))
    private void create_upgrades$onRead(CompoundTag tag, boolean clientPacket, CallbackInfo ci) {
        loadUpgrades(tag);
    }

    /** 速度を返すメソッドにアップグレード倍率を適用 */
    @Inject(method = "getTheoreticalSpeed", at = @At("RETURN"), cancellable = true)
    private void create_upgrades$modifySpeed(CallbackInfoReturnable<Float> cir) {
        int speedLevel = getUpgradeLevel(UpgradeType.SPEED);
        if (speedLevel > 0) {
            cir.setReturnValue(cir.getReturnValue() * UpgradeType.SPEED.getSpeedMultiplier(speedLevel));
        }
    }
}
