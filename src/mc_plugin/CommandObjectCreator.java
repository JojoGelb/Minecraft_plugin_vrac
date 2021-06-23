package mc_plugin;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.mojang.authlib.GameProfile;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import net.minecraft.server.v1_16_R3.WorldServer;

public class CommandObjectCreator implements CommandExecutor {
	
	/* ========================================================
	 					Objets de la classe
	   ======================================================== */
	
	public Main main;
	
	
	/* ========================================================
							Constructeur
		======================================================== */

	
	public CommandObjectCreator(Main main) {
		//Constructor: appellé lorsque la classe est généré dans le code (bien pour passé des objets en paramètre ou créé des objets utiles pour après)
		//This permet d'accéder aux objets de la classe
		this.main = main;
	}
	
	
	/* ========================================================
							Méthodes
		======================================================== */


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			
			if(cmd.getName().equalsIgnoreCase("gui_compass")){
			
				ItemStack compass = create_gui_compass(this.main);
				
				//Ajout à l'inventaire du joueur
				player.getInventory().addItem(compass);
				
				//Très important après modification: met à jour sur le jeu
				player.updateInventory();
				return true;
			}
			
			if(cmd.getName().equalsIgnoreCase("fly_bow")){
				
				player.addScoreboardTag("FLY_BOW");
				
				ItemStack bow = create_fly_bow(this.main);
				
				//Ajout à l'inventaire du joueur
				player.getInventory().addItem(bow);
				
				//Très important après modification: met à jour sur le jeu
				player.updateInventory();
				return true;
			}
			if(cmd.getName().equalsIgnoreCase("zeus_trident")){
				player.addScoreboardTag("ZEUS");
				
				ItemStack trident = create_zeus_trident(this.main, player.getName());
				
				//Ajout à l'inventaire du joueur
				player.getInventory().addItem(trident);
				
				//Très important après modification: met à jour sur le jeu
				player.updateInventory();
			}
			
		}
		
		return false;
	}
	
	public static ItemStack create_zeus_trident(Main main, String player_name){
		ItemStack trident = new ItemStack(Material.TRIDENT, 1);
		trident.addEnchantment(Enchantment.LOYALTY, 1);
		
		ItemMeta custom = trident.getItemMeta();
		
		//Change les propriétés de l'objet: voir la documentation de ItemMeta pour voir ce qu'on peut faire d'autre
		custom.setDisplayName(ChatColor.DARK_PURPLE + "Trident de Zeus, Propriétaire : " + player_name);
		custom.setLore(Arrays.asList("Ce trident ne peut être manié que par"," celui qu'il a reconnu comme maitre"));
		custom.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		custom.setUnbreakable(true);
		//applique les modifications
		trident.setItemMeta(custom);
		return trident;
	}
	
	public static ItemStack create_fly_bow(Main main){
		//Créé une boussole
		ItemStack bow = new ItemStack(Material.BOW, 1);
		bow.addEnchantment(Enchantment.VANISHING_CURSE, 1);
		bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		
		//Récupère l'objet gérant les données de l'épé
		ItemMeta custom = bow.getItemMeta();
		
		custom.setUnbreakable(true);
		
		//Change les propriétés de l'objet: voir la documentation de ItemMeta pour voir ce qu'on peut faire d'autre
		custom.setDisplayName(ChatColor.DARK_PURPLE + "Flying Bow");
		custom.setLore(Arrays.asList("Attention les dégats de chute"," ne sont pas pris en compte"));
		custom.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		//applique les modifications
		bow.setItemMeta(custom);
		
		//on retourne le résultat à la fonction qui l'a appellé
		return bow;
	}
	
	public static ItemStack create_gui_compass( Main main) {
		
		//Créé une boussole
		ItemStack compass = new ItemStack(Material.COMPASS, 1);
		
		//Récupère l'objet gérant les données de l'épé
		ItemMeta custom = compass.getItemMeta();
		
		//Change les propriétés de l'objet: voir la documentation de ItemMeta pour voir ce qu'on peut faire d'autre
		custom.setDisplayName(main.getConfig().getString("message.bousolle.gui_compass.nom"));
		custom.setLore(Arrays.asList(main.getConfig().getString("message.bousolle.gui_compass.Lore")));
		
		//applique les modifications
		compass.setItemMeta(custom);
		
		//on retourne le résultat à la fonction qui l'a appellé
		return compass;
	}

}
