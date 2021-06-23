package mc_plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandBorder implements CommandExecutor {
	
	public Main main;
	
	public TimerBorder timer;
	
	public double max_border;

	public CommandBorder(Main main) {
		this.main = main;
		this.max_border = this.main.getConfig().getInt("border.size");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println(this.timer);
		
		if(cmd.getName().equalsIgnoreCase("start_border")) {
			if(this.timer != null) {
				sender.sendMessage("Border déja existante. Essayer /restart_border");
				return false;
			}
			this.timer = new TimerBorder(this.main);
			this.timer.runTaskTimer(this.main, 0, 20);
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("stop_border")) {
			if(this.timer == null) {
				sender.sendMessage("La border n'a pas encore été lancée");
				return false;
			}
			
			this.timer.cancel();
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("restart_border")) {
			if(this.timer == null) {
				sender.sendMessage("La border n'a pas encore été lancée");
				return false;
			}
			
			this.timer.cancel();
			this.timer = new TimerBorder(this.main);
			this.timer.runTaskTimer(this.main, 0, 20);
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("reset_border")) {
			if(this.timer == null) {
				sender.sendMessage("La border n'a pas encore été lancée");
				return false;
			}
			
			this.timer.cancel();
			this.timer = null;
			this.main.world_border.setSize(this.max_border);
			
			return true;
		}
		
		return false;
	}

}

