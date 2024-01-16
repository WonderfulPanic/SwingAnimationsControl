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

package wonderfulpanic.swinganimationscontrol.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import wonderfulpanic.swinganimationscontrol.ISwingAnimator;
import wonderfulpanic.swinganimationscontrol.SwingAnimationsControl;

@Mixin(EntityPlayerSP.class)
public abstract class MixinSwingArm extends EntityLivingBase implements ISwingAnimator{
	@Shadow
	private NetHandlerPlayClient connection;
	public MixinSwingArm(){
		super(null);
	}
	@Overwrite
	@Override
	public void swingArm(EnumHand hand){
		if(!SwingAnimationsControl.doSwing())
			return;
		animateSwinging(SwingAnimationsControl.getHand(hand));
	}
	@Override
	public void animateSwinging(EnumHand hand){
		super.swingArm(hand);
        connection.sendPacket(new CPacketAnimation(hand));
	}
}