package pack;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
//import lejos.hardware.Sound;
//import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.RegulatedMotor;
//import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;

public class wall_follower {

	public static byte NUMBER_ACTIONS = 3;//???
	public static byte NUMBER_STATES = 3;//???

	static MovePilot pilot;//???
	//static TouchSensor touch1, touch2;
	static ColorSensor color1;
	static byte[] states;//???

	byte[] env = new byte[2];//???
	byte motorCommand = 0;// motorCommand -> initialization

	static QTools q = new QTools(NUMBER_ACTIONS, NUMBER_STATES); //???
	static Learning Q = new Learning(NUMBER_ACTIONS, NUMBER_STATES, q); //???

	@SuppressWarnings("deprecation") //remove warnings

	public static void main(String[] args) { // <---start--->
		Brick brick1 = BrickFinder.getDefault(); //device <- initialization

		//{{{ touch sensor
		//Port s1 = (Port) brick1.getPort("S1"); // set Port S1
		//Port s2 = (Port) brick1.getPort("S2"); // set Port S2
		//EV3TouchSensor sensor1 = new EV3TouchSensor(s1);
		//EV3TouchSensor sensor2 = new EV3TouchSensor(s2);
		//touch1 = new TouchSensor(sensor1);
		//touch2 = new TouchSensor(sensor2);
		// }}}

		Port s1 = (Port)brick1.getPort("S4"); // set ColorSensor s1
		EV3ColorSensor sensor1 = new EV3ColorSensor(s1);
		color1 = new ColorSensor(sensor1);

		pilot = new MovePilot(6, 13, Motor.B, Motor.C); // 1:wheelDiameter, 2:trackWidth
		states = new byte[3]; //???
		for(int i = 0; i < states.length; i++) {
			states[i] = (byte)i;
		}
		byte state = -1;
		String testString;
		int testInt;

		//{{{ core
		int initial_height = 220;
		int height = initial_height;
		int height_delta = 60;
		int color = -1;

		int prev_color = -1;
		int now_color = -2;
		int buf_color = -3;
		int[] state_history = new int[1000];
		int history_count = 0;



		//}}}


		while(Button.ESCAPE.isUp()){  // button left above?
			/*
			 <-
			 tukamu
			 colorsensor >
			 	rewards

			 down
			 drop
			 up
			 ->
			 <
			 tukamu
			 collorsensor
			 */

			motorCommand(Motor.B, 0, 55); //tukamu

			now_color = sensor1.getColorID();
			// state judge 0:good 1:cool 2:bad
			if(now_color == prev_color) { //sonomama
				state = 2;
				state_history[history_count] = 1;
			}else if( (now_color == 0 && prev_color == 6) ||
					   (now_color == 1 && prev_color == 3) ||
					   (now_color == 2 && prev_color == 7) ||
					   (now_color == 3 && prev_color == 1) ||
					   (now_color == 6 && prev_color == 0) ||
					   (now_color == 7 && prev_color == 2)) { //hanntai
				state = 0;
			}
			else {//sokumen
				state = 1;
				state_history[history_count] = 0;
			}
			prev_color = now_color;
			history_count++;
			int probability = 0;
			if(history_count > 30) {
				for(int i = 0; i < 30; i++) {
					probability += state_history[history_count - i];
				}
			//	probability = probability / 20.0;
			}
			byte a = Q.getAction(state);

			if(a == 0) {
				if(height > initial_height - height_delta) {
					height = initial_height;
				}
			}else if(a == 1) {
				if(height < initial_height + height_delta) {
					height = initial_height;
				}
			}


			motorCommand(Motor.C, 1, height);//sageru
			motorCommand(Motor.B, 1, 55);//hiraku drop
			motorCommand(Motor.C, 0, height);//ageru

			// {{{ test part
			// command 0: up tukamu command 1: down hiraku
			//int command = 1;//up hiraku

			//Random rand = new Random();
			//motor.B : arm  motor.C : height
			// tukamu > sageru > hiraku > ageru
			//command = 0;
			//motorCommand(Motor.B, command, 55);//tukamu
			//command = 1;
			//motorCommand(Motor.C, command, 200);//sageru
			//command = 1;//down
			//motorCommand(Motor.B, command, 55);//hiraku
			//command = 0;
			//motorCommand(Motor.C, command,200);//ageru

			//testInt = sensor1.getColorID();


			/*LCD.drawString("blue:", 0, 0);
			LCD.drawInt(Color.BLUE, 7, 0);//2
			LCD.drawString("red:", 9, 0);
			LCD.drawInt(Color.RED, 16, 0);
			LCD.drawString("black:", 0, 1);
			LCD.drawInt(Color.BLACK, 7, 1);
			LCD.drawString("green:", 9, 1);
			LCD.drawInt(Color.GREEN, 16, 1);
			LCD.drawString("yellow:", 0, 2);
			LCD.drawInt(Color.YELLOW, 7, 2);
			LCD.drawString("white:", 9, 2);
			LCD.drawInt(Color.WHITE, 16, 2);//7
			*/
			LCD.clear();

			LCD.drawString("height:", 1, 0);
			LCD.drawInt(height, 10, 0);

			LCD.drawString("probability:", 1, 1);
			LCD.drawInt(probability, 1, 2);
			LCD.drawString("/ 30", 3, 2);


			LCD.drawString("prev_color:", 0, 3);
			LCD.drawInt(buf_color, 12, 3);
			buf_color = now_color;

			LCD.drawString("now_color:", 0, 4);
			LCD.drawInt(now_color, 12, 4);
			LCD.drawString("count:", 0, 5);
			LCD.drawInt(history_count, 12, 5);

			// test part}}}
			Delay.msDelay(300);

		}
		//exit(sensor1);

	}

	/* Void instruction for closing the program
	 * Safely with the sensor specifically used.
	 */
	public static void exit(EV3ColorSensor s1){
		pilot.stop();
		s1.close();
		System.exit(0);
	}

	/* The commands are:
	 * r = going to the right 45~
	 * l = going to the left -45~
	 * f = turn 180~
	 * otherwise do nothing
	 */
	public static void motorCommand(RegulatedMotor motor, int command, int angle){
		switch(command){

		case 0:  // + rotation
			motor.rotate(angle);
			break;
		case 1:  // - rotation
			motor.rotate(-angle);
			break;

		default:
			break;
		}
	}

	/* This function helps to write the messages
	 * within the size of the EV3's display and
	 * centered.
	 */


}