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
package com.aionemu.gameserver.model;

public enum TeleportAnimation
{
    NO_ANIMATION(0, 0),
    BEAM_ANIMATION(1, 3),
    JUMP_ANIMATION(3, 10),
    JUMP_ANIMATION_2(4, 10),
    FIRE_ANIMATION(4, 0x0B),
    JUMP_ANIMATION_3(8, 3),
	MAGE_ANIMATION(8, 10);
	
    private int startAnimation;
    private int endAnimation;
	
    private TeleportAnimation(int startAnimation, int endAnimation) {
	   this.startAnimation = startAnimation;
       this.endAnimation = endAnimation;
	}
	
    public int getStartAnimationId() {
        return startAnimation;
    }
	
    public int getEndAnimationId() {
        return endAnimation;
    }
	
    public boolean isNoAnimation() {
        return getStartAnimationId() == 0;
    }
}