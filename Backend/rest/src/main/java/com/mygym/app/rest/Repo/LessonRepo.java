package com.mygym.app.rest.Repo;

import com.mygym.app.rest.Models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface LessonRepo extends JpaRepository<Lesson, Long> {
    @Query("SELECT l FROM Lesson l WHERE l.date > :currentDate OR (l.date = :currentDate AND l.endTime > :currentTime) ORDER BY l.date ASC, l.startTime ASC")
    List<Lesson> findAllUpcomingLessons(@Param("currentDate") LocalDate currentDate, @Param("currentTime") LocalTime currentTime);
}

