package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocalSyncViewModel : ViewModel() {

    // -----------------------------------------
    // Core Seed State Stores
    // -----------------------------------------
    private val _inventory = MutableStateFlow(LocalSyncSeeds.initialInventory)
    val inventory: StateFlow<List<InventoryItem>> = _inventory.asStateFlow()

    private val _families = MutableStateFlow(LocalSyncSeeds.initialFamilies)
    val families: StateFlow<List<KhataFamily>> = _families.asStateFlow()

    private val _stores = MutableStateFlow(LocalSyncSeeds.stores)
    val stores: StateFlow<List<Store>> = _stores.asStateFlow()

    // -----------------------------------------
    // Voice-Stock Stocking Simulator States
    // -----------------------------------------
    private val _isRecordingVoice = MutableStateFlow(false)
    val isRecordingVoice: StateFlow<Boolean> = _isRecordingVoice.asStateFlow()

    private val _voiceTranscript = MutableStateFlow("")
    val voiceTranscript: StateFlow<String> = _voiceTranscript.asStateFlow()

    private val _voiceParsedResult = MutableStateFlow("")
    val voiceParsedResult: StateFlow<String> = _voiceParsedResult.asStateFlow()

    private val _isVoiceProcessed = MutableStateFlow(false)
    val isVoiceProcessed: StateFlow<Boolean> = _isVoiceProcessed.asStateFlow()

    // -----------------------------------------
    // Flash Sale Discounts State
    // -----------------------------------------
    private val _discountPercentSelection = MutableStateFlow(30)
    val discountPercentSelection: StateFlow<Int> = _discountPercentSelection.asStateFlow()

    private val _broadcastNotification = MutableStateFlow<String?>(null)
    val broadcastNotification: StateFlow<String?> = _broadcastNotification.asStateFlow()

    private val _countdownTimer = MutableStateFlow("01:59:45")
    val countdownTimer: StateFlow<String> = _countdownTimer.asStateFlow()

    // -----------------------------------------
    // Customer Store Discovery (Search & Map) State
    // -----------------------------------------
    private val _storeSearchQuery = MutableStateFlow("")
    val storeSearchQuery: StateFlow<String> = _storeSearchQuery.asStateFlow()

    private val _selectedStoreId = MutableStateFlow<String?>("1")
    val selectedStoreId: StateFlow<String?> = _selectedStoreId.asStateFlow()

    // -----------------------------------------
    // Mobile Payment / Family Checkout State
    // -----------------------------------------
    private val _paymentSelectedFamily = MutableStateFlow<KhataFamily?>(LocalSyncSeeds.initialFamilies.first())
    val paymentSelectedFamily: StateFlow<KhataFamily?> = _paymentSelectedFamily.asStateFlow()

    private val _paymentState = MutableStateFlow("idle") // idle, in_progress, completed
    val paymentState: StateFlow<String> = _paymentState.asStateFlow()

    private val _paymentProvider = MutableStateFlow<String?>("GPay") // GPay, PhonePe, Paytm
    val paymentProvider: StateFlow<String?> = _paymentProvider.asStateFlow()

    init {
        // Start simulated timer countdown
        viewModelScope.launch {
            var hours = 1
            var minutes = 59
            var seconds = 45
            while (true) {
                delay(1000)
                seconds--
                if (seconds < 0) {
                    seconds = 59
                    minutes--
                    if (minutes < 0) {
                        minutes = 59
                        hours--
                        if (hours < 0) {
                            hours = 1
                        }
                    }
                }
                val hrsStr = hours.toString().padStart(2, '0')
                val minsStr = minutes.toString().padStart(2, '0')
                val secsStr = seconds.toString().padStart(2, '0')
                _countdownTimer.value = "$hrsStr:$minsStr:$secsStr"
            }
        }
    }

    // -----------------------------------------
    // Interactive Business Actions
    // -----------------------------------------

    private val _lastParsedItemName = MutableStateFlow("Custom Item")
    val lastParsedItemName: StateFlow<String> = _lastParsedItemName.asStateFlow()

    private val _lastParsedQuantity = MutableStateFlow(10)
    val lastParsedQuantity: StateFlow<Int> = _lastParsedQuantity.asStateFlow()

    private val _lastParsedPrice = MutableStateFlow(50.0)
    val lastParsedPrice: StateFlow<Double> = _lastParsedPrice.asStateFlow()

    private val _lastParsedCategory = MutableStateFlow("Groceries")
    val lastParsedCategory: StateFlow<String> = _lastParsedCategory.asStateFlow()

    private val _lastParsedIntent = MutableStateFlow("add") // "add" or "discount"
    val lastParsedIntent: StateFlow<String> = _lastParsedIntent.asStateFlow()

    // Voice Stocking Simulator
    fun simulateVoicePreset(preset: VoicePreset) {
        viewModelScope.launch {
            _isRecordingVoice.value = true
            _voiceTranscript.value = "Listening to voice..."
            _voiceParsedResult.value = ""
            _isVoiceProcessed.value = false
            
            delay(1200) // Simulate listening
            _voiceTranscript.value = preset.transcript
            _isRecordingVoice.value = false
            
            delay(600) // Simulate processing
            processSpeechInput(preset.transcript)
        }
    }

    fun processSpeechInputCombined(text: String) {
        viewModelScope.launch {
            _isRecordingVoice.value = true
            _voiceTranscript.value = "Voice processing command..."
            _voiceParsedResult.value = ""
            _isVoiceProcessed.value = false
            
            delay(1000)
            _voiceTranscript.value = text
            _isRecordingVoice.value = false
            
            delay(500)
            processSpeechInput(text)
        }
    }

    fun processSpeechInput(input: String) {
        val uppercaseInput = input.uppercase()
        
        // 1. Discount intent
        if (uppercaseInput.contains("DISCOUNT") || uppercaseInput.contains("OFFER") || uppercaseInput.contains("FLASH") || uppercaseInput.contains("SALE") || uppercaseInput.contains("OFF")) {
            _lastParsedIntent.value = "discount"
            // Find percentage (e.g., 20% or 20 percent)
            val percentageRegex = Regex("(\\d+)\\s*%")
            var match = percentageRegex.find(input)
            var percent = match?.groupValues?.get(1)?.toIntOrNull()
            if (percent == null) {
                // Try percent words
                val percentWordRegex = Regex("(\\d+)\\s*(?:PERCENT|%|OFF|DISCOUNT)", RegexOption.IGNORE_CASE)
                match = percentWordRegex.find(input)
                percent = match?.groupValues?.get(1)?.toIntOrNull()
            }
            if (percent == null) {
                // Find any number
                val numberRegex = Regex("\\b(\\d+)\\b")
                val numberMatch = numberRegex.find(input)
                percent = numberMatch?.groupValues?.get(1)?.toIntOrNull() ?: 20
            }
            if (percent <= 0 || percent > 100) {
                percent = 25
            }
            
            _lastParsedQuantity.value = percent
            _lastParsedItemName.value = "Flash Discount Broadcast"
            _lastParsedPrice.value = percent.toDouble()
            _lastParsedCategory.value = "Action"
            _voiceParsedResult.value = "✔ PROPOSED DISCOUNT: Broadcast flash sale offer of $percent% to nearby customers?"
            _isVoiceProcessed.value = true
            return
        }
        
        // 2. Add / Stock intent
        _lastParsedIntent.value = "add"
        var matchedProduct = "Custom Item"
        var category = "Groceries"
        if (uppercaseInput.contains("PARLE")) {
            matchedProduct = "Parle-G Biscuit"
            category = "Snacks"
        } else if (uppercaseInput.contains("ATTA") || uppercaseInput.contains("AASHIRVAAD")) {
            matchedProduct = "Aashirvaad Atta 5kg"
            category = "Staples"
        } else if (uppercaseInput.contains("MILK") || uppercaseInput.contains("AMUL")) {
            matchedProduct = "Amul Gold Milk 1L"
            category = "Dairy"
        } else if (uppercaseInput.contains("SALT") || uppercaseInput.contains("TATA")) {
            matchedProduct = "Tata Salt 1kg"
            category = "Groceries"
        } else if (uppercaseInput.contains("OIL") || uppercaseInput.contains("FORTUNE")) {
            matchedProduct = "Fortune Mustard Oil 1L"
            category = "Staples"
        } else {
            // Try to extract name
            val addMatch = Regex("(?:ADD|STOCK|PUT|JODO|REGISTER|KIP)\\s+(?:\\d+\\s*(?:pouches|packets|bags|bottles|pack|bag|pouch|bottle|units|pcs|of)?)?\\s*([A-Za-z0-9\\s\\-]{3,15})", RegexOption.IGNORE_CASE).find(input)
            if (addMatch != null) {
                matchedProduct = addMatch.groupValues[1].trim()
            }
        }
        
        // Find numbers for quantity/price
        val quantityRegex = Regex("\\b\\d+\\b")
        val numbers = quantityRegex.findAll(input).map { it.value.toInt() }.toList()
        
        val quantity = if (numbers.isNotEmpty()) {
            if (numbers[0] < 1000) numbers[0] else 10
        } else 10
        
        val price = if (numbers.size > 1) {
            numbers[1].toDouble()
        } else {
            when (matchedProduct) {
                "Parle-G Biscuit" -> 10.0
                "Aashirvaad Atta 5kg" -> 260.0
                "Amul Gold Milk 1L" -> 66.0
                "Tata Salt 1kg" -> 28.0
                "Fortune Mustard Oil 1L" -> 175.0
                else -> 50.0
            }
        }
        
        _lastParsedItemName.value = matchedProduct
        _lastParsedQuantity.value = quantity
        _lastParsedPrice.value = price
        _lastParsedCategory.value = category
        
        _voiceParsedResult.value = "✔ PROPOSED STOCK: Add $quantity units of '$matchedProduct' at ₹$price per unit ($category section)?"
        _isVoiceProcessed.value = true
    }

    fun executeCommittedVoiceResult() {
        if (_lastParsedIntent.value == "discount") {
            launchFlashSale(_lastParsedQuantity.value)
        } else {
            applyVoiceInputToInventory(
                _lastParsedItemName.value,
                _lastParsedQuantity.value,
                _lastParsedPrice.value,
                _lastParsedCategory.value
            )
        }
        _isVoiceProcessed.value = false
        _voiceTranscript.value = ""
        _voiceParsedResult.value = ""
    }

    fun applyVoiceInputToInventory(name: String, quantity: Int, price: Double, category: String) {
        val currentList = _inventory.value.toMutableList()
        val index = currentList.indexOfFirst { it.name.lowercase().contains(name.lowercase()) || name.lowercase().contains(it.name.lowercase()) }
        
        if (index != -1) {
            val existing = currentList[index]
            currentList[index] = existing.copy(
                quantity = existing.quantity + quantity,
                pricePerUnit = price,
                lastUpdated = "Just stocked via Voice"
            )
        } else {
            currentList.add(
                0,
                InventoryItem(
                    id = System.currentTimeMillis().toString(),
                    name = name,
                    quantity = quantity,
                    unit = "pcs",
                    pricePerUnit = price,
                    category = category,
                    lastUpdated = "Just added via Voice"
                )
            )
        }
        _inventory.value = currentList
        _isVoiceProcessed.value = false
        _voiceTranscript.value = ""
        _voiceParsedResult.value = ""
    }

    fun dismissVoiceResult() {
        _isVoiceProcessed.value = false
        _voiceTranscript.value = ""
        _voiceParsedResult.value = ""
    }

    // Family Khata Ledger Interactions
    fun addCreditPurchase(familyId: String, description: String, amount: Double) {
        val updatedList = _families.value.map { family ->
            if (family.id == familyId) {
                // Credit purchase increases outstanding (negative balance goes further negative)
                val newBal = family.balance - amount
                val newLoyalty = family.loyaltyPoints + (amount * 0.1).toInt()
                val unlockedMessage = if (newLoyalty >= 1000 && family.rewardUnlocked == null) {
                    "₹400 Loyalty Credit Unlocked"
                } else family.rewardUnlocked

                family.copy(
                    balance = newBal,
                    loyaltyPoints = newLoyalty,
                    rewardUnlocked = unlockedMessage
                )
            } else family
        }
        _families.value = updatedList
        if (_paymentSelectedFamily.value?.id == familyId) {
            _paymentSelectedFamily.value = updatedList.firstOrNull { it.id == familyId }
        }
    }

    fun triggerCheckoutPay(familyId: String) {
        val familyObj = _families.value.firstOrNull { it.id == familyId }
        if (familyObj != null) {
            _paymentSelectedFamily.value = familyObj
            _paymentState.value = "idle"
        }
    }

    fun runUPIPaymentFlow(amountToClear: Double) {
        viewModelScope.launch {
            _paymentState.value = "in_progress"
            delay(2000) // Simulate secure UPI verification
            
            // Clear entire balance (balance reduces towards zero, i.e. becomes less negative or neutral)
            val famId = _paymentSelectedFamily.value?.id
            if (famId != null) {
                val updatedList = _families.value.map { family ->
                    if (family.id == famId) {
                        family.copy(
                            balance = family.balance + amountToClear,
                            loyaltyPoints = family.loyaltyPoints + (amountToClear * 0.05).toInt()
                        )
                    } else family
                }
                _families.value = updatedList
                _paymentSelectedFamily.value = updatedList.firstOrNull { it.id == famId }
            }
            _paymentState.value = "completed"
        }
    }

    fun resetPaymentWidget() {
        _paymentState.value = "idle"
    }

    fun updatePaymentProvider(provider: String) {
        _paymentProvider.value = provider
    }

    // Live Flash Sale Discount triggers
    fun launchFlashSale(discountPercent: Int) {
        _discountPercentSelection.value = discountPercent
        
        // Update stores list specifically for Store ID "1" (Ramesh Kirana Store)
        val updatedStores = _stores.value.map { store ->
            if (store.id == "1") {
                store.copy(hasDiscount = true, discountPercent = discountPercent)
            } else store
        }
        _stores.value = updatedStores

        // Trigger push broadcast notification mockup!
        val discountLabel = if (discountPercent > 0) "${discountPercent}% OFF" else "exclusive discounts"
        _broadcastNotification.value = "📢 Ramesh Kirana sent Flash Deal: $discountLabel on staples for early-bird shoppers! Next 2 hours."
    }

    fun clearBroadcast() {
        _broadcastNotification.value = null
    }

    // Discovery searches
    fun updateStoreSearchQuery(query: String) {
        _storeSearchQuery.value = query
    }

    fun selectMapStore(id: String) {
        _selectedStoreId.value = id
    }
}
