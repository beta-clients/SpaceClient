package net.minecraft.src.Space;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Space.hacks.*;
import net.minecraft.src.Space.settings.*;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;

public class Client{
	public static Minecraft mc;
	public static Hack[] hacks = new Hack[4096];
	public static ArrayList<Hack> hackList = new ArrayList<Hack>();
	public static String currentVersion = "1.0.1";
	public Client(Minecraft mc){
		this.mc = mc;
	}
	public static void addChatMessage(String msg){
		mc.ingameGUI.addChatMessage(msg);
	}
	public static void addHack(int i, Hack h){
		hacks[i] = h;
		hackList.add(h);
	}
	public static void loadHacks(){
		addHack(0, new AntiKB(25));
		addHack(1, new Fullbright(46));
		addHack(2, new JumpBoost(22));
		addHack(3, new NoFall(24));
		addHack(4, new Fly(19));
		addHack(5, new FastFly(49));
		addHack(6, new XRay(45));
		addHack(7, new KillAura(37));
		addHack(8, new Instant(47));
		addHack(9, new Jesus(36));
		addHack(10, new NoPush(38));
		addHack(11, new NoWater(0));
		addHack(12, new Freecam(34));
		addHack(13, new Coords(35));
		addHack(14, new Radar(-1));
		addHack(15, new AutoEat(-1));
		addHack(16, new FixedTime(-1));
		addHack(17, new VisualRange(-1));
		addHack(18, new AutoWalk(-1));
		addHack(19, new AutoHold(-1));
		addHack(20, new Scaffold(-1));
		addHack(21, new Timer(-1));
		addHack(22, new AutoReconnect(-1));
		addHack(23, new AutoTunnel(-1));
		addHack(24, new Yaw(-1));
		addHack(25, new HUD(-1));
		addHack(26, new XCarry(-1));
		addHack(27, new Tracers(-1));
		addHack(28, new PacketSpeed(-1));
		addHack(29, new PlayerNotify(-1));
		addHack(30, new DogOwner(-1));
		addHack(31, new Keybinds(-1));
		addHack(32, new InventoryWalk(-1));
		addHack(33, new Packet19Sender(-1));
		addHack(34, new NoClip(-1));
		try{
			FileReader loadSettings = new FileReader("hacks.txt");
			int i = 0;
			BufferedReader br = new BufferedReader(loadSettings);
			String line;
			while((line = br.readLine()) != null){
				String[] arguments = line.split(" ");
				int key = Integer.parseInt(arguments[0]);
				boolean isToggled = Boolean.parseBoolean(arguments[1]);
				hacks[i].key = key;
				if(isToggled){
					hacks[i].toggle();
				}
				++i;
			}
			br.close();
			loadSettings.close();
		}
		catch(Exception e){
			System.out.println("An error occured.");
		}
		File f = new File("settings.txt");
		try{
			if(f.exists() && !f.isDirectory()){
				SettingsSystem.loadSettings();
			}
			else{
				SettingsSystem.settingsList.add(new IntegerSetting(((KillAura)hacks[7]).mode));
				SettingsSystem.settingsList.add(new IntegerSetting(((KillAura)hacks[7]).radius));
				SettingsSystem.settingsList.add(new IntegerSetting((int)((FixedTime)hacks[16]).fixedWorldTime));
				SettingsSystem.settingsList.add(new DoubleSetting(((Timer)hacks[21]).speed));
				SettingsSystem.settingsList.add(new DoubleSetting(((AutoTunnel)hacks[23]).distance));
				SettingsSystem.settingsList.add(new DoubleSetting((double)((Yaw)hacks[24]).current_yaw));
				SettingsSystem.settingsList.add(new DoubleSetting(((FastFly)hacks[5]).speed));
				SettingsSystem.settingsList.add(new IntegerSetting(((Scaffold)hacks[20]).radius));
			}
		}
		catch(Exception e){
			SettingsSystem.settingsList.add(new IntegerSetting(((KillAura)hacks[7]).mode));
			SettingsSystem.settingsList.add(new IntegerSetting(((KillAura)hacks[7]).radius));
			SettingsSystem.settingsList.add(new IntegerSetting((int)((FixedTime)hacks[16]).fixedWorldTime));
			SettingsSystem.settingsList.add(new DoubleSetting(((Timer)hacks[21]).speed));
			SettingsSystem.settingsList.add(new DoubleSetting(((AutoTunnel)hacks[23]).distance));
			SettingsSystem.settingsList.add(new DoubleSetting((double)((Yaw)hacks[24]).current_yaw));
			SettingsSystem.settingsList.add(new DoubleSetting(((FastFly)hacks[5]).speed));
			SettingsSystem.settingsList.add(new IntegerSetting(((Scaffold)hacks[20]).radius));
		}
		FriendSystem.loadFriends();
		((KillAura)hacks[7]).mode = ((IntegerSetting)SettingsSystem.settingsList.get(0)).value;
		((KillAura)hacks[7]).radius = ((IntegerSetting)SettingsSystem.settingsList.get(1)).value;
		((FixedTime)hacks[16]).fixedWorldTime = ((IntegerSetting)SettingsSystem.settingsList.get(2)).value;
		((Timer)hacks[21]).speed = (float)((DoubleSetting)SettingsSystem.settingsList.get(3)).value;
		((AutoTunnel)hacks[23]).distance = ((DoubleSetting)SettingsSystem.settingsList.get(4)).value;
		((Yaw)hacks[24]).current_yaw = (float)((DoubleSetting)SettingsSystem.settingsList.get(5)).value;
		((FastFly)hacks[5]).speed = ((DoubleSetting)SettingsSystem.settingsList.get(6)).value;
		((Scaffold)hacks[20]).radius = ((IntegerSetting)SettingsSystem.settingsList.get(7)).value;
	}
	public static void saveHacks() {
		try{
			FileWriter saveHacks = new FileWriter("hacks.txt");
			for(int i = 0; i < hackList.size(); ++i){
				saveHacks.write(hacks[i].key + " " + hacks[i].isToggled + "\n");
			}
			saveHacks.close();
		}
		catch(Exception e){
			System.out.println("An error occured.");
		}
		((IntegerSetting)SettingsSystem.settingsList.get(0)).value = ((KillAura)hacks[7]).mode;
		((IntegerSetting)SettingsSystem.settingsList.get(1)).value = ((KillAura)hacks[7]).radius;
		((IntegerSetting)SettingsSystem.settingsList.get(2)).value = (int)((FixedTime)hacks[16]).fixedWorldTime;
		((DoubleSetting)SettingsSystem.settingsList.get(3)).value = (double)((Timer)hacks[21]).speed;
		((DoubleSetting)SettingsSystem.settingsList.get(4)).value = ((AutoTunnel)hacks[23]).distance;
		((DoubleSetting)SettingsSystem.settingsList.get(5)).value = (double)((Yaw)hacks[24]).current_yaw;
		((DoubleSetting)SettingsSystem.settingsList.get(6)).value = ((FastFly)hacks[5]).speed;
		((IntegerSetting)SettingsSystem.settingsList.get(7)).value = ((Scaffold)hacks[20]).radius;
		SettingsSystem.saveSettings();
		FriendSystem.saveFriends();
	}
}