package mc_plugin;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TimerTask extends BukkitRunnable {
	
	private int timer = 10;
	
	@Override
	public void run() {
		
		if(timer == 0) {
			Bukkit.broadcastMessage("Fin du Timer");
			cancel();
		}
		
		//Bukkit.broadcastMessage("Timer: " + timer);
		
		timer = timer -1; // ou aussi timer --;
		
	}
	

}
