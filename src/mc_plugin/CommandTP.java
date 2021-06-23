package mc_plugin;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTP implements CommandExecutor {
	
	/* ========================================================
						Objets de la classe
	======================================================== */

	public Main main;


	/* ========================================================
							Constructeur
	======================================================== */

	//Constructor: appellé lorsque la classe est généré dans le code 
	//(bien pour passé des objets en paramètre ou créé des objets utiles pour après)
	public CommandTP(Main main) {
		this.main = main;
	}
	
	/* ========================================================
							Méthodes
	======================================================== */

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			
			if(cmd.getName().equalsIgnoreCase("tpRandom")){
				Random rand = new Random();
				Location playerLocation = player.getLocation();
				
				Location randomLocation = new Location(player.getWorld(), playerLocation.getX() + rand.nextInt(50), 
																		  playerLocation.getY(), 
																		  playerLocation.getZ() + rand.nextInt(50));
				player.teleport(randomLocation);
				return true;
			}
			//Position dans l'espace: x,y,z direction regard
			Location spawn = new Location(player.getWorld(), 0.5,102,0.5, 0.5f, 0f);
			player.sendMessage("§2Retour au spawn");
			player.teleport(spawn);
		}
		
		return false;
	}

}
