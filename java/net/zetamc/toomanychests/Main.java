package net.zetamc.toomanychests;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import net.zetamc.toomanychests.proxy.*;
import net.zetamc.toomanychests.util.*;
import net.zetamc.toomanychests.util.handlers.GUIHandler;

@Mod (modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class Main {
	@Instance
	public static Main instance;
	
	@SidedProxy (clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void PreInit (FMLPreInitializationEvent event) {
		
	}
	
	@EventHandler
	public static void init (FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler (instance, new GUIHandler ());
	}
	
	@EventHandler
	public static void PostInit (FMLPostInitializationEvent event) {
		
	}
}
