package com.sstep.demo.checklist.service;

import com.sstep.demo.category.domain.Category;
import com.sstep.demo.checklist.CheckListRepository;
import com.sstep.demo.checklist.domain.CheckList;
import com.sstep.demo.checklist.dto.CheckListRequestDto;
import com.sstep.demo.checklist.dto.CheckListResponseDto;
import com.sstep.demo.photo.PhotoRepository;
import com.sstep.demo.photo.domain.Photo;
import com.sstep.demo.photo.dto.PhotoResponseDto;
import com.sstep.demo.photo.service.PhotoService;
import com.sstep.demo.staff.StaffRepository;
import com.sstep.demo.staff.domain.Staff;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CheckListService {
    private final CheckListRepository checkListRepository;
    private final StaffRepository staffRepository;
    private final PhotoService photoService;
    private final PhotoRepository photoRepository;

    public void saveCheckList(Long staffId, CheckListRequestDto checkListRequestDto) {
        Staff staff = getStaff(staffId);
        Category c = new Category();

        Set<CheckList> checkLists = getCheckListsByStaffId(staffId);
        CheckList checkList = CheckList.builder()
                .contents(checkListRequestDto.getContents())
                .endDay(checkListRequestDto.getEndDay())
                .id(checkListRequestDto.getId())
                .isComplete(checkListRequestDto.isComplete())
                .memo(checkListRequestDto.getMemo())
                .title(checkListRequestDto.getTitle())
                .date(checkListRequestDto.getDate())
                .needPhoto(checkListRequestDto.isNeedPhoto())
                .photos(new HashSet<>())
                .checkListManagers(new HashSet<>())
                .categories(new HashSet<>())
                .build();

        checkList.setStaff(staff);
        checkListRepository.save(checkList);

        c.setCheckList(checkList);
        c.setStore(staff.getStore());

        checkLists.add(checkList);
        staff.setCheckLists(checkLists);
        staffRepository.save(staff);
    }


    public Set<CheckListResponseDto> getCompleteCheckListsByCategory(Long storeId, CheckListRequestDto checkListRequestDto) {
        Set<CheckListResponseDto> checkLists = new HashSet<>();
        for (CheckList findCheckList : checkListRepository.findCheckListByStoreIdAndCategoryAndIsCompleteAndDate(storeId, checkListRequestDto.getCategoryName(), checkListRequestDto.getDate())) {
            CheckListResponseDto checkList = CheckListResponseDto.builder()
                    .title(findCheckList.getTitle())
                    .contents(findCheckList.getContents())
                    .endDay(findCheckList.getEndDay())
                    .id(findCheckList.getId())
                    .date(findCheckList.getDate())
                    .needPhoto(findCheckList.isNeedPhoto())
                    .isComplete(findCheckList.isComplete())
                    .memo(findCheckList.getMemo())
                    .build();

            checkLists.add(checkList);
        }
        return checkLists;
    }

    public Set<CheckListResponseDto> getUnCompletedCheckListsByCategory(Long storeId, CheckListRequestDto checkListRequestDto) {
        Set<CheckListResponseDto> checkLists = new HashSet<>();
        for (CheckList findCheckList : checkListRepository.findCheckListByStoreIdAndCategoryAndIsUnCompleteAndDate(storeId, checkListRequestDto.getCategoryName(), checkListRequestDto.getDate())) {
            CheckListResponseDto checkList = CheckListResponseDto.builder()
                    .title(findCheckList.getTitle())
                    .contents(findCheckList.getContents())
                    .endDay(findCheckList.getEndDay())
                    .id(findCheckList.getId())
                    .needPhoto(findCheckList.isNeedPhoto())
                    .isComplete(findCheckList.isComplete())
                    .memo(findCheckList.getMemo())
                    .build();
            checkLists.add(checkList);
        }
        return checkLists;
    }

    public CheckListResponseDto getCheckList(Long checklistId) {
        Set<PhotoResponseDto> photos = new HashSet<>();
        for (Photo photo : photoRepository.findPhotosByCheckListId(checklistId)) {
            PhotoResponseDto p = PhotoResponseDto.builder()
                    .contentType(photo.getContentType())
                    .data(photo.getData())
                    .fileName(photo.getFileName())
                    .id(photo.getId())
                    .build();

            photos.add(p);
        }


        CheckList findCheckList = checkListRepository.findById(checklistId).orElseThrow();
        return CheckListResponseDto.builder()
                .memo(findCheckList.getMemo())
                .id(findCheckList.getId())
                .endDay(findCheckList.getEndDay())
                .contents(findCheckList.getContents())
                .title(findCheckList.getTitle())
                .isComplete(findCheckList.isComplete())
                .needPhoto(findCheckList.isNeedPhoto())
                .photoResponseDto(photos)
                .build();
    }

    public void completeCheckList(Long staffId, Long checklistId, CheckListRequestDto checkListRequestDto) throws IOException {
        Staff staff = getStaff(staffId);

        Set<CheckList> checkLists = getCheckListsByStaffId(staffId);
        CheckList checkList = checkListRepository.findById(checklistId).orElseThrow();
        if (Arrays.stream(checkListRequestDto.getMultipartFiles()).findAny().isPresent()) {
            for (MultipartFile imageFile : checkListRequestDto.getMultipartFiles()) {
                Set<Photo> photos = new HashSet<>();
                Photo photo = photoService.savePhoto(imageFile);
                photoRepository.save(photo);
                photos.add(photo);
                checkList.setPhotos(photos);
            }
        }
        checkList.setMemo(checkListRequestDto.getMemo());
        checkList.setComplete(true);
        checkListRepository.save(checkList);

        checkLists.add(checkList);
        staff.setCheckLists(checkLists);
        staffRepository.save(staff);
    }

    private Set<CheckList> getCheckListsByStaffId(Long staffId) {
        return checkListRepository.findCheckListsByStaffId(staffId);
    }

    private Staff getStaff(Long staffId) {
        return staffRepository.findById(staffId).orElseThrow();
    }
}
