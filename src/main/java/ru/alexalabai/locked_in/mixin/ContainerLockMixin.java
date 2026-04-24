package ru.alexalabai.locked_in.mixin;

import net.minecraft.inventory.ContainerLock;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.alexalabai.locked_in.LockedIn;

@Mixin(ContainerLock.class)
public class ContainerLockMixin {
    @Shadow @Final private String key;

    @Inject(method = "canOpen", at = @At("HEAD"), cancellable = true)
    void canOpen(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        if(key.isEmpty()) return;
        String keyId=stack.getOrDefault(LockedIn.KEY_ID,"");
        if(keyId.equals(key)||keyId.equals("master")) info.setReturnValue(true);
    }
}
