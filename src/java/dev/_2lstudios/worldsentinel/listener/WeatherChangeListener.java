package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.weather.WeatherChangeEvent;
import dev._2lstudios.worldsentinel.configurations.MainConfiguration;
import org.bukkit.event.Listener;

class WeatherChangeListener implements Listener {
    private final MainConfiguration mainConfiguration;

    WeatherChangeListener(final MainConfiguration mainConfiguration) {
        this.mainConfiguration = mainConfiguration;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onWeatherChange(final WeatherChangeEvent event) {
        if (event.toWeatherState() && !this.mainConfiguration.isWeatherAllowed()) {
            event.setCancelled(true);
        }
    }
}
