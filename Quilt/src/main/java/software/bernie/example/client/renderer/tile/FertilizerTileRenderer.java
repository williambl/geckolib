package software.bernie.example.client.renderer.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.example.block.tile.FertilizerTileEntity;
import software.bernie.example.client.model.tile.FertilizerModel;
import software.bernie.geckolib3q.renderers.geo.GeoBlockRenderer;

public class FertilizerTileRenderer extends GeoBlockRenderer<FertilizerTileEntity> {
	public FertilizerTileRenderer() {
		super(new FertilizerModel());
	}

	@Override
	public RenderType getRenderType(FertilizerTileEntity animatable, float partialTicks, PoseStack stack,
			MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
