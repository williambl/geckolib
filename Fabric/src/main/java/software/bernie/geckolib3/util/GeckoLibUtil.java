package software.bernie.geckolib3.util;

import static software.bernie.geckolib3.world.storage.GeckoLibIdTracker.Type.ITEM;

import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.world.storage.GeckoLibIdTracker;

public class GeckoLibUtil {
	private static final String GECKO_LIB_ID_NBT = "GeckoLibID";

	/**
	 * Tries to return the ID stored in the stack's NBT data.
	 * <p>
	 * If no ID exists, it will calculate a pseudo-unique one based on the stack's
	 * item, its NBT data, and the stack's size.
	 */
	public static int getIDFromStack(ItemStack stack) {
		if (stackHasIDTag(stack)) {
			return stack.getNbt().getInt(GECKO_LIB_ID_NBT);
		}
		return Objects.hash(stack.getItem().getTranslationKey(), stack.getNbt(), stack.getCount());
	}

	/**
	 * This generates a new ID and writes it to the stack's NBT data, if it doesn't
	 * already have an ID on it.
	 * <p>
	 * Note: This may make items unstackable since it modifies their NBT data. Only
	 * use on items with a max stack size of one, or if you know what you're doing.
	 */
	public static void writeIDToStack(ItemStack stack, ServerWorld world) {
		if (!stackHasIDTag(stack)) {
			final int id = GeckoLibIdTracker.from(world).getNextId(ITEM);
			stack.getOrCreateNbt().putInt(GECKO_LIB_ID_NBT, id);
		}
	}

	/**
	 * Convenience method that combines the effects of both
	 * {@linkplain #getIDFromStack} and {@linkplain #writeIDToStack}.
	 * <p>
	 * Will always return a unique ID that's stored in the stack's NBT data.
	 */
	public static int guaranteeIDForStack(ItemStack stack, ServerWorld world) {
		if (!stackHasIDTag(stack)) {
			final int id = GeckoLibIdTracker.from(world).getNextId(ITEM);
			stack.getOrCreateNbt().putInt(GECKO_LIB_ID_NBT, id);
			return id;
		} else {
			return stack.getNbt().getInt(GECKO_LIB_ID_NBT);
		}
	}

	/**
	 * Removes the unique ID from the given stack, if present.
	 */
	public static void removeIDFromStack(ItemStack stack) {
		if (stackHasIDTag(stack)) {
			stack.getNbt().remove(GECKO_LIB_ID_NBT);
		}
	}

	/**
	 * Returns true if the stack has an ID stored in its NBT data.
	 */
	public static boolean stackHasIDTag(ItemStack stack) {
		return stack.hasNbt() && stack.getNbt().contains(GECKO_LIB_ID_NBT, 3);
	}

	public static AnimationController getControllerForStack(AnimationFactory factory, ItemStack stack,
			String controllerName) {
		return getControllerForID(factory, getIDFromStack(stack), controllerName);
	}

	public static AnimationController getControllerForID(AnimationFactory factory, Integer id, String controllerName) {
		return factory.getOrCreateAnimationData(id).getAnimationControllers().get(controllerName);
	}
}