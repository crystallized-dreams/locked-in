package ru.alexalabai.locked_in;

import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Utils {
    public static int getEnchantmentLevel(ItemStack stack, World world, RegistryKey<Enchantment> enchantment) {
        return stack.getEnchantments().getLevel(getEntry(world, enchantment));
    }
    public static RegistryEntry<Enchantment> getEntry(@Nullable World world, RegistryKey<Enchantment> enchantment) {
        if(world==null) world= MinecraftClient.getInstance().world;
        return world.getRegistryManager().getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(enchantment);
    }
    public static boolean hasEnchantmentLevel(ItemStack stack, World world, RegistryKey<Enchantment> enchantment) {
        return getEnchantmentLevel(stack, world, enchantment)>=1;
    }
}
