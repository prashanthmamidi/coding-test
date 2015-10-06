package uk.sky;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static uk.sky.DataFilterer.filterByCountry;
import static uk.sky.DataFilterer.filterByCountryWithResponseTimeAboveLimit;

public class DataFiltererTest {

    public static final String EMPTY_FILE = "src/test/resources/empty";
    public static final String SINGLE_LINE_FILE = "src/test/resources/single-line";
    public static final String MULTI_LINE_FILE = "src/test/resources/multi-lines";
    public static final String GB = "GB";
    public static final String US = "US";
    public static final String UNKNOWN = "UNKNOWN";

    @Test
    public void whenEmpty() throws FileNotFoundException {
        assertTrue(filterByCountry(openFile(EMPTY_FILE), GB).isEmpty());
    }

    @Test
    public void should_return_filtered_log_extract_for_a_valid_country_code_whenSingleLine() throws FileNotFoundException {
        Collection<?> expectedLogExtracts = Arrays.asList(new LogExtract(1431592497L, GB, 200L));

        Collection<?> actualLogExtracts = filterByCountry(openFile(SINGLE_LINE_FILE), GB);

        assertThat(actualLogExtracts.size(), is(1));
        assertEquals(actualLogExtracts, expectedLogExtracts);
    }

    @Test
    public void should_not_return_filtered_log_extract_for_a_invalid_country_code_whenSingleLine() throws FileNotFoundException {
        Collection<?> actualLogExtracts = filterByCountry(openFile(SINGLE_LINE_FILE), UNKNOWN);

        assertThat(actualLogExtracts.size(), is(0));
    }

    @Test
    public void should_return_filtered_log_extract_for_a_valid_country_code_whenMultipleLine() throws FileNotFoundException {
        Collection<?> expectedLogExtracts = Arrays.asList(new LogExtract(1433190845L,US,539L), new LogExtract(1433666287L,US,789L), new LogExtract(1432484176L,US,850L));

        Collection<?> actualLogExtracts = filterByCountry(openFile(MULTI_LINE_FILE), US);

        assertThat(actualLogExtracts.size(), is(3));
        assertEquals(actualLogExtracts, expectedLogExtracts);
    }

    @Test
    public void should_not_return_filtered_log_extract_for_a_invalid_country_code_whenMultipleLine() throws FileNotFoundException {
        Collection<?> actualLogExtracts = filterByCountry(openFile(MULTI_LINE_FILE), UNKNOWN);

        assertThat(actualLogExtracts.size(), is(0));
    }

    @Test
    public void should_return_filtered_log_extract_greater_than_for_a_given_response_time_limit_whenEmpty() throws FileNotFoundException {
        assertTrue(filterByCountryWithResponseTimeAboveLimit(openFile(EMPTY_FILE), GB, 100).isEmpty());
    }

    @Test
    public void should_return_filtered_log_extract_greater_than_for_a_given_response_time_limit_whenSingleLine()throws FileNotFoundException {
        Collection<?> expectedLogExtracts = Arrays.asList(new LogExtract(1431592497L, GB, 200L));

        Collection<?> actualLogExtracts = filterByCountryWithResponseTimeAboveLimit(openFile(SINGLE_LINE_FILE), GB, 199);

        assertThat(actualLogExtracts.size(), is(1));
        assertEquals(actualLogExtracts, expectedLogExtracts);
    }

    @Test
    public void should_return_filtered_log_extract_greater_than_for_a_given_response_time_limit_whenMultipleLine()throws FileNotFoundException {
        Collection<?> expectedLogExtracts = Arrays.asList(new LogExtract(1433666287L,US,789L), new LogExtract(1432484176L,US,850L));

        Collection<?> actualLogExtracts = filterByCountryWithResponseTimeAboveLimit(openFile(MULTI_LINE_FILE), US, 550);

        assertThat(actualLogExtracts.size(), is(2));
        assertEquals(actualLogExtracts, expectedLogExtracts);
    }


    private FileReader openFile(String filename) throws FileNotFoundException {
        return new FileReader(new File(filename));
    }


}
