package com.palette.repository;

import com.palette.domain.post.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    @Transactional
    @Modifying
    @Query("delete from Photo p where p.post.id = :id")
    void deleteAllPhotoByPostId(@Param("id") Long id);
}
