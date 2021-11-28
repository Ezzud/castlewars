package fr.ezzud.castlewar.methods;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomUtil {
	public static String RandomPlayer(Set<String> set) {
		List<String> lst = new ArrayList<>();
		for(String pl : set) {
			if(pl.equalsIgnoreCase("team1")) continue;
			if(pl.equalsIgnoreCase("team2")) continue;
			lst.add(pl);
		}
		Random generator = new Random();
		int randomIndex = 0;
		if(set.size() > 1) randomIndex = generator.nextInt(lst.size());
		
		return lst.get(randomIndex);
	}
}
