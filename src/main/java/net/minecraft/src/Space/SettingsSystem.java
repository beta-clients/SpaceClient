package net.minecraft.src.Space;
import net.minecraft.src.Space.settings.*;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

public class SettingsSystem{
	public static ArrayList<Setting> settingsList = new ArrayList<Setting>();
	public static void loadSettings(){
		try{
			FileReader settingsLoader = new FileReader("settings.txt");
			BufferedReader br = new BufferedReader(settingsLoader);
			String line;
			while((line = br.readLine()) != null){
				String[] arguments = line.split(" ");
				String type = arguments[0];
				if(type.equals("double")){
					settingsList.add(new DoubleSetting(Double.parseDouble(arguments[1])));
				}
				else if(type.equals("int")){
					settingsList.add(new IntegerSetting(Integer.parseInt(arguments[1])));
				}
			}
			br.close();
			settingsLoader.close();
		}
		catch(Exception e){
			System.out.println("An error occured.");
		}
	}
	public static void saveSettings(){
		try{
			FileWriter settingsSaver = new FileWriter("settings.txt");	
			for(int i = 0; i < settingsList.size(); ++i){
				if(settingsList.get(i) instanceof IntegerSetting){
					IntegerSetting setting = ((IntegerSetting)settingsList.get(i));
					settingsSaver.write("int " + setting.value + "\n");
				}
				else if(settingsList.get(i) instanceof DoubleSetting){
					DoubleSetting setting = ((DoubleSetting)settingsList.get(i));
					settingsSaver.write("double " + setting.value + "\n");
				}
			}
			settingsSaver.close();
		}
		catch(Exception e){
			System.out.println("An error occured.");
		}
	}
};