package ru.alexalabai.locked_in.emi;

import dev.emi.emi.api.EmiInitRegistry;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import net.minecraft.util.Identifier;
import ru.alexalabai.locked_in.LockedIn;

public class EmiIntegration implements EmiPlugin {
    @Override
    public void initialize(EmiInitRegistry registry) {
        EmiPlugin.super.initialize(registry);
    }

    @Override
    public void register(EmiRegistry registry) {
        registry.addRecipe(new KeyDuplicateEmiRecipe(Identifier.of(LockedIn.MOD_ID,"/key_duplication")));
    }
}
