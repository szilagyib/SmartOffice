package intelligentOffice;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import jason.environment.grid.*;

public class OfficeView extends GridWorldView {

	private static final long serialVersionUID = 1L;
	
	OfficeModel model;
	
	JTextField jtTempOpt;
	JTextField jtTempThres;
	JTextField jtHumidOpt;
	JTextField jtHumidThres;
	JComboBox<String> jcbOutTemp;
	JButton jbOK;
	JButton jbCO;
	JLabel jlTemp;
	JLabel jlHumid;
	JLabel jlCO;
	JLabel jlTime;
	
	public OfficeView(OfficeModel m)
	{
		super(m, "Intelligent Office", 600);
		model = m;
        setResizable(false);
        setVisible(true);
        repaint();
        initTextFields();
	}
	
	@Override
	public void initComponents(int width) {
		super.initComponents(width);
		JPanel panel = new JPanel(new GridLayout(0, 2));
		
		JPanel p1 = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		TitledBorder tbChange = new TitledBorder("Set boundaries");
		tbChange.setTitleJustification(TitledBorder.CENTER);
		p1.setBorder(tbChange);
		gbc.gridx = 0;
		gbc.gridy = 0;
        p1.add(new JLabel("Temperature (\u2103): "), gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
        p1.add(new JLabel("opt "), gbc);
        jtTempOpt = new JTextField(5);
		gbc.gridx = 2;
		gbc.gridy = 0;
        p1.add(jtTempOpt, gbc);
		gbc.gridx = 3;
		gbc.gridy = 0;
        p1.add(new JLabel("+/- "), gbc);
        jtTempThres = new JTextField(5);
		gbc.gridx = 4;
		gbc.gridy = 0;
        p1.add(jtTempThres, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
        p1.add(new JLabel("Humidity (%): "), gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
        p1.add(new JLabel("opt "), gbc);
        jtHumidOpt = new JTextField(5);
		gbc.gridx = 2;
		gbc.gridy = 1;
        p1.add(jtHumidOpt, gbc);
		gbc.gridx = 3;
		gbc.gridy = 1;
        p1.add(new JLabel("+/- "), gbc);
        jtHumidThres = new JTextField(5);
		gbc.gridx = 4;
		gbc.gridy = 1;
        p1.add(jtHumidThres, gbc);
		gbc.gridx = 0;
		gbc.gridy = 2;
        p1.add(new JLabel("Outside: "), gbc);
		gbc.gridx = 1;
		gbc.gridy = 2;
        String[] outTempStrings = {"hot", "cold"};
        jcbOutTemp = new JComboBox<String>(outTempStrings);
        p1.add(jcbOutTemp, gbc);
        jcbOutTemp.setSelectedIndex(0);
		gbc.gridx = 0;
		gbc.gridy = 3;
        jbCO = new JButton("Inject CO");
        jbCO.setBackground(Color.red);
        p1.add(jbCO, gbc);
		gbc.gridx = 4;
		gbc.gridy = 3;
        jbOK = new JButton("OK");
        p1.add(jbOK, gbc);
        
        jbOK.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
        	  model.optTemp = Double.parseDouble(jtTempOpt.getText());
        	  model.thresTemp = Double.parseDouble(jtTempThres.getText());
        	  model.optHumid = Double.parseDouble(jtHumidOpt.getText());
        	  model.thresHumid = Double.parseDouble(jtHumidThres.getText());
        	  model.isHotOutside = jcbOutTemp.getSelectedIndex() == 0 ? true : false;
          }
        });
        
        jbCO.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
        	  model.setCO(true);
          }
        });
        
        JPanel p2 = new JPanel(new GridLayout(0, 1));
		TitledBorder tbActual = new TitledBorder("Actual values");
		tbActual.setTitleJustification(TitledBorder.CENTER);
		p2.setBorder(tbActual);
		jlTemp = new JLabel("Temperature (\u2103):");
		jlHumid = new JLabel("Humidity (%):");
		jlCO = new JLabel("CO concentration is OK");
		jlCO.setForeground(Color.green);
		jlTime = new JLabel(" ");
		p2.add(jlTemp);
		p2.add(jlHumid);
		p2.add(jlCO);
		p2.add(jlTime);
        
        panel.add(p1);
        panel.add(p2);
        getContentPane().add(BorderLayout.SOUTH, panel);
        getContentPane().setBackground(Color.white);
	}
	
	@Override
	public void drawAgent(Graphics g, int x, int y, Color c, int id) {
		
		String agentName = "";

		if (id == model.agentLightingID) {
			agentName = "Lighting";
			if (model.lightOn)
				c = Color.yellow;
			else
				c = Color.lightGray;
		}
		else if (id == model.agentWindowID) {
			agentName = "Window";
			if (model.windowOpen)
				c = Color.blue;
			else
				c = Color.lightGray;
		}
		else if (id == model.agentSensorID) {
			agentName = "CO-Sensor";
			if(model.presentCO)
				c = Color.red;
			else
				c = Color.lightGray;
		}
		else if (id == model.agentTemperatureID) {
			
			if (model.stateOfAC == 0) {
				agentName = "AC-Off";
				c = Color.lightGray;
			}
			else if (model.stateOfAC == 1) {
				agentName = "AC-Heat";
				c = Color.orange;
			}
			else {
				agentName = "AC-Cool";
				c = Color.cyan;
			}
		}
		else if (id == model.agentUserID) {
			agentName = "Józsi";
			c = Color.pink;
		}
		else return;
		
		super.drawAgent(g, x, y, c, -1);
		g.setColor(Color.black);
        super.drawString(g, x, y, defaultFont, agentName);
	}
	
	public void drawActualValues() {
		if (model.presentCO) {
			jlCO.setForeground(Color.red);
			jlCO.setText("CO concentration is CRITICAL");
			jlTime.setVisible(true);
			jlTime.setText("Time left until safe again (s): " + model.timeLeftUntilSafe);
		}
		else {
			jlCO.setForeground(Color.green);
			jlCO.setText("CO concentration is OK");
			jlTime.setVisible(false);
		}
		jlTemp.setText(String.format("Temperature (\u2103): %.2f", model.actualTemp));
		jlHumid.setText(String.format("Humidity (%%): %.2f", model.actualHumid));
	}
	
	public void initTextFields(){
		jtTempOpt.setText(String.valueOf(model.optTemp));
		jtTempThres.setText(String.valueOf(model.thresTemp));
		jtHumidOpt.setText(String.valueOf(model.optHumid));
		jtHumidThres.setText(String.valueOf(model.thresHumid));
	}
	
}
