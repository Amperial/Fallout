/*
 * This file is part of Fallout.
 *
 * Copyright (c) 2013-2015 <http://github.com/ampayne2/Fallout//>
 *
 * Fallout is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Fallout is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Fallout.  If not, see <http://www.gnu.org/licenses/>.
 */
package ninja.amp.fallout.message;

/**
 * Messages sent by the fallout plugin.
 *
 * @author Austin Payne
 */
public enum FOMessage implements Message {
    PREFIX("Prefix", "&8[&bFallout&8] &7"),
    PREFIX_ERROR("ErrorPrefix", "&8[&bFallout&8] &4"),
    RELOAD("Reload", "Reloaded %s."),

    COMMAND_NOTAPLAYER("Command.NotAPlayer", "You must be a player to use this command."),
    COMMAND_NOPERMISSION("Command.NoPermission", "You do not have permission to use this command."),
    COMMAND_INVALID("Command.Invalid", "Unknown commad. Type &b/fo help&4 for help."),
    COMMAND_USAGE("Command.Usage", "Usage: %s"),

    ERROR_NUMBERFORMAT("Error.NumberFormat", "Value must be a positive integer."),
    ERROR_BOOLEANFORMAT("Error.BooleanFormat", "Value must be true or false."),
    ERROR_MODIFIERSYNTAX("Error.ModifierSyntax", "Modifier must be be of the format +integer or -integer."),
    ERROR_NAMEFORMAT("Error.NameFormat", "Character names must be comprised of between 3 and 20 letters."),
    ERROR_CHARACTERLOAD("Error.CharacterLoad", "Failed to load character &b%1$s&4. %2$s"),
    ERROR_ALLOPTIONS("Error.AllOptions", "All options must be selected."),

    CHARACTER_CREATE("Character.Create", "Created a character named &b%s&7."),
    CHARACTER_DELETE("Character.Delete", "Deleted your character."),
    CHARACTER_POSSESS("Character.Possess", "Possessed &b%s&7."),
    CHARACTER_ABANDON("Character.Abandon", "Abandoned your character."),
    CHARACTER_NAME("Character.Name", "The player in possession of &b%1$s&7 is &b%2$s&7."),
    CHARACTER_UPGRADE("Character.Upgrade", "Upgraded &b%1$s&7 to level &b%2$s&7."),
    CHARACTER_UPGRADED("Character.Upgraded", "Your character was upgraded to level &b%s&7."),
    CHARACTER_MAXLEVEL("Character.MaxLevel", "&b%s&4 is already max level."),
    CHARACTER_ALREADYEXISTS("Character.AlreadyExists", "You already have a character!"),
    CHARACTER_ALREADYOWNED("Character.AlreadyOwned", "A character of that name is already possessed!"),
    CHARACTER_NAMETAKEN("Character.NameTaken", "A character of that name already exists!"),
    CHARACTER_NOTOWNER("Character.NotOwner", "You do not have a character!"),
    CHARACTER_DOESNTEXIST("Character.DoesntExist", "A character of that name doesn't exist or isn't loaded!"),

    RACE_ONLYLEATHER("Race.OnlyLeather", "Your race can only wear leather armor!"),

    SPECIAL_SET("Special.Set", "Set &b%s&7's SPECIAL."),
    SPECIAL_LIST("Special.List", "&b%1$s&7's SPECIAL is &b%2$s&7."),
    SPECIAL_INVALID("Special.Invalid", "Invalid SPECIAL for your race."),

    SKILLS_CONFIRM("Skills.Confirm", "Confirmed Skill Allocation."),
    SKILLS_RESET("Skills.Reset", "Reset &b%s&7's Skill levels."),
    SKILLS_RESETTED("Skills.Resetted", "Your character's Skill levels were reset."),

    PERKS_CONFIRM("Perks.Confirm", "Confirmed Perk Selection."),
    PERKS_RESET("Perks.Reset", "Reset &b%s&7's Perks."),
    PERKS_RESETTED("Perks.Resetted", "Your character's Perks were reset."),

