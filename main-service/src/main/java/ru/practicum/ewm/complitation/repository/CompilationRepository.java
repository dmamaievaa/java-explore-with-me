package ru.practicum.ewm.complitation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.complitation.model.Compilation;

import java.util.List;


public interface CompilationRepository extends JpaRepository<Compilation, Integer> {
    List<Compilation> findAllByPinned(boolean pinned, Pageable pageable);
}
