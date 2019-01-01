package org.sdoaj.bellarminecraft.eventhandlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.sdoaj.bellarminecraft.Configuration;

import java.util.function.Consumer;
import java.util.function.Function;
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

    private <T extends EntityEvent> void doEntityAction(T event, Consumer<T> consumer, Predicate<EntityEvent> predicate) {
        if (!Configuration.isProtected(event.getEntity().getPosition())) {
            return;
        }

        if (predicate.test(event)) {
            consumer.accept(event);
        }
    }

    private Consumer<EntityEvent> cancel = e -> e.setCanceled(true);
    private Consumer<EntityEvent> setDead = e -> e.getEntity().setDead();
    private Predicate<EntityEvent> isMonster = e -> e.getEntity() instanceof EntityMob;

    @SubscribeEvent
    public void preventSpawning(EntityJoinWorldEvent event) {
        doEntityAction(event, cancel, isMonster);
    }

   @SubscribeEvent
    public void preventExisting(LivingEvent.LivingUpdateEvent event) {
        doEntityAction(event, setDead, isMonster);
    }

    // allows only OPs to attack things
    @SubscribeEvent
    public void preventAttack(LivingAttackEvent event) {
        doEntityAction(event, cancel, e -> {
            if (event.getSource().getTrueSource() instanceof EntityPlayer) {
                return !isOp((EntityPlayer) event.getSource().getTrueSource());
            }

            return true;
        });
    }
}