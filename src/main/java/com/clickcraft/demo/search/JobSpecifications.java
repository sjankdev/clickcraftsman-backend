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

    static Specification<ClientJobPosting> hasLocations(List<ELocations> locations) {
        return (root, query, criteriaBuilder) -> root.get("location").in(locations);
    }

    static Specification<ClientJobPosting> hasRequiredSkillIds(List<Long> filters) {
        return (root, query, criteriaBuilder) -> root.join("requiredSkills").get("id").in(filters);
    }

    static Specification<ClientJobPosting> buildSpecification(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("requiredSkillsId")) {
                List<Long> requiredSkillsId = parseLongList(params.get("requiredSkillsId"));
                predicates.add(hasRequiredSkillIds(requiredSkillsId).toPredicate(root, query, criteriaBuilder));
            }
            if (params.containsKey("locations")) {
                List<ELocations> locations = parseLocationList(params.get("locations"));
                predicates.add(hasLocations(locations).toPredicate(root, query, criteriaBuilder));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    static List<Long> parseLongList(String input) {
        return Stream.of(input.split(",")).map(Long::valueOf).toList();
    }

    static List<ELocations> parseLocationList(String input) {
        return Stream.of(input.split(",")).map(ELocations::valueOf).toList();
    }
}
