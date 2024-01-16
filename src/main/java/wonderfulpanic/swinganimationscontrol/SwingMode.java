/*
 * Copyright (C) 2024 WonderfulPanic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package wonderfulpanic.swinganimationscontrol;

import static net.minecraft.util.EnumHand.*;
import java.util.Random;
import java.util.function.Function;
import net.minecraft.util.EnumHand;

public enum SwingMode implements Function<EnumHand,EnumHand>{
	RANDOM{
		private final Random rand=new Random();
		@Override
		public EnumHand apply(EnumHand t){
			return rand.nextBoolean()?MAIN_HAND:OFF_HAND;
		}
	},
	SWITCH{
		private boolean toggle;
		@Override
		public EnumHand apply(EnumHand t){
			return (toggle=!toggle)?MAIN_HAND:OFF_HAND;
		}
	},
	TIMED_SWITCH{
		private boolean toggle;
		private long lastSwitch=System.currentTimeMillis()+200L;
		@Override
		public EnumHand apply(EnumHand t){
			long currentTime=System.currentTimeMillis();
			if(lastSwitch<currentTime){
				lastSwitch=currentTime+200L;
				toggle=!toggle;
			}
			return toggle?MAIN_HAND:OFF_HAND;
		}
	};
}