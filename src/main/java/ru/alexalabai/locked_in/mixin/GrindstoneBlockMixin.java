package ru.alexalabai.locked_in.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.GrindstoneBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import ru.alexalabai.locked_in.LockedIn;
import ru.alexalabai.locked_in.Utils;

@SuppressWarnings("unused")
@Mixin(GrindstoneBlock.class)
public abstract class GrindstoneBlockMixin extends Block {
    @Shadow @Final private static Text TITLE;

    public GrindstoneBlockMixin(Settings settings) {
        super(settings);
    }

    @Unique
    void processItem(World world, BlockPos pos, PlayerEntity player, ItemStack stack) {
        if(world.isClient) return;
        player.getItemCooldownManager().set(stack.getItem(),10);
        stack.decrementUnlessCreative(1,player);
        world.playSound(null,pos,SoundEvents.BLOCK_GRINDSTONE_USE,SoundCategory.BLOCKS);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(player.getItemCooldownManager().isCoolingDown(stack.getItem())||
                !stack.isIn(LockedIn.KEYS)||
                stack.getOrDefault(LockedIn.KEY_ID,"").isEmpty()) return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
        if(!world.isClient()) {
            ItemStack cleanKey = stack.copy();
            cleanKey.remove(LockedIn.COPIED);
            cleanKey.set(LockedIn.KEY_ID, "");
            processItem(world, pos, player, stack);
            Utils.tryToInsertStack(player, cleanKey);
        }
        return ItemActionResult.CONSUME;
    }
}
