package amymialee.peculiarpieces.mixin;

import amymialee.peculiarpieces.util.RedstoneInstance;
import amymialee.peculiarpieces.util.RedstoneManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.RedstoneView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedstoneView.class)
public interface RedstoneViewMixin extends BlockView {

    @Inject(method = "isReceivingRedstonePower", at = @At("HEAD"), cancellable = true)
    default void PeculiarPieces$RedstoneInstances(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        var world = ((World) this);
        if (RedstoneManager.isPowered(world, pos)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getReceivedRedstonePower", at = @At("RETURN"), cancellable = true)
    default void PeculiarPieces$RedstoneInstancePower(BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        var world = ((World) this);
        var instancePower = RedstoneManager.getPower(world, pos);
        if (cir.getReturnValue() < instancePower) {
            cir.setReturnValue(instancePower);
        }
    }

    @Inject(method = "getReceivedStrongRedstonePower", at = @At("RETURN"), cancellable = true)
    default void PeculiarPieces$StrongRedstoneInstancePower(BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        var world = ((World) this);
        var instance = RedstoneManager.getInstance(world, pos);
        if (instance != null) {
            if (instance.isStrong()) {
                var instancePower = instance.getPower();
                if (cir.getReturnValue() < instancePower) {
                    cir.setReturnValue(instancePower);
                }
            }
        }
    }
}