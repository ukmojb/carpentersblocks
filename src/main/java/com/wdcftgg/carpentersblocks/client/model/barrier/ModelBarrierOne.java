package com.wdcftgg.carpentersblocks.client.model.barrier;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBarrierOne extends ModelBarrierBase {
    private final ModelRenderer bb_main;

    public ModelBarrierOne() {
        textureWidth = 32;
        textureHeight = 32;

        bb_main = new ModelRenderer(this);
        bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -2.0F, -16.0F, -2.0F, 4, 16, 4, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 16, 0, -1.0F, -15.0F, -8.0F, 2, 2, 6, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 16, 8, -1.0F, -7.0F, -8.0F, 2, 2, 6, 0.0F, false));
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