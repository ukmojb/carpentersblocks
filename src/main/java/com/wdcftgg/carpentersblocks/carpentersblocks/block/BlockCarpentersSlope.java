package com.wdcftgg.carpentersblocks.carpentersblocks.block;

import com.wdcftgg.carpentersblocks.carpentersblocks.block.data.SlopeData;
import com.wdcftgg.carpentersblocks.carpentersblocks.tileentity.CbTileEntity;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.RotationUtil;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.handler.EventHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockCarpentersSlope extends BlockCoverable {

    public final static String[] slopeType = {
    	"wedge",
    	"wedgeInterior",
    	"wedgeExterior",
    	"obliqueInterior",
    	"obliqueExterior",
    	"prismWedge",
    	"prism",
    	"invertPrism" };


    public BlockCarpentersSlope(Material material) {
        super(material);
    }

    @Override
    /**
     * Alters block direction.
     */
    protected boolean onHammerLeftClick(CbTileEntity cbTileEntity, EntityPlayer entityPlayer) {
    	return rotateBlock(cbTileEntity.getWorld(), cbTileEntity.getPos(), EventHandler.eventFace);
    }

    @Override
    /**
     * Alters block type.
     */
    protected boolean onHammerRightClick(CbTileEntity cbTileEntity, EntityPlayer entityPlayer) {
    	new SlopeData().setNextType(cbTileEntity);
        return true;        
    }

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos,
									  AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes,
									  @Nullable Entity entityIn, boolean isActualState) {

		CbTileEntity cbTileEntity = getTileEntity(worldIn, pos);
		if (cbTileEntity == null) return;

		SlopeData.Type type = SlopeData.getType(cbTileEntity.getCbMetadata());
		RotationUtil.Rotation rotation = SlopeData.getRotation(cbTileEntity);

		// 获取基础碰撞箱
		List<AxisAlignedBB> baseBoxes = getBaseCollisionBoxes(type);

		// 应用旋转
		for (AxisAlignedBB box : baseBoxes) {
			AxisAlignedBB rotatedBox = applyRotation(box, rotation);
			AxisAlignedBB offsetBox = rotatedBox.offset(pos);
			if (entityBox.intersects(offsetBox)) {
				collidingBoxes.add(offsetBox);
			}
		}
	}

	private AxisAlignedBB applyRotation(AxisAlignedBB box, RotationUtil.Rotation rotation) {
		// 检查是否为简单旋转（仅绕单轴）
		if (rotation.getX() == 0 && rotation.getZ() == 0 && rotation.getY() != 0) {
			return applyYRotation(box, rotation.getY());
		}

		// 通用旋转方法
		double[][] vertices = {
				{box.minX, box.minY, box.minZ},
				{box.minX, box.minY, box.maxZ},
				{box.minX, box.maxY, box.minZ},
				{box.minX, box.maxY, box.maxZ},
				{box.maxX, box.minY, box.minZ},
				{box.maxX, box.minY, box.maxZ},
				{box.maxX, box.maxY, box.minZ},
				{box.maxX, box.maxY, box.maxZ}
		};

		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double minZ = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		double maxZ = Double.NEGATIVE_INFINITY;

		for (double[] vertex : vertices) {
			double[] rotated = rotateVertex(vertex, rotation);
			minX = Math.min(minX, rotated[0]);
			minY = Math.min(minY, rotated[1]);
			minZ = Math.min(minZ, rotated[2]);
			maxX = Math.max(maxX, rotated[0]);
			maxY = Math.max(maxY, rotated[1]);
			maxZ = Math.max(maxZ, rotated[2]);
		}

		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}

	private double[] rotateVertex(double[] vertex, RotationUtil.Rotation rotation) {
		double x = vertex[0] - 0.5;
		double y = vertex[1] - 0.5;
		double z = vertex[2] - 0.5;

		// 应用旋转顺序：X -> Y -> Z
		for (int i = 0; i < rotation.getX(); i++) {
			double newY = -z;
			double newZ = y;
			y = newY;
			z = newZ;
		}

		for (int i = 0; i < rotation.getY(); i++) {
			double newX = z;
			double newZ = -x;
			x = newX;
			z = newZ;
		}

		for (int i = 0; i < rotation.getZ(); i++) {
			double newX = -y;
			double newY = x;
			x = newX;
			y = newY;
		}

		return new double[]{x + 0.5, y + 0.5, z + 0.5};
	}

	private boolean canUseOptimizedRotation(RotationUtil.Rotation rotation) {
		// 只有当绕单轴旋转时才使用优化路径
		return (rotation.getX() > 0 &&  rotation.getY() == 0 &&  rotation.getZ() == 0) || // 仅X轴
				( rotation.getX() == 0 &&  rotation.getY() > 0 &&  rotation.getZ() == 0) || // 仅Y轴
				( rotation.getX() == 0 &&  rotation.getY() == 0 &&  rotation.getZ() > 0);   // 仅Z轴
	}

	private AxisAlignedBB applyOptimizedRotation(AxisAlignedBB box, RotationUtil.Rotation rotation) {
		if ( rotation.getX() > 0) {
			return applyXRotation(box,  rotation.getX());
		} else if ( rotation.getY() > 0) {
			return applyYRotation(box,  rotation.getY());
		} else {
			return applyZRotation(box,  rotation.getZ());
		}
	}

	private AxisAlignedBB applyYRotation(AxisAlignedBB box, int yRot) {
		switch (yRot) {
			case 1: // 90度 (现在对应WEST)
				return new AxisAlignedBB(
						1 - box.maxZ, box.minY, box.minX,
						1 - box.minZ, box.maxY, box.maxX
				);
			case 2: // 180度 (SOUTH)
				return new AxisAlignedBB(
						1 - box.maxX, box.minY, 1 - box.maxZ,
						1 - box.minX, box.maxY, 1 - box.minZ
				);
			case 3: // 270度 (现在对应EAST)
				return new AxisAlignedBB(
						box.minZ, box.minY, 1 - box.maxX,
						box.maxZ, box.maxY, 1 - box.minX
				);
			default: // 0度 (NORTH)
				return box;
		}
	}

	private AxisAlignedBB applyXRotation(AxisAlignedBB box, int xRot) {
		switch (xRot) {
			case 1: // 90度
				return new AxisAlignedBB(
						box.minX, 1 - box.maxZ, box.minY,
						box.maxX, 1 - box.minZ, box.maxY
				);
			case 2: // 180度
				return new AxisAlignedBB(
						box.minX, 1 - box.maxY, 1 - box.maxZ,
						box.maxX, 1 - box.minY, 1 - box.minZ
				);
			case 3: // 270度
				return new AxisAlignedBB(
						box.minX, box.minZ, 1 - box.maxY,
						box.maxX, box.maxZ, 1 - box.minY
				);
			default:
				return box;
		}
	}

	private AxisAlignedBB applyZRotation(AxisAlignedBB box, int zRot) {
		switch (zRot) {
			case 1: // 90度
				return new AxisAlignedBB(
						1 - box.maxY, box.minX, box.minZ,
						1 - box.minY, box.maxX, box.maxZ
				);
			case 2: // 180度
				return new AxisAlignedBB(
						1 - box.maxX, 1 - box.maxY, box.minZ,
						1 - box.minX, 1 - box.minY, box.maxZ
				);
			case 3: // 270度
				return new AxisAlignedBB(
						box.minY, 1 - box.maxX, box.minZ,
						box.maxY, 1 - box.minX, box.maxZ
				);
			default:
				return box;
		}
	}

	private AxisAlignedBB applyGenericRotation(AxisAlignedBB box, RotationUtil.Rotation rotation) {
		double[][] vertices = {
				{box.minX, box.minY, box.minZ},
				{box.minX, box.minY, box.maxZ},
				{box.minX, box.maxY, box.minZ},
				{box.minX, box.maxY, box.maxZ},
				{box.maxX, box.minY, box.minZ},
				{box.maxX, box.minY, box.maxZ},
				{box.maxX, box.maxY, box.minZ},
				{box.maxX, box.maxY, box.maxZ}
		};

		// 计算旋转后的边界
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double minZ = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		double maxZ = Double.NEGATIVE_INFINITY;

		for (double[] vertex : vertices) {
			double[] rotated = applySingleRotation(vertex, rotation);
			minX = Math.min(minX, rotated[0]);
			minY = Math.min(minY, rotated[1]);
			minZ = Math.min(minZ, rotated[2]);
			maxX = Math.max(maxX, rotated[0]);
			maxY = Math.max(maxY, rotated[1]);
			maxZ = Math.max(maxZ, rotated[2]);
		}

		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}

	private double[] applySingleRotation(double[] vertex, RotationUtil.Rotation rotation) {
		double x = vertex[0] - 0.5;
		double y = vertex[1] - 0.5;
		double z = vertex[2] - 0.5;

		// 应用X轴旋转
		for (int i = 0; i < rotation.getX(); i++) {
			double newY = -z;
			double newZ = y;
			y = newY;
			z = newZ;
		}

		// 应用Y轴旋转
		for (int i = 0; i < rotation.getY(); i++) {
			double newX = z;
			double newZ = -x;
			x = newX;
			z = newZ;
		}

		// 应用Z轴旋转
		for (int i = 0; i < rotation.getZ(); i++) {
			double newX = -y;
			double newY = x;
			x = newX;
			y = newY;
		}

		return new double[]{x + 0.5, y + 0.5, z + 0.5};
	}

	

	private double[][] getRotatedVertices(AxisAlignedBB box, RotationUtil.Rotation rotation) {
		// 获取原始8个顶点
		double[][] vertices = {
				{box.minX, box.minY, box.minZ},
				{box.minX, box.minY, box.maxZ},
				{box.minX, box.maxY, box.minZ},
				{box.minX, box.maxY, box.maxZ},
				{box.maxX, box.minY, box.minZ},
				{box.maxX, box.minY, box.maxZ},
				{box.maxX, box.maxY, box.minZ},
				{box.maxX, box.maxY, box.maxZ}
		};

		// 应用旋转
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = applySingleRotation(vertices[i], rotation);
		}

		return vertices;
	}

	private List<AxisAlignedBB> getBaseCollisionBoxes(SlopeData.Type type) {
		List<AxisAlignedBB> boxes = new ArrayList<>();

		switch (type) {
			case WEDGE:
				// 基础楔形 - 从0到1的斜坡
				boxes.addAll(createSlopedBox(EnumFacing.NORTH, 1.0, 8));
				break;

			case WEDGE_INTERIOR:
				// 内部楔形 - 90度内角
				boxes.add(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0));
				boxes.add(new AxisAlignedBB(0.0, 0.5, 0.5, 1.0, 1.0, 1.0));
				break;

			case WEDGE_EXTERIOR:
				// 外部楔形 - 90度外角
				boxes.add(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 0.5));
				boxes.add(new AxisAlignedBB(0.0, 0.5, 0.0, 1.0, 1.0, 0.5));
				boxes.add(new AxisAlignedBB(0.0, 0.0, 0.5, 1.0, 1.0, 1.0));
				break;

			case OBLIQUE_INTERIOR:
				// 斜角内部 - 对角线内角
				boxes.addAll(createDiagonalSlopedBox(true, 8));
				break;

			case OBLIQUE_EXTERIOR:
				// 斜角外部 - 对角线外角
				boxes.addAll(createDiagonalSlopedBox(false, 8));
				break;

			case PRISM_WEDGE:
				// 棱柱楔形 - 1/4圆柱
				boxes.addAll(createSlopedBox(EnumFacing.NORTH, 1.0, 8));
				break;

			case PRISM:
				// 完整棱柱 - 半圆柱
