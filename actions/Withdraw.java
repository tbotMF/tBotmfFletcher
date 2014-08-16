package scripts.actions;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills.SKILLS;

import scripts.Globals;
import scripts.sbf.action.Action;
import scripts.sbf.util.ABC;

public class Withdraw extends Action {
	private final String action = selections.get("Action");
	private final ABC abc = manager.getABC();

	@Override
	public void execute() {
		print("Withdrawing");
		switch (action) {
		case "Make":
			for (final String inventoryResource : skillManager
					.getInventoryResources())
				if (Inventory.getCount(inventoryResource) < 1)
					for (int i = 0; i < 50; i++) {
						if (Banking.find(inventoryResource).length > 0) {
							if (Banking.withdraw(0, inventoryResource)) {
								if (Timing.waitCondition(new Condition() {

									@Override
									public boolean active() {
									abc.waitItemInteractionDelay();
										return Inventory
												.getCount(inventoryResource) > 0;
									}

								}, General.random(5000, 6000)))
									i = 50;
							}
						} else if (i == 49) {
							print("Couldn't find "
									+ inventoryResource.toLowerCase()
									+ ", exiting script.");
							General.sleep(2000);
							manager.loadTerminationCondition(true);
							return;
						}
					}
			break;
		case "String":
			for (final String inventoryResource : skillManager
					.getInventoryResources())
				if (Inventory.getCount(inventoryResource) < 14)
					for (int i = 0; i < 50; i++) {

						if (Banking.find(inventoryResource).length > 0) {
							if (Banking.withdraw(
									14 - Inventory.getCount(inventoryResource),
									inventoryResource)) {
								if (Timing.waitCondition(new Condition() {

									@Override
									public boolean active() {
										abc.waitItemInteractionDelay();
										return Inventory
												.getCount(inventoryResource) > 0;
									}

								}, General.random(5000, 6000)))
									i = 50;
							}
						} else if (i == 49) {
							print("Couldn't find "
									+ inventoryResource.toLowerCase()
									+ ", exiting script.");
							General.sleep(2000);
							manager.loadTerminationCondition(true);
							return;
						}
					}

			break;
		case "Cut":
			for (final String inventoryResource : skillManager
					.getInventoryResources()) {
				if (inventoryResource.equalsIgnoreCase("knife")
						&& Inventory.getCount("Knife") < 1) {
					for (int i = 0; i < 50; i++) {
						if (Banking.find("Knife").length > 0) {
							if (Banking.withdraw(1, inventoryResource)) {
								if (Timing.waitCondition(new Condition() {

									@Override
									public boolean active() {
										abc.waitItemInteractionDelay();
										return Inventory
												.getCount(inventoryResource) == 1;
									}

								}, General.random(5000, 6000)))
									i = 50;
							}
						} else if (i == 49) {
							print("Couldn't find a knife, exiting script.");
							General.sleep(2000);
							manager.loadTerminationCondition(true);
							return;
						}
					}
				} else if (!inventoryResource.equalsIgnoreCase("Knife"))
					for (int i = 0; i < 50; i++) {
						if (Banking.find(inventoryResource).length > 0) {
							if (Banking.withdraw(0, inventoryResource)) {
								if (Timing.waitCondition(new Condition() {

									@Override
									public boolean active() {
										abc.waitItemInteractionDelay();
										return Inventory
												.getCount(inventoryResource) > 0;
									}

								}, General.random(5000, 6000)))
									i = 50;
							}
						} else if (i == 49) {
							print("Couldn't find "
									+ inventoryResource.toLowerCase()
									+ ", exiting script.");
							General.sleep(2000);
							manager.loadTerminationCondition(true);
							return;
						}
					}
			}
			break;
		}

		for (final String inventoryResource : skillManager
				.getInventoryResources())
			if (Inventory.find(inventoryResource).length < 1)
				return;

		if (!Banking.close())
			return;
		if (!Timing.waitCondition(new Condition() {

			@Override
			public boolean active() {
				General.sleep(100, 200);
				return !Banking.isBankScreenOpen();
			}

		}, General.random(4000, 5000)))
			return;
		this.abc.doAllIdleActions(SKILLS.FLETCHING, GameTab.TABS.INVENTORY);

	}

	@Override
	public boolean isValid() {
		return Globals.WITHDRAW.getStatus();
	}

}
