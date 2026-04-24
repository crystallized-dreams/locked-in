package ru.alexalabai.locked_in;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class LockedInClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelPredicateProviderRegistry.register(LockedIn.KEY, Identifier.of(LockedIn.MOD_ID,"copied"),(stack, world, entity, seed)->stack.getOrDefault(LockedIn.COPIED,false)?1:0);
        LockedIn.LOGGER.info("[LOCKEDIN]: Initialized client.");
    }
}
