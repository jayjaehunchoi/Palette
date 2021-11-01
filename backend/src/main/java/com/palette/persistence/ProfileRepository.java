package com.palette.persistence;

import com.palette.domain.member.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, String> {

}
