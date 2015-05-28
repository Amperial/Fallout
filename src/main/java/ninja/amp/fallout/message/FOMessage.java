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
 * Messages in Fallout.
 */
public enum FOMessage {
    PREFIX("Prefix", "&c[&eFallout&c] &e"),
    RELOAD("Reload", "Reloaded Fallout."),

    COMMAND_NOTAPLAYER("Command.NotAPlayer", "&4You must be a player to use this command."),
    COMMAND_NOPERMISSION("Command.NoPermission", "&4You do not have permission to use this command."),
    COMMAND_INVALID("Command.Invalid", "&4%s is not a sub command of %s."),
    COMMAND_USAGE("Command.Usage", "&4Usage: %s"),

    ERROR_NUMBERFORMAT("Error.NumberFormat", "&4Value must be a positive integer."),
    ERROR_BOOLEANFORMAT("Error.BooleanFormat", "&4Value must be true or false."),
    ERROR_MODIFIERSYNTAX("Error.ModifierSyntax", "&4Modifier must be appended with +integer or -integer."),
    ERROR_ALLOPTIONS("Error.AllOptions", "&4All options must be selected."),

    CHARACTER_CREATE("Character.Create", "Created a character named %s."),
    CHARACTER_DELETE("Character.Delete", "Deleted your character."),
    CHARACTER_POSSESS("Character.Possess", "Possessed the character named %s."),
    CHARACTER_ABANDON("Character.Abandon", "Abandoned your character."),
    CHARACTER_NAME("Character.Name", "%s's real name is %s."),
    CHARACTER_UPGRADE("Character.Upgrade", "Upgraded %s to level %s."),
    CHARACTER_UPGRADED("Character.Upgraded", "Your character was upgraded to level %s."),
    CHARACTER_MAXLEVEL("Character.MaxLevel", "%s is already max level."),
    CHARACTER_ALREADYEXISTS("Character.AlreadyExists", "&4You already have a character!"),
    CHARACTER_ALREADYOWNED("Character.AlreadyOwned", "&4A character of that name is already possessed!"),
    CHARACTER_NAMETAKEN("Character.NameTaken", "&4A character of that name already exists!"),
    CHARACTER_NOTOWNER("Character.NotOwner", "&4You do not have a character!"),
    CHARACTER_DOESNTEXIST("Character.DoesntExist", "&4A character of that name doesn't exist or isn't loaded!"),

    RACE_DOESNTEXIST("Race.DoesntExist", "&4The race %s doesn't exist!"),
    RACE_ONLYLEATHER("Race.OnlyLeather", "&4Your race can only wear leather armor!"),

    SPECIAL_SET("Special.Set", "Set %s's SPECIAL."),
    SPECIAL_LIST("Special.List", "%s's SPECIAL is %s."),
    SPECIAL_INVALID("Special.Invalid", "Invalid SPECIAL for your race."),

    SKILLS_CONFIRM("Skills.Confirm", "Confirmed Skill Allocation."),
    SKILLS_RESET("Skills.Reset", "Reset %s's Skill levels."),
    SKILLS_RESETTED("Skills.Resetted", "Your character's Skill levels were reset."),

    PERKS_CONFIRM("Perks.Confirm", "Confirmed Perk Selection."),
    PERKS_RESET("Perks.Reset", "Reset %s's Perks."),
    PERKS_RESETTED("Perks.Resetted", "Your character's Perks were reset."),
//    PERK_LEARN("Perk.Learn", "Learned perk %s."),
//    PERK_FORGET("Perk.Forget", "Forgot perk %s."),
//    PERK_TEACH("Perk.Teach", "Taught perk %s to %s."),
//    PERK_UNTEACH("Perk.UnTeach", "Untaught perk %s from %s."),
//    PERK_LIST("Perk.List", "%s's perks are: %s."),
//    PERK_DOESNTEXIST("Perk.DoesntExist", "&4The perk %s doesn't exist!"),
//    PERK_ALREADYTAUGHT("Perk.AlreadyTaught", "&4%s already knows that perk."),
//    PERK_NOTTAUGHT("Perk.NotTaught", "&4%s doesn't know that perk."),
//    PERK_NOPERKS("Perk.NoPerks", "&4%s has no perks."),
//    PERK_MISSINGREQUIRED("Perk.MissingRequired", "&4You are missing the required perk %s."),

    ROLL_MESSAGE("Roll.Message", "You rolled a %s in %s with a modifier of %s. %s!"),
    ROLL_BROADCAST("Roll.Broadcast", "%s rolled a %s in %s with a modifier of %s. %s!"),
    ROLL_DICEMESSAGE("Roll.DiceMessage", "You rolled a %s. %s: %s"),
    ROLL_DICEBROADCAST("Roll.DiceBroadcast", "%s rolled a %s. %s: %s"),
    ROLL_ARMORMESSAGE("Roll.ArmorMessage", "You attempt to block %s damage. %s!"),
    ROLL_ARMORBROADCAST("Roll.ArmorBroadcast", "%s attempts to block %s damage. %s!"),
    ROLL_CANTROLL("Roll.CantRoll", "&4%s is not a Skill or SPECIAL trait."),
    ROLL_DICEAMOUNT("Roll.DiceAmount", "&4Cannot roll this many dice at once!"),
    ROLL_DICESIDES("Roll.DiceSides", "&4Dice cannot have this many sides!");

    private String message;
    private final String path;
    private final String defaultMessage;

    private FOMessage(String path, String defaultMessage) {
        this.message = defaultMessage;
        this.path = path;
        this.defaultMessage = defaultMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public String getDefault() {
        return defaultMessage;
    }

    public String toString() {
        return message;
    }
}