//				boxes.addAll(createHalfCylinderBox(12));
				boxes.add(new AxisAlignedBB((double) 3 / 16, 0, (double) 3 / 16, (double) 13 / 16, (double) 3 / 16, (double) 13 / 16));
				boxes.add(new AxisAlignedBB((double) 6 / 16, (double) 3 / 16, (double) 6 / 16, (double) 10 / 16, (double) 6 / 16, (double) 10 / 16));
				break;

			case PRISM_1P:
				// 1点棱柱 - 1/8圆柱
				boxes.add(new AxisAlignedBB((double) 3 / 16, 0, (double) 3 / 16, (double) 13 / 16, (double) 3 / 16, (double) 13 / 16));
				boxes.add(new AxisAlignedBB((double) 6 / 16, (double) 3 / 16, (double) 6 / 16, (double) 10 / 16, (double) 6 / 16, (double) 10 / 16));
				break;

			case PRISM_2P:
				// 2点棱柱 - 2/8圆柱
				boxes.add(new AxisAlignedBB((double) 3 / 16, 0, (double) 3 / 16, (double) 13 / 16, (double) 3 / 16, (double) 13 / 16));
				boxes.add(new AxisAlignedBB((double) 6 / 16, (double) 3 / 16, (double) 6 / 16, (double) 10 / 16, (double) 6 / 16, (double) 10 / 16));
				break;

			case PRISM_3P:
				// 3点棱柱 - 3/8圆柱
				boxes.add(new AxisAlignedBB((double) 3 / 16, 0, (double) 3 / 16, (double) 13 / 16, (double) 3 / 16, (double) 13 / 16));
				boxes.add(new AxisAlignedBB((double) 6 / 16, (double) 3 / 16, (double) 6 / 16, (double) 10 / 16, (double) 6 / 16, (double) 10 / 16));
				break;

			case PRISM_4P:
				// 4点棱柱 - 半圆柱 (同PRISM)
				boxes.add(new AxisAlignedBB((double) 3 / 16, 0, (double) 3 / 16, (double) 13 / 16, (double) 3 / 16, (double) 13 / 16));
				boxes.add(new AxisAlignedBB((double) 6 / 16, (double) 3 / 16, (double) 6 / 16, (double) 10 / 16, (double) 6 / 16, (double) 10 / 16));
				break;

