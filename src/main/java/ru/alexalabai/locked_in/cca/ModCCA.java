package ru.alexalabai.locked_in.cca;

import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;
import ru.alexalabai.locked_in.LockedIn;

public class ModCCA implements WorldComponentInitializer {
    public static final ComponentKey<KeyStorageComponent> KEY_STORAGE_COMPONENT=reg("key_storage", KeyStorageComponent.class);
    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry reg) {
        reg.register(KEY_STORAGE_COMPONENT,world->new KeyStorageComponent());
    }

    private static <T extends Component> ComponentKey<T> reg(String name, Class<T> tClass) {
        return ComponentRegistry.getOrCreate(Identifier.of(LockedIn.MOD_ID,name),tClass);
    }
}
