package ru.alexalabai.locked_in;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record LockData(String id, boolean unbreakable, boolean unbreachable) {
    public static final Codec<LockData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("id").forGetter(LockData::id),
                    Codec.BOOL.fieldOf("unbreakable").forGetter(LockData::unbreakable),
                    Codec.BOOL.fieldOf("unbreachable").forGetter(LockData::unbreachable)
            ).apply(instance, LockData::new)
    );
}
