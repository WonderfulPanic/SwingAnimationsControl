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

import java.util.function.Consumer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class SimpleClientCommand extends CommandBase{
	private final String name;
	private final Consumer<String[]>func;
	public SimpleClientCommand(String name,Consumer<String[]>func){
		this.name=name;
		this.func=func;
	}
	@Override
	public String getName(){
		return name;
	}
	@Override
	public void execute(MinecraftServer server,ICommandSender sender,String[]args){
		func.accept(args);
	}
	@Override
	public String getUsage(ICommandSender sender){
		return null;
	}
	@Override
	public int getRequiredPermissionLevel(){
		return 0;
	}
}