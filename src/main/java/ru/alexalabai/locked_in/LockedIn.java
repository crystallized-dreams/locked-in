package ru.alexalabai.locked_in;

import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockedIn implements ModInitializer {
	public static final String MOD_ID = "locked-in";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ComponentType<String> KEY_ID=ComponentType.<String>builder().codec(Codec.STRING).build();
	public static final ComponentType<Boolean> COPIED=ComponentType.<Boolean>builder().codec(Codec.BOOL).build();

	public static Item KEY=regItem("key",
			new KeyItem(new Item.Settings().maxCount(1)));
	public static Item MASTER_KEY=regItem("master_key",
			new KeyItem(new Item.Settings().maxCount(1).component(KEY_ID,"master")));
	public static Item LOCKPICK =regItem("lockpick",
			new KeyItem(new Item.Settings().maxCount(16).component(KEY_ID,"lockpick")));

	public static final RegistryKey<Enchantment> LOCK_UNBREACHABLE=enchantmentOf("lock_unbreachable");
	public static final RegistryKey<Enchantment> LOCK_UNBREAKABLE=enchantmentOf("lock_unbreakable");

	public static final TagKey<Block> LOCKABLE_CONTAINERS=
			TagKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, "lockable_containers"));

	static final Identifier ITEM_KEY_LOCK_SOUND_ID=Identifier.of(MOD_ID, "item.key.lock");
	public static SoundEvent ITEM_KEY_LOCK_SOUND=SoundEvent.of(ITEM_KEY_LOCK_SOUND_ID);

	public static final RecipeSerializer<KeyDuplicationRecipe> KEY_DUPLICATION_RECIPE_SERIALIZER=
			RecipeSerializer.register("locked-in:key_duplication", new SpecialRecipeSerializer<>(KeyDuplicationRecipe::new));
	public static final RecipeType<KeyDuplicationRecipe> KEY_DUPLICATION_RECIPE=registerRecipe( "key_duplication");

	@Override
	public void onInitialize() {
		regComp("key_id", KEY_ID);
		regComp("copied", COPIED);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(group->{
			group.add(KEY);
			group.add(MASTER_KEY);
			group.add(LOCKPICK);
		});
		PlayerBlockBreakEvents.BEFORE.register(((world, playerEntity, blockPos, blockState, blockEntity) -> {
			if(blockState.isIn(LOCKABLE_CONTAINERS)&&blockEntity!=null) {
				if(playerEntity.isCreative()) return true;
				NbtCompound nbt=blockEntity.createNbt(world.getRegistryManager());
				boolean stat=nbt.contains("LockUnbreakable")?!nbt.getBoolean("LockUnbreakable"):true;
				if(!stat) playerEntity.sendMessage(Text.translatable("text.locked-in.key.unbreakable").formatted(Formatting.RED),true);
				return stat;
			}
			return true;
		}));
		LOGGER.info("[LOCKEDIN]: Initialized common sources.");
	}

	public static Item regItem(String name, Item item) {
		return Registry.register(Registries.ITEM, Identifier.of(MOD_ID, name), item);
	}
	private static void regComp(String name, ComponentType<?> comp) {
		Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(MOD_ID,name), comp);
	}
	static <T extends Recipe<?>> RecipeType<T> registerRecipe(String id) {
		return Registry.register(Registries.RECIPE_TYPE, Identifier.of(MOD_ID,id), new RecipeType<T>() {
			public String toString() {
				return id;
			}
		});
	}
	static RegistryKey<Enchantment> enchantmentOf(String path) {
		Identifier id = Identifier.of(MOD_ID, path);
		return RegistryKey.of(RegistryKeys.ENCHANTMENT, id);
	}
}