//			case INVERT_PRISM:
//				// 倒置棱柱 - 反向半圆柱
//				boxes.add(new AxisAlignedBB(0, 0, 0, 1, 1, 1));
//				break;
//
//			case INVERT_PRISM_1P:
//				// 倒置1点棱柱
//				boxes.addAll(createInvertedPartialCylinderBox(6, 0, Math.PI/4));
//				break;
//
//			case INVERT_PRISM_2P:
//				// 倒置2点棱柱
//				boxes.addAll(createInvertedPartialCylinderBox(6, 0, Math.PI/2));
//				break;
//
//			case INVERT_PRISM_3P:
//				// 倒置3点棱柱
//				boxes.addAll(createInvertedPartialCylinderBox(6, 0, 3*Math.PI/4));
//				break;
//
//			case INVERT_PRISM_4P:
//				// 倒置4点棱柱 (同INVERT_PRISM)
//				boxes.addAll(createInvertedHalfCylinderBox(12));
//				break;

			default:
				boxes.add(FULL_BLOCK_AABB);
		}

		return boxes;
	}

	private List<AxisAlignedBB> createSlopedBox(EnumFacing facing, double height, int segments) {
		List<AxisAlignedBB> boxes = new ArrayList<>();

		for (int i = 0; i < segments; i++) {
			double progress = (double)i / segments;
			double nextProgress = (double)(i + 1) / segments;

			switch (facing) {
				case NORTH:
					boxes.add(new AxisAlignedBB(
							0.0, 0.0, 1.0 - nextProgress,
							1.0, height * nextProgress, 1.0 - progress
					));
					break;
				case SOUTH:
					boxes.add(new AxisAlignedBB(
							0.0, 0.0, progress,
							1.0, height * nextProgress, nextProgress
					));
					break;
				case EAST:  // 修正后的东朝向
					boxes.add(new AxisAlignedBB(
							progress, 0.0, 0.0,
							nextProgress, height * nextProgress, 1.0
					));
					break;
				case WEST:  // 修正后的西朝向
					boxes.add(new AxisAlignedBB(
							1.0 - nextProgress, 0.0, 0.0,
							1.0 - progress, height * nextProgress, 1.0
					));
					break;
				default:
					// 其他朝向不处理
			}
		}

		return boxes;
	}

	// 创建对角线斜坡碰撞箱
	private List<AxisAlignedBB> createDiagonalSlopedBox(boolean isInterior, int segments) {
		List<AxisAlignedBB> boxes = new ArrayList<>();

		if (isInterior) {
			// 内部斜角 - 从两个边升起
			for (int i = 0; i < segments; i++) {
				double progress = (double)i / segments;
				double nextProgress = (double)(i + 1) / segments;

				// X轴斜坡
				boxes.add(new AxisAlignedBB(
						0.0, 0.0, progress,
						nextProgress, nextProgress, nextProgress
				));

				// Z轴斜坡
				boxes.add(new AxisAlignedBB(
						progress, 0.0, 0.0,
						nextProgress, nextProgress, nextProgress
				));

				// 连接部分
				boxes.add(new AxisAlignedBB(
						0.0, 0.0, 0.0,
						nextProgress, nextProgress, nextProgress
				));
			}
		} else {
			// 外部斜角 - 从角落升起
			for (int i = 0; i < segments; i++) {
				double progress = (double)i / segments;
				double nextProgress = (double)(i + 1) / segments;

				boxes.add(new AxisAlignedBB(
						0.0, 0.0, 0.0,
						nextProgress, nextProgress, nextProgress
				));
			}
		}

		return boxes;
	}

	// 创建1/4圆柱碰撞箱
	private List<AxisAlignedBB> createQuarterCylinderBox(int segments) {
		return createPartialCylinderBox(segments, 0, Math.PI/2);
	}

	// 创建半圆柱碰撞箱
	private List<AxisAlignedBB> createHalfCylinderBox(int segments) {
		return createPartialCylinderBox(segments, 0, Math.PI);
	}

	// 创建部分圆柱碰撞箱 (通用)
	private List<AxisAlignedBB> createPartialCylinderBox(int segments, double startAngle, double endAngle) {
		List<AxisAlignedBB> boxes = new ArrayList<>();
		double angleStep = (endAngle - startAngle) / segments;

		for (int i = 0; i < segments; i++) {
			double angle1 = startAngle + i * angleStep;
			double angle2 = startAngle + (i + 1) * angleStep;

			double x1 = 0.5 - 0.5 * Math.cos(angle1);
			double z1 = 0.5 - 0.5 * Math.sin(angle1);
			double x2 = 0.5 - 0.5 * Math.cos(angle2);
			double z2 = 0.5 - 0.5 * Math.sin(angle2);

			boxes.add(new AxisAlignedBB(
					Math.min(x1, x2), 0.0, Math.min(z1, z2),
					Math.max(x1, x2), 1.0, Math.max(z1, z2)
			));
		}

		return boxes;
	}

	// 创建倒置半圆柱碰撞箱
	private List<AxisAlignedBB> createInvertedHalfCylinderBox(int segments) {
		List<AxisAlignedBB> boxes = createHalfCylinderBox(segments);
		return invertBoxesY(boxes);
	}

	// 创建倒置部分圆柱碰撞箱
	private List<AxisAlignedBB> createInvertedPartialCylinderBox(int segments, double startAngle, double endAngle) {
		List<AxisAlignedBB> boxes = createPartialCylinderBox(segments, startAngle, endAngle);
		return invertBoxesY(boxes);
	}

	// 反转Y坐标
	private List<AxisAlignedBB> invertBoxesY(List<AxisAlignedBB> boxes) {
		List<AxisAlignedBB> inverted = new ArrayList<>();
		for (AxisAlignedBB box : boxes) {
			inverted.add(new AxisAlignedBB(
					box.minX, 1.0 - box.maxY, box.minZ,
					box.maxX, 1.0 - box.minY, box.maxZ
			));
		}
		return inverted;
	}

