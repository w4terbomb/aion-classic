/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
 *
 *  Encom is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob.ItemBlobType;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemUpdateType;

public class S_CHANGE_ITEM_DESC extends AionServerPacket
{
	private final Player player;
	private final Item item;
	private final ItemUpdateType updateType;
	
	public S_CHANGE_ITEM_DESC(Player player, Item item) {
		this(player, item, ItemUpdateType.DEC_ITEM_USE);
	}
	
	public S_CHANGE_ITEM_DESC(Player player, Item item, ItemUpdateType updateType) {
		this.player = player;
		this.item = item;
		this.updateType = updateType;
	}
	
	@Override
	protected void writeImpl(AionConnection con) {
		ItemTemplate itemTemplate = item.getItemTemplate();
		writeD(item.getObjectId());
		writeNameId(itemTemplate.getNameId());
		ItemInfoBlob itemInfoBlob;
		switch (updateType) {
			case EQUIP_UNEQUIP:
				itemInfoBlob = new ItemInfoBlob(player, item);
				itemInfoBlob.addBlobEntry(ItemBlobType.EQUIPPED_SLOT);
			break;
			case CHARGE:
				itemInfoBlob = new ItemInfoBlob(player, item);
				itemInfoBlob.addBlobEntry(ItemBlobType.CONDITIONING_INFO);
			default:
				itemInfoBlob = ItemInfoBlob.getFullBlob(player, item);
			break;
		}
		itemInfoBlob.writeMe(getBuf());
		if (updateType.isSendable())
			writeH(updateType.getMask());
	}
}