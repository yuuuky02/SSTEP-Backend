package com.sstep.demo.checklist.domain;

import com.sstep.demo.category.domain.Category;
import com.sstep.demo.checklistmanager.domain.CheckListManager;
import com.sstep.demo.photo.domain.Photo;
import com.sstep.demo.schedule.domain.Schedule;
import com.sstep.demo.staff.domain.Staff;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
public class CheckList {
    @Id //기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; //체크 리스트 고유번호
    private String title; //체크 리스트 제목
    private String contents; //체크 리스트 내용
    private Date endDay; //체크 리스트 마감 일자
    private boolean needPhoto; //체크 리스트 사진 필수 여부
    private boolean isComplete; //체크 리스트 완료 여부
    private String memo; //체크 리스트 완료 시 메모

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public Date getEndDay() {
        return endDay;
    }

    public boolean isNeedPhoto() {
        return needPhoto;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos.clear();
        if (photos != null) {
            this.photos.addAll(photos);
            photos.forEach(photo -> photo.setCheckList(this));
        }
    }

    public void setCategories(List<Category> categories) {
        this.categories.clear();
        if (categories != null) {
            this.categories.addAll(categories);
            categories.forEach(category -> category.setCheckList(this));
        }
    }

    public void setCheckListManagers(List<CheckListManager> checkListManagers) {
        this.checkListManagers.clear();
        if (checkListManagers != null) {
            this.checkListManagers.addAll(checkListManagers);
            checkListManagers.forEach(checkListManager -> checkListManager.setCheckList(this));
        }
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    @ManyToOne
    private Staff staff;
    @OneToMany(mappedBy = "checklist", cascade = CascadeType.REMOVE)
    private List<Photo> photos;
    @OneToMany(mappedBy = "checklist", cascade = CascadeType.REMOVE)
    private List<Category> categories;
    @OneToMany(mappedBy = "checklist", cascade = CascadeType.REMOVE)
    private List<CheckListManager> checkListManagers;

}