package scripts;

import java.util.ArrayList;
import java.util.List;

import org.tribot.api.input.Mouse;

import scripts.actions.Cut;
import scripts.actions.Deposit;
import scripts.actions.Make;
import scripts.actions.OpenBank;
import scripts.actions.StringBow;
import scripts.actions.Withdraw;
import scripts.actions.DeathWalking.DeathWalk;
import scripts.sbf.graphics.AbstractGUI;
import scripts.sbf.state.State;
import scripts.states.Banking;
import scripts.states.Cutting;
import scripts.states.DeathWalking;
import scripts.states.Making;
import scripts.states.Stringing;

public class MFFletcherGUI extends AbstractGUI {

	/**
	 * Creates new form MFAIOFletcherGUI
	 */
	public MFFletcherGUI() {
		initComponents();
	}

	@Override
	public List<State> createStateAssociations() {
		List<State> states = new ArrayList<State>();
		State banking = new Banking();
		State cutting = new Cutting();
		State making = new Making();
		State stringing = new Stringing();
		State dWalking = new DeathWalking();
		banking.addActions(new OpenBank(), new Deposit(), new Withdraw());
		states.add(banking);
		if (makeRadioButton.isSelected()) {
			making.addActions(new Make());
			states.add(making);
		} else if (fletchRadioButton.isSelected()) {
			cutting.addActions(new Cut());
			states.add(cutting);
		} else {
			stringing.addActions(new StringBow());
			states.add(stringing);
		}
		dWalking.addActions(new DeathWalk());
		states.add(dWalking);
		return states;
	}
	

	@Override
	public void processUserSelections() {
		processItemToUse();
		processProduct();
		processMouseSpeed();
		processActions();
	}

	private void processActions() {
		if (makeRadioButton.isSelected()) {
			selections.put("Action", "Make");

			if (itemToUseComboBox.getSelectedItem().equals("Headless arrow"))
				skillManager.loadInventoryResources(
						productComboBox.getSelectedItem() + "tips",
						"Headless arrow");
			else if (itemToUseComboBox.getSelectedItem().equals("Bolts"))
				skillManager.loadInventoryResources("Feather",
						(String) productComboBox.getSelectedItem() + " (unf)");
			else
				skillManager.loadInventoryResources("Feather",
						(String) itemToUseComboBox.getSelectedItem());

		} else if (fletchRadioButton.isSelected()) {
			selections.put("Action", "Cut");
			skillManager.loadInventoryResources("Knife",
					(String) itemToUseComboBox.getSelectedItem());
			switch(selections.get("ItemToUse")){
			case "Logs":
				skillManager.loadMasterIndex(305);
				break;
			case "Maple logs":
				skillManager.loadMasterIndex(304);
				break;
			case "Magic logs":
				skillManager.loadMasterIndex(303);
				break;
			case "Oak logs":
				skillManager.loadMasterIndex(304);
				break;
			case "Willow logs":
				skillManager.loadMasterIndex(304);
				break;
			case "Yew logs":
				skillManager.loadMasterIndex(304);
				break;
			}
		} else {
			selections.put("Action", "String");
			skillManager.loadInventoryResources("Bow string",
					(String) productComboBox.getSelectedItem() + " (u)");
		}
	}

	private void processItemToUse() {
		selections.put("ItemToUse",
				(String) itemToUseComboBox.getSelectedItem());
		if (itemToUseComboBox.getSelectedItem().equals("Bolts"))
			selections.put("ItemToUse",
					(String) productComboBox.getSelectedItem() + " (unf)");
		else if (itemToUseComboBox.getSelectedItem().equals("Darts"))
			selections.put("ItemToUse",
					(String) productComboBox.getSelectedItem() + " tip");
		
		
	}

	private void processProduct() {
		selections.put("Product", (String) productComboBox.getSelectedItem());
		if (stringRadioButton.isSelected())
			selections.put("Product",
					(String) productComboBox.getSelectedItem() + " (u)");

	}

	private void processMouseSpeed() {
		Mouse.setSpeed((int) mouseSpeedSpinner.getValue());
	}
}
