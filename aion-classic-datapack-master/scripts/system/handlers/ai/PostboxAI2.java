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
package ai;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.S_NPC_HTML_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.S_MESSAGE_CODE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author Rinzler (Encom)
/****/

@AIName("postbox")
public class PostboxAI2 extends NpcAI2
{
	@Override
	protected void handleDialogStart(Player player) {
		int level = player.getLevel();
		if (level < 10) {
			PacketSendUtility.sendPacket(player, S_MESSAGE_CODE.STR_FREE_EXPERIENCE_CHARACTER_CANT_SEND_ITEM("10"));
			return;
		}
		PacketSendUtility.sendPacket(player, new S_NPC_HTML_MESSAGE(getObjectId(), 18));
		player.getMailbox().sendMailList(false);
	}
	
	@Override
	protected void handleDialogFinish(Player player) {
	}
}