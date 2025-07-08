package com.wdcftgg.carpentersblocks.client.model.torch;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTorchGround extends ModelTorchBase {
    private final ModelRenderer color;
    private final ModelRenderer bone;

    public ModelTorchGround() {
        textureWidth = 16;
        textureHeight = 16;

        color = new ModelRenderer(this);
        color.setRotationPoint(8.0F, 24.0F, -8.0F);
        color.cubeList.add(new ModelBox(color, 0, 4, -9.0F, -8.0F, 7.0F, 2, 8, 2, 0.0F, false));

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 16.0F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 0, 0, -1.0F, -2.0F, -1.0F, 2, 2, 2, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        color.render(f5);
        bone.render(f5);
    }

    public void renderWood(float size) {
        color.render(size);
    }

    public void renderOther(float size) {
        bone.render(size);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}