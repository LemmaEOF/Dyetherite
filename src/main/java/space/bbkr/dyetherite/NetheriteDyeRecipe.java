package space.bbkr.dyetherite;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.*;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetheriteDyeRecipe extends SpecialCraftingRecipe {
	private static final Map<Item, Item> REGULAR_TO_DYED = new HashMap<>();

	public NetheriteDyeRecipe(Identifier id) {
		super(id);
	}

	public boolean matches(CraftingInventory craftingInventory, World world) {
		ItemStack usedStack = ItemStack.EMPTY;
		List<ItemStack> ingredients = Lists.newArrayList();

		for(int i = 0; i < craftingInventory.size(); ++i) {
			ItemStack stack = craftingInventory.getStack(i);
			if (!stack.isEmpty()) {

				if (REGULAR_TO_DYED.containsKey(stack.getItem())) {
					if (!usedStack.isEmpty()) {
						return false;
					}

					usedStack = stack;
				} else {
					if (!(stack.getItem() instanceof DyeItem)) {
						return false;
					}

					ingredients.add(stack);
				}
			}
		}

		return !usedStack.isEmpty() && !ingredients.isEmpty();
	}

	public ItemStack craft(CraftingInventory craftingInventory) {
		List<DyeItem> dyes = Lists.newArrayList();
		ItemStack outStack = ItemStack.EMPTY;

		for(int i = 0; i < craftingInventory.size(); ++i) {
			ItemStack stack = craftingInventory.getStack(i);
			if (!stack.isEmpty()) {
				Item item = stack.getItem();
				if (REGULAR_TO_DYED.containsKey(item)) {
					if (!outStack.isEmpty()) {
						return ItemStack.EMPTY;
					}

					outStack = new ItemStack(REGULAR_TO_DYED.get(item));
					outStack.setTag(stack.getOrCreateTag().copy());
				} else {
					if (!(item instanceof DyeItem)) {
						return ItemStack.EMPTY;
					}

					dyes.add((DyeItem)item);
				}
			}
		}

		if (!outStack.isEmpty() && !dyes.isEmpty()) {
			return DyeableItem.blendAndSetColor(outStack, dyes);
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Environment(EnvType.CLIENT)
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Dyetherite.SERIALIZER;
	}

	static {
		REGULAR_TO_DYED.put(Items.NETHERITE_HELMET, Dyetherite.DYED_NETHERITE_HELMET);
		REGULAR_TO_DYED.put(Items.NETHERITE_CHESTPLATE, Dyetherite.DYED_NETHERITE_CHESTPLATE);
		REGULAR_TO_DYED.put(Items.NETHERITE_LEGGINGS, Dyetherite.DYED_NETHERITE_LEGGINGS);
		REGULAR_TO_DYED.put(Items.NETHERITE_BOOTS, Dyetherite.DYED_NETHERITE_BOOTS);
	}
}
