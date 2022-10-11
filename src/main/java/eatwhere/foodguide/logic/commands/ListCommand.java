package eatwhere.foodguide.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import eatwhere.foodguide.model.Model;

/**
 * Lists all eateries in the food guide to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all eateries";

    public static final String MESSAGE_HELP = "list - Lists all eateries.\n"
            + "Usage: list [-h]";

    /** Help message should be displayed in the response box. */
    private final boolean DisplayHelp;

    public ListCommand() {
        this.DisplayHelp = false;
    }
    public ListCommand(boolean DisplayHelp) {
        this.DisplayHelp = DisplayHelp;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        if(DisplayHelp) {
            return new CommandResult(MESSAGE_HELP);
        }

        model.updateFilteredEateryList(Model.PREDICATE_SHOW_ALL_EATERIES);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
