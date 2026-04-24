package ru.alexalabai.locked_in;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class KeyDuplicationRecipe extends SpecialCraftingRecipe {
    public KeyDuplicationRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        int keyWithId=0;
        int emptyKey =0;
        int nametags=0;

        for (int i = 0; i < input.getStacks().size(); i++) {
            ItemStack stack=input.getStackInSlot(i);
            if(stack.isEmpty()) continue;

            if(stack.isOf(LockedIn.KEY)) {
                boolean isCopied=stack.getOrDefault(LockedIn.COPIED,false);
                if(stack.contains(LockedIn.KEY_ID)&&!isCopied) keyWithId++;
                else if(!isCopied) emptyKey++;
            } else if(stack.isOf(Items.NAME_TAG)) nametags++;
            else return false;
        }
        return keyWithId == 1 && emptyKey == 1 && nametags == 1;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        String keyId = "";
        for (int i = 0; i < input.getStacks().size(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (stack.isOf(LockedIn.KEY) && stack.contains(LockedIn.KEY_ID)) {
                keyId = stack.get(LockedIn.KEY_ID);
                break;
            }
        }

        ItemStack result = new ItemStack(LockedIn.KEY, 1);
        result.set(LockedIn.KEY_ID, keyId);
        result.set(LockedIn.COPIED, true);
        return result;
    }

    @Override
    public DefaultedList<ItemStack> getRemainder(CraftingRecipeInput input) {
        DefaultedList<ItemStack> remainders=DefaultedList.ofSize(input.getSize(),ItemStack.EMPTY);
        int originalKeySlotId=-1;
        for (int i = 0; i < input.getStacks().size(); i++) {
            ItemStack stack=input.getStackInSlot(i);
            if(stack.isEmpty()) continue;

            if(stack.isOf(LockedIn.KEY)&&stack.contains(LockedIn.KEY_ID)) {
                originalKeySlotId=i;
                break;
            }
        }
        if(originalKeySlotId>=0) remainders.set(originalKeySlotId,input.getStackInSlot(originalKeySlotId).copy());
        return remainders;
    }

    @Override
    public boolean fits(int width, int height) {
        return width+height>=3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return LockedIn.KEY_DUPLICATION_RECIPE_SERIALIZER;
    }
}
