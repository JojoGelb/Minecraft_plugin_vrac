package mc_plugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.entity.Player;

import mc_plugin.CommandMessage;


@SuppressWarnings("unused")
public class Main extends JavaPlugin {
	
	public List<Player> players = new ArrayList<>();
	
	public List<Player> taupes = new ArrayList<>();
	
	public Scoreboard scoreboard;
	
	public WorldBorder world_border;
	
	
	// Se lance lorsque le serveur démarre
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		
		System.out.println("Le plugin vient de demarrer");
		
		//Fonction qui vérifie les teams et les instancies
		this.create_scordboard();
		
		saveDefaultConfig(); //important pour charger la config de jeu
		
		//--------Cette section traite des listeners-----------------
		//Listerner: attendent que l'utilisateur entre la commande au début avant de se lancer
		
		//1: attente une commande dans le chat:
		
		//5 fois sur le meme fichier: voir code pour différenciation: envoie de message
		getCommand("hello").setExecutor(new CommandMessage(this));
		getCommand("alerte").setExecutor(new CommandMessage(this));
		getCommand("insulte").setExecutor(new CommandMessage(this));
		getCommand("join_taupe").setExecutor(new CommandMessage(this));
		getCommand("msg_taupe").setExecutor(new CommandMessage(this));
		//La meme mais sur un autre fichier: création d'objet et ajout dans l'inventaire
		
		CommandObjectCreator objectCreator = new CommandObjectCreator(this);
		getCommand("gui_compass").setExecutor(objectCreator);
		getCommand("fly_bow").setExecutor(objectCreator);
		getCommand("zeus_trident").setExecutor(objectCreator);
		getCommand("create_buddy").setExecutor(objectCreator);
		
		//commandes relative à des déplacements dans l'espace (/tp)
		getCommand("spawn").setExecutor(new CommandTP(this));
		getCommand("tpRandom").setExecutor(new CommandTP(this));
		
		CommandBorder border = new CommandBorder(this);
		
		getCommand("start_border").setExecutor(border);
		getCommand("stop_border").setExecutor(border);
		getCommand("restart_border").setExecutor(border);
		getCommand("reset_border").setExecutor(border);
		
		// 2: Attente sur évenement:
		
		//Attend un evenement pour se déclencher(click ...)
		getServer().getPluginManager().registerEvents(new PluginListeners(this), this);
		
		
		//--------Cette section traite des Timers-----------------
		//Timer: une classe existante dans les fichiers qui étend Runnable
		//Permet de gérer les ticks
		TimerTask task = new TimerTask();
		//3 paramètres: 
		//L'instance du plugin: ici this, 
		//Le délais avant le lancement: 0 (appel tout de suite), 
		//Et le temps entre chaque appel de run : 20 tick = 1s
		task.runTaskTimer(this, 0, 20);
		
		//On récupère la world border
		World world = Bukkit.getWorld(this.getConfig().getString("message.nom_du_monde"));
		this.world_border = world.getWorldBorder();
		this.world_border.setCenter(this.getConfig().getInt("border.center.x"),this.getConfig().getInt("border.center.y"));
		this.world_border.setSize(this.getConfig().getInt("border.size"));
		
		//Une version plus propre est disponible dans CommandBorder
		/* Lancer la world border en mode full monkey
		 * Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
			
			@Override
			public void run() {
				if(wb.getSize() >= 25) {
					wb.setSize(wb.getSize() - 1.0);
					System.out.println(wb.getSize());
				}
			}
		}, 0, 20);*/
		
	}

	// Se lance lorsque le serveur s'éteint
	@Override
	public void onDisable() {
		System.out.println("Le plugin vient de s'arréter");
	}
	
	
	public void create_scordboard() {
		//Récupère le scoreboard de tab, ajoute les teams au besoin, change les couleurs
		scoreboard= Bukkit.getScoreboardManager().getMainScoreboard();
		
		// How to delete a team from code
		//scoreboard.getTeam("NO TEAM").unregister();
		
		//On ajoute des équipes si elles n'existent pas déja
		if(scoreboard.getTeam("NO_TEAM") == null) {
			scoreboard.registerNewTeam("NO_TEAM");
		}
		//Met un préfix devant le pseudo des joueurs
		scoreboard.getTeam("NO_TEAM").setPrefix(ChatColor.YELLOW + "[NO TEAM] ");
		//Change la couleur des joueurs
		scoreboard.getTeam("NO_TEAM").setColor(ChatColor.YELLOW);

		if(scoreboard.getTeam("BLUE") == null) {
			scoreboard.registerNewTeam("BLUE");
		}
		scoreboard.getTeam("BLUE").setColor(ChatColor.BLUE);
		scoreboard.getTeam("BLUE").setAllowFriendlyFire(false);
		
		if(scoreboard.getTeam("BLACK") == null) {
			scoreboard.registerNewTeam("BLACK");
		}
		scoreboard.getTeam("BLACK").setColor(ChatColor.BLACK);
		
		if(scoreboard.getTeam("PINK") == null) {
			scoreboard.registerNewTeam("PINK");
		}
		scoreboard.getTeam("PINK").setColor(ChatColor.LIGHT_PURPLE);
		
		if(scoreboard.getTeam("WHITE") == null) {
			scoreboard.registerNewTeam("WHITE");
		}
		scoreboard.getTeam("WHITE").setColor(ChatColor.WHITE);
		
	}
}