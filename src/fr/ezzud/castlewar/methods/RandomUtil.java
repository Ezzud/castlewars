package fr.ezzud.castlewar.methods;

import java.util.List;
import java.util.Random;

public class RandomUtil {
	public static String RandomPlayer(List<String> teamMembers) {
		List<String> lst = teamMembers;
		lst.remove(0);
		Random generator = new Random();
		int randomIndex = 0;
		if(teamMembers.size() > 1) randomIndex = generator.nextInt(lst.size());
		
		return teamMembers.get(randomIndex);
	}
}
