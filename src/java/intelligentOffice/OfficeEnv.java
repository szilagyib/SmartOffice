package intelligentOffice;

import jason.asSyntax.*;
import jason.environment.*;
import java.util.logging.*;

public class OfficeEnv extends Environment {
	
	private Logger logger = Logger.getLogger("intelligentOffice.mas2j."+OfficeEnv.class.getName());

	//actions
	public static final Literal ow = Literal.parseLiteral("open(window)");
	public static final Literal cw = Literal.parseLiteral("close(window)");
	public static final Literal onl = Literal.parseLiteral("turnon(light)");
	public static final Literal offl = Literal.parseLiteral("turnoff(light)");
	public static final Literal hac = Literal.parseLiteral("heaton(ac)");
	public static final Literal cac = Literal.parseLiteral("coolon(ac)");
	public static final Literal offac = Literal.parseLiteral("turnoff(ac)");
	public static final Literal mu = Literal.parseLiteral("move(user)");
	public static final Literal hu = Literal.parseLiteral("home(user)");
	
	//perceptions
	public static final Literal pu = Literal.parseLiteral("present(user)");
	public static final Literal au = Literal.parseLiteral("absent(user)");
	public static final Literal pco = Literal.parseLiteral("present(co)");
	public static final Literal aco = Literal.parseLiteral("absent(co)");
	public static final Literal now = Literal.parseLiteral("need(open)");
	public static final Literal ncw = Literal.parseLiteral("need(close)");
	public static final Literal nh = Literal.parseLiteral("need(heating)");
	public static final Literal nc = Literal.parseLiteral("need(cooling)");
	public static final Literal ns = Literal.parseLiteral("need(stop)");
	
	OfficeModel model;
	OfficeView view;
	
	@Override
	public void init(String[] args) {
		model = new OfficeModel();
		view = new OfficeView(model);
		model.setView(view);
		updatePercepts();
	}

	@Override
	public boolean executeAction(String agName, Structure action) {
		
		boolean result = false;
		boolean shouldPrint = true;
		
		if (action.equals(ow))
			result = model.openWindow();
		else if (action.equals(cw))
			result = model.closeWindow();
		else if (action.equals(onl))
			result = model.turnOnLight();
		else if (action.equals(offl))
			result = model.turnOffLight();
		else if (action.equals(hac))
			result = model.heat();
		else if (action.equals(cac))
			result = model.cool();
		else if (action.equals(offac))
			result = model.offAC();
		else if (action.equals(mu)) {
			result = model.moveUser();
			if (!result) {
				shouldPrint = false;
				result = true;
			}
		}
		else if (action.equals(hu)) {
			result = model.sendUserHome();
			if (!result) {
				shouldPrint = false;
				result = true;
			}
		}
		
		if(shouldPrint)
			System.out.println("[" + agName + "] doing: " + action);
		
		if (result)
			updatePercepts();
		return result;
	}

	@Override
	public void stop() {
		super.stop();
	}
	
	public void updatePercepts() {
		
		clearAllPercepts();
		
		if(model.presentCO)
			addPercept("agentSensor", pco);
		else
			addPercept("agentSensor", aco);
		if(model.isUserPresent())
			addPercept("agentLighting", pu);
		else
			addPercept("agentLighting", au);
		if(model.neededStateOfAC()==0)
			addPercept("agentTemperature", ns);
		else if(model.neededStateOfAC()==1)
			addPercept("agentTemperature", nh);
		else if(model.neededStateOfAC()==2)
			addPercept("agentTemperature", nc);
		if(model.neededStateOfWindow()==1)
			addPercept("agentWindow", now);
		else if(model.neededStateOfWindow()==2)
			addPercept("agentWindow", ncw);
	}
	
}