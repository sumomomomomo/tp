package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MONTHPAID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOT_MONTHPAID;

import java.util.Arrays;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.ClassIdContainsKeywordsPredicate;
import seedu.address.model.person.MonthPaidContainsKeywordsPredicate;
import seedu.address.model.person.NameAndClassIdContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.NotMonthPaidContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * Accepts n/ for name and c/ for classId.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        // tokenize's args expects a preamble - we do not have a preamble!
        // which is why tests fail if userInput does not start with a single whitespace
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_CLASSID, PREFIX_MONTHPAID,
                PREFIX_NOT_MONTHPAID);
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_CLASSID, PREFIX_MONTHPAID, PREFIX_NOT_MONTHPAID);

        FindCommandType findCommandType = getFindCommandType(argMultimap);

        switch (findCommandType) {
        case NAMEANDCLASSID:
            return createNameAndClassIdFindCommand(argMultimap);
        case NAME:
            return createNameFindCommand(argMultimap);
        case CLASSID:
            return createClassIdFindCommand(argMultimap);
        case MONTHPAID:
            return createMonthsPaidFindCommand(argMultimap);
        case NOTMONTHPAID:
            return createNotMonthsPaidFindCommand(argMultimap);
        case NONE:
        default:
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
    }

    private FindCommandType getFindCommandType(ArgumentMultimap argMultiMap) {
        boolean isNamePresent = argMultiMap.getValue(PREFIX_NAME).isPresent();
        boolean isClassIdPresent = argMultiMap.getValue(PREFIX_CLASSID).isPresent();
        boolean isMonthPaidPresent = argMultiMap.getValue(PREFIX_MONTHPAID).isPresent();
        boolean isNotMonthPaidPresent = argMultiMap.getValue(PREFIX_NOT_MONTHPAID).isPresent();

        if (isMonthPaidPresent && !isNamePresent && !isClassIdPresent && !isNotMonthPaidPresent) {
            return FindCommandType.MONTHPAID;
        } else if (isNamePresent && isClassIdPresent && !isMonthPaidPresent && !isNotMonthPaidPresent) {
            return FindCommandType.NAMEANDCLASSID;
        } else if (isNamePresent && !isClassIdPresent && !isMonthPaidPresent && !isNotMonthPaidPresent) {
            return FindCommandType.NAME;
        } else if (!isNamePresent && isClassIdPresent && !isMonthPaidPresent && !isNotMonthPaidPresent) {
            return FindCommandType.CLASSID;
        } else if (isNotMonthPaidPresent && !isNamePresent && !isClassIdPresent && !isMonthPaidPresent) {
            return FindCommandType.NOTMONTHPAID;
        } else {
            return FindCommandType.NONE;
        }
    }

    private FindCommand createNameAndClassIdFindCommand(ArgumentMultimap argMultimap) throws ParseException {

        String[] nameKeywords = argMultimap.getValue(PREFIX_NAME).get().split("\\s+");
        String[] classIdKeywords = argMultimap.getValue(PREFIX_CLASSID).get().split("\\s+");
        if (nameKeywords[0].isEmpty() && classIdKeywords[0].isEmpty()) {
            throw new ParseException(FindCommand.EMPTY_SEARCH_VALUE_PROVIDED);
        }
        return new FindCommand(new NameAndClassIdContainsKeywordsPredicate(Arrays.asList(nameKeywords),
                Arrays.asList(classIdKeywords)));
    }

    private FindCommand createNameFindCommand(ArgumentMultimap argumentMultimap) throws ParseException {
        String[] nameKeywords = argumentMultimap.getValue(PREFIX_NAME).get().split("\\s+");
        if (nameKeywords[0].isEmpty() && nameKeywords[0].isEmpty()) {
            throw new ParseException(FindCommand.EMPTY_SEARCH_VALUE_PROVIDED);
        }
        return new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }

    private FindCommand createClassIdFindCommand(ArgumentMultimap argumentMultimap) throws ParseException {
        String[] classIdKeywords = argumentMultimap.getValue(PREFIX_CLASSID).get().split("\\s+");
        if (classIdKeywords[0].isEmpty()) {
            throw new ParseException(FindCommand.EMPTY_SEARCH_VALUE_PROVIDED);
        }
        return new FindCommand(new ClassIdContainsKeywordsPredicate(Arrays.asList(classIdKeywords)));
    }

    private FindCommand createMonthsPaidFindCommand(ArgumentMultimap argumentMultimap) throws ParseException {
        String[] monthPaidKeywords = argumentMultimap.getValue(PREFIX_MONTHPAID).get().split("\\s+");
        if (monthPaidKeywords[0].isEmpty()) {
            throw new ParseException(FindCommand.EMPTY_SEARCH_VALUE_PROVIDED);
        }
        return new FindCommand(new MonthPaidContainsKeywordsPredicate(Arrays.asList(monthPaidKeywords)));
    }

    private FindCommand createNotMonthsPaidFindCommand(ArgumentMultimap argumentMultimap) throws ParseException {
        String[] notMonthPaidKeywords = argumentMultimap.getValue(PREFIX_NOT_MONTHPAID).get().split("\\s+");
        if (notMonthPaidKeywords[0].isEmpty()) {
            throw new ParseException(FindCommand.EMPTY_SEARCH_VALUE_PROVIDED);
        }
        return new FindCommand(new NotMonthPaidContainsKeywordsPredicate(Arrays.asList(notMonthPaidKeywords)));
    }


}