//	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
//	{
//		CbTileEntity cbTileEntity = getTileEntity(worldIn, pos);
//
//        collidingBoxes.addAll(StateFactory.getState(cbTileEntity).getAxisAlignedBBs());
//	}

//	@Deprecated
//	@Nullable
//	@Override
//	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
//
//		CbTileEntity cbTileEntity = getTileEntity(worldIn, pos);
//		AxisAlignedBB aabb = NULL_AABB;
//		SlopeData.Type type = SlopeData.getType(cbTileEntity.getCbMetadata());
//		int cbMetadata = cbTileEntity.getCbMetadata();
////		RotationUtil.Rotation rotation = RotationUtil.Rotation.get(cbMetadata);
//
//		switch (type) {
//			case WEDGE:
//				collidingBoxes.add(new AxisAlignedBB(0, 0, 0, 1, 0.1, 1));
//				break;
//			case WEDGE_INTERIOR:
//
//			break;
//			case WEDGE_EXTERIOR:
//
//			break;
//			case OBLIQUE_INTERIOR:
//
//			break;
//			case OBLIQUE_EXTERIOR:
//
//			break;
//			case PRISM_WEDGE:
//
//			break;
//			case PRISM:
//
//			break;
//			case PRISM_1P:
//
//			break;
//			case PRISM_2P:
//
//			break;
//			case PRISM_3P:
//
//			break;
//			case PRISM_4P:
//
//			break;
//			case INVERT_PRISM:
//
//			break;
//			case INVERT_PRISM_1P:
//
//			break;
//			case INVERT_PRISM_2P:
//
//			break;
//			case INVERT_PRISM_3P:
//
//			break;
//			case INVERT_PRISM_4P:
//
//			break;
//		}
//	}

