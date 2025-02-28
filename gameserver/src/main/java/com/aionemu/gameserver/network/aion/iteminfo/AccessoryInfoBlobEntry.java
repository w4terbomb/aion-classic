/*
 * This file is part of aion-lightning <js-emu.ru>.
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
package com.aionemu.gameserver.network.aion.iteminfo;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob.ItemBlobType;

import java.nio.ByteBuffer;

/**
 * This blob is sent for accessory items (such as ring, earring, waist). It keeps info about slots that item can be
 * equipped to.
 * 
 * @author -Nemesiss-
 * @modified Rolandas
 */
public class AccessoryInfoBlobEntry extends ItemBlobEntry{

	AccessoryInfoBlobEntry() {
		super(ItemBlobType.SLOTS_ACCESSORY);
	}

	@Override
	public
	void writeThisBlob(ByteBuffer buf) {
		Item item = ownerItem;

		ItemSlot[] slots = ItemSlot.getSlotsFor(item.getItemTemplate().getItemSlot());
		writeQ(buf, slots[0].getSlotIdMask());
	}

	@Override
	public int getSize() {
		return 8;
	}
}
