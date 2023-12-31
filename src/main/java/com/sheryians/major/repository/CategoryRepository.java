package com.sheryians.major.repository;

import com.sheryians.major.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    Optional<Category> findCategoryByName(String name);
//    boolean getCategoryBooleanId(int id);


}
