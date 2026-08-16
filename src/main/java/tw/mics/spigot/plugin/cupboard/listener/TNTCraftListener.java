package tw.mics.spigot.plugin.cupboard.listener;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import tw.mics.spigot.plugin.cupboard.Cupboard;
import tw.mics.spigot.plugin.cupboard.config.Locales;
import tw.mics.spigot.plugin.cupboard.utils.Util;

public class TNTCraftListener extends MyListener {
	public TNTCraftListener(Cupboard instance)
	{
		super(instance);
	    overwriteTNTRecipes();
	}

	private void overwriteTNTRecipes(){
    	Iterator<Recipe> it = this.plugin.getServer().recipeIterator();
    	Recipe recipe;
    	
    	//remove TNT Recipes（把原版跟其他插件可能加的TNT配方都清掉，重新加我們要的兩種）
    	while(it.hasNext()){
    		recipe = it.next();
    		if (recipe != null && recipe.getResult().getType() == Material.TNT){
				it.remove();
    		}
    	}
    	
    	//setup 特殊TNT：中間原版TNT，周圍8格火藥
    	ItemStack item = new ItemStack(Material.TNT);
    	ItemMeta meta = item.getItemMeta();
    	meta.setDisplayName(Locales.TNT_SPECIAL_NAME.getString());
    	meta.setLore(Locales.TNT_TNT_LORE.getStringList());
    	item.setItemMeta(meta);
    	Util.markSpecialTNTItem(item); // 標記成特殊TNT，跟原版TNT區分，放置時才會走邪惡精華/自動引爆邏輯
    	
    	ShapedRecipe shapedRecipe = new ShapedRecipe(item);
    	shapedRecipe.shape("GGG", "GTG", "GGG");
    	shapedRecipe.setIngredient('G', Material.GUNPOWDER);
    	shapedRecipe.setIngredient('T', Material.TNT);
    	Bukkit.addRecipe(shapedRecipe);
    	
    	//setup 原版TNT：真正原版配方＆外觀，不帶任何標記
    	item = new ItemStack(Material.TNT);
    	shapedRecipe = new ShapedRecipe(item);
    	shapedRecipe.shape("GSG", "SGS", "GSG");
    	shapedRecipe.setIngredient('S', Material.SAND);
    	shapedRecipe.setIngredient('G', Material.GUNPOWDER);
    	Bukkit.addRecipe(shapedRecipe);
    }

}
