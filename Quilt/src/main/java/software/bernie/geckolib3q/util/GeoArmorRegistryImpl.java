package software.bernie.geckolib3q.util;

import java.util.HashMap;
import java.util.function.BiConsumer;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ArmorItem;

public class GeoArmorRegistryImpl implements GeoArmorRendererRegistry {
	private static HashMap<Class<? extends ArmorItem>, GeoArmorRendererFactory<?>> map = new HashMap<>();
	private static BiConsumer<Class<? extends ArmorItem>, GeoArmorRendererFactory<?>> handler = (type, function) -> map
			.put(type, function);

	public <T extends Entity> void register(Class<? extends ArmorItem> entityType, GeoArmorRendererFactory<T> factory) {
		handler.accept(entityType, factory);
	}

	public static void setup(BiConsumer<Class<? extends ArmorItem>, GeoArmorRendererFactory<?>> vanillaHandler) {
		map.forEach(vanillaHandler);
		handler = vanillaHandler;
	}

}
