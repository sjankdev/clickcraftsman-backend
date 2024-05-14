package com.clickcraft.demo.search;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.enums.JobType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface JobSpecifications {

    static Specification<ClientJobPosting> filters(List<Long> filters) {
        return (root, query, criteriaBuilder) -> root.join("filtersId").get("id").in(filters);
    }

    public static Specification<ClientJobPosting> buildSpecification(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("jobTypes")) {
                String[] jobTypesArray = params.get("jobTypes").split(",");
                List<JobType> jobTypes = Arrays.stream(jobTypesArray)
                        .map(JobType::valueOf)
                        .collect(Collectors.toList());
                predicates.add(root.get("jobType").in(jobTypes));
            }
            if (params.containsKey("filtersId")) {
                List<Long> filtersId = parseLongList(params.get("filtersId"));
                predicates.add(filters(filtersId).toPredicate(root, query, criteriaBuilder));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    static List<Long> parseLongList(String input) {
        return Stream.of(input.split(",")).map(Long::valueOf).toList();
    }
}
