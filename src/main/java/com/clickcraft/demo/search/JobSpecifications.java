package com.clickcraft.demo.search;

import com.clickcraft.demo.models.ClientJobPosting;
import com.clickcraft.demo.models.enums.JobType;
import com.clickcraft.demo.models.enums.PriceType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface JobSpecifications {

    Logger logger = LoggerFactory.getLogger(JobSpecifications.class);

    static Specification<ClientJobPosting> jobName(String jobName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("jobName"), "%" + jobName + "%");
    }

    static Specification<ClientJobPosting> requiredSkills(List<Long> skillIds) {
        return (root, query, criteriaBuilder) -> root.join("requiredSkills").get("id").in(skillIds);
    }

    static Specification<ClientJobPosting> locations(List<String> locations) {
        return (root, query, criteriaBuilder) -> root.get("location").in(locations);
    }

    static Specification<ClientJobPosting> isRemote(Boolean isRemote) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("remote"), isRemote);
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

    static Specification<ClientJobPosting> datePostedThisMonth() {
        return (root, query, criteriaBuilder) -> {
            LocalDate today = LocalDate.now();
            LocalDate startOfThisMonth = today.with(TemporalAdjusters.firstDayOfMonth());
            LocalDateTime startOfDay = startOfThisMonth.atStartOfDay();
            return criteriaBuilder.greaterThanOrEqualTo(root.get("datePosted"), startOfDay);
        };
    }

    static Specification<ClientJobPosting> datePostedEarlierThanThisMonth() {
        return (root, query, criteriaBuilder) -> {
            LocalDate today = LocalDate.now();
            LocalDate startOfThisMonth = today.with(TemporalAdjusters.firstDayOfMonth());
            return criteriaBuilder.lessThan(root.get("datePosted"), startOfThisMonth);
        };
    }

    static Specification<ClientJobPosting> resumeRequired(Boolean resumeRequired) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("resumeRequired"), resumeRequired);
    }

    static Specification<ClientJobPosting> buildSpecification(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Map<String, BiConsumer<CriteriaBuilder, Root<ClientJobPosting>>> parameterHandlers = Map.of(
                    "jobName", (cb, r) -> predicates.add(jobName(params.get("jobName")).toPredicate(r, query, cb)),
                    "skillIds", (cb, r) -> predicates.add(requiredSkills(parseLongList(params.get("skillIds"))).toPredicate(r, query, cb)),
                    "locations", (cb, r) -> predicates.add(locations(Arrays.asList(params.get("locations").split(","))).toPredicate(r, query, cb)),
                    "isRemote", (cb, r) -> predicates.add(isRemote(parseBoolean(params.get("isRemote"))).toPredicate(r, query, cb)),
                    "jobTypes", (cb, r) -> predicates.add(jobTypes(Arrays.stream(params.get("jobTypes").split(",")).map(JobType::valueOf).collect(Collectors.toList())).toPredicate(r, query, cb)),
                    "priceTypes", (cb, r) -> predicates.add(priceTypes(Arrays.stream(params.get("priceTypes").split(",")).map(PriceType::valueOf).collect(Collectors.toList())).toPredicate(r, query, cb)),
                    "budgetFrom", (cb, r) -> {
                        Double budgetFrom = parseDouble(params.get("budgetFrom"));
                        Double budgetTo = parseDouble(params.get("budgetTo"));
                        predicates.add(budgetRange(budgetFrom, budgetTo).toPredicate(r, query, cb));
                    },
                    "priceRangeFrom", (cb, r) -> {
                        Double priceRangeFrom = params.containsKey("priceRangeFrom") ? parseDouble(params.get("priceRangeFrom")) : null;
                        Double priceRangeTo = params.containsKey("priceRangeTo") ? parseDouble(params.get("priceRangeTo")) : null;
                        predicates.add(priceRange(priceRangeFrom, priceRangeTo).toPredicate(r, query, cb));
                    },
                    "dateRange", (cb, r) -> {
                        switch (params.get("dateRange")) {
                            case "today":
                                predicates.add(datePostedToday().toPredicate(r, query, cb));
                                break;
                            case "yesterday":
                                predicates.add(datePostedYesterday().toPredicate(r, query, cb));
                                break;
                            case "thisWeek":
                                predicates.add(datePostedThisWeek().toPredicate(r, query, cb));
                                break;
                            case "thisMonth":
                                predicates.add(datePostedThisMonth().toPredicate(r, query, cb));
                                break;
                            case "earlierThanThisMonth":
                                predicates.add(datePostedEarlierThanThisMonth().toPredicate(r, query, cb));
                                break;
                        }
                    },
                    "resumeRequired", (cb, r) -> predicates.add(resumeRequired(parseBoolean(params.get("resumeRequired"))).toPredicate(r, query, cb))
            );

            parameterHandlers.forEach((key, handler) -> {
                if (params.containsKey(key)) {
                    handler.accept(criteriaBuilder, root);
                }
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    static List<Long> parseLongList(String input) {
        try {
            return Stream.of(input.split(",")).map(Long::valueOf).toList();
        } catch (NumberFormatException e) {
            logger.error("Error parsing long list: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid input for long list: " + input, e);
        }
    }

    static Double parseDouble(String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            logger.error("Error parsing double value: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid input for double value: " + input, e);
        }
    }

    static Boolean parseBoolean(String input) {
        try {
            return Boolean.parseBoolean(input);
        } catch (NumberFormatException e) {
            logger.error("Error parsing boolean value: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid input for boolean value: " + input, e);
        }
    }
}