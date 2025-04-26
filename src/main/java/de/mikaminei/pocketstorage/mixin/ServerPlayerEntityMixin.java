package de.mikaminei.pocketstorage.mixin;

import de.mikaminei.pocketstorage.util.RemoteAccessData;
import de.mikaminei.pocketstorage.util.RemoteAccessManager;
import de.mikaminei.pocketstorage.world.ChunkLoadingHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(method = "closeHandledScreen", at = @At("HEAD"))
    private void closeHandledScreen(CallbackInfo callbackInfo) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        RemoteAccessData accessData = RemoteAccessManager.removeAccess(player.getUuid());

        if (accessData != null) {
            ChunkLoadingHelper.unrequestChunkLoad(player.getServerWorld(), accessData.pos());
        }
    }
}
