package com.wdcftgg.carpentersblocks.blocks.te;

public interface ICustomRenderBase {

    public void setBlockAndMeta(String block, int meta);

    public boolean hasBlock();

    public String getBlock();

    public int getMeta();
}
