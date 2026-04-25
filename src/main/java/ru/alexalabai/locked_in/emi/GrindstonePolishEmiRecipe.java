package ru.alexalabai.locked_in.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ru.alexalabai.locked_in.LockedIn;

import java.util.List;

public class GrindstonePolishEmiRecipe implements EmiRecipe {
    public static final EmiStack WORKSTATION=EmiStack.of(Items.GRINDSTONE);
    public static final EmiRecipeCategory CATEGORY=
            new EmiRecipeCategory(Identifier.of(LockedIn.MOD_ID,"grindstone_polish_ctg"),WORKSTATION);

    final ItemStack input;
    final ItemStack output;
    final Identifier id;

    public GrindstonePolishEmiRecipe(Identifier id, ItemStack input, ItemStack output) {
        this.id=id;
        this.input=input;
        this.output=output;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return CATEGORY;
    }
    @Override
    public @Nullable Identifier getId() {
        return id;
    }
    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(EmiIngredient.of(Ingredient.ofStacks(input)));
    }
    @Override
    public List<EmiStack> getOutputs() {
        return List.of(EmiStack.of(output));
    }

    @Override
    public int getDisplayWidth() {
        return 80;
    }
    @Override
    public int getDisplayHeight() {
        return 30;
    }

    @Override
    public void addWidgets(WidgetHolder ctx) {
        ctx.addSlot(getInputs().get(0),0, 7);
        ctx.addTexture(EmiTexture.EMPTY_ARROW, 34, 7);
        EmiUtils.addTexture(ctx,Identifier.of(LockedIn.MOD_ID,"textures/gui/grind.png"),19,7,16,16);
        ctx.addSlot(getOutputs().get(0),60,7).recipeContext(this);
    }
}
