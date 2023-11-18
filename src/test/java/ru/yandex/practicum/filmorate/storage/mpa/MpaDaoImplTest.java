package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MpaDaoImplTest {
    private final JdbcTemplate jdbcTemplate;
    private MpaDao mpaDao;
    private Mpa mpa;

    @BeforeEach
    void setUp() {
        mpaDao = new MpaDaoImpl(jdbcTemplate);
        mpa = Mpa.builder()
                .id(1)
                .name("G")
                .build();
    }

    @Test
    void testFindAll() {
        assertThat(mpaDao.findAll())
                .isNotNull();
    }

    @Test
    void testFindById() {
        assertThat(mpaDao.findById(mpa.getId()))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(mpa);
    }

    @Test
    void testFindByIdWhenIdIsUnknown() {
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> mpaDao.findById(9999)
        );
    }

    @Test
    void testFindByIdWithNullableId() {
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> mpaDao.findById(null)
        );
    }
}