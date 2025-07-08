package com.wdcftgg.carpentersblocks.client.model.lever;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLeverOn extends ModelLeverBase{
    private final ModelRenderer bone;
    private final ModelRenderer cube_r1;
    private final ModelRenderer color;

    public ModelLeverOn() {
        textureWidth = 32;
        textureHeight = 32;

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 23.0F, 0.0F);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.7854F, 0.0F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 11, -1.0F, -10.0F, -1.0F, 2, 10, 2, 0.0F, false));

        color = new ModelRenderer(this);
        color.setRotationPoint(8.0F, 24.0F, -8.0F);
        color.cubeList.add(new ModelBox(color, 0, 0, -11.0F, -3.0F, 4.0F, 6, 3, 8, 0.0F, false));
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
        color.render(size);
    }

    public void renderOther(float size) {
        bone.render(size);
    }
}
