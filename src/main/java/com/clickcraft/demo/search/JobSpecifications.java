package com.clickcraft.demo.search;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.enums.ELocations;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface JobSpecifications {

    static Specification<ClientJobPosting> hasRequiredSkillIds(List<Long> filters) {
        return (root, query, criteriaBuilder) -> root.join("filters").get("id").in(filters);
    }

    static Specification<ClientJobPosting> buildSpecification(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("filterId")) {
                List<Long> filterId = parseLongList(params.get("filterId"));
                predicates.add(hasRequiredSkillIds(filterId).toPredicate(root, query, criteriaBuilder));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    static List<Long> parseLongList(String input) {
        return Stream.of(input.split(",")).map(Long::valueOf).toList();
    }
}
