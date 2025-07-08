package com.wdcftgg.carpentersblocks.client.render;

import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntityFlowerPot;
import com.wdcftgg.carpentersblocks.client.model.pot.ModelPot;
import com.wdcftgg.carpentersblocks.client.model.pot.ModelPotBase;
import com.wdcftgg.carpentersblocks.client.model.pot.ModelPotDirt;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class RenderBlockPot extends TileEntitySpecialRenderer<TileEntityFlowerPot> {

    private final ModelPotBase pot = new ModelPot();
    private final ModelPotBase potDirt = new ModelPotDirt();
    private ModelPotBase model = null;


    @Override
    public void render(TileEntityFlowerPot te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {

        World world = te.getWorld();

        boolean flag = te.getWorld() != null;
//        EnumDyeColor enumdyecolor = te != null ? te.getColor() : EnumDyeColor.RED;
        int face = flag ? te.getBlockMetadata() & 3 : 0;

        if (destroyStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }
        else
        {
//            ResourceLocation resourcelocation = getBedRes(OCCUPIED == BlockBed.EnumPartType.HEAD);
//
//            if (resourcelocation != null)
//            {
//                this.bindTexture(resourcelocation);
//            }
        }

        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();

        this.model = te.hasSoil() ? potDirt : pot;

        if (flag)
        {
            this.renderPiece(x, y, z, face, alpha, te);
        }
        else
        {
            GlStateManager.pushMatrix();
            this.renderPiece(x, y, z, face, alpha, te);
            this.renderPiece(x, y, z - 1.0D, face, alpha, te);
            GlStateManager.popMatrix();
        }

        if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();

    }

    private void renderPiece(double x, double y, double z, int face, float alpha, TileEntityFlowerPot te)
    {
//        this.model.preparePiece(p_193847_1_);
        GlStateManager.pushMatrix();
        float f = 0.0F;
        float f1 = 0.0F;
        float f2 = 0.0F;

        GlStateManager.translate(x, y, z);

        if (face == EnumFacing.NORTH.getHorizontalIndex())
        {
            //weat
            GlStateManager.rotate(180, 1.0F, 0.0F, 0.0F);
            f1 = 0.5F;
            f2 = -0.5F;
        }
        else if (face == EnumFacing.SOUTH.getHorizontalIndex())
        {
            //north
            GlStateManager.rotate(180, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(90, 0.0F, 1.0F, 0.0F);
            f1 = 0.5F;
            f2 = 0.5F;
        }
        else if (face == EnumFacing.WEST.getHorizontalIndex())
        {
            //east
            GlStateManager.rotate(180, 0.0F, 0.0F, 1.0F);
            f1 = -0.5F;
            f2 = 0.5F;
        }
        else if (face == EnumFacing.EAST.getHorizontalIndex())
        {
            //south
            GlStateManager.rotate(180, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(90, 0.0F, 1.0F, 0.0F);
            f1 = -0.5F;
            f2 = -0.5F;
        }



        GlStateManager.translate((float)f1, (float)-1.5, (float)f2);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();



        renderTorch(this.model, te);


        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        GlStateManager.popMatrix();
    }

    private void renderTorch(ModelPotBase modelPotBase, TileEntityFlowerPot te) {
        renderOther(modelPotBase, te);
        renderWood(modelPotBase, te);
    }

    private void renderOther(ModelPotBase modelPotBase, TileEntityFlowerPot te) {
        ResourceLocation res = new ResourceLocation(CarpentersBlocks.MODID + ":textures/model/pot.png");
        this.bindTexture(res);
        modelPotBase.renderDirt(0.0625F);

        if (te.hasPlant()) {
//            System.out.println("ksjhkad--" + te.getPlant().getItem().getRegistryName().toString());
            GlStateManager.pushMatrix();
            GlStateManager.pushAttrib();
            GlStateManager.disableLighting();
            GlStateManager.translate(0, 1 + (float) 3 / 16, 0);
            GlStateManager.rotate(180, 1, 0, 0);

//            ItemStack itemStack = new ItemStack(te.getPlant().getItem(), 1, te.getPlant().getMetadata());
            ItemBlock itemBlock = (ItemBlock) te.getPlant().getItem();
            Block block = itemBlock.getBlock();
            IBlockState blockState = block.getStateFromMeta(te.getPlant().getMetadata());

            Minecraft mc = Minecraft.getMinecraft();
            BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            // 获取渲染模型（是 baked model，不含偏移）
            IBakedModel model = dispatcher.getModelForState(blockState);

            // 绑定方块纹理
            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            // 准备渲染
            GlStateManager.pushMatrix();
            if (!te.getPlant().getItem().getRegistryName().toString().contains("mushroom")) {
                GlStateManager.translate((float) -4 / 16, 0, (float) -4 / 16);
            } else {
                GlStateManager.translate((float) -8 / 16, 0, (float) -8 / 16);
            }
            GlStateManager.disableLighting();

            // 渲染模型
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            dispatcher.getBlockModelRenderer().renderModel(
                    mc.world,
                    model,
                    blockState,
                    BlockPos.ORIGIN,  // 用原点，避免触发 getOffset()
                    buffer,
                    false
            );
            tessellator.draw();

            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
            GlStateManager.popAttrib();
            GlStateManager.popMatrix();
        }
    }

    private void renderWood(ModelPotBase modelPotBase, TileEntityFlowerPot te) {

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        IBlockState iBlockState = te.getiBlockState();
        ResourceLocation res;

        if (!te.hasBlock()) {
            res = new ResourceLocation(CarpentersBlocks.MODID + ":textures/model/pot.png");
        } else {
            res = getResFromTexture(iBlockState);
        }

        if (res != null) {
            this.bindTexture(res);
        }
        modelPotBase.renderWood(0.0625F);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();

    }


    private ResourceLocation getResFromTexture(IBlockState state) {
        try {

            String s = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state).getParticleTexture().toString();
            s = s.substring(25, (s.indexOf("',")));

            if(s == null || s.isEmpty() || s.equals("missingno") || s.equals("missing"))
                return null;

            String[] split = s.split(":");

            String prefix = "";
            String suffix = "";

            if(split.length == 2) {
                prefix = split[0];
                suffix = split[1];
            } else {
                prefix = "minecraft";
                suffix = s;
            }

            ResourceLocation resourceLocation = new ResourceLocation(prefix + ":textures/" + suffix + ".png");

            resourceLocation = setModRes(prefix, suffix, resourceLocation);

            return resourceLocation;
        } catch (ArrayIndexOutOfBoundsException a){
            return null;
        }
    }

    public ResourceLocation setModRes(String modid, String name, ResourceLocation resourceLocation) {
//        System.out.println("-" + modid + "-");
//        if (Objects.equals(modid, "chisel")) {
//                System.out.println(name);
//            if (name.contains("wallvents-ctmh")) {
//                resourceLocation = new ResourceLocation(CarpentersBlocks.MODID + ":textures/block/wallvents-ctmh.png");
//            }
//        }

        return resourceLocation;
    }
}