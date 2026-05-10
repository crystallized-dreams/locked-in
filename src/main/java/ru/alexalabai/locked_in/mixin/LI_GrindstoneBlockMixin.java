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
import ru.crystallized_dreams.interdimensionallib.common.ItemUtils;
import ru.alexalabai.locked_in.LockedIn;

@SuppressWarnings("unused")
@Mixin(GrindstoneBlock.class)
public abstract class LI_GrindstoneBlockMixin extends Block {
    @Shadow @Final private static Text TITLE;

    public LI_GrindstoneBlockMixin(Settings settings) {
        super(settings);
    }

    @Unique
    void processItem$locked_in(World world, BlockPos pos, PlayerEntity player, ItemStack stack) {
        if(world.isClient) return;
        player.getItemCooldownManager().set(stack.getItem(),10);
        stack.decrementUnlessCreative(1,player);
        world.playSound(null,pos,SoundEvents.BLOCK_GRINDSTONE_USE,SoundCategory.BLOCKS);
    }

    @Unique @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(player.getItemCooldownManager().isCoolingDown(stack.getItem())||
                !stack.isIn(LockedIn.KEYS)||
                stack.getOrDefault(LockedIn.KEY_ID,"").isEmpty()) return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
        if(!world.isClient()) {
            ItemStack cleanKey = stack.copy();
            cleanKey.remove(LockedIn.COPIED);
            cleanKey.remove(LockedIn.KEY_ID);
            processItem$locked_in(world, pos, player, stack);
            ItemUtils.tryToInsertStack(player, cleanKey);
        }
        return ItemActionResult.CONSUME;
    }
}
