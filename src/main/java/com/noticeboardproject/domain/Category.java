package com.noticeboardproject.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Category {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private Long id;

	private CategoryEnum category;
	
	@OneToMany(mappedBy = "category", fetch=FetchType.LAZY)
	private Set<Notice> notices;
	
	public Category() {
		notices = new HashSet<>();
	}
	
	public void addNotice(Notice notice) {
        notices.add(notice);
        notice.setCategory(this);
    }
 
    public void removeNotice(Notice notice) {
        notices.remove(notice);
        notice.setCategory(null);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (category != other.category)
			return false;
		return true;
	}
    
    
}

