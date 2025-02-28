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
package quest.heiron;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author Rinzler (Encom)
/****/

public class _1647Dressing_Up_For_Bollvig extends QuestHandler
{
	private final static int questId = 1647;
	
	public _1647Dressing_Up_For_Bollvig() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerOnMovieEndQuest(199, questId);
		qe.registerQuestNpc(790019).addOnQuestStart(questId);
		qe.registerQuestNpc(790019).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 790019) {
				switch (env.getDialog()) {
                    case START_DIALOG: {
						return sendQuestDialog(env, 4762);
					} case ASK_ACCEPTION: {
						return sendQuestDialog(env, 4);
					} case ACCEPT_QUEST: {
						PacketSendUtility.sendWhiteMessage(player, "Buy the items to <Arcinia> in sanctum & equip them !!!");
						return sendQuestStartDialog(env, 182201783, 1); //Myanee's Flute.
					} case REFUSE_QUEST: {
				        return closeDialogWindow(env);
					}
                }
			}
		} else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 790019) {
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 10002);
				} else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					removeQuestItem(env, 182201783, 1); //Myanee's Flute.
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (movieId == 199) {
			//Graveknight.
            QuestService.addNewSpawn(210040000, player.getInstanceId(), 204635, player.getX() + 3, player.getY(), player.getZ(), (byte) 0);
            QuestService.addNewSpawn(210040000, player.getInstanceId(), 204635, player.getX(), player.getY() + 3, player.getZ(), (byte) 0);
            QuestService.addNewSpawn(210040000, player.getInstanceId(), 204635, player.getX() - 3, player.getY(), player.getZ(), (byte) 0);
			return true;
		}
		return false;
	}
}