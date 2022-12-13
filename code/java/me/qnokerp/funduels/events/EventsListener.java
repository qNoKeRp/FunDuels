package me.qnokerp.funduels.events;

import me.qnokerp.funduels.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Objects;

public class EventsListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(Main.duelists.containsKey(event.getEntity())) {
            Events.finishEvent(Objects.requireNonNull(event.getEntity().getKiller()), event.getEntity());
        }
    }

}
