/*
 * Modified Version 9/27 1:48am
 */


package pack;

//import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
//import lejos.utility.Delay;

public class Learning {
	public static byte NUMBER_OF_ACTIONS = 2;

	public static float LEARN_RATE = 0.35f;
	public static float EXPLORE_RATE = 0.15f;

	int ls = -1; // last state
	byte la = -1; // last action
	byte MaxA = 0; // action with highest q=value

	float[][] Q; // table of Q values

	float r = 0; // last reward

	QTools qT;

	// Creates the Q-values table
	public Learning(byte actions, byte states, QTools qT){
		Q = new float[actions][states];  //Table of Q-values for action-states
		this.qT = qT;
		qT.setRandomVal(Q);
	}

	public byte getAction(byte[] states){

		byte st = qT.getState(states);  //Environment -> ID state (New state)
		if(ls >= 0){

			r = qT.reward(ls, la);

			if(r == 1){  //In case that the reward is greather that zero

				if(LEARN_RATE > 0){
					LEARN_RATE = LEARN_RATE - 0.001f;
				}
				if(EXPLORE_RATE > 0){
					EXPLORE_RATE = EXPLORE_RATE -0.001f;
				}

			}

			MaxA = getMaxAction(st);
			Q[la][ls] = Q[la][ls] + LEARN_RATE*(r + Q[MaxA][st] - Q[la][ls]);

		}

		ls = st;

		float rand = (float) Math.random();
		if(rand > EXPLORE_RATE){
			la = MaxA;
		}else{
			la = (byte)(Math.random()*NUMBER_OF_ACTIONS);
		}

		writeMess("State & Action", "Reward = ", ls, la, r);

		return la;

	}

	// Find the action with the largest Q-Value
	public byte getMaxAction(int st){
		float max = -1000;
		byte action = 0;

		for(byte i=0; i<Q.length; ++i){

			if(Q[i][st] > max){
				max = Q[i][st];
				action = i;
			}
		}
		return action;
	}

	public void writeMess(String mess1, String mess2, int val1, int val2, float val3){

		LCD.clear();
		LCD.drawString(mess1, 2, 1); LCD.drawInt(val1, 4, 2); LCD.drawInt(val2, 12, 2);

		LCD.drawString(mess2, 2, 4); LCD.drawInt((int)(val3*100), 11, 4);

		LCD.drawString("L.R = ", 1, 6); LCD.drawInt((int)(LEARN_RATE*100), 6, 6);
		LCD.drawString("E.R = ", 10, 6); LCD.drawInt((int)(EXPLORE_RATE*100), 15, 6);

	}

	/*
	 * Consider it to a next opportunity
	 *
	public float BoltzmannDist(){  // PLEASE modify.

		return Q[1][1]/maxQ(Q);

	}

	public float maxQ(float[][] q){  // SEARCH if is the max overall or just maxAction.

		float mQ = -1f;

		for(byte i = 0; i < q.length; ++i){

			for(byte j = 0; i < q[i].length; ++j){

				if(q[i][j] > mQ){

					mQ = q[i][j];

				}

			}

		}

		return mQ;

	}*/

}
