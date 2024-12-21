package cleaners;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RekrutCleanerTest {

    private final ObjectMapper objectMapper = mock(ObjectMapper.class);


    @Test
    public void testCleanEducationLevel() {
        // Valid test cases
        assertEquals("Bac+3", RekrutCleaner.cleanEducationLevel("Bac+3"));
        assertEquals("Bac", RekrutCleaner.cleanEducationLevel("Bac"));
        assertEquals("Bac+5", RekrutCleaner.cleanEducationLevel("Bac+5"));

        // Invalid or empty test cases
        assertEquals("", RekrutCleaner.cleanEducationLevel(" "));
        assertEquals("", RekrutCleaner.cleanEducationLevel(null));
        assertEquals("", RekrutCleaner.cleanEducationLevel("Master"));
    }

    @Test
    public void testCleanContractType() {
        // Valid contract types
        assertEquals("CDI", RekrutCleaner.cleanContractType("CDI - Full time"));
        assertEquals("CDD", RekrutCleaner.cleanContractType("CDD"));
        assertEquals("Stage", RekrutCleaner.cleanContractType("Stage - Internship"));

        // Empty or null contract types
        assertEquals("", RekrutCleaner.cleanContractType(""));
        assertEquals("", RekrutCleaner.cleanContractType(null));
    }

    @Test
    public void testCleanTelework() {
        // Valid telework values
        assertEquals("Télétravail", RekrutCleaner.cleanTelework("Télétravail possible"));
        assertEquals("Télétravail", RekrutCleaner.cleanTelework("Télétravail à 100%"));

        // Invalid or empty telework values
        assertEquals("", RekrutCleaner.cleanTelework(""));
        assertEquals("", RekrutCleaner.cleanTelework(null));
    }

    @Test
    public void testCleanExperience() {
        // Valid experience values
        assertEquals("2", RekrutCleaner.cleanExperience("2 ans d'expérience"));
        assertEquals("5", RekrutCleaner.cleanExperience("5+ years of experience"));

        // Invalid or empty experience values
        assertEquals("", RekrutCleaner.cleanExperience(""));
        assertEquals("", RekrutCleaner.cleanExperience(null));
        assertEquals("", RekrutCleaner.cleanExperience("Aucune expérience"));
    }
}
