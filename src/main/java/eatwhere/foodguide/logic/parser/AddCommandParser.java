package eatwhere.foodguide.logic.parser;

import static eatwhere.foodguide.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static eatwhere.foodguide.logic.parser.CliSyntax.PREFIX_EMAIL;
import static eatwhere.foodguide.logic.parser.CliSyntax.PREFIX_NAME;
import static eatwhere.foodguide.logic.parser.CliSyntax.PREFIX_PHONE;
import static eatwhere.foodguide.logic.parser.CliSyntax.PREFIX_TAG;
import static eatwhere.foodguide.logic.parser.ParserUtil.arePrefixesPresent;

import java.util.Set;
import java.util.stream.Stream;

import eatwhere.foodguide.commons.core.Messages;
import eatwhere.foodguide.logic.commands.AddCommand;
import eatwhere.foodguide.logic.parser.exceptions.ParseException;
import eatwhere.foodguide.model.eatery.Eatery;
import eatwhere.foodguide.model.eatery.Email;
import eatwhere.foodguide.model.eatery.Location;
import eatwhere.foodguide.model.eatery.Name;
import eatwhere.foodguide.model.eatery.Phone;
import eatwhere.foodguide.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Location location = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Eatery eatery = new Eatery(name, phone, email, location, tagList);

        return new AddCommand(eatery);
    }

}
