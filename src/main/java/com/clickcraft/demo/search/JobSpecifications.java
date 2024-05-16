package com.clickcraft.demo.search;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.enums.JobType;
import com.clickcraft.demo.models.enums.PriceType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface JobSpecifications {

    static Specification<ClientJobPosting> requiredSkills(List<Long> skillIds) {
        return (root, query, criteriaBuilder) -> root.join("requiredSkills").get("id").in(skillIds);
    }

    static Specification<ClientJobPosting> locations(List<String> locations) {
        return (root, query, criteriaBuilder) -> root.get("location").in(locations);
    }

    static Specification<ClientJobPosting> jobTypes(List<JobType> jobTypes) {
        return (root, query, criteriaBuilder) -> root.get("jobType").in(jobTypes);
    }

    static Specification<ClientJobPosting> priceTypes(List<PriceType> priceTypes) {
        return (root, query, criteriaBuilder) -> root.get("priceType").in(priceTypes);
    }

    static Specification<ClientJobPosting> budgetRange(Double budgetFrom, Double budgetTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("budget"), budgetFrom, budgetTo);
    }

    static Specification<ClientJobPosting> jobName(String jobName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("jobName"), "%" + jobName + "%");
    }

    static Specification<ClientJobPosting> priceRange(Double from, Double to) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (from != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("priceRangeFrom"), from));
            }
            if (to != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("priceRangeTo"), to));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

     static Specification<ClientJobPosting> buildSpecification(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("jobTypes")) {
                List<JobType> jobTypes = Arrays.stream(params.get("jobTypes").split(",")).map(JobType::valueOf).collect(Collectors.toList());
                predicates.add(jobTypes(jobTypes).toPredicate(root, query, criteriaBuilder));
            }
            if (params.containsKey("skillIds")) {
                List<Long> skillIds = parseLongList(params.get("skillIds"));
                predicates.add(requiredSkills(skillIds).toPredicate(root, query, criteriaBuilder));
            }
            if (params.containsKey("locations")) {
                List<String> locations = Arrays.asList(params.get("locations").split(","));
                predicates.add(locations(locations).toPredicate(root, query, criteriaBuilder));
            }
            if (params.containsKey("priceTypes")) {
                List<PriceType> priceTypes = Arrays.stream(params.get("priceTypes").split(",")).map(PriceType::valueOf).collect(Collectors.toList());
                predicates.add(priceTypes(priceTypes).toPredicate(root, query, criteriaBuilder));
            }
            if (params.containsKey("jobName")) {
                predicates.add(jobName(params.get("jobName")).toPredicate(root, query, criteriaBuilder));
            }

            if (params.containsKey("priceRangeFrom") || params.containsKey("priceRangeTo")) {
                Double priceRangeFrom = params.containsKey("priceRangeFrom") ? Double.parseDouble(params.get("priceRangeFrom")) : null;
                Double priceRangeTo = params.containsKey("priceRangeTo") ? Double.parseDouble(params.get("priceRangeTo")) : null;
                predicates.add(priceRange(priceRangeFrom, priceRangeTo).toPredicate(root, query, criteriaBuilder));
            }

            if (params.containsKey("budgetFrom") && params.containsKey("budgetTo")) {
                Double budgetFrom = Double.parseDouble(params.get("budgetFrom"));
                Double budgetTo = Double.parseDouble(params.get("budgetTo"));
                predicates.add(budgetRange(budgetFrom, budgetTo).toPredicate(root, query, criteriaBuilder));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


    static List<Long> parseLongList(String input) {
        return Stream.of(input.split(",")).map(Long::valueOf).toList();
    }
}
