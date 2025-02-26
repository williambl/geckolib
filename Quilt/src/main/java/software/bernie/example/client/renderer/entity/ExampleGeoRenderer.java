package software.bernie.example.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.example.client.model.entity.ExampleEntityModel;
import software.bernie.example.entity.GeoExampleEntity;
import software.bernie.geckolib3q.renderers.geo.GeoEntityRenderer;

public class ExampleGeoRenderer extends GeoEntityRenderer<GeoExampleEntity> {
	public ExampleGeoRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new ExampleEntityModel());
	}

	@Override
	public RenderType getRenderType(GeoExampleEntity animatable, float partialTicks, PoseStack stack,
			MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation) {
		return RenderType.entityCutout(getTextureLocation(animatable));
	}

}
