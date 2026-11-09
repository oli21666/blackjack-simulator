package com.example.gra_blackjack

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gra_blackjack.game.BlackjackGame
import com.example.gra_blackjack.game.GameState
import com.example.gra_blackjack.game.RoundOutcome
import com.example.gra_blackjack.model.Card
import com.google.android.material.button.MaterialButton

/**
 * Główna Aktywność aplikacji obsługująca interfejs użytkownika stołu kasynowego.
 * Odpowiada za wiązanie elementów UI z silnikiem gry [BlackjackGame], reakcję na przyciski
 * żetonów i akcji oraz dynamiczne renderowanie kart z kinowym opóźnieniem tury krupiera.
 */
class MainActivity : AppCompatActivity() {

    private val game = BlackjackGame()
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var tvBalance: TextView
    private lateinit var tvCurrentBet: TextView
    private lateinit var tvDealerScore: TextView
    private lateinit var tvPlayerScore: TextView
    private lateinit var tvGameStatus: TextView

    private lateinit var llDealerCards: LinearLayout
    private lateinit var llPlayerCards: LinearLayout

    private lateinit var btnChip10: TextView
    private lateinit var btnChip25: TextView
    private lateinit var btnChip100: TextView
    private lateinit var btnChip500: TextView
    private lateinit var btnClearBet: MaterialButton

