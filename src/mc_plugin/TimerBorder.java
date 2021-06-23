package mc_plugin;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TimerBorder extends BukkitRunnable {
	
	public Main main;
	
	public TimerBorder(Main main) {
		// TODO Auto-generated constructor stub
		this.main = main;
	}

	@Override
	public void run() {
		
		if(this.main.world_border.getSize() <= 25) {
			Bukkit.broadcastMessage("Fin du rétrécissement de la border");
			cancel();
		}
		
		this.main.world_border.setSize(this.main.world_border.getSize()- this.main.getConfig().getInt("border.speed"));
		
	}

}
