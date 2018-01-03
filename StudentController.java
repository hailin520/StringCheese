

package ufl.cs1.controllers;

import game.controllers.DefenderController;
import game.models.Defender;
import game.models.Game;
import game.models.Node;

import java.util.List;

public final class StudentController implements DefenderController{

	public void init(Game game) { }

	public void shutdown(Game game) { }

	public int[] update(Game game,long timeDue)
	{   int[] actions = new int[Game.NUM_DEFENDER];
		List<Defender> enemies = game.getDefenders();


		actions [0] = getGhost0ToMove(game, enemies.get(0));
		actions [1] = getGhost1ToMove(game, enemies.get(1));
		actions [2] = getGhost2ToMove(game, enemies.get(2));
		actions [3] = getGhost3ToMove(game, enemies.get(3));

		return actions;

	}



	public int getGhost0ToMove(Game game, Defender enem) {

		//Code for Defender created by Alex. Enemy 3
		//Goal: Defender will change behavior depending on number of pellets consumed, going from somewhat aggressive to extremely aggressive.
		//During vulnerability mode, Defender move away from attacker.
		//Method: Use of the getNextDir mainly.

		//Checks if Defender is vulnerable or not.

		int d;

		Defender def = enem;

		if (def.isVulnerable()) {
			//Vulnerable Mode.
			//Goal: At start, defender will move in direct direction away from Attacker based on options. Higher priority will be given to
			//the lower number between abs(atk.X-def.X) and abs(atk.Y-def.Y). From there on, Defender will move in direction.
			//Let's just use getNextDir.


			game.models.Actor atk = game.getAttacker();
			Node AtkNode = atk.getLocation();

			d = def.getNextDir(AtkNode, false);

		} else {
			//Need to test if possible to get number of available pellets in order to change behavior.

			int numPellets = game.getPillList().size();

			//Aggressive Behavior
			//Always chases after Attacker Directly
			if (numPellets < 180) {
				game.models.Actor atk = game.getAttacker();

				Node AtkNode = atk.getLocation();

				int DirAlex = def.getNextDir(AtkNode, true);
				d = DirAlex;

				//Ambushing Behavior
				//Always tries to go where the Attacker will be, 5 nodes in the direction of the Attacker.
			} else {

				game.models.Actor atk = game.getAttacker();
				Node AtkNode = atk.getLocation();
				int AtkDir = atk.getDirection();

				List path = def.getPathTo(AtkNode);
				if (path.size() < 5) {

					d = def.getNextDir(AtkNode, true);

				} else {

					for (int i = 0; i < 5; i++) {

						try {

							AtkNode = AtkNode.getNeighbor(AtkDir);

						} catch (NullPointerException NPE){

							AtkNode = AtkNode;

						}

					}

					try {
						d = def.getNextDir(AtkNode, true);
					} catch (NullPointerException NPE){
						d = atk.getDirection();
					}

				}
			}
		}
		return d;
	}






	public int getGhost1ToMove(Game game, Defender enem) {
		List<Node> pillList = game.getPowerPillList();
		Node nearestNode = null;
		int returnVal = 0;
		Node pacmanNode = game.getAttacker().getLocation();


		if (!enem.isVulnerable()) {

			if (pacmanNode.getPathDistance(enem.getLocation()) < 10){returnVal = enem.getNextDir(pacmanNode, true);}

			else{

				if (pillList.size() > 1) {
					nearestNode = pillList.get(0);
					for (int i = 0; i < pillList.size(); i++) {

						if (pacmanNode.getPathDistance(nearestNode) > pacmanNode.getPathDistance(pillList.get(i))) {
							nearestNode = pillList.get(i);

						}
					}

					if (pacmanNode == nearestNode){

						returnVal = enem.getNextDir(nearestNode, false);}


					else{
						returnVal = enem.getNextDir(nearestNode, true);
					}

				}

				if (pillList.size() == 1) {
					if (pacmanNode == pillList.get(0)) {
						returnVal = enem.getNextDir(pillList.get(0), false);
					} else {
						returnVal = enem.getNextDir(pillList.get(0), true);
					}
				}

				if (pillList.size() == 0) {
					returnVal = enem.getNextDir(pacmanNode, true);
				}






				if (enem.isVulnerable()){
					returnVal = enem.getNextDir(pacmanNode, false);
				}


			}} return returnVal;}









	public int getGhost2ToMove(Game game, Defender enem) {

		int d = 0;


		Defender defender = enem;
		if (defender.isVulnerable()) {
			d = defender.getNextDir(game.getAttacker().getLocation(), false);
		}
		else {

			Node closestpt = null;
			for (int i = 0; i < game.getCurMaze().getPowerPillNodes().size(); i++) {

				Node powerPill = game.getCurMaze().getPowerPillNodes().get(i);
				if (!game.checkPowerPill(powerPill)) {
					continue;
				}

				if (closestpt == null) {
					closestpt = powerPill;
					continue;
				}
				int dist = defender.getLocation().getPathDistance(game.getCurMaze().getPowerPillNodes().get(i));
				int smallDist = defender.getLocation().getPathDistance(closestpt);
				if (smallDist > dist) {
					closestpt = powerPill;
				}
				//designed to guard the powerpill paths closest to it.  The ghost can detect the locations of
				// powerpills and measure the shortest distance to protect one of the powerpills.





			}
			if (game.checkPowerPill(closestpt)) {
				d = defender.getNextDir(closestpt, true);
			} else {
				d = defender.getNextDir(game.getAttacker().getLocation(), true);
			}




		}
		return d;
	}



	public int getGhost3ToMove(Game game, Defender enemies) {
		int lol;
		Defender defender = enemies;
		if (defender.isVulnerable()) {
			//chasing Pacman in different directions in order to interrupt Pacman to get pills easily.
			lol= defender.getNextDir(game.getAttacker().getLocation(), false);
		} else {
			lol = defender.getNextDir(game.getAttacker().getLocation(), true);
		}
		return lol;
	}}