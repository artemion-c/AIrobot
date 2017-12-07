package pack;

public class QTools {
	
	byte[][] state;
	byte[] action;
		
	
	/* Constructor for the Q values
	 * matrix.
	 */
	public QTools(byte actions, byte states){
		state = new byte[states][2];
		action = new byte[actions];
		initAction(action);
		initState(state);
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
	
	public float reward(int s, byte a){
		float r = 0;
		switch(s){
		case 0:  //No contacts and ON
			switch(a){
			case 5:  // Motors Moving forward
				r = 1;  // Large reward
				break;
				
			case 1:  // Robot turn to the left (soft)
				r = -0.3f;  // good reward
				break;
				
			case 2:  // Robot turn to the left (hard)
				r = -1f;  // I'll not complain
				break;
				
			case 3:  // Robot turn to the right	(soft)
				r = -0.3f;  // punishment
				break;
				
			case 4:  // Robot turn to the right	(hard)
				r = -1f;  // punishment
				break;
				
			case 0:  // Robot stops
				r = -0.2f;  // Small punishment
				break;
				
			default: break;
			}
			
			break;
		
		case 1:  // Right bumper is ON
			
			switch(a){
			case 5:  // Motors Moving forward
				r = 1;  // Large reward
				break;
				
			case 1:  // Robot turn to the left (soft)
				r = 0f;  // good reward
				break;
				
			case 2:  // Robot turn to the left (hard)
				r = -1;  // I'll not complain
				break;
				
			case 3:  // Robot turn to the right	(soft)
				r = 0.2f;  // punishment
				break;
				
			case 4:  // Robot turn to the right	(hard)
				r = -1f;  // punishment
				break;
				
			case 0:  // Robot stops
				r = -0.2f;  // Small punishment
				break;
				
			default: break;
			}
			
			break;
		
		case 2:  // Left bumper is ON
			
			switch(a){
			case 5:  // Motors Moving forward
				r = 1;  // Large reward
				break;
				
			case 1:  // Robot turn to the left (soft)
				r = 0.2f;  // Punishment
				break;
				
			case 2:  // Robot turn to the left (hard)
				r = -1f;  // Punishment
				break;
				
			case 3:  // Robot turn to the right	(soft)
				r = 0f;  // reward
				break;
				
			case 4:  // Robot turn to the right	(hard)
				r = -1f;  // I'll not complain
				break;
				
			case 0:  // Robot stops
				r = -0.2f;  // Small punishment
				break;
				
			default: break;
			}

			break;

			
		case 3:  // Both bumpers ON
			switch(a){
			case 5:  // Motors Moving forward
				r = -1f;  // Large reward
				break;
				
			case 1:  // Robot turn to the left (soft)
				r = -1f;  // Punishment
				break;
				
			case 2:  // Robot turn to the left (hard)
				r = 1f;  // Punishment
				break;
				
			case 3:  // Robot turn to the right	(soft)
				r = -1f;  // reward
				break;
				
			case 4:  // Robot turn to the right	(hard)
				r = 1f;  // I'll not complain
				break;
				
			case 0:  // Robot stops
				r = -0.2f;  // Small punishment
				break;
				
			default: break;
			}
			
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
