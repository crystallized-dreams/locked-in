package ru.alexalabai.locked_in.emi;

import dev.emi.emi.api.EmiInitRegistry;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import ru.crystallized_dreams.interdimensionallib.emi.recipe.GrindstonePolishEmiRecipe;
import ru.crystallized_dreams.interdimensionallib.recipe.all.GrindstonePolishRecipe;
import ru.alexalabai.locked_in.LockedIn;

public class EmiIntegration implements EmiPlugin {
    @Override
    public void initialize(EmiInitRegistry registry) {
        EmiPlugin.super.initialize(registry);
    }

    @Override
    public void register(EmiRegistry registry) {
        registry.addRecipe(new KeyDuplicateEmiRecipe(Identifier.of(LockedIn.MOD_ID,"/key_duplication")));
        ItemStack fullKey=LockedIn.KEY.getDefaultStack();
        fullKey.set(LockedIn.KEY_ID,"your-key-id");
        registry.addRecipe(new GrindstonePolishEmiRecipe(new GrindstonePolishRecipe(Identifier.of(LockedIn.MOD_ID,"/clean_key"),Ingredient.ofStacks(fullKey),LockedIn.KEY.getDefaultStack())));
    }
}