/*    @Override
    *//**
     * Damages hammer with a chance to not damage.
     *//*
    protected void damageItemWithChance(World world, EntityPlayer entityPlayer) {
        if (world.rand.nextFloat() <= ItemRegistry.itemHammerDamageChanceFromSlopes) {
            super.damageItemWithChance(world, entityPlayer);
        }
    }*/

/*    @Override
    *//**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     *//*
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, BlockPos blockPos) {
        if (!rayTracing) {

            CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);

            if (cbTileEntity != null) {

                Slope slope = Slope.getSlope(cbTileEntity);

                switch (slope.getPrimaryType()) {
                    case PRISM:
                    case PRISM_1P:
                    case PRISM_2P:
                    case PRISM_3P:
                    case PRISM_4P:
                        if (slope.isPositive) {
                            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
                        } else {
                            setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
                        }
                        break;
                    default:
                        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                        break;
                }

            }

        }
    }*/

    /*@Override
    *//**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     *//*
    public MovingObjectPosition collisionRayTrace(World world, BlockPos blockPos, Vec3 startVec, Vec3 endVec) {
        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        MovingObjectPosition finalTrace = null;

        if (cbTileEntity != null) {

            Slope slope = Slope.getSlope(cbTileEntity);
            SlopeUtil slopeUtil = new SlopeUtil();

            int numPasses = slopeUtil.getNumPasses(slope);
            int precision = slopeUtil.getNumBoxesPerPass(slope);

            rayTracing = true;

             Determine if ray trace is a hit on slope. 
            for (int pass = 0; pass < numPasses; ++pass)
            {
                for (int slice = 0; slice < precision && finalTrace == null; ++slice)
                {
                    float[] box = slopeUtil.genBounds(slope, slice, precision, pass);

                    if (box != null) {
                        setBlockBounds(box[0], box[1], box[2], box[3], box[4], box[5]);
                        finalTrace = super.collisionRayTrace(world, x, y, z, startVec, endVec);
                    }
                }
                if (slope.type.equals(Type.OBLIQUE_EXT)) {
                    --precision;
                }
            }

            rayTracing = false;

             Determine true face hit since sloped faces are two or more shared faces. 

            if (finalTrace != null) {
                setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                finalTrace = super.collisionRayTrace(world, x, y, z, startVec, endVec);
            }

        }

        return finalTrace;
    }*/

    /*@Override
    *//**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
//    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
//        CbTileEntity cbTileEntity = getTileEntity(worldIn, pos);
//
//        if (cbTileEntity != null) {
//
//            AxisAlignedBB box = null;
//
//			float x = pos.getX();
//			float y = pos.getY();
//			float z = pos.getZ();
//
//			SlopeData slope = SlopeData.getSlope(cbTileEntity);
//            SlopeUtil slopeUtil = new SlopeUtil();
//
//            int precision = slopeUtil.getNumBoxesPerPass(slope);
//            int numPasses = slopeUtil.getNumPasses(slope);
//
//            for (int pass = 0; pass < numPasses; ++pass) {
//
//                for (int slice = 0; slice < precision; ++slice)
//                {
//                    float[] dim = slopeUtil.genBounds(slope, slice, precision, pass);
//
//                    if (dim != null) {
//                        box = new  AxisAlignedBB(x + dim[0], y + dim[1], z + dim[2], x + dim[3], y + dim[4], z + dim[5]);
//                    }
//
//                    if (box != null && entityBox.intersects(box)) {
//						collidingBoxes.add(box);
//                    }
//                }
//
//                if (slope.type.equals(Type.OBLIQUE_EXT)) {
//                    --precision;
//                }
//
//            }
//
//        }
//    }

/*    @Override
    *//**
     * Checks if the block is a solid face on the given side, used by placement logic.
     *//*
    public boolean isSideSolid(IBlockAccess blockAccess, BlockPos blockPos, EnumFacing side) {
        CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
        if (cbTileEntity != null) {
            if (isBlockSolid(blockAccess, blockPos)) {
                return Slope.getSlope(cbTileEntity).isFaceFull(side);
            }
        }
        return false;
    }*/

    @Override
    /**
     * Returns whether sides share faces based on sloping property and face shape.
     */
    protected boolean shareFaces(CbTileEntity cbTileEntity_adj, CbTileEntity cbTileEntity_src, EnumFacing side_adj, EnumFacing side_src) {
/*        if (cbTileEntity_adj.getBlockType() == this) {
            Slope slope_src = Slope.getSlope(cbTileEntity_src);
            Slope slope_adj = Slope.getSlope(cbTileEntity_adj);
            if (!slope_adj.hasSide(side_adj)) {
                return false;
            } else if (slope_src.getFaceBias(side_src) == slope_adj.getFaceBias(side_adj)) {
                return true;
            } else {
                return false;
            }
        }
        return super.shareFaces(cbTileEntity_adj, cbTileEntity_src, side_adj, side_src);*/
    	return false;
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
    	// IUnlistedProperty values are not retained between this method and onBlockPlacedBy(). Store with thread temporarily.
    	_threadLocalHitCoords.set(new Float[] { hitX, hitY, hitZ });
    	_threadLocalFacing.set(facing);
    	return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
    }
    
    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase entityLivingBase, ItemStack itemStack) {
    	super.onBlockPlacedBy(world, blockPos, blockState, entityLivingBase, itemStack);
        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        if (cbTileEntity != null) {

        	// Set type
        	switch (itemStack.getItemDamage()) {
	        	case 0:
	        		SlopeData.setType(cbTileEntity, SlopeData.Type.WEDGE);
	        		break;
	        	case 1:
	        		SlopeData.setType(cbTileEntity, SlopeData.Type.WEDGE_INTERIOR);
	        		break;
	        	case 2:
	        		SlopeData.setType(cbTileEntity, SlopeData.Type.WEDGE_EXTERIOR);
	        		break;
	        	case 3:
	        		SlopeData.setType(cbTileEntity, SlopeData.Type.OBLIQUE_INTERIOR);
	        		break;
	        	case 4:
	        		SlopeData.setType(cbTileEntity, SlopeData.Type.OBLIQUE_EXTERIOR);
	        		break;
	        	case 5:
	        		SlopeData.setType(cbTileEntity, SlopeData.Type.PRISM_WEDGE);
	        		break;
	        	case 6:
	        		SlopeData.setType(cbTileEntity, SlopeData.Type.PRISM);
	        		break;
        		default: // 7
        			SlopeData.setType(cbTileEntity, SlopeData.Type.INVERT_PRISM);
        			break;
        	}
        	
        	// Set rotation
        	float x = _threadLocalHitCoords.get()[0];
        	float y = _threadLocalHitCoords.get()[1];
        	float z = _threadLocalHitCoords.get()[2];
        	_threadLocalHitCoords.remove();
        	EnumFacing facing = _threadLocalFacing.get();
        	_threadLocalFacing.remove();
        	RotationUtil.Rotation rotation = RotationUtil.Rotation.X0_Y0_Z0;
        	EnumFacing cardinalFacing = entityLivingBase.getHorizontalFacing().getOpposite();
        	switch (facing) {
	        	case DOWN:
	        		if (x >= 0.2f && x <= 0.8f && z >= 0.2f && z <= 0.8f) {
	        			switch (entityLivingBase.getHorizontalFacing()) {
		        			case NORTH:
		        				rotation = RotationUtil.Rotation.X0_Y0_Z180;
		        				break;
		        			case SOUTH:
		        				rotation = RotationUtil.Rotation.X180_Y0_Z0;
		        				break;
		        			case WEST:
		        				rotation = RotationUtil.Rotation.X180_Y90_Z0;
		        				break;
		        			case EAST:
		        				rotation = RotationUtil.Rotation.X180_Y270_Z0;
		        				break;
							default:
								break;
	        			}
	        		} else if (1.0f - x > z && x <= 0.5f && x < z) {
	        			rotation = RotationUtil.Rotation.X180_Y90_Z0;
        			} else if (z > x && z >= 0.5f) {
        				rotation = RotationUtil.Rotation.X180_Y0_Z0;
        			} else if (x > 1.0f - z) {
        				rotation = RotationUtil.Rotation.X180_Y270_Z0;
        			} else {
        				rotation = RotationUtil.Rotation.X0_Y0_Z180;
        			}
	        		break;
	        	case UP:
	        		if (x >= 0.2f && x <= 0.8f && z >= 0.2f && z <= 0.8f) {
	        			switch (entityLivingBase.getHorizontalFacing()) {
		        			case NORTH:
		        				rotation = RotationUtil.Rotation.X0_Y0_Z0;
		        				break;
		        			case SOUTH:
		        				rotation = RotationUtil.Rotation.X0_Y180_Z0;
		        				break;
		        			case WEST:
		        				rotation = RotationUtil.Rotation.X0_Y270_Z0;
		        				break;
		        			case EAST:
		        				rotation = RotationUtil.Rotation.X0_Y90_Z0;
		        				break;
							default:
								break;
	        			}
	        		} else if (x < z && x <= 0.5f && 1.0f - x > z) {
	        			rotation = RotationUtil.Rotation.X0_Y270_Z0;
	        		} else if (1.0f - z > x && z <= 0.5f) {
	        			rotation = RotationUtil.Rotation.X0_Y0_Z0;
	        		} else if (x > z) {
	        			rotation = RotationUtil.Rotation.X0_Y90_Z0;
	        		} else {
	        			rotation = RotationUtil.Rotation.X0_Y180_Z0;
	        		}
	        		break;
	        	case NORTH:
	        		if (x >= 0.2f && x <= 0.8f) {
	        			if (y >= 0.5f) {
	        				rotation = RotationUtil.Rotation.X90_Y0_Z180;
	        			} else {
	        				rotation = RotationUtil.Rotation.X90_Y0_Z0;
	        			}
	        		} else if (x > y && x >= 0.5f && x > 1.0f - y) {
	        			rotation = RotationUtil.Rotation.X90_Y0_Z270;
	        		} else if (y > 1.0f - x && y >= 0.5f) {
	        			rotation = RotationUtil.Rotation.X90_Y0_Z180;
	        		} else if (x < y) {
	        			rotation = RotationUtil.Rotation.X90_Y0_Z90;
	        		} else {
	        			rotation = RotationUtil.Rotation.X90_Y0_Z0;
	        		}
	        		break;
	        	case SOUTH:
	        		if (x >= 0.2f && x <= 0.8f) {
	        			if (y >= 0.5f) {
	        				rotation = RotationUtil.Rotation.X270_Y0_Z0;
	        			} else {
	        				rotation = RotationUtil.Rotation.X90_Y180_Z0;
	        			}
	        		} else if (1.0f - x > y && x <= 0.5f && x < y) {
	        			rotation = RotationUtil.Rotation.X90_Y180_Z90;
	        		} else if (y > x && y >= 0.5f) {
	        			rotation = RotationUtil.Rotation.X270_Y0_Z0;
	        		} else if (x > 1.0f - y) {
	        			rotation = RotationUtil.Rotation.X270_Y0_Z90;
	        		} else {
	        			rotation = RotationUtil.Rotation.X90_Y180_Z0;
	        		}
	        		break;
	        	case WEST:
	        		if (z >= 0.2f && z <= 0.8f) {
	        			if (y >= 0.5f) {
	        				rotation = RotationUtil.Rotation.X270_Y90_Z0;
	        			} else {
	        				rotation = RotationUtil.Rotation.X90_Y270_Z0;
	        			}
	        		} else if (1.0f - z > y && z <= 0.5f && z < y) {
	        			rotation = RotationUtil.Rotation.X0_Y0_Z270;
	        		} else if (y > z && y >= 0.5f) {
	        			rotation = RotationUtil.Rotation.X270_Y90_Z0;
	        		} else if (z > 1.0f - y) {
	        			rotation = RotationUtil.Rotation.X180_Y0_Z90;
	        		} else {
	        			rotation = RotationUtil.Rotation.X90_Y270_Z0;
	        		}
	        		break;
	        	case EAST:
	        		if (z >= 0.2f && z <= 0.8f) {
	        			if (y >= 0.5f) {
	        				rotation = RotationUtil.Rotation.X0_Y270_Z90;
	        			} else {
	        				rotation = RotationUtil.Rotation.X90_Y90_Z0;
	        			}
	        		} else if (z > y && z >= 0.5f && z > 1.0f - y) {
	        			rotation = RotationUtil.Rotation.X0_Y180_Z90;
	        		} else if (y > 1.0f - z && y >= 0.5f) {
	        			rotation = RotationUtil.Rotation.X0_Y270_Z90;
	        		} else if (z < y) {
	        			rotation = RotationUtil.Rotation.X0_Y0_Z90;
	        		} else {
	        			rotation = RotationUtil.Rotation.X90_Y90_Z0;
	        		}
	        		break;
        	}
        	SlopeData.setRotation(cbTileEntity, rotation);
        }
    }

    @Override
    public EnumFacing[] getValidRotations(World world, BlockPos blockPos) {
        return EnumFacing.VALUES;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos blockPos, EnumFacing facing) {
        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        if (cbTileEntity != null) {
        	SlopeData.rotate(cbTileEntity, facing.getAxis());
        	return true;
        }
        return false;
    }
    
    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
	@Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

	@Override
    public boolean isFullCube(IBlockState blockState) {
        return false;
    }
    
    /**
     * Check if the face of a block should block rendering.
     *
     * Faces which are fully opaque should return true, faces with transparency
     * or faces which do not span the full size of the block should return false.
     *
     * @param state The current block state
     * @param world The current world
     * @param pos Block position in world
     * @param face The side to check
     * @return True if the block is opaque on the specified side.
     */
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return false;
    }

	public AxisAlignedBB rotateAABB(AxisAlignedBB aabb, EnumFacing facing) {
		// 计算旋转后的最小/最大坐标
		double minX = aabb.minX;
		double minY = aabb.minY;
		double minZ = aabb.minZ;
		double maxX = aabb.maxX;
		double maxY = aabb.maxY;
		double maxZ = aabb.maxZ;

		// 根据朝向旋转AABB
		switch (facing) {
			case WEST:
				// 旋转90度（西朝向）
				return new AxisAlignedBB(
						1 - maxZ, minY, minX,
						1 - minZ, maxY, maxX
				);
			case SOUTH:
				// 旋转180度（南朝向）
				return new AxisAlignedBB(
						1 - maxX, minY, 1 - maxZ,
						1 - minX, maxY, 1 - minZ
				);
			case EAST:
				// 旋转270度（东朝向）
				return new AxisAlignedBB(
						minZ, minY, 1 - maxX,
						maxZ, maxY, 1 - minX
				);
			case UP:
			case DOWN:
				// 上下朝向需要特殊处理
				// 这里简单返回原始AABB，实际可能需要绕X或Y轴旋转
				return aabb;
			case NORTH:
			default:
				// 北朝向不旋转
				return aabb;
		}
	}

}
