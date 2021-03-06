package wraith.waystones;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import wraith.waystones.registries.BlockEntityRendererRegistry;
import wraith.waystones.registries.CustomScreenRegistry;

@Environment(EnvType.CLIENT)
public class WaystonesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.RegisterBlockEntityRenderers();
        CustomScreenRegistry.registerScreens();
        registerPacketHandlers();
    }

    private void registerPacketHandlers() {
        ClientSidePacketRegistry.INSTANCE.register(new Identifier(Waystones.MOD_ID, "waystone_packet"), (packetContext, attachedData) -> {
            PlayerEntity player = packetContext.getPlayer();

            // A note for the future: Please just treat the integrated server as remote. This is a bit of a hack.
            if(!(MinecraftClient.getInstance().getNetworkHandler().getConnection().isLocal())) {
                Waystones.WAYSTONE_DATABASE = new WaystoneDatabase(player == null ? null : player.getServer());
            }

            ListTag list = attachedData.readCompoundTag().getList("Waystones", 10);
            for (int i = 0; i < list.size(); ++i) {
                CompoundTag tag = list.getCompound(i);
                String name = tag.getString("Name");
                String world = tag.getString("World");
                String facing = tag.getString("Facing");
                int[] coords = tag.getIntArray("Coordinates");
                BlockPos pos = new BlockPos(coords[0], coords[1], coords[2]);
                Waystone waystone = new Waystone(name, pos, world, facing);
                Waystones.WAYSTONE_DATABASE.addWaystone(waystone);
                Waystones.WAYSTONE_DATABASE.discoverWaystone(player, name);
            }
        });
    }

}
