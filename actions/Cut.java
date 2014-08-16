package scripts.actions;

import java.util.Random;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
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
import scripts.sbf.util.MFUtil;

public class Cut extends Action {
	private final String itemToUse = selections.get("ItemToUse");
	private final String product = selections.get("Product");
	private final Random randomizeEnterAmount = new Random();
	private final ABC abc = manager.getABC();
	private final String knife = "Knife";

	@Override
	public void execute() {
		print("Cutting " + itemToUse.toLowerCase() + " to make "
				+ itemToUse.toLowerCase().split(" ")[0] + " "
				+ product.toLowerCase() + "s.");

		if (Inventory.find(knife)[0].click("Use"))
			this.abc.waitItemInteractionDelay();
		else
			return;

		if (Inventory.find(itemToUse)[0].click("Use"))
			this.abc.waitItemInteractionDelay();
		else
			return;

		if (!Timing.waitCondition(new Condition() {
			@Override
			public boolean active() {
				General.sleep(50, 70);
				return Interfaces.get(skillManager.getMasterIndex()) != null;
			}

		}, General.random(3000, 4000)))
			return;

		RSInterface cutProduct = skillManager.getChildInterfaceFor(
				Interfaces.get(skillManager.getMasterIndex()), product);
		if (cutProduct == null)
			return;

		if (!cutProduct.click("Make X"))
			return;

		if (!Timing.waitCondition(new Condition() {

			@Override
			public boolean active() {
				General.sleep(50, 70);
				return MFUtil.enterAmountIsOpen();
			}

		}, General.random(3000, 4000)))
			return;

		Keyboard.typeSend(String.valueOf(randomizeEnterAmount.nextInt(90 - 27) + 27));

		if (!Timing.waitCondition(new Condition() {

			@Override
			public boolean active() {
				General.sleep(100, 200);
				return Player.getAnimation() > -1;
			}

		}, General.random(3000, 4000)))
			return;
		while (Inventory.getCount(itemToUse) > 0) {
			General.sleep(100, 200);
			this.abc.doAllIdleActions(SKILLS.FLETCHING, GameTab.TABS.INVENTORY);
			this.abc.hoverNextObject("Bank");
			if (Skills.getCurrentLevel(SKILLS.FLETCHING) > skillManager
					.getCurrentLevel()) {
				skillManager.updateCurrentLevel();
				break;
			}

		}
		SkillGlobals.SKILLING.setStatus(Inventory.getCount(itemToUse) > 0);
	}

	@Override
	public boolean isValid() {

		return Globals.CUT.getStatus();
	}

}
