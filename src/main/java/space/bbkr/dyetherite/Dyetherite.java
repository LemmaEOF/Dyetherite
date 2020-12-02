package space.bbkr.dyetherite;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.Registry;

public class Dyetherite implements ModInitializer {
	public static final String MODID = "dyetherite";

	public static final Item DYED_NETHERITE_HELMET = register("dyed_netherite_helmet",
			new DyeableArmorItem(DyedNetheriteArmorMaterial.INSTANCE, EquipmentSlot.HEAD, new Item.Settings().fireproof()));

	public static final Item DYED_NETHERITE_CHESTPLATE = register("dyed_netherite_chestplate",
			new DyeableArmorItem(DyedNetheriteArmorMaterial.INSTANCE, EquipmentSlot.CHEST, new Item.Settings().fireproof()));

	public static final Item DYED_NETHERITE_LEGGINGS = register("dyed_netherite_leggings",
			new DyeableArmorItem(DyedNetheriteArmorMaterial.INSTANCE, EquipmentSlot.LEGS, new Item.Settings().fireproof()));

	public static final Item DYED_NETHERITE_BOOTS = register("dyed_netherite_boots",
			new DyeableArmorItem(DyedNetheriteArmorMaterial.INSTANCE, EquipmentSlot.FEET, new Item.Settings().fireproof()));

	public static final RecipeSerializer<NetheriteDyeRecipe> SERIALIZER = register("crafting_special_netheritedye",
			new SpecialRecipeSerializer<>(NetheriteDyeRecipe::new));

	@Override
	public void onInitialize() { }

	private static Item register(String name, Item item) {
		return Registry.register(Registry.ITEM, new Identifier(MODID, name), item);
	}

	private static <T extends Recipe<?>> RecipeSerializer<T> register(String name, RecipeSerializer<T> serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MODID, name), serializer);
	}

	private static class DyedNetheriteArmorMaterial implements ArmorMaterial {
		private static final DyedNetheriteArmorMaterial INSTANCE = new DyedNetheriteArmorMaterial();
		private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
		private static final int[] PROTECTION_AMOUNTS = new int[]{3, 6, 8, 3};
		private static final Lazy<Ingredient> INGREDIENT = new Lazy<>(() -> Ingredient.ofItems(Items.NETHERITE_INGOT));

		@Override
		public int getDurability(EquipmentSlot slot) {
			return BASE_DURABILITY[slot.getEntitySlotId()] * 37;
		}

		@Override
		public int getProtectionAmount(EquipmentSlot slot) {
			return PROTECTION_AMOUNTS[slot.getEntitySlotId()];
		}

		@Override
		public int getEnchantability() {
			return 15;
		}

		@Override
		public SoundEvent getEquipSound() {
			return SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return INGREDIENT.get();
		}

		@Override
		public String getName() {
			return "dyed_netherite";
		}

		@Override
		public float getToughness() {
			return 3;
		}

		@Override
		public float getKnockbackResistance() {
			return 0.1f;
		}
	}
}
