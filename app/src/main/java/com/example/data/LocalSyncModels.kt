package com.example.data

import androidx.compose.ui.graphics.vector.ImageVector

data class InventoryItem(
    val id: String,
    val name: String,
    val quantity: Int,
    val unit: String,
    val pricePerUnit: Double,
    val category: String,
    val lastUpdated: String
) {
    val totalPrice: Double get() = quantity * pricePerUnit
}

data class KhataFamily(
    val id: String,
    val familyName: String,
    val passCode: String,
    val registeredMembers: List<String>,
    val balance: Double, // Negative is credit owed by family to shopkeeper
    val creditLimit: Double = 5000.0,
    val loyaltyPoints: Int = 0,
    val rewardUnlocked: String? = null
)

data class Store(
    val id: String,
    val name: String,
    val distance: String,
    val rating: Double,
    val hasDiscount: Boolean,
    val discountPercent: Int = 0,
    val popularProducts: List<String>,
    val contactNo: String
)

data class Testimonial(
    val ownerName: String,
    val shopName: String,
    val quote: String,
    val statKeyword: String,
    val statDescription: String,
    val avatarInitial: String
)

data class VoicePreset(
    val transcript: String,
    val parsedResult: String,
    val category: String,
    val label: String
)

object LocalSyncSeeds {
    val initialInventory = listOf(
        InventoryItem("1", "Parle-G Biscuit", 45, "pack", 10.0, "Snacks", "5 mins ago"),
        InventoryItem("2", "Aashirvaad Atta 5kg", 12, "bag", 260.0, "Staples", "Just now"),
        InventoryItem("3", "Amul Gold Milk 1L", 30, "pouch", 66.0, "Dairy", "1 hour ago"),
        InventoryItem("4", "Fortune Mustard Oil 1L", 18, "bottle", 175.0, "Staples", "2 hours ago"),
        InventoryItem("5", "Tata Salt 1kg", 25, "pack", 28.0, "Groceries", "Yesterday")
    )

    val initialFamilies = listOf(
        KhataFamily(
            id = "verma_12",
            familyName = "Verma Family",
            passCode = "123456",
            registeredMembers = listOf("Anil Verma (Father)", "Rita Verma (Mother)", "Siddharth Verma (Son)"),
            balance = -1850.0,
            creditLimit = 10000.0,
            loyaltyPoints = 450,
            rewardUnlocked = "₹150 Discount Coupon applied"
        ),
        KhataFamily(
            id = "sharma_98",
            familyName = "Sharma Family",
            passCode = "789012",
            registeredMembers = listOf("Rajesh Sharma (Father)", "Priya Sharma (Daughter)"),
            balance = -750.0,
            creditLimit = 5000.0,
            loyaltyPoints = 120,
            rewardUnlocked = null
        ),
        KhataFamily(
            id = "gupta_45",
            familyName = "Gupta Family",
            passCode = "543210",
            registeredMembers = listOf("Sanjay Gupta (Grandfather)", "Vikram Gupta (Son)", "Neha Gupta (Daughter-in-law)"),
            balance = -4200.0,
            creditLimit = 15000.0,
            loyaltyPoints = 980,
            rewardUnlocked = "₹400 Loyalty Credit Unlocked"
        )
    )

    val testimonials = listOf(
        Testimonial(
            ownerName = "Ramesh Kumar",
            shopName = "Ramesh Kirana Store, Sector 4",
            quote = "\"Traditional registers were slow. With LocalSync's voice stock manager, I simply speak in Hinglish '20 packets Parle-G jodo' and it records instantly. I save 2 hours every single day!\"",
            statKeyword = "+35% Sales",
            statDescription = "via Flash Deals footfall",
            avatarInitial = "R"
        ),
        Testimonial(
            ownerName = "Sunita Sharma",
            shopName = "Sharma Mini Mart, Karol Bagh",
            quote = "\"Managing ledger diaries ('Khata') was a nightmare with disputes on pending dues. Digital Khata matches with local family OTPs transparently. Outstanding credit disputes are now 100% resolved!\"",
            statKeyword = "Zero Dispute",
            statDescription = "100% digital matching",
            avatarInitial = "S"
        ),
        Testimonial(
            ownerName = "Vikram Gupta",
            shopName = "Gupta Provisions",
            quote = "\"I set a lightning discount on bread packets expiring in two days in just 3 seconds. Ten neighborhood families showed up within an hour. Amazing waste reduction!\"",
            statKeyword = "3 Seconds",
            statDescription = "Discount broadcasting",
            avatarInitial = "V"
        )
    )

    val stores = listOf(
        Store("1", "Ramesh Kirana Store", "150m away", 4.8, true, 10, listOf("Parle-G", "Amul Milk", "Fortune Oil"), "R"),
        Store("2", "Sharma Mini Mart", "450m away", 4.6, false, 0, listOf("Tata Salt", "Parle-G", "Aashirvaad Atta"), "S"),
        Store("3", "Gupta Provisions", "800m away", 4.9, true, 25, listOf("Maggi Nodles", "Basmati Rice", "Amul Butter"), "G")
    )

    val voicePresets = listOf(
        VoicePreset(
            transcript = "20 packets Parle-G added, 10 rupees per pack",
            parsedResult = "Added 20 Parle-G, ₹10/each.",
            category = "Snacks",
            label = "Parle-G voice command (Hinglish)"
        ),
        VoicePreset(
            transcript = "10 bags Aashirvaad Atta, 260 rupees per bag, category Staple",
            parsedResult = "Added 10 Aashirvaad Atta, ₹260/each.",
            category = "Staples",
            label = "Bulk Atta supply"
        ),
        VoicePreset(
            transcript = "30 pouches Amul Gold Milk, price 66 rupees, dairy section",
            parsedResult = "Added 30 Amul Gold Milk, ₹66/each.",
            category = "Dairy",
            label = "Morning milk stock"
        )
    )
}
