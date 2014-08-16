package scripts.actions.DeathWalking;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Objects;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSObject;

import scripts.sbf.action.Action;
import scripts.sbf.skill.SkillGlobals;

public class DeathWalk extends Action {
		private final Filter<RSObject> bankFilter = Filters.Objects
			.actionsContains("Bank").combine(new Filter<RSObject>() {

				@Override
				public boolean accept(RSObject bank) {

					return bank != null && bank.isOnScreen();
				}

			}, true);

	@Override
	public void execute() {
		print("Deathwalking");
		if (WebWalking.walkToBank())
			Timing.waitCondition(
					new Condition() {

						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Objects.findNearest(5, bankFilter).length > 0;
						}

					}, General.random(5000, 6000));

	}

	@Override
	public boolean isValid() {
		return SkillGlobals.IN_RESPAWN_LOCATION.getUpdatedStatus();
	}

}
