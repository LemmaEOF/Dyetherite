package space.bbkr.dyetherite.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Lazy;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import space.bbkr.dyetherite.Dyetherite;

import java.util.HashMap;
import java.util.Map;

@Mixin(CauldronBlock.class)
public abstract class MixinCauldronBlock {
	@Shadow public abstract void setLevel(World world, BlockPos pos, BlockState state, int level);

	private static final Lazy<Map<Item, Item>> DYED_TO_NORMAL = new Lazy<>(() -> {
		Map<Item, Item> ret = new HashMap<>();
		ret.put(Dyetherite.DYED_NETHERITE_HELMET, Items.NETHERITE_HELMET);
		ret.put(Dyetherite.DYED_NETHERITE_CHESTPLATE, Items.NETHERITE_CHESTPLATE);
		ret.put(Dyetherite.DYED_NETHERITE_LEGGINGS, Items.NETHERITE_LEGGINGS);
		ret.put(Dyetherite.DYED_NETHERITE_BOOTS, Items.NETHERITE_BOOTS);
		return ret;
	});

	@Inject(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/DyeableItem;removeColor(Lnet/minecraft/item/ItemStack;)V"), cancellable = true)
	private void doNetheriteCleaning(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> info) {
		ItemStack stack = player.getStackInHand(hand);
		int level = state.get(CauldronBlock.LEVEL);
		if (level > 0 && DYED_TO_NORMAL.get().containsKey(stack.getItem())) {
			ItemStack retStack = new ItemStack(DYED_TO_NORMAL.get().get(stack.getItem()));
			retStack.setTag(stack.getOrCreateTag().copy());
			((DyeableItem) stack.getItem()).removeColor(retStack);
			player.setStackInHand(hand, retStack);
			setLevel(world, pos, state, level - 1);
			player.incrementStat(Stats.CLEAN_ARMOR);
			info.setReturnValue(ActionResult.SUCCESS);
		}
	}

}
