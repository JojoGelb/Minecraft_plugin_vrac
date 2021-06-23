package mc_plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import org.bukkit.util.Vector;



import net.md_5.bungee.api.ChatColor;




public class PluginListeners implements Listener {
	//documentation : https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/event/player/package-summary.html
	
	public Main main;
	
	public List<UUID> zeus_tridents = new ArrayList<>();
	
	/* =====================================================================
	  							Zone des evenements
	   =====================================================================*/
	
	public PluginListeners(Main main) {
		// TODO Auto-generated constructor stub
		this.main = main;
	}
	
	@EventHandler
	public void projectile_shot(ProjectileLaunchEvent event) {
		
		
		//Il y a une complication lors du lancement d'un objet: Il se transforme de item vers entité
		//Il faut donc faire la transistion. J'utilise ici L'id de l'objet que je stock pour vérifier que c'est le même.
		//Voir post: https://www.spigotmc.org/threads/detect-when-a-player-throws-a-named-snowball.408610/#post-3633558
		if(event.getEntity() instanceof Trident && event.getEntity().getShooter() instanceof Player) {
			Player player = (Player) event.getEntity().getShooter();
	        PlayerInventory inventory = player.getInventory();
	        if(inventory.getItemInMainHand().getType() != Material.AIR &&
	                inventory.getItemInMainHand().getType() == Material.TRIDENT &&
	                inventory.getItemInMainHand().getItemMeta().hasDisplayName() &&
	                inventory.getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_PURPLE + "Trident de Zeus, Propriétaire : " + player.getName())) {
	        	
	        	zeus_tridents.add(event.getEntity().getUniqueId());
	        	
	        }
		}
	}
	
	@EventHandler
	public void projectile_shot(ProjectileHitEvent event) {
		
		if(event.getEntity() instanceof Trident && zeus_tridents.contains(event.getEntity().getUniqueId()) ) {
			Trident trident = (Trident) event.getEntity();
				
			Block hit_bloc = event.getHitBlock(); 
			if(hit_bloc != null) {
				Location loc = hit_bloc.getLocation();
				loc.getWorld().strikeLightning(loc);
			}else {
				Entity hit_entity = event.getHitEntity(); 
				Location loc = hit_entity.getLocation();
				loc.getWorld().strikeLightning(loc);
			}
			zeus_tridents.remove(event.getEntity().getUniqueId());
			return;
			
		}
		
	}
	
	
	//Canne à peche shotbow
	@EventHandler
	public void fishing_rod(PlayerFishEvent event) {
		
			
			if(event.getState() == State.IN_GROUND) {
				
				Player p = event.getPlayer();
				
				Location hookLoc = event.getHook().getLocation();
				Location playerLoc = p.getLocation();
				
				Vector velo = new Vector();
				
				double veloX = hookLoc.getX() - playerLoc.getX();
				double veloY = hookLoc.getY() - playerLoc.getY();
				double veloZ = hookLoc.getZ() - playerLoc.getZ();
				
				if(veloY < 0) {
					veloY = 0.5;
					veloZ = veloZ/4;
					veloX = veloX/4;
				}else {
					veloY = veloY/7;
					veloZ = veloZ/8;
					veloX = veloX/8;
				}
				
				velo.setX( veloX);
				velo.setY( veloY);
				velo.setZ(veloZ);
				
				p.setVelocity(velo);

				
		}
			
	}
	
	@EventHandler
	public void shoot_bow_event(EntityShootBowEvent event) {
		
		Entity entity = event.getEntity();
		
		if (entity instanceof Player) {
			Player p = (Player) entity;
		
		
			ItemStack bow = event.getBow();
			if ((bow.hasItemMeta() && bow.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_PURPLE + "Flying Bow"))) {
					
				if (!p.getScoreboardTags().contains("FLY_BOW")) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.BOLD + "" + ChatColor.DARK_RED + "Le dieu de ce serveur ne t'a pas reconnu comme porteur de cette relique. Seul la mort peut nettoyer tes péchés");
					return;
				}
					
					Vector projectile = event.getProjectile().getVelocity();
					
					projectile.setY(projectile.getY() * 0.9);
					projectile.setX(projectile.getX()*1.5);
					projectile.setZ(projectile.getZ()*1.5);
					System.out.println(projectile.getY());
					System.out.println(projectile.getX());
					System.out.println(projectile.getZ());
					event.setProjectile(entity);
					entity.setVelocity(projectile);
					return;
			}
			
			Vector projectile = event.getProjectile().getVelocity();
			
			//Arc normal
			event.setCancelled(true);
			
			TNTPrimed tnt = (TNTPrimed) p.getWorld().spawnEntity(p.getLocation().add(new Location(p.getWorld(),0,2,0)), EntityType.PRIMED_TNT);
			tnt.setFuseTicks(40);
			tnt.setVelocity(projectile);
			
		}
		
	}
		
		

	//Event qui s'active lorsqu'un joueur se connecte au serveur
	@EventHandler
	public void onJoint(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		//player.getInventory().clear();
		
		boolean player_exist = false;
		
		for(Player p : this.main.players){

			if(p.getName().equalsIgnoreCase(player.getName())) {
				player_exist = true;
				break;
			}
		}
		
		if(player_exist == false) {
			this.main.players.add(player);
			
			//Ce sera ici pour corriger les soucis de redémarage serveur in game et conservation d'équipe
			
			this.main.scoreboard.getTeam("NO_TEAM").addEntry(player.getName());
			player.sendMessage("§6[PLUGIN INFO] You are in no team: use the command /gui_compass to change team");
			player.setDisplayName(ChatColor.YELLOW + player.getName() + ChatColor.WHITE);
			//player.setPlayerListName("§6[NO TEAM] §f" + player.getDisplayName());*/
			
		}
		
		
		//Fonction créé plus bas pour donner un exemple de comment marche les fonctions
		//Création d'une épé cheaté
		ItemStack godSword = createGodSword();
		
		//Ajout à l'inventaire du joueur
		player.getInventory().addItem(godSword);
		
		//Création d'un objet mis à la place du casque du joueur
		ItemStack head = new ItemStack(Material.GLASS, 1);
		player.getInventory().setHelmet(head);
		
		//Très important après modification: met à jour sur le jeu
		player.updateInventory();
		
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		//S'occupe des actions de click de l'utilisateur
		
		Player p = event.getPlayer();
		Action a = event.getAction();
		
		ItemStack it = event.getItem();
		
		//Vérification click sur panneaux
		if(event.getClickedBlock() != null && a == Action.RIGHT_CLICK_BLOCK) {
			
			BlockState bs = event.getClickedBlock().getState();
			if(bs instanceof Sign) {
				
				Sign sign = (Sign) bs;
				if(sign.getLine(1).equalsIgnoreCase("Spawn")) {
					//Force la commande déja définit /spawn sur le joueur
					p.chat("/spawn");
					return;
				}
				
			}
			
		}
		
		//Si rien en main lors de l'action : rien ne se passe
		if (it == null) return;
		
		//Vérifie si click droit
		if(a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK)  {
			
			//Création d'un éclair sur la position du joueur avec la godSword
			if(it.getType() == Material.IRON_SWORD && it.hasItemMeta()) {
				ItemStack godSword = createGodSword();
				if (it.equals(godSword)) {
					p.setNoDamageTicks(20);
					Location loc = p.getLocation();
					loc.getWorld().strikeLightning(loc);
					loc.getWorld().spawnEntity(loc, EntityType.LIGHTNING);
					
					Bukkit.broadcastMessage("§4" + this.main.getConfig().getString("message.god_sword.eclair"));
				}
				return;
			}
			
			//Ouverture inventaire après click droit sur bousolle spécial
			if(it.equals(CommandObjectCreator.create_gui_compass(this.main))) {
				
				//Créé un inventaire, paramètres: type d'inventaire: null, nombre de cases: 1 (max = 54)
				Inventory inv = Bukkit.createInventory(null, 9, "§8Menu GUI");
				
				populate_GUI_inventory(inv);
				
				p.openInventory(inv);
				
			}
			
			if(it.getType() == Material.TRIDENT && it.hasItemMeta()) {
				if(it.getItemMeta().getDisplayName().contains((ChatColor.DARK_PURPLE + "Trident de Zeus, Propriétaire : "))) {
					System.out.println("CONTAIN");
				}
			}
			
			
			//Vérifie si click droit dans le vide
			if(a == Action.RIGHT_CLICK_AIR) {
				//Si l'objet en main est une houe en diamond alors donne un effet de speed : click dans l'air
				if(it.getType() == Material.DIAMOND_HOE) {
					
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 2));
					return;
				}
				
			}
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		//Check les clicks dans un inventaire
		
		//Inventory inv = event.getInventory();
		Player p = (Player) event.getWhoClicked();
		ItemStack current = event.getCurrentItem();
		
		//Click dans le vide
		if(current == null) return;
		
		/* Petit problème: l'utilisateur peut bouger l'objet avec shift
		 * if(p.getOpenInventory().getTitle().equalsIgnoreCase("Crafting") ) {
			if (current.hasItemMeta() && current.getItemMeta().getDisplayName().equalsIgnoreCase("TEAM")){
				event.setCancelled(true);
				return;
			}
		}*/
		
		System.out.println(p.getOpenInventory().getTitle());
		
		if(p.getOpenInventory().getTitle().equalsIgnoreCase("§8Menu GUI") ) {
			
			//On evite qu'il garde l'item en main
			event.setCancelled(true);
			p.closeInventory();
			
			ItemStack item = create_itemStack(current.getType(), "Ne le déséquipé pas sinon impossible de le remettre");
			p.getInventory().setHelmet(item);
			
			//Switch c'est comme un if:
			
			//if (current.getType() == Material.BLACK_WOOL){
			//	p.setGameMode(GameMode.CREATIVE);
			//if (current.getType() == Material.BLUE_WOOL){
				//	truc
			//...
			
			//En fr ce serait. On vérifie ça: (current.getType())
			//Dans ce cas: (BLEU)
				//On fait ça ...
			
			//Ici on vérifie la couleur de la laine selectionné dans l'inventaire
			switch(current.getType()) {
				case BLACK_WOOL:
					this.main.scoreboard.getTeam("BLACK").addEntry(p.getName());
					p.setDisplayName(ChatColor.BLACK + p.getName() + ChatColor.WHITE);
					p.sendMessage("§6[PLUGIN INFO] JOIN TEAM BLACK");
					break;
				case BLUE_WOOL:
					this.main.scoreboard.getTeam("BLUE").addEntry(p.getName());
					p.setDisplayName(ChatColor.BLUE + p.getName() + ChatColor.WHITE);
					p.sendMessage("§6[PLUGIN INFO] JOIN TEAM BLUE");
					break;
				case WHITE_WOOL:
					this.main.scoreboard.getTeam("WHITE").addEntry(p.getName());
					p.setDisplayName(ChatColor.WHITE + p.getName() + ChatColor.WHITE);
					p.sendMessage("§6[PLUGIN INFO] JOIN TEAM WHITE");
					break;
				case PINK_WOOL:
					this.main.scoreboard.getTeam("PINK").addEntry(p.getName());
					p.setDisplayName(ChatColor.LIGHT_PURPLE + p.getName() + ChatColor.WHITE);
					p.sendMessage("§6[PLUGIN INFO] JOIN TEAM PINK");
					break;
				default:
					p.sendMessage("§1WTF elle sort d'ou cette laine?");
					break;
			}
			
		}
	}
	
	
	/* =====================================================================
									Autre Fonctions 
		=====================================================================*/
	
	public void populate_GUI_inventory(Inventory inv) {
		
		
		ItemStack blackWool = create_itemStack(Material.BLACK_WOOL, "§aEquipe noir");
		inv.setItem(0, blackWool);
		
		ItemStack whiteWool = create_itemStack(Material.WHITE_WOOL, "§aEquipe blanche");
		inv.setItem(1, whiteWool);
		
		ItemStack blueWool = create_itemStack(Material.BLUE_WOOL, "§aEquipe bleu");
		inv.setItem(2, blueWool);
		
		ItemStack pinkWool = create_itemStack(Material.PINK_WOOL, "§aEquipe Rose");
		inv.setItem(3, pinkWool);
	
	
	}
	
	public ItemStack create_itemStack(Material mat, String name) {
		ItemStack item = new ItemStack(mat, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack createGodSword() {
		
		//Créé une épé
		ItemStack customsword = new ItemStack(Material.IRON_SWORD, 1);
		
		//Récupère l'objet gérant les données de l'épé
		ItemMeta custom = customsword.getItemMeta();
		
		//Change les propriétés de l'objet: voir la documentation de ItemMeta pour voir ce qu'on peut faire d'autre
		custom.setDisplayName("§cPunisher");
		custom.setLore(Arrays.asList("Ceci est l'épé créé par les dieux",
				"Seul le modérateur peut la posséder", 
				"Une légende dit que un coup de cette lame peut","transcender les limites du serveur"));
		custom.addEnchant(Enchantment.DAMAGE_ALL, 200, true);
		//Cache la ligne moche avec enchant lvl 200
		custom.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		//applique les modifications à l'épé
		customsword.setItemMeta(custom);
		
		//on retourne le résultat à la fonction qui l'a appellé
		return customsword;
	}
	
	
}
