package ru.alexalabai.locked_in.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ru.alexalabai.locked_in.LockedIn;

import java.util.List;

public class KeyDuplicateEmiRecipe implements EmiRecipe {
    final Identifier id;

    final EmiIngredient emptySchematic;
    final EmiIngredient variantSchematic;
    final EmiIngredient nametag;
    final EmiStack output;

    public KeyDuplicateEmiRecipe(Identifier id) {
        this.id = id;
        this.emptySchematic = EmiStack.of(new ItemStack(LockedIn.KEY))
                .setRemainder(EmiStack.EMPTY);

        ItemStack variantStack = new ItemStack(LockedIn.KEY);
        variantStack.set(LockedIn.KEY_ID, "your-key-id");
        this.variantSchematic = EmiStack.of(variantStack);

        this.nametag = EmiStack.of(Items.NAME_TAG);

        ItemStack outputStack = new ItemStack(LockedIn.KEY, 1);
        outputStack.set(LockedIn.KEY_ID, "your-key-id");
        outputStack.set(LockedIn.COPIED, true);
        this.output = EmiStack.of(outputStack);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return VanillaEmiRecipeCategories.CRAFTING;
    }

    @Override
    public @Nullable Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(emptySchematic, variantSchematic, nametag);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 118;
    }

    @Override
    public int getDisplayHeight() {
        return 54;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 60, 18);
        widgets.addTexture(EmiTexture.SHAPELESS, 97, 0);

        widgets.addSlot(emptySchematic, 0, 0);
        widgets.addSlot(variantSchematic, 18, 0);
        widgets.addSlot(nametag, 36, 0);
        widgets.addSlot(EmiStack.of(ItemStack.EMPTY), 0, 18);
        widgets.addSlot(EmiStack.of(ItemStack.EMPTY), 18, 18);
        widgets.addSlot(EmiStack.of(ItemStack.EMPTY), 36, 18);
        widgets.addSlot(EmiStack.of(ItemStack.EMPTY), 0, 36);
        widgets.addSlot(EmiStack.of(ItemStack.EMPTY), 18, 36);
        widgets.addSlot(EmiStack.of(ItemStack.EMPTY), 36, 36);

        widgets.addSlot(output, 92, 14).large(true).recipeContext(this);
    }

    @Override
    public boolean supportsRecipeTree() {
        return true;
    }
}
