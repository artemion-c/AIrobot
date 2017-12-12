package pack;

public class QTools {

	byte[][] state;
	byte[] action;


	/* Constructor for the Q values
	 * matrix.
	 */
	public QTools(byte actions, byte states){//[3, 3]
		state = new byte[states][2];//[3, 2]
		action = new byte[actions];//[3]
		initAction(action);
		//initState(state);
	}

	/* Initialize the Q-values matrix with random values.
	 * The size of this matrix depends on the amount of states and actions.
	 */
	public void setRandomVal(float[][] Q){
		for(byte i=0; i<Q.length; ++i){
			for(byte j=0; j<Q[0].length; ++j){
				Q[i][j] = (float)Math.random();
				//                                        System.out.println("random = " + Q[i][j]);
			}
		}
	}

	/* Initialize the matrix of states given the amount of
	 * combinations of the sensors (That establish the environment)
	 * In this robots exists 2 contact sensors with ON/OFF states
	 * So, it will give 2x2 combinations.
	 */
	public static void initState(byte[][] state){
		byte count = 0;
		for(byte i=0; i<2; ++i){
			for(byte j=0; j<2; ++j){
				state[count][0] = i;
				state[count][1] = j;
				++count;
			}
		}
	}

	/*Initialize the matrix of actions the robot can make
	 * regarding both motors as one entity.
	 */
	public static void initAction(byte[] action){
		// up:0 down:1 stay:2
		for(byte i=0; i<action.length; ++i){
			action[i] = i;
		}
	}

	/*Giving an action ID (a) from 1~theMaxNumberOfActions, give back
	 * the action related with the ID.
	 */
	public byte getCommand(byte a){
		return action[a];
	}

	// s:state(good, cool, bad) = (0, 1, 2)
	// a:action(up, down, stay) = (0, 1, 2)
	public float reward(int s, byte a){
		float r = 0;
		switch(s){

			case 0:  //good
				switch(a){
				case 0: //up
					r = -1f;
					break;
				case 1: //down
					r = -1f;
					break;
				case 2: //stay
					r = 1;
					break;
				default: break;
				}

			break;

			case 1:  //cool
				switch(a){
				case 0: //up
					r = 0.5f;
					break;
				case 1: //down
					r = 0.5f;
					break;
				case 2: //stay
					r = -0.5f;
					break;
				default: break;
				}

			break;

			case 2:  //bad
				switch(a){
				case 0: //up
					r = 1;
					break;
				case 1: //down
					r = 1;
					break;
				case 2: //stay
					r = -1f;
					break;
				default: break;
				}

			break;

			default: break;
			}

		return r;

	}

	/* Given an array of environments or actual states of the sensors
	 * Compare with the states matrix and get the ID of the matching one.
	 */
	public byte getState(byte[] states){
		byte count = 0;
		for(byte i=0; i<2; ++i){
			for(byte j=0; j<2; ++j){
				if(states[0] == state[count][0]){
					if(states[1] == state[count][1]){
						return count;
					}
				} ++count;
			}
		}
		return -1;
	}

}
