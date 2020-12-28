package net.zetamc.toomanychests.util;

import org.apache.logging.log4j.*;

public class Reference {
	public static final String MOD_ID = "tmchests";
	public static final String NAME = "Too Many Chests!";
	public static final String VERSION = "0.3";
	public static final String ACCEPTED_VERSIONS = "[1.12.2]";
	public static final String CLIENT_PROXY_CLASS = "net.zetamc.toomanychests.proxy.ClientProxy";
	public static final String COMMON_PROXY_CLASS = "net.zetamc.toomanychests.proxy.CommonProxy";

	public static final Logger LOGGER = LogManager.getLogger (Reference.MOD_ID);
	
	public static final int GUI_CUSTOM_CHEST = 2;
	public static final int GUI_PADLOCK = 3;
}
