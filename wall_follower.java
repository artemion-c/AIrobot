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

	public static byte NUMBER_ACTIONS = 2;//???
	public static byte NUMBER_STATES = 4;//???

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

		Port s1 = (Port)brick1.getPort("S1"); // set ColorSensor s1
		EV3ColorSensor sensor1 = new EV3ColorSensor(s1);
		color1 = new ColorSensor(sensor1);

		pilot = new MovePilot(6, 13, Motor.B, Motor.C); // 1:wheelDiameter, 2:trackWidth
		states = new byte[2]; //???
		String testString;
		while(Button.ESCAPE.isUp()){  // button left above?
			//states = getEnvironment(states, color1);

			//byte a = Q.getAction(states);

			byte command;// = q.getCommand(a);

			// {{{ test part
			command = 0;
			motorCommand(Motor.B, command, 50);
			motorCommand(Motor.C, command, 50);
			Delay.msDelay(500);
			command = 1;
			motorCommand(Motor.B, command, 50);
			motorCommand(Motor.C, command, 50);
			testString = sensor1.getColorIDMode().getName();
			LCD.clear();
			LCD.drawString(testString, 2, 2);
			
			
			// test part}}}
			Delay.msDelay(500);

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
	public static void motorCommand(RegulatedMotor motor, byte command, int angle){
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

	public static byte[] getEnvironment(byte[] states2, ColorSensor color2){
		states2 = new byte[2];
		states2[0] = 0;
		states2[0] = 1;

		return states2;
	}
}