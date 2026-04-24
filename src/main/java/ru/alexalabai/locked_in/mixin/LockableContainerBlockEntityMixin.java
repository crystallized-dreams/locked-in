package ru.alexalabai.locked_in.mixin;

import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LockableContainerBlockEntity.class)
public abstract class LockableContainerBlockEntityMixin {
    @Unique
    private boolean lockUnbreakable$li;
    @Unique
    private boolean lockUnbreachable$li;

    @Inject(method = "readNbt", at = @At("TAIL"))
    void readNbt$kc(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo ci) {
        lockUnbreakable$li =nbt.getBoolean("LockUnbreakable");
        lockUnbreachable$li =nbt.getBoolean("LockUnbreachable");
    }
    @Inject(method = "writeNbt", at = @At("TAIL"))
    void writeNbt$kc(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo ci) {
        nbt.putBoolean("LockUnbreakable", lockUnbreakable$li);
        nbt.putBoolean("LockUnbreachable", lockUnbreachable$li);
    }
}
