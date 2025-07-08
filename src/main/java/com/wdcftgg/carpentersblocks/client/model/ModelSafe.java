package com.wdcftgg.carpentersblocks.client.model;
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class ModelSafe extends ModelBase {
	private final ModelRenderer bone;
	private final List<ModelBox> ModelBoxs = new ArrayList<>();

	public ModelSafe() {
		textureWidth = 78;
		textureHeight = 34;

		bone = new ModelRenderer(this);
		bone.setRotationPoint(8.0F, 24.0F, -8.0F);
		ModelBoxs.add(new ModelBox(bone, 0, 0, -16.0F, -1.0F, 0.0F, 16, 1, 16, 0.0F, false));
		ModelBoxs.add(new ModelBox(bone, 0, 17, -16.0F, -16.0F, 0.0F, 16, 1, 16, 0.0F, false));
		ModelBoxs.add(new ModelBox(bone, 16, 2, -1.0F, -15.0F, 0.0F, 1, 14, 16, 0.0F, false));
		ModelBoxs.add(new ModelBox(bone, 16, 2, -16.0F, -15.0F, 0.0F, 1, 14, 16, 0.0F, false));
		ModelBoxs.add(new ModelBox(bone, 17, 17, -15.0F, -15.0F, 15.0F, 14, 14, 1, 0.0F, false));
		ModelBoxs.add(new ModelBox(bone, 29, 16, -11.0F, -15.0F, 0.0F, 1, 14, 2, 0.0F, false));
		ModelBoxs.add(new ModelBox(bone, 22, 17, -10.0F, -15.0F, 1.0F, 9, 14, 1, 0.0F, false));
		ModelBoxs.add(new ModelBox(bone, 74, 1, -3.0F, -10.0F, 0.0F, 1, 4, 1, 0.0F, false));
		ModelBoxs.add(new ModelBox(bone, 5, 18, -15.0F, -6.0F, 2.0F, 14, 1, 13, 0.0F, false));
		ModelBoxs.add(new ModelBox(bone, 4, 18, -15.0F, -11.0F, 2.0F, 14, 1, 13, 0.0F, false));
		ModelBoxs.add(new ModelBox(bone, 64, 1, -15.0F, -15.0F, 0.0F, 4, 14, 1, 0.0F, false));

		bone.cubeList.addAll(ModelBoxs);
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

	public List<ModelBox> getModelBoxs() {
		return ModelBoxs;
	}

	public ModelRenderer getBone() {
		return bone;
	}
}