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
import static net.minecraft.util.math.RayTraceResult.Type.*;
import static wonderfulpanic.swinganimationscontrol.SwingMode.*;
import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;

@Mod(modid="swinganimationscontrol",name="SwingAnimationsControl",version="1.0",
	clientSideOnly=true,acceptedMinecraftVersions="*",acceptableRemoteVersions="*")
public class SwingAnimationsControl{
	private static final KeyBinding clickOffHand=new KeyBinding("key.attackoffhand",KeyConflictContext.IN_GAME,-96,"key.categories.gameplay");
	private static final Function<EnumHand,EnumHand>NORMAL=hand->hand;
	private static final Function<EnumHand,EnumHand>INVERSE=hand->hand==MAIN_HAND?OFF_HAND:MAIN_HAND;
	private static Function<EnumHand,EnumHand>mode=NORMAL;
	private static boolean swing=true,swingOnAir=true,swingOnBlocks=true,swingOnEntities=true;
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		ClientRegistry.registerKeyBinding(clickOffHand);
		EVENT_BUS.register(this);
	}
	@EventHandler
	public void init(FMLInitializationEvent event){
		SimpleClientCommandTree swingCommand=new SimpleClientCommandTree("swing");
		SimpleClientCommandTree modeCommand=new SimpleClientCommandTree("mode");
		modeCommand.addSubcommand(new SimpleClientCommand("normal",args->setMode("normal",NORMAL)));
		modeCommand.addSubcommand(new SimpleClientCommand("inverse",args->setMode("inverse",INVERSE)));
		modeCommand.addSubcommand(new SimpleClientCommand("random",args->setMode("random",RANDOM)));
		modeCommand.addSubcommand(new SimpleClientCommand("switch",args->setMode("switch",SWITCH)));
		modeCommand.addSubcommand(new SimpleClientCommand("timed",args->setMode("timed",TIMED_SWITCH)));
		swingCommand.addSubcommand(modeCommand);
		SimpleClientCommandTree toggleCommand=new SimpleClientCommandTree("toggle");
		toggleCommand.addSubcommand(new SimpleClientCommand("swing",args->
			sendMessage("Swing "+stateToString(swing=!swing))));
		toggleCommand.addSubcommand(new SimpleClientCommand("miss",args->
			sendMessage("Swing on air "+stateToString(swingOnAir=!swingOnAir))
		));
		toggleCommand.addSubcommand(new SimpleClientCommand("entities",args->
			sendMessage("Swing on entities "+stateToString(swingOnEntities=!swingOnEntities))
		));
		toggleCommand.addSubcommand(new SimpleClientCommand("blocks",args->
			sendMessage("Swing on blocks "+stateToString(swingOnBlocks=!swingOnBlocks))
		));
		swingCommand.addSubcommand(toggleCommand);
		ClientCommandHandler.instance.registerCommand(swingCommand);
	}
	@SubscribeEvent
	public void mouseInput(MouseInputEvent event){
		if(clickOffHand.isPressed()){
			Minecraft minecraft=Minecraft.getMinecraft();
			if(minecraft.objectMouseOver!=null&&minecraft.objectMouseOver.typeOfHit==BLOCK){
				BlockPos pos=minecraft.objectMouseOver.getBlockPos();
				if(!minecraft.world.isAirBlock(pos))
					minecraft.playerController.clickBlock(pos,minecraft.objectMouseOver.sideHit);
			}
			((ISwingAnimator)minecraft.player).animateSwinging(OFF_HAND);
		}
	}
	public static void setMode(String msg,Function<EnumHand,EnumHand>mode){
		SwingAnimationsControl.mode=mode;
		sendMessage("Swing mode changed to "+msg);
	}
	public static void sendMessage(String msg){
		Minecraft.getMinecraft().player.sendMessage(new TextComponentString(msg));
	}
	public static String stateToString(boolean state){
		return state?"enabled":"disabled";
	}
	public static boolean doSwing(){
		if(!swing)
			return false;
		RayTraceResult res=Minecraft.getMinecraft().objectMouseOver;
		return 
			(swingOnAir&&res.typeOfHit==MISS)||
			(swingOnBlocks&&res.typeOfHit==BLOCK)||
			(swingOnEntities&&res.typeOfHit==ENTITY);
	}
	public static EnumHand getHand(EnumHand hand){
		return mode.apply(hand);
	}
}