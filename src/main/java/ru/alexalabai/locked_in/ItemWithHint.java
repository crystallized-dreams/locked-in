package ru.alexalabai.locked_in;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class ItemWithHint extends Item {
    public ItemWithHint(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext ctx, List<Text> tooltip, TooltipType type) {
        beforeHint(stack,ctx,tooltip);
        if(Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("text.locked-in.hide_hint",
                    /*ClientGameOptions.hintKey.getBoundKeyLocalizedText()*/"Shift").formatted(Formatting.YELLOW));
            showHint(stack,ctx,tooltip);
        } else
            tooltip.add(Text.translatable("text.locked-in.hint",
                    /*ClientGameOptions.hintKey.getBoundKeyLocalizedText()*/"Shift").formatted(Formatting.YELLOW));
        super.appendTooltip(stack, ctx, tooltip, type);
    }

    public void beforeHint(ItemStack stack, TooltipContext ctx, List<Text> tooltip) { }
    public void showHint(ItemStack stack, TooltipContext ctx, List<Text> tooltip) { }
}
