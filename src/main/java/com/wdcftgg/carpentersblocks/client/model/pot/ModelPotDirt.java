package com.wdcftgg.carpentersblocks.client.model.pot;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPotDirt extends ModelPotBase {
    private final ModelRenderer bone;
    private final ModelRenderer bone2;

    public ModelPotDirt() {
        textureWidth = 32;
        textureHeight = 32;

        bone = new ModelRenderer(this);
        bone.setRotationPoint(8.0F, 24.0F, -8.0F);
        bone.cubeList.add(new ModelBox(bone, 0, 0, -6.0F, -6.0F, 5.0F, 1, 6, 6, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 12, -11.0F, -6.0F, 5.0F, 1, 6, 6, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 14, 5, -10.0F, -6.0F, 5.0F, 4, 6, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 14, 12, -10.0F, -6.0F, 10.0F, 4, 6, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 14, 0, -10.0F, -1.0F, 6.0F, 4, 1, 4, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone2.cubeList.add(new ModelBox(bone2, 0, 24, -2.0F, -4.0F, -2.0F, 4, 3, 4, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        bone.render(f5);
        bone2.render(f5);
    }

    public void renderWood(float size) {
        bone.render(size);
    }

    public void renderDirt(float size) {
        bone2.render(size);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
