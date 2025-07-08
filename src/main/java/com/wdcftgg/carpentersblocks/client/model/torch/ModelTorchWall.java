package com.wdcftgg.carpentersblocks.client.model.torch;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTorchWall extends ModelTorchBase {
    private final ModelRenderer color;
    private final ModelRenderer bone;

    public ModelTorchWall() {
        textureWidth = 16;
        textureHeight = 16;

        color = new ModelRenderer(this);
        color.setRotationPoint(8.0F, 24.0F, -8.0F);


        ModelRenderer cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(0.0F, -3.5F, 8.0F);
        color.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.0F, -0.3927F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 4, -1.0F, -8.0F, -1.0F, 2, 8, 2, 0.0F, false));

        bone = new ModelRenderer(this);
        bone.setRotationPoint(8.0F, 20.5F, 0.0F);


        ModelRenderer cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.0F, -0.3927F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 0, 0, -1.0F, -10.0F, -1.0F, 2, 2, 2, 0.0F, false));
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