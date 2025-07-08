package com.wdcftgg.carpentersblocks.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelDaylightSensor extends ModelBase {
    private final ModelRenderer color;
    private final ModelRenderer cube_r1;
    private final ModelRenderer bone;

    public ModelDaylightSensor() {
        textureWidth = 128;
        textureHeight = 128;

        color = new ModelRenderer(this);
        color.setRotationPoint(7.0F, 23.0F, -7.0F);
        color.cubeList.add(new ModelBox(color, 0, 16, -14.0F, 0.0F, 0.0F, 14, 1, 14, 0.0F, false));
        color.cubeList.add(new ModelBox(color, 0, 45, 0.0F, -3.0F, -1.0F, 1, 4, 16, 0.0F, false));
        color.cubeList.add(new ModelBox(color, 34, 45, -15.0F, -3.0F, -1.0F, 1, 4, 16, 0.0F, false));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(1.0F, 1.0F, -1.0F);
        color.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, -1.5708F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 56, 18, 15.0F, -4.0F, 1.0F, 1, 4, 14, 0.0F, false));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 56, 0, 0.0F, -4.0F, 1.0F, 1, 4, 14, 0.0F, false));

        bone = new ModelRenderer(this);
        bone.setRotationPoint(7.0F, 23.0F, -7.0F);
        bone.cubeList.add(new ModelBox(bone, 0, 31, -14.0F, -3.0F, 0.0F, 14, 0, 14, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 0, -14.0F, -2.0F, 0.0F, 14, 2, 14, 0.0F, false));
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