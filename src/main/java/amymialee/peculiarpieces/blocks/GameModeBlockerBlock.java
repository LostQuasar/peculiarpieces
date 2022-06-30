package amymialee.peculiarpieces.blocks;

import amymialee.visiblebarriers.VisibleBarriers;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameMode;

@SuppressWarnings("deprecation")
public class GameModeBlockerBlock extends Block {
    private final GameMode gameMode;

    public GameModeBlockerBlock(GameMode gameMode, FabricBlockSettings settings) {
        super(settings);
        this.gameMode = gameMode;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (context.isHolding(this.asItem())) {
            return VoxelShapes.fullCube();
        }
        if (VisibleBarriers.isVisible()) {
            return VoxelShapes.fullCube();
        }
        ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
        if (networkHandler != null) {
            PlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                PlayerListEntry playerListEntry = MinecraftClient.getInstance().getNetworkHandler().getPlayerListEntry(player.getGameProfile().getId());
                if (playerListEntry != null && playerListEntry.getGameMode() == gameMode) {
                    return VoxelShapes.fullCube();
                }
            }
        }
        return VoxelShapes.empty();
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        if (VisibleBarriers.isVisible()) return BlockRenderType.MODEL;
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0f;
    }
}