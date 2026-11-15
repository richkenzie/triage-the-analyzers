package team.projectpulse.student;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class StudentSpecsTest {

    private Locale originalLocale;

    @BeforeEach
    void saveLocale() {
        originalLocale = Locale.getDefault();
    }

    @AfterEach
    void restoreLocale() {
        Locale.setDefault(originalLocale); // don't leak Turkish into other tests
    }

    @Test
    @SuppressWarnings("unchecked")
    void firstNameSearchIsLowercasedTheSameOnATurkishLocaleServer() {
        Locale.setDefault(Locale.forLanguageTag("tr-TR"));

        Root<Student> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        StudentSpecs.containsFirstName("Ivan").toPredicate(root, query, cb);

        ArgumentCaptor<String> pattern = ArgumentCaptor.forClass(String.class);
        verify(cb).like(any(), pattern.capture());
        // The database lowercases "Ivan" to "ivan"; the search text must match.
        assertThat(pattern.getValue()).isEqualTo("%ivan%");
    }
}