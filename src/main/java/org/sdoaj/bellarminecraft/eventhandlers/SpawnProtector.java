package org.sdoaj.bellarminecraft.eventhandlers;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.sdoaj.bellarminecraft.Configuration;

import java.util.function.Predicate;

public class SpawnProtector {
    private void sendMessage(EntityPlayer player) {
        player.sendMessage(new TextComponentString(TextFormatting.RED + "" + TextFormatting.BOLD + "This area is protected."));
    }

    private boolean isOp(EntityPlayer player) {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile()) != null;
    }

    @SubscribeEvent
    public void preventBreaking(BlockEvent.BreakEvent event) {
        if (isOp(event.getPlayer())) {
            return;
        }

        if (Configuration.isProtected(event.getPos())) {
            event.setCanceled(true);
            sendMessage(event.getPlayer());
        }
    }

    @SubscribeEvent
    public void preventPlacing(BlockEvent.PlaceEvent event) {
        if (isOp(event.getPlayer())) {
            return;
        }

        if (Configuration.isProtected(event.getPos())) {
            event.setCanceled(true);
            sendMessage(event.getPlayer());
        }
    }

    private void preventEntityEvent(EntityEvent event, Predicate<Entity> predicate) {
        Entity entity = event.getEntity();

        if (!Configuration.isProtected(entity.getPosition())) {
            return;
        }

        if (predicate.test(entity)) {
            event.setCanceled(true);
        }
    }

    private void preventEntityEvent(EntityEvent event) {
        preventEntityEvent(event, x -> true);
    }

    @SubscribeEvent
    public void preventSpawning(EntityJoinWorldEvent event) {
        preventEntityEvent(event, x -> x instanceof EntityMob);
    }
}