package software.bernie.geckolib3q.geo.render.built;

import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;

import net.minecraft.core.Direction;
import software.bernie.geckolib3q.geo.raw.pojo.Cube;
import software.bernie.geckolib3q.geo.raw.pojo.FaceUv;
import software.bernie.geckolib3q.geo.raw.pojo.ModelProperties;
import software.bernie.geckolib3q.geo.raw.pojo.UvFaces;
import software.bernie.geckolib3q.geo.raw.pojo.UvUnion;
import software.bernie.geckolib3q.util.VectorUtils;

public class GeoCube {
	public GeoQuad[] quads = new GeoQuad[6];
	public Vector3f pivot;
	public Vector3f rotation;
	public Vector3f size = new Vector3f();
	public double inflate;
	public Boolean mirror;

	private GeoCube(double[] size) {
		if (size.length >= 3) {
			this.size.set((float) size[0], (float) size[1], (float) size[2]);
		}
	}

	public static GeoCube createFromPojoCube(Cube cubeIn, ModelProperties properties, Double boneInflate,
			Boolean mirror) {
		GeoCube cube = new GeoCube(cubeIn.getSize());

		UvUnion uvUnion = cubeIn.getUv();
		UvFaces faces = uvUnion.faceUV;
		boolean isBoxUV = uvUnion.isBoxUV;
		cube.mirror = cubeIn.getMirror();
		cube.inflate = cubeIn.getInflate() == null ? (boneInflate == null ? 0 : boneInflate) : cubeIn.getInflate() / 16;

		float textureHeight = properties.getTextureHeight().floatValue();
		float textureWidth = properties.getTextureWidth().floatValue();

		Vector3d size = VectorUtils.fromArray(cubeIn.getSize());
		Vector3d origin = VectorUtils.fromArray(cubeIn.getOrigin());
		origin.x = -(origin.x + size.x) / 16;
		origin.y /= 16;
		origin.z /= 16;

		size.x /= 16;
		size.y /= 16;
		size.z /= 16;

		if (size.x == 0) {
			size.x = 0f;
		}
		if (size.y == 0) {
			size.y = 0f;
		}
		if (size.z == 0) {
			size.z = 0f;
		}
		Vector3f rotation = VectorUtils.convertDoubleToFloat(VectorUtils.fromArray(cubeIn.getRotation()));
		rotation.mul(-1, -1, 1);

		rotation.set((float) Math.toRadians(rotation.x()), (float) Math.toRadians(rotation.y()),
				(float) Math.toRadians(rotation.z()));

		Vector3f pivot = VectorUtils.convertDoubleToFloat(VectorUtils.fromArray(cubeIn.getPivot()));
		pivot.mul(-1, 1, 1);

		cube.pivot = pivot;
		cube.rotation = rotation;

		//
		//
		// P7 P8
		// - - - - - - - - - - - - -
		// | \ | \
		// | \ | \
		// | \ | \
		// | \ | \
		// Y | \ | \
		// | \ | \
		// | \ P3 | \ P4
		// | - - - - - - - - - - - - -
		// | | | |
		// | | | |
		// | | | |
		// P5 - - - - - - - - | - - - - P6 |
		// \ | \ |
		// \ | \ |
		// \ | \ |
		// X \ | \ |
		// \ | \ |
		// \ | \ |
		// \ | \ |
		// - - - - - - - - - - - - -
		// P1 P2
		// Z
		// this drawing corresponds to the points declared below
		//

		// Making all 8 points of the cube using the origin (where the Z, X, and Y
		// values are smallest) and offseting each point by the right size values
		GeoVertex P1 = new GeoVertex(origin.x - cube.inflate, origin.y - cube.inflate, origin.z - cube.inflate);
		GeoVertex P2 = new GeoVertex(origin.x - cube.inflate, origin.y - cube.inflate,
				origin.z + size.z + cube.inflate);
		GeoVertex P3 = new GeoVertex(origin.x - cube.inflate, origin.y + size.y + cube.inflate,
				origin.z - cube.inflate);
		GeoVertex P4 = new GeoVertex(origin.x - cube.inflate, origin.y + size.y + cube.inflate,
				origin.z + size.z + cube.inflate);
		GeoVertex P5 = new GeoVertex(origin.x + size.x + cube.inflate, origin.y - cube.inflate,
				origin.z - cube.inflate);
		GeoVertex P6 = new GeoVertex(origin.x + size.x + cube.inflate, origin.y - cube.inflate,
				origin.z + size.z + cube.inflate);
		GeoVertex P7 = new GeoVertex(origin.x + size.x + cube.inflate, origin.y + size.y + cube.inflate,
				origin.z - cube.inflate);
		GeoVertex P8 = new GeoVertex(origin.x + size.x + cube.inflate, origin.y + size.y + cube.inflate,
				origin.z + size.z + cube.inflate);

		GeoQuad quadWest;
		GeoQuad quadEast;
		GeoQuad quadNorth;
		GeoQuad quadSouth;
		GeoQuad quadUp;
		GeoQuad quadDown;

		if (!isBoxUV) {
			FaceUv west = faces.getWest();
			FaceUv east = faces.getEast();
			FaceUv north = faces.getNorth();
			FaceUv south = faces.getSouth();
			FaceUv up = faces.getUp();
			FaceUv down = faces.getDown();
			// Pass in vertices starting from the top right corner, then going
			// counter-clockwise
			quadWest = west == null ? null
					: new GeoQuad(new GeoVertex[] { P4, P3, P1, P2 }, west.getUv(), west.getUvSize(), textureWidth,
							textureHeight, cubeIn.getMirror(), Direction.WEST);
			quadEast = east == null ? null
					: new GeoQuad(new GeoVertex[] { P7, P8, P6, P5 }, east.getUv(), east.getUvSize(), textureWidth,
							textureHeight, cubeIn.getMirror(), Direction.EAST);
			quadNorth = north == null ? null
					: new GeoQuad(new GeoVertex[] { P3, P7, P5, P1 }, north.getUv(), north.getUvSize(), textureWidth,
							textureHeight, cubeIn.getMirror(), Direction.NORTH);
			quadSouth = south == null ? null
					: new GeoQuad(new GeoVertex[] { P8, P4, P2, P6 }, south.getUv(), south.getUvSize(), textureWidth,
							textureHeight, cubeIn.getMirror(), Direction.SOUTH);
			quadUp = up == null ? null
					: new GeoQuad(new GeoVertex[] { P4, P8, P7, P3 }, up.getUv(), up.getUvSize(), textureWidth,
							textureHeight, cubeIn.getMirror(), Direction.UP);
			quadDown = down == null ? null
					: new GeoQuad(new GeoVertex[] { P1, P5, P6, P2 }, down.getUv(), down.getUvSize(), textureWidth,
							textureHeight, cubeIn.getMirror(), Direction.DOWN);

			if (cubeIn.getMirror() == Boolean.TRUE || mirror == Boolean.TRUE) {
				quadWest = west == null ? null
						: new GeoQuad(new GeoVertex[] { P7, P8, P6, P5 }, west.getUv(), west.getUvSize(), textureWidth,
								textureHeight, cubeIn.getMirror(), Direction.WEST);
				quadEast = east == null ? null
						: new GeoQuad(new GeoVertex[] { P4, P3, P1, P2 }, east.getUv(), east.getUvSize(), textureWidth,
								textureHeight, cubeIn.getMirror(), Direction.EAST);
			}
		} else {
			double[] UV = cubeIn.getUv().boxUVCoords;
			Vector3d UVSize = VectorUtils.fromArray(cubeIn.getSize());
			UVSize = new Vector3d(Math.floor(UVSize.x), Math.floor(UVSize.y), Math.floor(UVSize.z));

			quadWest = new GeoQuad(new GeoVertex[] { P4, P3, P1, P2 },
					new double[] { UV[0] + UVSize.z + UVSize.x, UV[1] + UVSize.z }, new double[] { UVSize.z, UVSize.y },
					textureWidth, textureHeight, cubeIn.getMirror(), Direction.WEST);
			quadEast = new GeoQuad(new GeoVertex[] { P7, P8, P6, P5 }, new double[] { UV[0], UV[1] + UVSize.z },
					new double[] { UVSize.z, UVSize.y }, textureWidth, textureHeight, cubeIn.getMirror(),
					Direction.EAST);
			quadNorth = new GeoQuad(new GeoVertex[] { P3, P7, P5, P1 },
					new double[] { UV[0] + UVSize.z, UV[1] + UVSize.z }, new double[] { UVSize.x, UVSize.y },
					textureWidth, textureHeight, cubeIn.getMirror(), Direction.NORTH);
			quadSouth = new GeoQuad(new GeoVertex[] { P8, P4, P2, P6 },
					new double[] { UV[0] + UVSize.z + UVSize.x + UVSize.z, UV[1] + UVSize.z },
					new double[] { UVSize.x, UVSize.y }, textureWidth, textureHeight, cubeIn.getMirror(),
					Direction.SOUTH);
			quadUp = new GeoQuad(new GeoVertex[] { P4, P8, P7, P3 }, new double[] { UV[0] + UVSize.z, UV[1] },
					new double[] { UVSize.x, UVSize.z }, textureWidth, textureHeight, cubeIn.getMirror(), Direction.UP);
			quadDown = new GeoQuad(new GeoVertex[] { P1, P5, P6, P2 },
					new double[] { UV[0] + UVSize.z + UVSize.x, UV[1] + UVSize.z },
					new double[] { UVSize.x, -UVSize.z }, textureWidth, textureHeight, cubeIn.getMirror(),
					Direction.DOWN);

			if (cubeIn.getMirror() == Boolean.TRUE || mirror == Boolean.TRUE) {
				quadWest = new GeoQuad(new GeoVertex[] { P7, P8, P6, P5 },
						new double[] { UV[0] + UVSize.z + UVSize.x, UV[1] + UVSize.z },
						new double[] { UVSize.z, UVSize.y }, textureWidth, textureHeight, cubeIn.getMirror(),
						Direction.WEST);
				quadEast = new GeoQuad(new GeoVertex[] { P4, P3, P1, P2 }, new double[] { UV[0], UV[1] + UVSize.z },
						new double[] { UVSize.z, UVSize.y }, textureWidth, textureHeight, cubeIn.getMirror(),
						Direction.EAST);
			}
		}

		cube.quads[0] = quadWest;
		cube.quads[1] = quadEast;
		cube.quads[2] = quadNorth;
		cube.quads[3] = quadSouth;
		cube.quads[4] = quadUp;
		cube.quads[5] = quadDown;
		return cube;
	}
}
