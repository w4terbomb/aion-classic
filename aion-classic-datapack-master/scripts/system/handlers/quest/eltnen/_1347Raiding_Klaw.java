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
package quest.eltnen;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Rinzler (Encom)
/****/

public class _1347Raiding_Klaw extends QuestHandler
{
	private final static int questId = 1347;
	
	public _1347Raiding_Klaw() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(203965).addOnQuestStart(questId);
		qe.registerQuestNpc(203965).addOnTalkEvent(questId);
		qe.registerQuestNpc(203966).addOnTalkEvent(questId);
		qe.registerQuestNpc(210874).addOnKillEvent(questId);
		qe.registerQuestNpc(210908).addOnKillEvent(questId);
		qe.registerQuestNpc(210917).addOnKillEvent(questId);
		qe.registerQuestNpc(212056).addOnKillEvent(questId);
		qe.registerQuestNpc(212137).addOnKillEvent(questId);
	}
	
	@Override
    public boolean onDialogEvent(final QuestEnv env) {
        final Player player = env.getPlayer();
        int targetId = env.getTargetId();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 203965) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
						playQuestMovie(env, 186);
						return sendQuestDialog(env, 1011);
					} case ASK_ACCEPTION: {
						return sendQuestDialog(env, 4);
					} case ACCEPT_QUEST: {
						return sendQuestStartDialog(env);
					} case REFUSE_QUEST: {
						return closeDialogWindow(env);
					}
                }
            }
        } else if (qs != null && qs.getStatus() == QuestStatus.START) {
            if (targetId == 203966) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
						qs.setStatus(QuestStatus.REWARD);
					    updateQuestStatus(env);
						return sendQuestDialog(env, 1352);
					}
                }
            }
        } else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203966) {
				if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
        return false;
    }
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		int[] klawWorker = {210874, 210908};
		int[] klawGatherer = {210917, 212056, 212137};
		if (defaultOnKillEvent(env, klawWorker, 0, 15, 0) ||
		    defaultOnKillEvent(env, klawGatherer, 0, 7, 1)) {
			return true;
		} else {
			return false;
		}
	}
}