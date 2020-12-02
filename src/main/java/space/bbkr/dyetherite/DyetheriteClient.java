package space.bbkr.dyetherite;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.item.DyeableItem;

public class DyetheriteClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ColorProviderRegistry.ITEM.register(
				(stack, tintIndex) -> tintIndex == 0? ((DyeableItem) stack.getItem()).getColor(stack) : 0xFFFFFFF,
				Dyetherite.DYED_NETHERITE_HELMET,
				Dyetherite.DYED_NETHERITE_CHESTPLATE,
				Dyetherite.DYED_NETHERITE_LEGGINGS,
				Dyetherite.DYED_NETHERITE_BOOTS
		);
	}
}
