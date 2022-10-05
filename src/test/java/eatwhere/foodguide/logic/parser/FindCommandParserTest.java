package eatwhere.foodguide.logic.parser;

import static eatwhere.foodguide.logic.parser.CommandParserTestUtil.assertParseFailure;
import static eatwhere.foodguide.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import eatwhere.foodguide.commons.core.Messages;
import eatwhere.foodguide.logic.commands.FindCommand;
import eatwhere.foodguide.model.person.NameContainsKeywordsPredicate;
import org.junit.jupiter.api.Test;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);
    }

}
