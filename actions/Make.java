package scripts.actions;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSInterface;

import scripts.Globals;
import scripts.sbf.action.Action;
import scripts.sbf.skill.SkillGlobals;
import scripts.sbf.util.ABC;

public class Make extends Action {
	private final int masterIndex = 582;
	private final String product = selections.get("Product");
	private final String itemToUse = selections.get("ItemToUse");
	private final ABC abc = manager.getABC();
	private final String secondItemToUse = itemToUse
			.equalsIgnoreCase("Headless arrow") ? product.concat("tips")
			: "Feather";

	private boolean clickMakeTenSets() {
		if (!Timing.waitCondition(new Condition() {

			@Override
			public boolean active() {
				General.sleep(100, 200);
				return Interfaces.get(masterIndex) != null;
			}

		}, General.random(5000, 6000)))
			return false;
		RSInterface makeProduct = skillManager.getChildInterfaceFor(
				Interfaces.get(masterIndex), product);

		return makeProduct != null && makeProduct.click("Make 10 sets");
	}

	private void performAntiBan() {
		this.abc.doAllIdleActions(SKILLS.FLETCHING, GameTab.TABS.INVENTORY);
		this.abc.hoverNextItem(itemToUse);
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
		print("Making " + product.toLowerCase() + ".");
		if (!itemToUse.contains("(unf)")) {
			if (!(Inventory.find(itemToUse)[0].click("Use") && Inventory
					.find(secondItemToUse)[0].click("Use")))
				return;
			
		} else {
			while (Globals.MAKE.getStatus()) {
				Inventory.find(itemToUse)[0].click("Use");
				this.abc.waitItemInteractionDelay();
				Inventory.find(secondItemToUse)[0].click("Use");
				this.abc.waitItemInteractionDelay();
			}
			return;
		}

		final int productCountInitial = Inventory.getCount(product);
		final String minInput = (Inventory.getCount(itemToUse) == Math.min(
				Inventory.getCount(itemToUse),
				Inventory.getCount(secondItemToUse))) ? itemToUse
				: secondItemToUse;
		if (clickMakeTenSets())
			while (productCountInitial + 150 != Inventory.getCount(product)
					&& Inventory.getCount(minInput) > 0) {
				General.sleep(100, 200);
				performAntiBan();
				if(updateFletchingLevel())
					break;

			}
		if (Inventory.getCount(minInput) < 1)
			this.abc.getABCUtil().BOOL_TRACKER.HOVER_NEXT.reset();

		SkillGlobals.SKILLING.setStatus(Inventory.getCount(minInput) > 0);
	}

	@Override
	public boolean isValid() {
		return Globals.MAKE.getStatus();
	}

}