    INFORMATION_LEARN("Information.Learn", "You have learned the secrets of &b%s&7."),
    INFORMATION_FORGET("Information.Forget", "You have forgotten the secrets of &b%s&7."),
    INFORMATION_TEACH("Information.Teach", "Taught &b%1$s&7 the secrets of &b%2$s&7."),
    INFORMATION_UNTEACH("Information.UnTeach", "Made &b%1$s&7 forget the secrets of &b%2$s&7."),
    INFORMATION_DOESNTEXIST("Information.DoesntExist", "The information &b%s&4 doesn't exist!"),
    INFORMATION_ALREADYTAUGHT("Information.AlreadyTaught", "&b%1$s&4 already knows the secrets of &b%2$s&4."),
    INFORMATION_NOTTAUGHT("Information.NotTaught", "&b%1$s&4 doesn't know the secrets of &b%2$s&4."),
    INFORMATION_MISSING("Information.Missing", "You are missing the required information &b%s&4."),

    FACTION_CREATE("Faction.Create", "Created a faction named &b%s&7."),
    FACTION_DISBAND("Faction.Disband", "Disbanded &b%s&7."),
    FACTION_JOIN("Faction.Join", "Joined &b%s&7."),
    FACTION_LEAVE("Faction.Leave", "Left &b%s&7."),
    FACTION_INVITE("Faction.Invite", "Invited &b%1$s&7 to join &b%2$s&7."),
    FACTION_KICK("Faction.Kick", "Kicked &b%1$s&7 from &b%2$s&7."),
    FACTION_KICKED("Faction.Kicked", "You were kicked from &b%s&7."),
    FACTION_NAMETAKEN("Faction.NameTaken", "A faction of that name already exists!"),
    FACTION_DOESNTEXIST("Faction.DoesntExist", "A faction of that name doesn't exist!"),

    ROLL_STANDARD_PRIVATE("Roll.Standard.Private", "You rolled &b%1$s&7&b%2$s&7.\\n%3$s&7: &b%4$s&7!"),
    ROLL_STANDARD_PUBLIC("Roll.Standard.Public", "&b%1$s&7 rolled &b%2$s&7&b%3$s&7.\\n%4$s&7: &b%5$s&7!"),
    ROLL_DICE_PRIVATE("Roll.Dice.Private", "You rolled &b%1$s&7. %2$s: &b%3$s&7"),
    ROLL_DICE_PUBLIC("Roll.Dice.Public", "&b%1$s&7 rolled &b%2$s&7. %3$s: &b%4$s&7"),
    ROLL_ARMOR_PRIVATE("Roll.Armor.Private", "You attempt to block %s damage. %s!"),
    ROLL_ARMOR_PUBLIC("Roll.Armor.Public", "%s attempts to block %s damage. %s!"),
    ROLL_CANTROLL("Roll.CantRoll", "%s is not a Skill or SPECIAL trait."),
    ROLL_DICEAMOUNT("Roll.DiceAmount", "Cannot roll this many dice at once!"),
    ROLL_DICESIDES("Roll.DiceSides", "Dice cannot have this many sides!"),

    RESULT_CRITICALFAILURE("Result.CriticalFailure", "&4Critical Failure"),
    RESULT_FAILURE("Result.Failure", "&cFailure"),
    RESULT_NEARSUCCESS("Result.NearSuccess", "&aNear Success"),
    RESULT_SUCCESS("Result.Success", "&aSuccess"),
    RESULT_CRITICALSUCCESS("Result.CriticalSuccess", "&2Critical Success");

    private final String path;
    private final String defaultMessage;
    private String message;

    FOMessage(String path, String defaultMessage) {
        this.message = defaultMessage;
        this.path = path;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getDefault() {
        return defaultMessage;
    }

    @Override
    public String toString() {
        return message;
    }

}
