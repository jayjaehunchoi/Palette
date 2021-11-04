package com.palette.service;

import com.palette.domain.group.Group;
import com.palette.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    public void updateGroupName(Long id, String updateName){
        Optional<Group> group = groupRepository.findById(id);
        group.ifPresent(selectGroup ->{
            selectGroup.updateGroupName(updateName);
        });
    }
}
