/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.controllers.movement;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.network.aion.serverpackets.S_MOVE_NEW;
import com.aionemu.gameserver.utils.PacketSendUtility;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author ATracer
 */
public abstract class CreatureMoveController<T extends VisibleObject> implements MoveController {

	protected T owner;
	protected byte heading;
	protected long lastMoveUpdate = System.currentTimeMillis();
	protected boolean isInMove = false;
	protected transient AtomicBoolean started = new AtomicBoolean(false);

	// TODO [AT] not good ...
	public byte movementMask;
	protected float targetDestX;
	protected float targetDestY;
	protected float targetDestZ;

	public CreatureMoveController(T owner) {
		this.owner = owner;
	}

	@Override
	public void moveToDestination() {
	}

	@Override
	public float getTargetX2() {
		return targetDestX;
	}

	@Override
	public float getTargetY2() {
		return targetDestY;
	}

	@Override
	public float getTargetZ2() {
		return targetDestZ;
	}

	@Override
	public void setNewDirection(float x, float y, float z, byte heading) {
		this.heading = heading;
		setNewDirection(x, y, z);
	}

	protected void setNewDirection(float x, float y, float z) {
		this.targetDestX = x;
		this.targetDestY = y;
		this.targetDestZ = z;
	}

	@Override
	public void startMovingToDestination() {
	}

	@Override
	public void abortMove() {
	}

	protected void setAndSendStopMove(Creature owner) {
		movementMask = MovementMask.IMMEDIATE;
		PacketSendUtility.broadcastPacket(owner, new S_MOVE_NEW(owner));
	}

	public final void updateLastMove() {
		lastMoveUpdate = System.currentTimeMillis();
	}

	/**
	 * @return the lastMoveUpdate
	 */
	public long getLastMoveUpdate() {
		return lastMoveUpdate;
	}

	@Override
	public byte getMovementMask() {
		return movementMask;
	}

	@Override
	public boolean isInMove() {
		return isInMove;
	}

	@Override
	public void setInMove(boolean value) {
		isInMove = value;
	}

}
