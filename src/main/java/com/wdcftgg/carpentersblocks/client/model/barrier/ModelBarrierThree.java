package com.wdcftgg.carpentersblocks.client.model.barrier;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBarrierThree extends ModelBarrierBase {
    private final ModelRenderer bb_main;
    private final ModelRenderer Lowerbar_r1;
    private final ModelRenderer Lowerbar_r2;

    public ModelBarrierThree() {
        textureWidth = 32;
        textureHeight = 32;

        bb_main = new ModelRenderer(this);
        bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -2.0F, -16.0F, -2.0F, 4, 16, 4, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 16, 0, -1.0F, -15.0F, -8.0F, 2, 2, 6, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 16, 8, -1.0F, -7.0F, -8.0F, 2, 2, 6, 0.0F, false));

        Lowerbar_r1 = new ModelRenderer(this);
        Lowerbar_r1.setRotationPoint(-2.0F, 0.0F, 8.0F);
        bb_main.addChild(Lowerbar_r1);
        setRotationAngle(Lowerbar_r1, 0.0F, -1.5708F, 0.0F);
        Lowerbar_r1.cubeList.add(new ModelBox(Lowerbar_r1, 16, 8, -9.0F, -7.0F, 0.0F, 2, 2, 6, 0.0F, false));
        Lowerbar_r1.cubeList.add(new ModelBox(Lowerbar_r1, 16, 0, -9.0F, -15.0F, 0.0F, 2, 2, 6, 0.0F, false));

        Lowerbar_r2 = new ModelRenderer(this);
        Lowerbar_r2.setRotationPoint(8.0F, 0.0F, 8.0F);
        bb_main.addChild(Lowerbar_r2);
        setRotationAngle(Lowerbar_r2, 0.0F, -1.5708F, 0.0F);
        Lowerbar_r2.cubeList.add(new ModelBox(Lowerbar_r2, 16, 8, -9.0F, -7.0F, 0.0F, 2, 2, 6, 0.0F, false));
        Lowerbar_r2.cubeList.add(new ModelBox(Lowerbar_r2, 16, 0, -9.0F, -15.0F, 0.0F, 2, 2, 6, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        bb_main.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void renderBarrier(float size) {
        bb_main.render(size);
    }
}