package com.clickcraft.demo.search;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.enums.JobType;
import com.clickcraft.demo.models.enums.PriceType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
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

    static Specification<ClientJobPosting> datePostedToday() {
        return (root, query, criteriaBuilder) -> {
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
            return criteriaBuilder.between(root.get("datePosted"), startOfDay, endOfDay);
        };
    }

    static Specification<ClientJobPosting> datePostedYesterday() {
        return (root, query, criteriaBuilder) -> {
            LocalDate yesterday = LocalDate.now().minusDays(1);
            LocalDateTime startOfDay = yesterday.atStartOfDay();
            LocalDateTime endOfDay = yesterday.atTime(LocalTime.MAX);
            return criteriaBuilder.between(root.get("datePosted"), startOfDay, endOfDay);
        };
    }

    static Specification<ClientJobPosting> datePostedThisWeek() {
        return (root, query, criteriaBuilder) -> {
            LocalDate today = LocalDate.now();
            LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDateTime startOfDay = startOfWeek.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
            return criteriaBuilder.between(root.get("datePosted"), startOfDay, endOfDay);
        };
    }

    static Specification<ClientJobPosting> datePostedLastTwoWeeks() {
        return (root, query, criteriaBuilder) -> {
            LocalDate today = LocalDate.now();
            LocalDate twoWeeksAgo = today.minusWeeks(2);
            LocalDateTime startOfDay = twoWeeksAgo.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
            return criteriaBuilder.between(root.get("datePosted"), startOfDay, endOfDay);
        };
    }

    static Specification<ClientJobPosting> datePostedLastMonth() {
        return (root, query, criteriaBuilder) -> {
            LocalDate today = LocalDate.now();
            LocalDate startOfLastMonth = today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
            LocalDate endOfLastMonth = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
            LocalDateTime startOfDay = startOfLastMonth.atStartOfDay();
            LocalDateTime endOfDay = endOfLastMonth.atTime(LocalTime.MAX);
            return criteriaBuilder.between(root.get("datePosted"), startOfDay, endOfDay);
        };
    }

    static Specification<ClientJobPosting> isRemote(Boolean isRemote) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("remote"), isRemote);
    }

    static Specification<ClientJobPosting> resumeRequired(Boolean resumeRequired) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("resumeRequired"), resumeRequired);
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
            if (params.containsKey("resumeRequired")) {
                Boolean resumeRequired = Boolean.parseBoolean(params.get("resumeRequired"));
                predicates.add(resumeRequired(resumeRequired).toPredicate(root, query, criteriaBuilder));
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
            if (params.containsKey("isRemote")) {
                Boolean isRemote = Boolean.parseBoolean(params.get("isRemote"));
                predicates.add(isRemote(isRemote).toPredicate(root, query, criteriaBuilder));
            }
            if (params.containsKey("dateRange") && params.get("dateRange") != null) {
                switch (params.get("dateRange")) {
                    case "today":
                        predicates.add(datePostedToday().toPredicate(root, query, criteriaBuilder));
                        break;
                    case "yesterday":
                        predicates.add(datePostedYesterday().toPredicate(root, query, criteriaBuilder));
                        break;
                    case "thisWeek":
                        predicates.add(datePostedThisWeek().toPredicate(root, query, criteriaBuilder));
                        break;
                    case "lastTwoWeeks":
                        predicates.add(datePostedLastTwoWeeks().toPredicate(root, query, criteriaBuilder));
                        break;
                    case "lastMonth":
                        predicates.add(datePostedLastMonth().toPredicate(root, query, criteriaBuilder));
                        break;
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


    static List<Long> parseLongList(String input) {
        return Stream.of(input.split(",")).map(Long::valueOf).toList();
    }
}
