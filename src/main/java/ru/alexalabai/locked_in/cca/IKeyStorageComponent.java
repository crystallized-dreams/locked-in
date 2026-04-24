package ru.alexalabai.locked_in.cca;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.ComponentV3;

import java.util.List;

public interface IKeyStorageComponent extends ComponentV3 {
    boolean add(String id);
    boolean remove(String id);
    boolean contains(String id);
    String create();
    List<String> getAll();

    @Override
    default void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        if(nbt.contains("keys")) {
            var keys=nbt.getList("keys", NbtList.STRING_TYPE);
            for(var key : keys) {
                add(key.asString());
            }
        }
    }

    @Override
    default void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        NbtList keys=new NbtList();
        for(var key : getAll()) keys.add(NbtString.of(key));
        nbt.put("keys",keys);
    }
}
