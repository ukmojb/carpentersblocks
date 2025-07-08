package com.wdcftgg.carpentersblocks.client.model.gate;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelGateOpen extends ModelGateBase {
    private final ModelRenderer bone2;
    private final ModelRenderer bone;

    public ModelGateOpen() {
        textureWidth = 32;
        textureHeight = 32;

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(-7.0F, 24.0F, 0.0F);
        setRotationAngle(bone2, 0.0F, -1.5708F, 0.0F);
        bone2.cubeList.add(new ModelBox(bone2, 8, 13, 5.0F, -15.0F, -1.0F, 2, 9, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 16, 10, 1.0F, -9.0F, -1.0F, 4, 3, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 16, 15, 1.0F, -15.0F, -1.0F, 4, 3, 2, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 8, 0, -1.0F, -16.0F, -1.0F, 2, 11, 2, 0.0F, false));

        bone = new ModelRenderer(this);
        bone.setRotationPoint(7.0F, 24.0F, 0.0F);
        setRotationAngle(bone, 0.0F, 1.5708F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 0, 13, -7.0F, -15.0F, -1.0F, 2, 9, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 16, 5, -5.0F, -15.0F, -1.0F, 4, 3, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 16, 0, -5.0F, -9.0F, -1.0F, 4, 3, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 0, -1.0F, -16.0F, -1.0F, 2, 11, 2, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        bone2.render(f5);
        bone.render(f5);
    }

    public void render(float f5) {
        bone2.render(f5);
        bone.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}