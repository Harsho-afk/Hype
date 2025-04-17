package Harsho.Hype.Bot.Systems;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import Harsho.Hype.Bot.Database.GetData;
import Harsho.Hype.Bot.Database.UpdateData;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CoinFlipSys {
    public static CoinFlipSys INSTANCE;

    public final Map<Guild, Long> coinFlipManagers;

    public CoinFlipSys() {
        this.coinFlipManagers = new HashMap<>();
    }

    public static String coinFlipSys(Member member, String[] message, MessageReceivedEvent event) {
        CoinFlipSys.INSTANCE.coinFlipManagers.put(member.getGuild(), member.getIdLong());
        Random rand = new Random();
        int random = rand.nextInt(2);
        if (message.length < 2) {
            switch (random) {
                case 0:
                    return "Its Head";
                case 1:
                    return "Its Tails";
            }
        }
        switch (message[1].toLowerCase()) {
            case "heads":
                random = rand.nextInt(2);
                if (message.length < 3) {
                    switch (random) {
                        case 0:
                            UpdateData.updateAmount(event.getMember().getIdLong(),
                                    GetData.getAmount(event.getMember().getIdLong()));
                            return "Congratzz!! Its Heads";
                        case 1:
                            UpdateData.updateAmount(event.getMember().getIdLong(),
                                    GetData.getAmount(event.getMember().getIdLong()));
                            return "Get Rekt Its Tails";
                    }
                }
                int bet = 0;
                try {
                    bet = Integer.parseInt(message[2]);
                } catch (Exception ignored) {
                }
                if (bet > GetData.getAmount(event.getMember().getIdLong())) {
                    return "You do not have enough cash to bet";
                }
                switch (random) {
                    case 0:
                        UpdateData.updateAmount(event.getMember().getIdLong(),
                                GetData.getAmount(event.getMember().getIdLong()) + bet);
                        return "Congratzz!! Its Heads You won " + bet + " cash";
                    case 1:
                        UpdateData.updateAmount(event.getMember().getIdLong(),
                                GetData.getAmount(event.getMember().getIdLong()) - bet);
                        return "Get Rekt Its Tails You lose " + bet + " cash";
                }
            case "tails":
                random = rand.nextInt(2);
                if (message.length < 3) {
                    switch (random) {
                        case 0:
                            UpdateData.updateAmount(event.getMember().getIdLong(),
                                    GetData.getAmount(event.getMember().getIdLong()));
                            return "Congratzz!! Its Tails";
                        case 1:
                            UpdateData.updateAmount(event.getMember().getIdLong(),
                                    GetData.getAmount(event.getMember().getIdLong()));
                            return "Get Rekt Its Heads";
                    }
                }
                bet = 0;
                try {
                    bet = Integer.parseInt(message[2]);
                } catch (Exception ignored) {
                }
                if (bet > GetData.getAmount(event.getMember().getIdLong())) {
                    return "You do not have enough cash to bet";
                }
                switch (random) {
                    case 0:
                        UpdateData.updateAmount(event.getMember().getIdLong(),
                                GetData.getAmount(event.getMember().getIdLong()) + bet);
                        return "Congratzz!! Its Tails You won " + bet + " cash";
                    case 1:
                        UpdateData.updateAmount(event.getMember().getIdLong(),
                                GetData.getAmount(event.getMember().getIdLong()) - bet);
                        return "Get Rekt Its Heads You lose " + bet + " cash";
                }
        }
        return "Something went wrong, try again";
    }

    public static CoinFlipSys getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CoinFlipSys();
        }

        return INSTANCE;
    }
}
