package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {
    private final MpaDao mpaDao;

    @Override
    public List<Mpa> findAll() {
        return mpaDao.findAll();
    }

    @Override
    public Mpa findById(Integer id) {
        return mpaDao.findById(id);
    }
}
