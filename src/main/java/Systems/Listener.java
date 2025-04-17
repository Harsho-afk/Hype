package Systems;

import Commands.*;
import Commands.ConfigCommands.*;
import Commands.FunCommands.*;
import Commands.LogCommands.*;
import Commands.ModCommands.*;
import Events.BotEvents.*;
import Events.GuildMemberEvents.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class Listener extends ListenerAdapter {
    @Override
    public void onGenericEvent(GenericEvent event) {
        if (event instanceof ReadyEvent) {
            JDA jdaBuilder = event.getJDA();
            // commands
            jdaBuilder.addEventListener(new PingCommand());
            jdaBuilder.addEventListener(new HelpCommand());
            jdaBuilder.addEventListener(new PermissionCommand());
            jdaBuilder.addEventListener(new AvatarCommand());
            // ConfigCommands
            jdaBuilder.addEventListener(new SetPrefixCommand());
            // FunCommands
            jdaBuilder.addEventListener(new CashCommand());
            jdaBuilder.addEventListener(new CatCommand());
            jdaBuilder.addEventListener(new CoinFlipCommand());
            jdaBuilder.addEventListener(new DogCommand());
            jdaBuilder.addEventListener(new GiveCashCommand());
            jdaBuilder.addEventListener(new MemeCommand());
            jdaBuilder.addEventListener(new DailyCommand());
            // LogCommands
            jdaBuilder.addEventListener(new RemoveLogCommand());
            jdaBuilder.addEventListener(new SetEventsCommand());
            jdaBuilder.addEventListener(new SetLogCommand());
            // ModCommands
            jdaBuilder.addEventListener(new BanCommand());
            jdaBuilder.addEventListener(new CleanCommand());
            jdaBuilder.addEventListener(new KickCommand());
            jdaBuilder.addEventListener(new MuteCommand());
            jdaBuilder.addEventListener(new UnMuteCommand());
            // BotEvents
            jdaBuilder.addEventListener(new BotJoinEvent());
            jdaBuilder.addEventListener(new BotLeaveEvent());
            // GuildMemberEvents
            jdaBuilder.addEventListener(new MemberJoinEvent());
            jdaBuilder.addEventListener(new MemberLeaveEvent());
        }
    }
}
