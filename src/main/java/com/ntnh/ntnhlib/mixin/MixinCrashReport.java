package com.ntnh.ntnhlib.mixin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.crash.CrashReport;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrashReport.class)
public abstract class MixinCrashReport {

    @Inject(method = "getWittyComment", at = @At("HEAD"), cancellable = true)
    private static void onGetWittyComment(CallbackInfoReturnable<String> cir) {
        List<String> comments = new ArrayList<>(Arrays.asList(
                "Who set us up the TNT?",
                "Everything's going to plan. No, really, that was supposed to happen.",
                "Uh... Did I do that?",
                "Oops.",
                "Why did you do that?",
                "I feel sad now :(",
                "My bad.",
                "I'm sorry, Dave.",
                "I let you down. Sorry :(",
                "On the bright side, I bought you a teddy bear!",
                "Daisy, daisy...",
                "Oh - I know what I did wrong!",
                "Hey, that tickles! Hehehe!",
                "I blame Dinnerbone.",
                "You should try our sister game, Minceraft!",
                "Don't be sad. I'll do better next time, I promise!",
                "Don't be sad, have a hug! <3",
                "I just don't know what went wrong :(",
                "Shall we play a game?",
                "Quite honestly, I wouldn't worry myself about that.",
                "I bet Cylons wouldn't have this problem.",
                "Sorry :(",
                "Surprise! Haha. Well, this is awkward.",
                "Would you like a cupcake?",
                "Hi. I'm Minecraft, and I'm a crashaholic.",
                "Ooh. Shiny.",
                "This doesn't make any sense!",
                "Why is it breaking :(",
                "Don't do that.",
                "Ouch. That hurt :(",
                "You're mean.",
                "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]",
                "There are four lights!",
                "But it works on my machine."
        ));

        comments.add("Have you tried turning off the nuclear reactor and on again?");
        comments.add("The uranium pile was NOT supposed to do that.");
        comments.add("This crash has been logged and forwarded to /dev/null.");
        comments.add("Blame it on the creepers. It's always the creepers.");
        comments.add("Error 404: stable build not found.");
        comments.add("Your computer is fine. Probably.");
        comments.add("NTM: because who needs a stable game anyway.");
        comments.add("At least it didn't explode. Or did it?");

        Random random = new Random();
        String chosen = comments.get(random.nextInt(comments.size()));
        cir.setReturnValue(chosen);
    }
}
