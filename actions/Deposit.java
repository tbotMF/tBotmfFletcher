package scripts.actions;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;

import scripts.Globals;
import scripts.sbf.action.Action;

public class Deposit extends Action {
	private final String action = selections.get("Action");

	@Override
	public void execute() {
		print("Depositing");
		if (action.equals("Cut") && Inventory.getCount("Knife") > 0) {
			if (Banking.depositAllExcept("Knife") > 0)
				Timing.waitCondition(new Condition() {

					@Override
					public boolean active() {
						General.sleep(100, 200);
						return Inventory.getAll().length == 1;
					}

				}, General.random(5000, 6000));
		} else if (Banking.depositAll() == Inventory.getAll().length)
			Timing.waitCondition(new Condition() {

				@Override
				public boolean active() {
					General.sleep(100, 200);
					return Inventory.getAll().length < 1;
				}

			}, General.random(5000, 6000));
	}

	@Override
	public boolean isValid() {
		return Globals.DEPOSIT.getStatus();
	}

}
