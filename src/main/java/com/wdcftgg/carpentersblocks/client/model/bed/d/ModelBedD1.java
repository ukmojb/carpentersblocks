package com.wdcftgg.carpentersblocks.client.model.bed.d;

import com.wdcftgg.carpentersblocks.client.model.bed.ModelBedBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBedD1 extends ModelBedBase {
    private final ModelRenderer bb_main1;
    private final ModelRenderer bb_main2;

    public ModelBedD1() {
        textureWidth = 128;
        textureHeight = 128;

        bb_main1 = new ModelRenderer(this);
        bb_main2 = new ModelRenderer(this);
        bb_main1.setRotationPoint(0.0F, 24.0F, 0.0F);
        bb_main1.cubeList.add(new ModelBox(bb_main1, 0, 0, -8.0F, -4.0F, -8.0F, 16, 2, 16, 0.0F, false));
        bb_main1.cubeList.add(new ModelBox(bb_main1, 40, 31, -8.0F, -13.0F, -8.0F, 16, 9, 2, 0.0F, false));

        bb_main2.setRotationPoint(0.0F, 24.0F, 0.0F);
        bb_main2.cubeList.add(new ModelBox(bb_main2, 0, 31, -8.0F, -8.0F, -6.0F, 16, 4, 6, 0.0F, false));
        bb_main2.cubeList.add(new ModelBox(bb_main2, 0, 18, -8.0F, -9.0F, 0.0F, 16, 5, 8, 0.0F, false));
        bb_main2.cubeList.add(new ModelBox(bb_main2, 0, 41, -6.0F, -10.0F, -5.0F, 12, 2, 6, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        bb_main1.render(f5);
        bb_main2.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void renderWood(float size) {
        super.renderWood(size);
        bb_main1.render(size);
    }

    @Override
    public void renderWooL(float size) {
        super.renderWooL(size);
        bb_main2.render(size);
    }
}