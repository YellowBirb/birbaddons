package yellowbirb.birbaddons.client;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;


public class BirbAddonsClient implements ClientModInitializer {

	public static float x = 0F;
	public static float y = 0F;
	public static float z = 0F;
	public static float tx = 0F;
	public static float ty = 0F;
	public static float tz = 0F;

	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
				dispatcher.register(ClientCommands.literal("turn")
						.then(ClientCommands.argument("axis", StringArgumentType.string())
								.then(ClientCommands.argument("value", FloatArgumentType.floatArg())
										.executes(BirbAddonsClient::blablabla))))
		);

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
				dispatcher.register(ClientCommands.literal("turnrel")
						.then(ClientCommands.argument("axis", StringArgumentType.string())
								.then(ClientCommands.argument("value", FloatArgumentType.floatArg())
										.executes(BirbAddonsClient::blablabla2))))
		);

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
				dispatcher.register(ClientCommands.literal("turnreset").executes((context) -> {
					x = 0F;
					y = 0F;
					z = 0F;
					tx = 0F;
					ty = 0F;
					tz = 0F;
					return 1;
				}))
		);

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
				dispatcher.register(ClientCommands.literal("turnoutput").executes((context) -> {
					Minecraft.getInstance().player.sendSystemMessage(Component.literal("x: " + x));
					Minecraft.getInstance().player.sendSystemMessage(Component.literal("y: " + y));
					Minecraft.getInstance().player.sendSystemMessage(Component.literal("z: " + z));
					Minecraft.getInstance().player.sendSystemMessage(Component.literal("tx: " + tx));
					Minecraft.getInstance().player.sendSystemMessage(Component.literal("ty: " + ty));
					Minecraft.getInstance().player.sendSystemMessage(Component.literal("tz: " + tz));
					return 1;
				}))
		);
	}

	private static int blablabla(CommandContext<FabricClientCommandSource> context) {
		String axis = StringArgumentType.getString(context, "axis");
		float value = FloatArgumentType.getFloat(context, "value");

		switch (axis) {
			case "x" -> x = value;
			case "y" -> y = value;
			case "z" -> z = value;
			case "tx" -> tx = value;
			case "ty" -> ty = value;
			case "tz" -> tz = value;
		}

		return 1;
	}

	private static int blablabla2(CommandContext<FabricClientCommandSource> context) {
		String axis = StringArgumentType.getString(context, "axis");
		float value = FloatArgumentType.getFloat(context, "value");

		switch (axis) {
			case "x" -> x += value;
			case "y" -> y += value;
			case "z" -> z += value;
			case "tx" -> tx += value;
			case "ty" -> ty += value;
			case "tz" -> tz += value;
		}

		return 1;
	}
}