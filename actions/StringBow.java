package scripts.actions;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSInterface;

import scripts.Globals;
import scripts.sbf.action.Action;
import scripts.sbf.skill.SkillGlobals;
import scripts.sbf.util.ABC;

public class StringBow extends Action {
	private final int masterIndex = 309;
	private final String product = selections.get("Product");
	private final ABC abc = manager.getABC();

	private boolean useBowString() {
		if (Inventory.find("Bow string")[0].click("Use"))
			this.abc.waitItemInteractionDelay();
		else
			return false;
		if (Inventory.find(product)[0].click("Use"))
			this.abc.waitItemInteractionDelay();
		else
			return false;
		return true;
	}

	private boolean selectMakeAll() {
		if (!Timing.waitCondition(new Condition() {

			@Override
			public boolean active() {
				General.sleep(100, 200);
				return Interfaces.get(masterIndex) != null;
			}

		}, General.random(5000, 6000)))
			return false;

		RSInterface stringingBow = skillManager.getChildInterfaceFor(
				Interfaces.get(masterIndex), product.split(" [(]")[0]);

		return stringingBow != null && stringingBow.click("Make All");
	}

	private void performAntiBan() {
		this.abc.doAllIdleActions(SKILLS.FLETCHING, GameTab.TABS.INVENTORY);
		this.abc.hoverNextObject("Bank");
	}

	private boolean updateFletchingLevel() {
		if (Skills.getCurrentLevel(SKILLS.FLETCHING) > skillManager
				.getCurrentLevel()) {
			skillManager.updateCurrentLevel();
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		print("Stringing " + product.toLowerCase() + ".");
		if (useBowString() && selectMakeAll())
			if (!Timing.waitCondition(new Condition() {

				@Override
				public boolean active() {
					General.sleep(100, 200);
					return Player.getAnimation() > -1;
				}

			}, General.random(3000, 4000)))
				return;
		while (Inventory.getCount("Bow string") > 0
				&& Inventory.getCount(product) > 0) {
			General.sleep(100, 200);
			performAntiBan();
			if (updateFletchingLevel())
				break;

		}

		SkillGlobals.SKILLING.setStatus(Inventory.getCount("Bow string") > 0
				&& Inventory.getCount(product) > 0);

	}

	@Override
	public boolean isValid() {
		return Globals.STRING_BOW.getStatus();
	}

}
