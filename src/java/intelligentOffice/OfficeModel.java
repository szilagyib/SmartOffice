package intelligentOffice;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

import jason.environment.grid.*;

public class OfficeModel extends GridWorldModel {
	
	OfficeView view;
	Timer timer;
	
	public static final int gridSize = 8;
	
	//IDs
	public final int agentLightingID = 0;
	public final int agentSensorID = 1;
	public final int agentTemperatureID = 2;
	public final int agentWindowID = 3;
	public final int agentUserID = 4;
	public final int wallID = 5;
	
	//locations
	final Location lLighting = new Location(5, 2);
	final Location lSensor = new Location(1, 3);
	final Location lTemperature = new Location(1, 4);
	final Location lWindow = new Location(2, 0);
	final Location lHome = new Location(gridSize-1, 5);
	
	//initial values
	boolean windowOpen = false;
	boolean lightOn = false;
	double optTemp = 23;
	double thresTemp = 1;
	double actualTemp = 23;
	double optHumid = 50;
	double thresHumid = 3;
	double actualHumid = 50;
	boolean presentCO = false;
	int stateOfAC = 0; //air conditioner present state: 0-off, 1-heat, 2-cool
	boolean isHotOutside = true;
	int timeLeftUntilSafe = 10;
	
	public OfficeModel() {
		
		super(gridSize, gridSize, 6);
		
		//set wall positions
		for(int i = 0; i < gridSize; i++) {
			add(wallID, 0, i);
			add(wallID, i, 0);
			add(wallID, gridSize-1, i);
			add(wallID, i, gridSize-1);
		}
		
		remove(wallID, gridSize-1, 5);
		
		//set agents position
		setAgPos(agentLightingID, lLighting);
		setAgPos(agentSensorID, lSensor);
		setAgPos(agentTemperatureID, lTemperature);
		setAgPos(agentWindowID, lWindow);
		setAgPos(agentUserID, lHome);
		
		//periodic temperature and humidity change
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new autoChangeValues(), 0, 1000);
		
	}
	
	@Override
	public void setView(GridWorldView v) {
		super.setView(v);
		view = (OfficeView)(v);
	}
	
	public boolean openWindow() {
		if(!windowOpen) {
			windowOpen = true;
			view.update(lWindow.x, lWindow.y);
			return true;
		}
		return false;
	}
	
	public boolean closeWindow() {
		if(windowOpen) {
			windowOpen = false;
			view.update(lWindow.x, lWindow.y);
			return true;
		}
		return false;
	}
	
	public boolean turnOnLight() {
		if(!lightOn) {
			lightOn = true;
			view.update(lLighting.x, lLighting.y);
			return true;
		}
		return false;
	}
	
	public boolean turnOffLight() {
		if(lightOn) {
			lightOn = false;
			view.update(lLighting.x, lLighting.y);
			return true;
		}
		return false;
	}
	
	public int neededStateOfAC() {
		if (actualTemp <= (optTemp-thresTemp))
			return 1;
		else if ((stateOfAC == 1) && (actualTemp<optTemp))
			return 1;
		else if (actualTemp >= (optTemp+thresTemp) )
			return 2;
		else if ((stateOfAC == 2) && (actualTemp>optTemp))
			return 2;
		return 0;
	}
	
	public boolean offAC() {
		if (!(stateOfAC == 0)) {
			stateOfAC = 0;
			view.update(lTemperature.x, lTemperature.y);
			return true;
		}
		return false;
	}
	
	public boolean heat() {
		if (!(stateOfAC == 1)) {
			stateOfAC = 1;
			view.update(lTemperature.x, lTemperature.y);
			return true;
		}
		return false;
	}
	
	public boolean cool() {
		if (!(stateOfAC == 2)) {
			stateOfAC = 2;
			view.update(lTemperature.x, lTemperature.y);
			return true;
		}
		return false;
	}

	public int neededStateOfWindow() {
		if (actualHumid >= optHumid+thresHumid)
			return 1; //should open
		if (actualHumid <= optHumid-thresHumid)
			return 2; //should close
		return 0; //nothing to do
	}
	
	public boolean isUserPresent() {
		Location lUser = getAgPos(agentUserID);
		if (lUser.x >= 1 && lUser.x <= gridSize-2 && lUser.y >= 1 && lUser.y <= gridSize-2)
			return true;
		return false;
	}
	
	public boolean hasObject(Location l) {
		for (int i = 1; i <= 5; i++) {
			if(hasObject(i, l))
				return true;
		}
		return false;
	}
    
	public boolean moveUser() {
		Location lUserOriginal = getAgPos(agentUserID);
        Location lUser = getAgPos(agentUserID);
        switch (new Random().nextInt(4)) {
        	case 0:
        		lUser.x--;
        		if(hasObject(lUser))
        			lUser.x++;
        		break;
        	case 1:
                if (lUser.x == gridSize-1 && lUser.y == 5)
                	break;
        		lUser.x++;
        		if(hasObject(lUser))
        			lUser.x--;
        		break;
        	case 2:
        		lUser.y++;
        		if(hasObject(lUser))
        			lUser.y--;
        		break;
        	case 3:
        		lUser.y--;
        		if(hasObject(lUser))
        			lUser.y++;
        		break;
        }

        if (lUser.x == lUserOriginal.x && lUser.y == lUserOriginal.y)
        	return false;
        setAgPos(agentUserID, lUser);
        return true;
	}
		
	public boolean sendUserHome() {
		Location lUser = getAgPos(agentUserID);
		
		if (lUser.x == lHome.x && lUser.y == lHome.y)
			return false;
		
		if (lUser.x > lHome.x) {
			lUser.x--;
    		if(!hasObject(lUser)) {
    	        setAgPos(agentUserID, lUser);
    			return true;
    		}
    		lUser.x++;
		}
		else if (lUser.x < lHome.x) {
			lUser.x++;
    		if(!hasObject(lUser)) {
    	        setAgPos(agentUserID, lUser);
    			return true;
    		}
    		lUser.x--;
		}
		if (lUser.y < lHome.y) {
			lUser.y++;
    		if(!hasObject(lUser)) {
    	        setAgPos(agentUserID, lUser);
    			return true;
    		}
    		lUser.y--;
		}
		else if (lUser.y > lHome.y) {
			lUser.y--;
    		if(!hasObject(lUser)) {
    	        setAgPos(agentUserID, lUser);
    			return true;
    		}
    		lUser.y++;
		}
		return true;
	}
	
	public boolean setCO(boolean value) {
		if (presentCO != value) {
			presentCO = value;
			view.update(lSensor.x, lSensor.y);
			return true;
		}
		return false;
	}
	
	private class autoChangeValues extends TimerTask {
		@Override
		public void run() {
			if(windowOpen)
				actualHumid-=0.5;
			else
				actualHumid+=0.1;
			if(stateOfAC == 1)
				actualTemp += 0.15;
			else if(stateOfAC == 2)
				actualTemp -= 0.15;
			if(isHotOutside)
				actualTemp += 0.05;
			else
				actualTemp -= 0.05;
			if(presentCO && timeLeftUntilSafe > 0)
				timeLeftUntilSafe--;
			else if(presentCO && timeLeftUntilSafe <= 0) {
				timeLeftUntilSafe = 10;
				presentCO = false;
				view.update(lSensor.x, lSensor.y);
			}
			if (view != null) 
				view.drawActualValues();
		}
	}
	
}
