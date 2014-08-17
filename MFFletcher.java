package scripts;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.sbf.manager.Manager;
import scripts.sbf.skill.SkillManager;
import scripts.sbf.util.ABC;
import scripts.sbf.util.MFUtil;

@ScriptManifest(authors = { "modulusfrank12" }, category = "Fletching", name = "mfFletcher")
public class MFFletcher extends Script implements Painting {
	private final Manager manager = Manager.getInstance();
	private final SkillManager skillManager = SkillManager.getInstance();
	private int currentFletchtingLevel = Skills
			.getCurrentLevel(SKILLS.FLETCHING);
	private long startTime;
	private final Font paintFont = new Font("Miriam Fixed", Font.BOLD, 14);

	@Override
	public void run() {
		skillManager.loadCurrentLevel(currentFletchtingLevel);
		manager.loadScript(this);
		manager.loadABC(new ABC());
		General.useAntiBanCompliance(true);
		final MFFletcherGUI gui = new MFFletcherGUI();
		gui.setTitle("mfFletcher v1.0");
		gui.setVisible(true);
		startTime = Timing.currentTimeMillis();
		manager.initialize(gui, 20, false);
	}

	@Override
	public void onPaint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		rh.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		String runtime = Timing.msToString(System.currentTimeMillis()
				- startTime);

		g2d.setRenderingHints(rh);
		g2d.setColor(Color.WHITE);
		g2d.setFont(paintFont);

		g2d.drawString(
				"Exp : "
						+ manager.getExpPerHrForFxnTime(
								MFUtil.getExpForTrainedSkills(),
								System.currentTimeMillis() - startTime)
						+ "\\hr", 10,

				50);
		g2d.drawString("Runtime : " + runtime, 10, 80);
		g2d.drawString("mfFletcher v1.0", 10, 110);
		g2d.setColor(Color.gray);
		g2d.drawRect(0, 30, 240, 27);
		g2d.drawRect(0, 58, 240, 27);
		g2d.drawRect(0, 88, 150, 27);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.5f));
		g2d.setColor(Color.cyan);
		g2d.fillRect(0, 30, 240, 27);
		g2d.setColor(Color.orange);
		g2d.fillRect(0, 58, 240, 27);
		g2d.setColor(Color.green);
		g2d.fillRect(0, 88, 150, 27);

	}


}
