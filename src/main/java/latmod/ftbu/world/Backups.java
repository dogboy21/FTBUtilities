package latmod.ftbu.world;

import java.io.File;
import java.util.Arrays;

import org.apache.logging.log4j.*;

import ftb.lib.FTBLib;
import latmod.ftbu.mod.FTBUTicks;
import latmod.ftbu.mod.config.FTBUConfigBackups;
import latmod.lib.LMFileUtils;
import net.minecraft.world.World;

public class Backups
{
	public static final Logger logger = LogManager.getLogger("FTBU Backups");
	
	public static File backupsFolder;
	public static long lastTimeRun = -1;
	public static boolean shouldRun = false;
	public static Thread thread;
	public static boolean commandOverride = false;
	
	public static void init()
	{
		backupsFolder = FTBUConfigBackups.folder.get().isEmpty() ? new File(FTBLib.folderMinecraft, "/backups/") : new File(FTBUConfigBackups.folder.get());
		if(!backupsFolder.exists()) backupsFolder.mkdirs();
		thread = null;
		clearOldBackups();
		logger.info("Backups folder created @ " + backupsFolder.getAbsolutePath());
	}
	
	public static boolean enabled()
	{ return commandOverride || FTBUConfigBackups.enabled.get(); }
	
	public static boolean run()
	{
		if(thread != null || !shouldRun || !enabled()) return false;
		World w = FTBLib.getServerWorld();
		if(w == null) return false;
		shouldRun = false;
		thread = new ThreadBackup(w);
		thread.start();
		return true;
	}
	
	public static long getSecondsUntilNextBackup()
	{ return ((lastTimeRun + (long)(FTBUConfigBackups.backupTimer.get() * 3600D * 1000D)) - FTBUTicks.currentMillis()) / 1000L; }
	
	public static void clearOldBackups()
	{
		String[] s = backupsFolder.list();
		
		if(s.length > FTBUConfigBackups.backupsToKeep.get())
		{
			Arrays.sort(s);
			
			int j = s.length - FTBUConfigBackups.backupsToKeep.get();
			logger.info("Deleting " + j + " old backups");
			
			for(int i = 0; i < j; i++)
			{
				File f = new File(backupsFolder, s[i]);
				if(f.isDirectory())
				{
					logger.info("Deleted old backup: " + f.getPath());
					LMFileUtils.delete(f);
				}
			}
		}
	}
}