package net.minecraft.src.Space;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

public class FriendSystem{
	public static ArrayList<String> friendList = new ArrayList<String>();
	public static void loadFriends(){
		try{
			FileReader friendLoader = new FileReader("friends.txt");
			BufferedReader br = new BufferedReader(friendLoader);
			String line;
			while((line = br.readLine()) != null){
				friendList.add(line);
			}
			br.close();
			friendLoader.close();
		}
		catch(Exception e){
			System.out.println("An error occured.");
		}
	}
	public static void saveFriends(){
		try{
			FileWriter friendsSaver = new FileWriter("friends.txt");	
			for(int i = 0; i < friendList.size(); ++i){
				friendsSaver.write(friendList.get(i));
			}
			friendsSaver.close();
		}
		catch(Exception e){
			System.out.println("An error occured.");
		}
	}
};