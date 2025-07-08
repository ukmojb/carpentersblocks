package com.wdcftgg.carpentersblocks.client.model.door;
// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelDoorUp extends ModelDoorBase {
	private final ModelRenderer bone;
	private final ModelRenderer color;

	public ModelDoorUp() {
		textureWidth = 64;
		textureHeight = 64;

		bone = new ModelRenderer(this);
		bone.setRotationPoint(5.0F, 21.0F, -4.0F);
		bone.cubeList.add(new ModelBox(bone, 0, 0, 1.5F, -10.0F, -1.0F, 0, 10, 10, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 20, 13, -2.0F, 1.0F, -3.0F, 1, 2, 1, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 0, 33, -1.0F, 1.0F, -3.0F, 1, 1, 1, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 4, 33, 3.0F, 1.0F, -3.0F, 1, 1, 1, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 20, 16, 4.0F, 1.0F, -3.0F, 1, 2, 1, 0.0F, false));

		color = new ModelRenderer(this);
		color.setRotationPoint(8.0F, 24.0F, -8.0F);
		color.cubeList.add(new ModelBox(color, 26, 13, -3.0F, -16.0F, 0.0F, 3, 16, 3, 0.0F, false));
		color.cubeList.add(new ModelBox(color, 26, 32, -3.0F, -16.0F, 13.0F, 3, 16, 3, 0.0F, false));
		color.cubeList.add(new ModelBox(color, 0, 20, -3.0F, -3.0F, 3.0F, 3, 3, 10, 0.0F, false));
		color.cubeList.add(new ModelBox(color, 20, 0, -3.0F, -16.0F, 3.0F, 3, 3, 10, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		bone.render(f5);
		color.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public void renderWood(float size) {
		bone.render(size);
	}

	public void renderOther(float size) {
		color.render(size);
	}
}