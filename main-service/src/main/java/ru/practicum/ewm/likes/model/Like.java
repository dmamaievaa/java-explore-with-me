package ru.practicum.ewm.likes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "likes")
@FieldDefaults(level = AccessLevel.PRIVATE)
@IdClass(LikeId.class)
public class Like {

    @Id
    @Column(name = "user_id")
    Integer userId;

    @Id
    @Column(name = "event_id")
    Integer eventId;
}