package ru.alexalabai.locked_in;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.alexalabai.locked_in.cca.IKeyStorageComponent;
import ru.alexalabai.locked_in.cca.ModCCA;

import java.util.List;

public class KeyItem extends ItemWithHint {
    public KeyItem(Item.Settings settings) {
        super(settings.component(LockedIn.COPIED,false));
    }

    @Override
    public int getEnchantability() {
        return 10;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        BlockPos pos=ctx.getBlockPos();
        BlockState state=ctx.getWorld().getBlockState(pos);
        PlayerEntity player=ctx.getPlayer();
        World world=ctx.getWorld();
        if(player!=null&&!world.isClient) {
            if(state.isIn(LockedIn.LOCKABLE_CONTAINERS) && player.isSneaking()) {
                String curId=ctx.getStack().getOrDefault(LockedIn.KEY_ID,"");
                if(curId.equals("master")) return ActionResult.FAIL;
                boolean isPicklock=curId.equals("lockpick");
                boolean isUnbreachable=Utils.hasEnchantmentLevel(ctx.getStack(),world,LockedIn.LOCK_UNBREACHABLE);
                boolean isUnbreakable=Utils.hasEnchantmentLevel(ctx.getStack(),world,LockedIn.LOCK_UNBREAKABLE);
                BlockEntity blockEntity=world.getBlockEntity(pos);
                if(blockEntity==null) return super.useOnBlock(ctx);
                NbtCompound nbt=blockEntity.createNbt(world.getRegistryManager());
                IKeyStorageComponent keyStorage=ModCCA.KEY_STORAGE_COMPONENT.get(world);
                if(nbt.contains("Lock", NbtElement.STRING_TYPE)) {
                    if(!curId.equals(nbt.getString("Lock"))&&!isPicklock) {
                        player.sendMessage(Text.translatable("text.locked-in.key.invalid").formatted(Formatting.RED),true);
                        return ActionResult.FAIL;
                    }
                    boolean isContainerUnbreachable=nbt.contains("LockUnbreachable")?nbt.getBoolean("LockUnbreachable"):false;
                    if((ctx.getWorld().random.nextInt(8)!=1||isContainerUnbreachable)&&isPicklock) {
                        if(isContainerUnbreachable) player.sendMessage(Text.translatable("text.locked-in.key.unbreachable").formatted(Formatting.RED),true);
                        if(!world.isClient()) world.playSound(null,pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS,0.5f,1);
                        ctx.getStack().decrementUnlessCreative(1,player);
                        return ActionResult.FAIL;
                    }
                    nbt.remove("Lock");
                    blockEntity.read(nbt,world.getRegistryManager());
                    blockEntity.markDirty();
                    player.sendMessage(Text.translatable("text.locked-in.key.unlocked",
                            Text.translatable(state.getBlock().getTranslationKey()).getString()),true);
                    if(!world.isClient()) world.playSound(null,pos,LockedIn.ITEM_KEY_LOCK_SOUND, SoundCategory.BLOCKS, 1.0F, 1.0F);
                } else {
                    if(isPicklock) return super.useOnBlock(ctx);
                    String newId=curId.isEmpty()?keyStorage.create():curId;
                    ctx.getStack().set(LockedIn.KEY_ID,newId);
                    nbt.putString("Lock",newId);
                    nbt.putBoolean("LockUnbreachable",isUnbreachable);
                    nbt.putBoolean("LockUnbreakable",isUnbreakable);
                    blockEntity.read(nbt,world.getRegistryManager());
                    blockEntity.markDirty();
                    player.sendMessage(Text.translatable("text.locked-in.key.locked",
                            Text.translatable(state.getBlock().getTranslationKey()).getString()),true);
                    if(!world.isClient()) world.playSound(null,pos,LockedIn.ITEM_KEY_LOCK_SOUND, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                return ActionResult.SUCCESS;
            }
        }
        return super.useOnBlock(ctx);
    }

    @Override
    public void beforeHint(ItemStack stack, Item.TooltipContext ctx, List<Text> tooltip) {
        if(stack.contains(LockedIn.KEY_ID)) {
            var id=stack.getOrDefault(LockedIn.KEY_ID,"Invalid");
            if(!id.equals("master")&&!id.equals("lockpick")) tooltip.add(Text.literal(id).formatted(Formatting.GRAY));
            if(stack.getOrDefault(LockedIn.COPIED,false)) tooltip.add(Text.translatable("text.locked-in.key.copy").formatted(Formatting.GRAY));
        }
    }
    @Override
    public void showHint(ItemStack stack, Item.TooltipContext ctx, List<Text> tooltip) {
        var id=stack.getOrDefault(LockedIn.KEY_ID,"");
        if(id.equals("master")) tooltip.add(Text.translatable("text.locked-in.key.tooltip_2"));
        else if(id.equals("lockpick")) tooltip.add(Text.translatable("text.locked-in.key.tooltip_3"));
        else {
            tooltip.add(Text.translatable("text.locked-in.key.tooltip_0"));
            tooltip.add(Text.translatable("text.locked-in.key.tooltip_1"));
        }
    }
}

