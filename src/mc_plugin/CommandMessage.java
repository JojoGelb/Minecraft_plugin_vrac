package mc_plugin;

import mc_plugin.Main;
import net.md_5.bungee.api.ChatColor;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class CommandMessage implements CommandExecutor {
	
	public Main main;
	
	public CommandMessage(Main main) {
		// TODO Auto-generated constructor stub
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		/* N'autorise que les joueurs à entrer les commandes (bloque la console
		 * if (!(sender instanceof Player)) {
			sender.sendMessage("Only player may execute this command!");
			return true;
		}*/
		
		//Color code : https://minecraft.tools/en/color-code.php
		
		if(cmd.getName().equalsIgnoreCase("alerte")){
			if(args.length == 0) {
				Bukkit.broadcastMessage("§4Message pour tout le monde!!!!!!!");
			}else {
				String message = "";
				for(int i =0; i < args.length; i++ ) {
					message = message + " " + args[i];
				}
				Bukkit.broadcastMessage("§4"+ message);
			}
			
			return true;
		}
		
		if ((sender instanceof Player)) {
		
			Player p = (Player) sender;
			
			if(cmd.getName().equalsIgnoreCase("hello")){
				
				if(p.getName().equals("CCforAkalabo")) {
					p.sendMessage("§eWelcome game master");
					return true;
				}else {
					p.sendMessage("§4You don't have the permission to execute this command§b" + p.getName());
					return false;
				}
			}
			
			if(cmd.getName().equalsIgnoreCase("insulte")){
				
				List<String> li = this.main.getConfig().getStringList("message.insulte");
				Random rand = new Random();
				
				if(args.length == 0) {
					Bukkit.broadcastMessage(li.get(rand.nextInt(li.size())));
					return true;
				}
				if(args.length == 1) {
					Player player_receving = Bukkit.getPlayerExact(args[0]);
					if(player_receving == null)
					{
					    p.sendMessage("Joueur introuvable");
					    return false;
					}else {
						player_receving.sendMessage(ChatColor.BOLD +""+ChatColor.GRAY + "<" +p.getDisplayName() + "> "+ li.get(rand.nextInt(li.size())));
						return true;
					}
				}
					
			}
			//Commande pour rejoindre l'équipe des taupes
			if(cmd.getName().equalsIgnoreCase("join_taupe")){
				p.addScoreboardTag("taupe");
				p.sendMessage(ChatColor.BOLD + "§4Vous êtes désormais dans l'équipe des taupes!");
				
			}
			
			//Commande pour envoyer des messages qu'aux taupes
			if(cmd.getName().equalsIgnoreCase("msg_taupe")){
				
				if(!p.getScoreboardTags().contains("taupe")) {
					p.sendMessage(ChatColor.BOLD + "§4Seul les taupes peuvent parler avec cette commande");
					return false;
				}
				
				if(args.length == 0) {
					p.sendMessage(ChatColor.BOLD +"§4Empty message");
					return false;
				}
				
				String message = "";
				
				for (String ar : args) {
					message += ar + " ";
				}
				
				//On boucle sur tous les joueurs: on regarde s'ils ont le tag taupe, si oui on envoie le msg
				for( Player pl : this.main.players ) {
					if(pl.getScoreboardTags().contains("taupe")) {
						pl.sendMessage(ChatColor.ITALIC + "§8[Taupe]<" + p.getDisplayName() + "> " + message);
					}
					
				}
				
				return true;
				
			}

		}
		
		
		return false;
	}
	
}