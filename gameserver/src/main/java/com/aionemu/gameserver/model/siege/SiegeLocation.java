package com.aionemu.gameserver.model.siege;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.siegelocation.SiegeLocationTemplate;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.aionemu.gameserver.world.zone.SiegeZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.handler.ZoneHandler;
import javolution.util.FastMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SiegeLocation implements ZoneHandler
{
	private static final Logger log = LoggerFactory.getLogger(SiegeLocation.class);
	public static final int STATE_INVULNERABLE = 0;
	public static final int STATE_VULNERABLE = 1;
	
	protected SiegeLocationTemplate template;
	protected int locationId;
	protected int occupyCount;
	protected SiegeType type;
	protected int worldId;
	protected SiegeRace siegeRace = SiegeRace.BALAUR;
	protected int legionId;
	protected long lastArtifactActivation;
	private boolean vulnerable;
	private int nextState;
	protected List<SiegeZoneInstance> zone;
	private List<SiegeShield> shields;
	private boolean isUnderShield;
	private boolean canTeleport;
	protected int siegeDuration;
	protected int influenceValue;
	private FastMap<Integer, Creature> creatures = new FastMap<Integer, Creature>();
	private FastMap<Integer, Player> players = new FastMap<Integer, Player>();
	protected int buffId;
	protected int buffIdA;
	protected int buffIdE;
	
	public SiegeLocation() {
	}
	
	public SiegeLocation(SiegeLocationTemplate template) {
		this.template = template;
		this.locationId = template.getId();
		this.occupyCount = template.getOccupyCount();
		this.worldId = template.getWorldId();
		this.type = template.getType();
		this.siegeDuration = template.getSiegeDuration();
		this.zone = new ArrayList<SiegeZoneInstance>();
		this.influenceValue = template.getInfluenceValue();
	}
	
	public SiegeLocationTemplate getTemplate() {
		return template;
	}
	
	public int getLocationId() {
		return this.locationId;
	}
	
	public int getWorldId() {
		return this.worldId;
	}
	
	public SiegeType getType() {
		return this.type;
	}
	
	public int getSiegeDuration() {
		return siegeDuration;
	}
	
	public SiegeRace getRace() {
		return this.siegeRace;
	}
	
	public void setRace(SiegeRace siegeRace) {
		this.siegeRace = siegeRace;
	}
	
	public int getLegionId() {
		return this.legionId;
	}

	public void setLegionId(int legionId) {
		this.legionId = legionId;
	}
	
	public int getOccupyCount() {
        return this.occupyCount;
    }

    public void setOccupyCount(int occupyCount) {
        this.occupyCount = occupyCount;
    }
	
	public int getNextState() {
		return nextState;
	}
	
	public void setNextState(int nextState) {
		this.nextState = nextState;
	}
	
	public boolean isVulnerable() {
		return this.vulnerable;
	}
	
	public boolean isUnderShield() {
		return this.isUnderShield;
	}

	public void setUnderShield(boolean value) {
		this.isUnderShield = value;
		if (shields != null) {
			for (SiegeShield shield : shields)
				shield.setEnabled(value);
		}
	}

	public void setShields(List<SiegeShield> shields) {
		this.shields = shields;
		log.debug("Attached shields for locId: " + locationId);
		for (SiegeShield shield : shields)
			log.debug(shield.toString());
	}
	
	public boolean isCanTeleport(Player player) {
		return canTeleport;
	}
	
	public void setCanTeleport(boolean canTeleport) {
		this.canTeleport = canTeleport;
	}
	
	public void setVulnerable(boolean value) {
		this.vulnerable = value;
	}
	
	public int getInfluenceValue() {
		return influenceValue;
	}
	
	public List<SiegeZoneInstance> getZone() {
		return zone;
	}
	
	public void addZone(SiegeZoneInstance zone) {
		this.zone.add(zone);
		zone.addHandler(this);
	}
	
	public boolean isInsideLocation(Creature creature) {
		if (zone.isEmpty())
			return false;
		for (int i = 0; i < zone.size(); i++)
			if (zone.get(i).isInsideCreature(creature))
				return true;
		return false;
	}
	
	public boolean isInActiveSiegeZone(Player player) {
		if (isVulnerable() && isInsideLocation(player)) {
			return true;
		}
		return false;
	}
	
	public void clearLocation() {
	}
	
	@Override
	public void onEnterZone(Creature creature, ZoneInstance zone) {
		if (!creatures.containsKey(creature.getObjectId())) {
			creatures.put(creature.getObjectId(), creature);
			if (creature instanceof Player) {
				players.put(creature.getObjectId(), (Player) creature);
			}
		}
	}
	
	@Override
	public void onLeaveZone(Creature creature, ZoneInstance zone) {
		if (!this.isInsideLocation(creature)) {
			creatures.remove(creature.getObjectId());
			players.remove(creature.getObjectId());
		}
	}
	
	public void doOnAllPlayers(Visitor<Player> visitor) {
		try {
			for (FastMap.Entry<Integer, Player> e = players.head(), mapEnd = players.tail(); (e = e.getNext()) != mapEnd;) {
				Player player = e.getValue();
				if (player != null) {
					visitor.visit(player);
				}
			}
		} catch (Exception ex) {
			log.error("Exception when running visitor on all players" + ex);
		}
	}
	
	public FastMap<Integer, Creature> getCreatures() {
		return creatures;
	}
	
	public FastMap<Integer, Player> getPlayers() {
		return players;
	}
	
	public int getBuffId() {
		return buffId = template.getBuffId();
	}
	
	public int getBuffIdA() {
		return buffIdA = template.getBuffIdA();
	}
	
	public int getBuffIdE() {
		return buffIdE = template.getBuffIdE();
	}
}