package com.noticeboardproject.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.noticeboardproject.domain.Category;
import com.noticeboardproject.domain.CategoryEnum;
import com.noticeboardproject.domain.Notice;

public final class NoticeSpecifications {
	private NoticeSpecifications() {}
	
	public static Specification<Notice> titleOrDescriptionContainsIgnoreCase(String searchTerm) {
        return (root, query, cb) -> {
            String containsLikePattern = getContainsLikePattern(searchTerm);
            return cb.or(
                    cb.like(cb.lower(root.<String>get("title")), containsLikePattern),
                    cb.like(cb.lower(root.<String>get("description")), containsLikePattern)
            );
        };
    }
 
    private static String getContainsLikePattern(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return "%";
        }
        else {
            return "%" + searchTerm.toLowerCase() + "%";
        }
    }
    
    public static Specification<Notice> cityContainsIgnoreCase(String searchTerm) {
		return (root, query, cb) -> {
			String containsLikePattern = getContainsLikePatternForCity(searchTerm);
			return cb.like(cb.lower(root.<String>get("city")), containsLikePattern);
		};
    }
    
    private static String getContainsLikePatternForCity(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty() || searchTerm.equals("Polska")) {
            return "%";
        }
        else {
            return "%" + searchTerm.toLowerCase() + "%";
        }
    }
    
    public static Specification<Notice> categotyEquals(CategoryEnum categoryEnum) {
    	if (categoryEnum==null) {
    		return (root, query, cb) -> {
    			return cb.isNotNull(root.<Category>get("category").<CategoryEnum>get("category"));
    		};
		}else {			
			return (root, query, cb) -> {
				return cb.equal(root.<Category>get("category").<CategoryEnum>get("category"), categoryEnum);
			};
		}
    }
}
