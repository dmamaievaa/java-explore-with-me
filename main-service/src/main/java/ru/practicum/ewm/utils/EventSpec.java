package ru.practicum.ewm.utils;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.ewm.events.dto.SearchEventParamsAdmin;
import ru.practicum.ewm.events.dto.SearchEventParamsPublic;
import ru.practicum.ewm.events.enums.State;
import ru.practicum.ewm.events.model.Event;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;
import java.util.List;

public class EventSpec {

    public static Specification<Event> hasUsers(List<Long> users) {
        return (root, query, builder) -> {
            if (users == null || users.isEmpty()) {
                return builder.conjunction();
            }
            return root.get("initiator").get("id").in(users);
        };
    }

    public static Specification<Event> hasStates(List<State> states) {
        return (root, query, builder) -> {
            if (states == null || states.isEmpty()) {
                return builder.conjunction();
            }
            return root.get("state").in(states);
        };
    }

    public static Specification<Event> hasCategories(List<Integer> categories) {
        return (root, query, builder) -> {
            if (categories == null || categories.isEmpty()) {
                return builder.conjunction();
            }
            return root.get("category").get("id").in(categories);
        };
    }

    public static Specification<Event> afterRangeStart(LocalDateTime rangeStart) {
        return (root, query, builder) -> rangeStart == null
                ? builder.conjunction()
                : builder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart);
    }

    public static Specification<Event> beforeRangeEnd(LocalDateTime rangeEnd) {
        return (root, query, builder) -> rangeEnd == null
                ? builder.conjunction()
                : builder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd);
    }

    public static Specification<Event> onlyPublished() {
        return (root, query, builder) -> builder.equal(root.get("state"), State.PUBLISHED);
    }

    public static Specification<Event> searchText(String text) {
        return (root, query, builder) -> {
            if (text == null || text.isEmpty()) {
                return builder.conjunction();
            }
            String searchPattern = "%" + text.toLowerCase() + "%";
            Predicate annotationPredicate = builder.like(builder.lower(root.get("annotation")), searchPattern);
            Predicate descriptionPredicate = builder.like(builder.lower(root.get("description")), searchPattern);
            return builder.or(annotationPredicate, descriptionPredicate);
        };
    }

    public static Specification<Event> isPaid(Boolean paid) {
        return (root, query, builder) -> paid == null
                ? builder.conjunction()
                : builder.equal(root.get("paid"), paid);
    }

    public static Specification<Event> onlyAvailable(Boolean onlyAvailable) {
        return (root, query, builder) -> onlyAvailable == null || !onlyAvailable
                ? builder.conjunction()
                : builder.greaterThan(root.get("participantLimit"), root.get("confirmedRequests"));
    }

    public static Specification<Event> getAdminFilters(SearchEventParamsAdmin searchEventParamsAdmin) {
        LocalDateTime now = LocalDateTime.now();
        return Specification.where(hasUsers(searchEventParamsAdmin.getUsers()))
                .and(hasStates(searchEventParamsAdmin.getStates()))
                .and(hasCategories(searchEventParamsAdmin.getCategories()))
                .and(afterRangeStart(searchEventParamsAdmin.getRangeStart() != null ? searchEventParamsAdmin.getRangeStart() : now))
                .and(beforeRangeEnd(searchEventParamsAdmin.getRangeEnd()));
    }

    public static Specification<Event> getPublicFilters(SearchEventParamsPublic searchEventPublicParams) {
        LocalDateTime now = LocalDateTime.now();
        return Specification.where(onlyPublished())
                .and(searchText(searchEventPublicParams.getText()))
                .and(hasCategories(searchEventPublicParams.getCategories()))
                .and(isPaid(searchEventPublicParams.getPaid()))
                .and(afterRangeStart(searchEventPublicParams.getRangeStart() != null ? searchEventPublicParams.getRangeStart() : now))
                .and(beforeRangeEnd(searchEventPublicParams.getRangeEnd()))
                .and(onlyAvailable(searchEventPublicParams.getOnlyAvailable()));
    }
}
