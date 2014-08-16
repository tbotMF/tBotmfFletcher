package scripts.actions;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Objects;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSObject;

import scripts.Globals;
import scripts.sbf.action.Action;
import scripts.sbf.util.ABC;

public class OpenBank extends Action {
	private final ABC abc = manager.getABC();
	@Override
	public void execute() {
		print("Opening bank");
		final RSObject[] bank = Objects.findNearest(5,
				Filters.Objects.actionsContains("Bank"));
		if (bank.length > 0 && bank[0] != null && bank[0].isOnScreen()) {
			abc.doAllIdleActions(SKILLS.FLETCHING, GameTab.TABS.INVENTORY);
			if (Banking.openBank())
				Timing.waitCondition(new Condition() {

					@Override
					public boolean active() {
						General.sleep(100, 200);
						return Banking.isBankScreenOpen();
					}

				}, General.random(5000, 6000));

		}

	}

	@Override
	public boolean isValid() {
		return Globals.OPEN_BANK.getStatus();
	}

}
