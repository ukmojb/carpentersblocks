package com.wdcftgg.carpentersblocks.client.model;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSafeOpen extends ModelBase {
	private final ModelRenderer bone;

	public ModelSafeOpen() {
		textureWidth = 78;
		textureHeight = 34;

		bone = new ModelRenderer(this);
		bone.setRotationPoint(8.0F, 24.0F, -8.0F);
		bone.cubeList.add(new ModelBox(bone, 0, 0, -16.0F, -1.0F, 0.0F, 16, 1, 16, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 0, 17, -16.0F, -16.0F, 0.0F, 16, 1, 16, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 16, 2, -1.0F, -15.0F, 0.0F, 1, 14, 16, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 16, 2, -16.0F, -15.0F, 0.0F, 1, 14, 16, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 17, 17, -15.0F, -15.0F, 15.0F, 14, 14, 1, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 29, 16, -11.0F, -15.0F, 0.0F, 1, 14, 2, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 23, 17, -15.0F, -15.0F, 1.0F, 8, 14, 1, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 74, 1, -9.0F, -10.0F, 0.0F, 1, 4, 1, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 5, 18, -15.0F, -6.0F, 2.0F, 14, 1, 13, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 4, 18, -15.0F, -11.0F, 2.0F, 14, 1, 13, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 64, 1, -15.0F, -15.0F, 0.0F, 4, 14, 1, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		bone.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}