    private lateinit var btnDeal: MaterialButton
    private lateinit var btnHit: MaterialButton
    private lateinit var btnStand: MaterialButton
    private lateinit var btnDouble: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupListeners()
        updateUI()
    }

    /**
     * Inicjalizuje referencje do widoków z układy XML [R.layout.activity_main].
     */
    private fun initViews() {
        tvBalance = findViewById(R.id.tvBalance)
        tvCurrentBet = findViewById(R.id.tvCurrentBet)
        tvDealerScore = findViewById(R.id.tvDealerScore)
        tvPlayerScore = findViewById(R.id.tvPlayerScore)
        tvGameStatus = findViewById(R.id.tvGameStatus)

        llDealerCards = findViewById(R.id.llDealerCards)
        llPlayerCards = findViewById(R.id.llPlayerCards)

        btnChip10 = findViewById(R.id.btnChip10)
        btnChip25 = findViewById(R.id.btnChip25)
        btnChip100 = findViewById(R.id.btnChip100)
        btnChip500 = findViewById(R.id.btnChip500)
        btnClearBet = findViewById(R.id.btnClearBet)

        btnDeal = findViewById(R.id.btnDeal)
        btnHit = findViewById(R.id.btnHit)
        btnStand = findViewById(R.id.btnStand)
        btnDouble = findViewById(R.id.btnDouble)
    }

    /**
     * Podpina obsługę zdarzeń kliknięć (OnClickListeners) dla żetonów i przycisków akcji gry.
     */
    private fun setupListeners() {
        btnChip10.setOnClickListener {
            if (game.addBet(10)) updateUI()
        }
        btnChip25.setOnClickListener {
            if (game.addBet(25)) updateUI()
        }
        btnChip100.setOnClickListener {
            if (game.addBet(100)) updateUI()
        }
        btnChip500.setOnClickListener {
            if (game.addBet(500)) updateUI()
        }

        btnClearBet.setOnClickListener {
            game.clearBet()
            updateUI()
        }

        btnDeal.setOnClickListener {
            if (game.gameState == GameState.GAME_OVER) {
                game.resetForNextRound()
            }

            if (game.balance <= 0) {
                game.resetBalanceIfZero()
                updateUI()
                return@setOnClickListener
            }

            if (game.currentBet == 0) {
                game.addBet(10)
            }

            if (game.startDeal()) {
                updateUI()
                if (game.gameState == GameState.DEALER_TURN) {
                    runDealerLoop()
                }
            }
        }

        btnHit.setOnClickListener {
            game.hit()
            updateUI()
        }

        btnStand.setOnClickListener {
            game.stand()
            updateUI()
            runDealerLoop()
        }

        btnDouble.setOnClickListener {
            if (game.doubleDown()) {
                updateUI()
                if (game.gameState == GameState.DEALER_TURN) {
                    runDealerLoop()
                }
            }
        }
    }

    /**
     * Uruchamia krokową pętlę doboru kart przez krupiera z opóźnieniem 700 ms per karta,
     * nadając rozgrywce realistyczne tempo kasynowe.
     */
    private fun runDealerLoop() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (game.gameState == GameState.DEALER_TURN) {
                    val continues = game.dealerStep()
                    updateUI()
                    if (continues) {
                        handler.postDelayed(this, 700)
                    }
                }
            }
        }, 600)
    }

    /**
     * Główna funkcja odświeżająca stan interfejsu UI na podstawie modelu [game]:
     * aktualizuje saldo, zakład, karty, stany aktywności przycisków oraz teksty w bannerze.
     */
    private fun updateUI() {
        tvBalance.text = getString(R.string.balance_format, game.balance)
        tvCurrentBet.text = getString(R.string.bet_format, game.currentBet)

        renderDealerCards()
        renderPlayerCards()

        val isBettingPhase = (game.gameState == GameState.BETTING) || (game.gameState == GameState.GAME_OVER)
        val isPlayerTurn = game.gameState == GameState.PLAYER_TURN

        btnChip10.isEnabled = isBettingPhase
        btnChip25.isEnabled = isBettingPhase
        btnChip100.isEnabled = isBettingPhase
        btnChip500.isEnabled = isBettingPhase
        btnClearBet.isEnabled = isBettingPhase && game.currentBet > 0

        btnDeal.isEnabled = (isBettingPhase && (game.currentBet > 0 || game.balance <= 0)) || game.gameState == GameState.GAME_OVER
        btnHit.isEnabled = isPlayerTurn
        btnStand.isEnabled = isPlayerTurn
        btnDouble.isEnabled = isPlayerTurn && game.playerHand.cards.size == 2 && game.balance >= game.currentBet

        tvGameStatus.text = when (game.gameState) {
            GameState.BETTING -> {
                if (game.balance <= 0) {
                    getString(R.string.status_out_of_chips)
                } else {
                    getString(R.string.status_welcome)
                }
            }
            GameState.PLAYER_TURN -> getString(R.string.status_player_turn)
            GameState.DEALER_TURN -> getString(R.string.status_dealer_turn)
            GameState.GAME_OVER -> {
                when (game.lastOutcome) {
                    RoundOutcome.PLAYER_BLACKJACK -> getString(R.string.status_blackjack, game.payoutAmount)
                    RoundOutcome.PLAYER_WIN -> getString(R.string.status_win, game.payoutAmount)
                    RoundOutcome.DEALER_BUST -> getString(R.string.status_dealer_bust, game.payoutAmount)
                    RoundOutcome.PLAYER_BUST -> getString(R.string.status_bust)
                    RoundOutcome.DEALER_WIN -> getString(R.string.status_dealer_win)
                    RoundOutcome.PUSH -> getString(R.string.status_push)
                    null -> getString(R.string.status_welcome)
                }
            }
        }
    }

    /**
     * Renderuje karty krupiera w kontenerze [llDealerCards].
     * Jeśli druga karta krupiera pozostaje zakryta, wyświetla jej koszulkę [R.layout.item_card_back].
     */
    private fun renderDealerCards() {
        llDealerCards.removeAllViews()

        if (game.dealerHand.cards.isEmpty()) {
            tvDealerScore.text = "0"
            return
        }

        for (i in game.dealerHand.cards.indices) {
            val card = game.dealerHand.cards[i]
            val isHidden = i == 1 && game.isDealerCardHidden
            renderCard(card, llDealerCards, isHidden)
        }

        if (game.isDealerCardHidden && game.dealerHand.cards.size >= 2) {
            tvDealerScore.text = game.dealerHand.cards[0].rank.baseValue.toString()
        } else {
            tvDealerScore.text = game.dealerHand.calculateScore().toString()
        }
    }

    /**
     * Renderuje karty gracza w kontenerze [llPlayerCards] oraz aktualizuje punkty w [tvPlayerScore].
     */
    private fun renderPlayerCards() {
        llPlayerCards.removeAllViews()

        if (game.playerHand.cards.isEmpty()) {
            tvPlayerScore.text = "0"
            return
        }

        for (card in game.playerHand.cards) {
            renderCard(card, llPlayerCards, isHidden = false)
        }

        tvPlayerScore.text = game.playerHand.calculateScore().toString()
    }

    /**
     * Pomocnicza metoda pompująca (inflating) pojedynczy widok karty z układu XML.
     * Ustawia właściwe kolory symboli (czerwony dla Kier/Karo, czarny dla Trefl/Pik).
     *
     * @param card Model karty [Card].
     * @param container Kontener [ViewGroup], do którego zostanie dodany widok karty.
     * @param isHidden Czy wyrenderować koszulkę karty (tył) czy jej awers.
     */
    private fun renderCard(card: Card, container: ViewGroup, isHidden: Boolean) {
        val inflater = LayoutInflater.from(this)
        val cardView = if (isHidden) {
            inflater.inflate(R.layout.item_card_back, container, false)
        } else {
            val view = inflater.inflate(R.layout.item_card, container, false)
            val tvTop = view.findViewById<TextView>(R.id.tvCardTop)
            val tvCenter = view.findViewById<TextView>(R.id.tvCardCenter)
            val tvBottom = view.findViewById<TextView>(R.id.tvCardBottom)

            val suitColor = if (card.suit.isRed) {
                ContextCompat.getColor(this, R.color.card_suit_red)
            } else {
                ContextCompat.getColor(this, R.color.card_suit_black)
            }

            tvTop.text = card.displayLabel
            tvTop.setTextColor(suitColor)

            tvCenter.text = card.suit.symbol
            tvCenter.setTextColor(suitColor)

            tvBottom.text = card.displayLabel
            tvBottom.setTextColor(suitColor)

            view
        }
        container.addView(cardView)
    }
}
