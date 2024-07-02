package com.clickcraft.demo.search;

import com.clickcraft.demo.models.FreelancerProfile;
import com.clickcraft.demo.models.enums.ELocations;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface FreelancerProfileSpecifications {

    static Specification<FreelancerProfile> hasSkillIds(List<Long> skillIds) {
        return (root, query, criteriaBuilder) -> root.join("skills").get("id").in(skillIds);
    }

    static Specification<FreelancerProfile> hasLocations(List<ELocations> locations) {
        return (root, query, criteriaBuilder) -> root.get("location").in(locations);
    }

    static Specification<FreelancerProfile> hasYearsOfExperience(String yearsOfExperienceRange) {
        if (yearsOfExperienceRange == null || yearsOfExperienceRange.isEmpty()) {
            return null;
        }
        if (yearsOfExperienceRange.equals("5+")) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("yearsOfExperience"), 5);
        }
        String[] range = yearsOfExperienceRange.split("-");
        int minYears = Integer.parseInt(range[0]);
        int maxYears = range.length > 1 ? Integer.parseInt(range[1]) : Integer.MAX_VALUE;

        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("yearsOfExperience"), minYears, maxYears);
    }

    static Specification<FreelancerProfile> buildSpecification(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("skillIds")) {
                List<Long> skillIds = parseLongList(params.get("skillIds"));
                predicates.add(hasSkillIds(skillIds).toPredicate(root, query, criteriaBuilder));
            }
            if (params.containsKey("locations")) {
                List<ELocations> locations = parseLocationList(params.get("locations"));
                predicates.add(hasLocations(locations).toPredicate(root, query, criteriaBuilder));
            }
            if (params.containsKey("yearsOfExperienceRange")) {
                String yearsOfExperienceRange = params.get("yearsOfExperienceRange");
                predicates.add(hasYearsOfExperience(yearsOfExperienceRange).toPredicate(root, query, criteriaBuilder));
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
