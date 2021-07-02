package services.xenlan.hub.parkour.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import services.xenlan.hub.xHub;
import services.xenlan.hub.utils.CC;

public class ParkourListener implements Listener {

	@EventHandler
	public void onParkourActivationLand(PlayerInteractEvent event) {
		String activationMat = xHub.getInstance().getSettingsYML().getConfig().getString("Parkour.Start-Material");
		String checkPointMat = xHub.getInstance().getSettingsYML().getConfig().getString("Parkour.CheckPoint-Material");
		boolean enabled = xHub.getInstance().getSettingsYML().getConfig().getBoolean("Parkour.Enabled");
		if (event.getAction() == Action.PHYSICAL) {
			if (event.getClickedBlock() == null)
				return;
			if (event.getClickedBlock().getType() == Material.valueOf(checkPointMat)) {
				if (xHub.getInstance().getParkourManager().clickedPlate.contains(event.getPlayer()))
					return;
				xHub.getInstance().getParkourManager().saveCheckPoint(event.getPlayer());
				xHub.getInstance().getParkourManager().clickedPlate.add(event.getPlayer());
				new BukkitRunnable() {
					@Override
					public void run() {
						xHub.getInstance().getParkourManager().clickedPlate.remove(event.getPlayer());
					}
				}.runTaskLater(xHub.getInstance(), 20 * 6);
			}
			if (event.getClickedBlock().getType() == Material.valueOf(activationMat)) {
				if (!xHub.getInstance().getParkourManager().parkour.contains(event.getPlayer())) {
					double x = xHub.getInstance().getSettingsYML().getConfig().getDouble("Parkour.Start-Location.X");
					double y = xHub.getInstance().getSettingsYML().getConfig().getDouble("Parkour.Start-Location.Y");
					double z = xHub.getInstance().getSettingsYML().getConfig().getDouble("Parkour.Start-Location.Z");
					float pitch = xHub.getInstance().getSettingsYML().getConfig().getInt("Parkour.Start-Location.Pitch");
					float yaw = xHub.getInstance().getSettingsYML().getConfig().getInt("Parkour.Start-Location.Yaw");
					Location location = new Location(event.getPlayer().getWorld(), x, y, z);
					location.setPitch(pitch);
					location.setYaw(yaw);
					xHub.getInstance().getParkourManager().activateParkour(event.getPlayer(), location);
					xHub.getInstance().getParkourManager().clickedPlate.add(event.getPlayer());
					new BukkitRunnable() {
						@Override
						public void run() {
							xHub.getInstance().getParkourManager().clickedPlate.remove(event.getPlayer());
						}
					}.runTaskLater(xHub.getInstance(), 20 * 6);
				}
			}
		}
	}

	@EventHandler
	public void onInteractFinishSign(PlayerInteractEvent event) {

		Block sign = event.getClickedBlock();

		if (sign == null)
			return;

		if (sign.getState() instanceof Sign) {
			if (xHub.getInstance().getParkourManager().parkour.contains(event.getPlayer())) {
				String line1 = xHub.getInstance().getSettingsYML().getConfig().getString("Parkour.Finish-Sign.Line-1");
				String line2 = xHub.getInstance().getSettingsYML().getConfig().getString("Parkour.Finish-Sign.Line-2");
				String line3 = xHub.getInstance().getSettingsYML().getConfig().getString("Parkour.Finish-Sign.Line-3");
				String line4 = xHub.getInstance().getSettingsYML().getConfig().getString("Parkour.Finish-Sign.Line-4");
				Sign block = (Sign) sign.getState();
				if (block.getLine(0).equalsIgnoreCase(CC.chat(line1))) {
					if (block.getLine(1).equalsIgnoreCase(CC.chat(line2))) {
						if (block.getLine(2).equalsIgnoreCase(CC.chat(line3))) {
							if (block.getLine(3).equalsIgnoreCase(CC.chat(line4))) {

								xHub.getInstance().getParkourManager().finishParkour(event.getPlayer());

							}
						}
					}
				}

			}
		}

	}

	@EventHandler
	public void onChange(SignChangeEvent event) {

		if (event.getLine(0).equalsIgnoreCase("[finish]")) {

			String line1 = xHub.getInstance().getSettingsYML().getConfig().getString("Parkour.Finish-Sign.Line-1");
			String line2 = xHub.getInstance().getSettingsYML().getConfig().getString("Parkour.Finish-Sign.Line-2");
			String line3 = xHub.getInstance().getSettingsYML().getConfig().getString("Parkour.Finish-Sign.Line-3");
			String line4 = xHub.getInstance().getSettingsYML().getConfig().getString("Parkour.Finish-Sign.Line-4");
			event.setLine(0, CC.chat(line1));
			event.setLine(1, CC.chat(line2));
			event.setLine(2, CC.chat(line3));
			event.setLine(3, CC.chat(line4));

		}

	}

}
