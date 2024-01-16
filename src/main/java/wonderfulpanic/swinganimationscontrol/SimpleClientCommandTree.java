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

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class SimpleClientCommandTree extends CommandTreeBase{
	private final String name;
	public SimpleClientCommandTree(String name){
		this.name=name;
	}
	@Override
	public String getName(){
		return name;
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