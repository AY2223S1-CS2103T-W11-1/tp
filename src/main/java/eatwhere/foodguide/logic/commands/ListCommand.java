package eatwhere.foodguide.logic.commands;

import static java.util.Objects.requireNonNull;

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
    private final boolean isDisplayHelp;

    public ListCommand() {
        this.isDisplayHelp = false;
    }
    public ListCommand(boolean isDisplayHelp) {
        this.isDisplayHelp = isDisplayHelp;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        if (isDisplayHelp) {
            return new CommandResult(MESSAGE_HELP);
        }

        model.updateFilteredEateryList(Model.PREDICATE_SHOW_ALL_EATERIES);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
