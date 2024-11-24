package ru.practicum.ewm.events.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.events.enums.State;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false, length = 2000)
    String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @Column(name = "confirmed_requests")
    Integer confirmedRequests;

    @Column(name = "create_on")
    LocalDateTime createdOn;

    @Column(name = "description", length = 7000)
    String description;

    @Column(name = "event_date", nullable = false)
    LocalDateTime eventDate;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "initiator_id")
    User initiator;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "location_id")
    Location location;

    @Column(nullable = false)
    boolean paid;

    @Column(name = "participant_limit")
    Integer participantLimit = 0;

    @Column(name = "published_date")
    LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    boolean requestModeration = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    State state;

    @Column(name = "title", nullable = false, length = 120)
    String title;

    Integer views;
}
