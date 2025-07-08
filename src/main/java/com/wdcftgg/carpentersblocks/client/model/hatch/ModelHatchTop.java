package com.wdcftgg.carpentersblocks.client.model.hatch;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelHatchTop extends ModelHatchBase{
    private final ModelRenderer color;
    private final ModelRenderer bone;

    public ModelHatchTop() {
        textureWidth = 64;
        textureHeight = 64;

        color = new ModelRenderer(this);
        color.setRotationPoint(8.0F, 24.0F, -8.0F);
        color.cubeList.add(new ModelBox(color, 0, 0, -16.0F, -16.0F, 0.0F, 16, 2, 16, 0.0F, false));

        bone = new ModelRenderer(this);
        bone.setRotationPoint(1.0F, 11.0F, -6.0F);
        bone.cubeList.add(new ModelBox(bone, 10, 18, 0.0F, -1.0F, -1.0F, 1, 1, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 14, 18, -3.0F, -1.0F, -1.0F, 1, 1, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 18, -3.0F, 0.0F, -1.0F, 4, 1, 1, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        color.render(f5);
        bone.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void renderWood(float size) {
        color.render(size);
    }

    public void renderOther(float size) {
        bone.render(size);
    }
}
