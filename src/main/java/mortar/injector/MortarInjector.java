package mortar.injector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.UnknownDependencyException;

public class MortarInjector
{
	public static final void inject(String pluginName)
	{
		if(!isMortarInstalled())
		{
			l("Mortar is not installed");
			downloadMortar();
			loadMortar();
		}
	}

	private static void loadMortar()
	{
		l("Loading Mortar");

		try
		{
			Bukkit.getPluginManager().loadPlugin(new File("plugins/Mortar.jar"));
			l("Mortar has been loaded.");
		}

		catch(UnknownDependencyException | InvalidPluginException | InvalidDescriptionException e)
		{
			e.printStackTrace();
			f("Failed to load Mortar");
		}
	}

	private static final boolean isMortarInstalled()
	{
		for(Plugin i : Bukkit.getPluginManager().getPlugins())
		{
			if(i.getName().equals("Mortar"))
			{
				return true;
			}
		}

		return false;
	}

	private static final String getMortarVersion()
	{
		return Bukkit.getPluginManager().getPlugin("Mortar").getDescription().getVersion();
	}

	public static final int getMortarVersionCode()
	{
		return getMortarVersionCodeFromVersion(getMortarVersion());
	}

	public static final void l(String s)
	{
		System.out.println("[Mortar Installer] INFO: " + s);
	}

	public static final void w(String s)
	{
		System.out.println("[Mortar Installer] WARN: " + s);
	}

	public static final void f(String s)
	{
		System.out.println("[Mortar Installer] FATAL: " + s);
	}

	private static void downloadMortar()
	{
		try
		{
			l("Preparing to download Mortar...");
			URL url = new URL("https://raw.githubusercontent.com/VolmitSoftware/Mortar/master/release/Mortar.jar");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			HttpURLConnection.setFollowRedirects(false);
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);
			InputStream in = con.getInputStream();
			File mortar = new File("plugins/Mortar.jar");
			FileOutputStream fos = new FileOutputStream(mortar);
			byte[] buf = new byte[8192];
			int r = 0;
			boolean downloading = false;
			while((r = in.read(buf)) != -1)
			{
				fos.write(buf, 0, r);

				if(!downloading)
				{
					downloading = true;
					l("Downloading Mortar");
				}
			}
			fos.close();
			in.close();
			con.disconnect();
			l("Mortar has been successfully downloaded.");
		}

		catch(Throwable e)
		{
			e.printStackTrace();
			f("Failed to download mortar!");
		}
	}

	private static final int getMortarVersionCodeFromVersion(String version)
	{
		if(!version.contains("."))
		{
			return (int) Math.pow(Integer.valueOf(version), 9);
		}

		String[] m = version.split("\\Q.\\E");
		int[] v = new int[] {0, 0, 0, 0};
		int c = 0;

		for(String i : m)
		{
			v[c] = Integer.valueOf(i);
			c++;
		}

		return (int) ((v[0] * Math.pow(10, 5)) + (v[1] * Math.pow(10, 4)) + (v[2] * Math.pow(10, 3)) + (v[3] * Math.pow(10, 2)));
	}
}
