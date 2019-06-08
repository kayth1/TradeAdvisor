package com.ceaver.assin.database.converter;

import android.arch.persistence.room.TypeConverter;

import com.ceaver.assin.alerts.AlertType;
import com.ceaver.assin.assets.Category;
import com.ceaver.assin.markets.Title;
import com.ceaver.assin.markets.TitleRepository;
import com.ceaver.assin.trades.TradeStrategy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Converters {

    @TypeConverter
    public static LocalDate daysFromEpochToLocalDate(Long daysFromEpoch) {
        return daysFromEpoch == null ? null : LocalDate.MIN.plusDays(daysFromEpoch);
    }

    @TypeConverter
    public static Long localDateToDaysFromEpoch(LocalDate date) {
        return date == null ? null : ChronoUnit.DAYS.between(LocalDate.MIN, date);
    }

    @TypeConverter
    public static LocalDateTime toLocalDateTime(Long secondsFromEpoch) {
        return secondsFromEpoch == null ? null : LocalDateTime.MIN.plusSeconds(secondsFromEpoch);
    }

    @TypeConverter
    public static Long fromLocalDateTime(LocalDateTime dateTime) {
        return dateTime == null ? null : ChronoUnit.SECONDS.between(LocalDateTime.MIN, dateTime);
    }

    @TypeConverter
    public static Set<TradeStrategy> toTradeStrategySet(String tradeStrategyString) {
        return Pattern.compile(";").splitAsStream(tradeStrategyString).map(i -> toStatus(i)).collect(Collectors.toSet());
    }

    @TypeConverter
    public static String toTradeStrategyString(Set<TradeStrategy> tradeStrategySet) {
        return tradeStrategySet.stream().map(i -> toOrdinal(i)).collect(Collectors.joining(";"));
    }

    @TypeConverter
    public static TradeStrategy toStatus(String name) {
        return TradeStrategy.valueOf(name);
    }

    @TypeConverter
    public static String toOrdinal(TradeStrategy strategy) {
        return strategy.name();
    }

    @TypeConverter
    public static AlertType toAlertType(String name) {
        return AlertType.valueOf(name);
    }

    @TypeConverter
    public static String fromAlertType(AlertType alertType) {
        return alertType.name();
    }


    @TypeConverter
    public static UUID toUuid(String uuid) {
        return UUID.fromString(uuid);
    }

    @TypeConverter
    public static String fromUuid(UUID uuid) {
        return uuid.toString();
    }

    @TypeConverter
    public static Optional<Double> toOptionalOfDouble(String optionalDouble) {
        return optionalDouble.isEmpty() ? Optional.empty() : Optional.of(Double.parseDouble(optionalDouble));
    }

    @TypeConverter
    public static String fromOptionalOfDouble(Optional<Double> optionalDouble) {
        return optionalDouble.isPresent() ? optionalDouble.get().toString() : "";
    }

    @TypeConverter
    public static Optional<LocalDateTime> toOptionalLocalDateTime(Long secondsFromEpoch) {
        return secondsFromEpoch == -1 ? Optional.empty() : Optional.of(LocalDateTime.MIN.plusSeconds(secondsFromEpoch));
    }

    @TypeConverter
    public static Long fromOptionalLocalDateTime(Optional<LocalDateTime> optionalLocalDateTime) {
        return optionalLocalDateTime.isPresent() ? ChronoUnit.SECONDS.between(LocalDateTime.MIN, optionalLocalDateTime.get()) : -1;
    }

    @TypeConverter
    public static Category toCategory(String name) {
        return Category.valueOf(name);
    }

    @TypeConverter
    public static String fromCategory(Category category) {
        return category.name();
    }

    @TypeConverter
    public static Optional<String> toOptionalString(String string) {
        return string.isEmpty() ? Optional.empty() : Optional.of(string);
    }

    @TypeConverter
    public static String fromOptionalString(Optional<String> optional) {
        return optional.map(String::toString).orElse("");
    }

    @TypeConverter
    public static Title toTitle(String id) {
        return TitleRepository.INSTANCE.loadTitle(id);
    }

    @TypeConverter
    public static String fromTitle(Title title) {
        return title.getId();
    }

    @TypeConverter
    public static Optional<Title> toOptionalTitle(String id) {
        return (id == null || id.isEmpty()) ? Optional.empty() : Optional.of(TitleRepository.INSTANCE.loadTitle(id));
    }

    @TypeConverter
    public static String fromOptionalTitle(Optional<Title> title) {
        return title.isPresent() ? title.get().getId() : "";
    }
}