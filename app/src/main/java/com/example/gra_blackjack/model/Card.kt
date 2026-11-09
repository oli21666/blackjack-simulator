package com.example.gra_blackjack.model

/**
 * Reprezentuje kolor karty w tradycyjnej talii.
 *
 * @property symbol Znak graficzny koloru (♥, ♦, ♣, ♠).
 * @property isRed Flaga określająca, czy kolor jest czerwony (Kier/Karo) czy czarny (Trefl/Pik).
 */
enum class Suit(val symbol: String, val isRed: Boolean) {
    HEARTS("♥", true),
    DIAMONDS("♦", true),
    CLUBS("♣", false),
    SPADES("♠", false)
}

/**
 * Reprezentuje rangę (figurę/wartość) karty w talii.
 *
 * @property symbol Skrócona nazwa/znak rangi (np. "2", "10", "J", "A").
 * @property baseValue Bazowa wartość punktowa karty w grze w Blackjacka (As domyślnie 11).
 */
enum class Rank(val symbol: String, val baseValue: Int) {
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    JACK("J", 10),
    QUEEN("Q", 10),
    KING("K", 10),
    ACE("A", 11)
}

/**
 * Model danych reprezentujący pojedynczą kartę do gry.
 *
 * @property rank Ranga karty (np. [Rank.ACE], [Rank.KING]).
 * @property suit Kolor karty (np. [Suit.HEARTS], [Suit.SPADES]).
 */
data class Card(
    val rank: Rank,
    val suit: Suit
) {
    /**
     * Zwraca etykietę wyświetlaną na karcie (np. "A♠", "10♥").
     */
    val displayLabel: String
        get() = "${rank.symbol}${suit.symbol}"
}
